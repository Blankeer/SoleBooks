
package com.blanke.solebook.core.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseActivity;
import com.blanke.solebook.core.picker.CommonImagePickerDetailActivity;
import com.blanke.solebook.core.qrcode.camera.CameraManager;
import com.blanke.solebook.core.qrcode.decode.DecodeThread;
import com.blanke.solebook.core.qrcode.decode.DecodeUtils;
import com.blanke.solebook.core.qrcode.utils.BeepManager;
import com.blanke.solebook.core.qrcode.utils.InactivityTimer;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;


@EActivity(R.layout.activity_capture)
public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {

    public static final int IMAGE_PICKER_REQUEST_CODE = 100;

    @ViewById(R.id.capture_preview)
    SurfaceView capturePreview;
    @ViewById(R.id.capture_error_mask)
    ImageView captureErrorMask;
    @ViewById(R.id.capture_scan_mask)
    ImageView captureScanMask;
    @ViewById(R.id.capture_crop_view)
    FrameLayout captureCropView;
    @ViewById(R.id.capture_picture_btn)
    Button capturePictureBtn;
    @ViewById(R.id.capture_light_btn)
    Button captureLightBtn;
    @ViewById(R.id.capture_container)
    RelativeLayout captureContainer;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;

    private boolean hasSurface;
    private boolean isLightOn;

    private InactivityTimer mInactivityTimer;
    private BeepManager mBeepManager;

    private int mQrcodeCropWidth = 0;
    private int mQrcodeCropHeight = 0;
    private int mBarcodeCropWidth = 0;
    private int mBarcodeCropHeight = 0;

    private ObjectAnimator mScanMaskObjectAnimator = null;

    private Rect cropRect;


    @AfterViews
    void init() {
        hasSurface = false;
        mInactivityTimer = new InactivityTimer(this);
        mBeepManager = new BeepManager(this);

        initCropViewAnimator();

        capturePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                readyGoForResult(CommonImagePickerListActivity.class, IMAGE_PICKER_REQUEST_CODE);
            }
        });

        captureLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightOn) {
                    cameraManager.setTorch(false);
                    captureLightBtn.setSelected(false);
                } else {
                    cameraManager.setTorch(true);
                    captureLightBtn.setSelected(true);
                }
                isLightOn = !isLightOn;
            }
        });

        PropertyValuesHolder bar2qrWidthVH = PropertyValuesHolder.ofFloat("width",
                1.0f, (float) mQrcodeCropWidth / mBarcodeCropWidth);
        PropertyValuesHolder bar2qrHeightVH = PropertyValuesHolder.ofFloat("height",
                1.0f, (float) mQrcodeCropHeight / mBarcodeCropHeight);
        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(bar2qrWidthVH, bar2qrHeightVH);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float fractionW = (Float) animation.getAnimatedValue("width");
                Float fractionH = (Float) animation.getAnimatedValue("height");

                RelativeLayout.LayoutParams parentLayoutParams = (RelativeLayout.LayoutParams) captureCropView.getLayoutParams();
                parentLayoutParams.width = (int) (mBarcodeCropWidth * fractionW);
                parentLayoutParams.height = (int) (mBarcodeCropHeight * fractionH);
                captureCropView.setLayoutParams(parentLayoutParams);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                initCrop();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    private void initCropViewAnimator() {
        mQrcodeCropWidth = getResources().getDimensionPixelSize(R.dimen.qrcode_crop_width);
        mQrcodeCropHeight = getResources().getDimensionPixelSize(R.dimen.qrcode_crop_height);

    }


    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        int[] location = new int[2];
        captureCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1];

        int cropWidth = captureCropView.getWidth();
        int cropHeight = captureCropView.getHeight();

        int containerWidth = captureContainer.getWidth();
        int containerHeight = captureContainer.getHeight();

        int x = cropLeft * cameraWidth / containerWidth;
        int y = cropTop * cameraHeight / containerHeight;

        int width = cropWidth * cameraWidth / containerWidth;
        int height = cropHeight * cameraHeight / containerHeight;

        setCropRect(new Rect(x, y, width + x, height + y));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(capturePreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            capturePreview.getHolder().addCallback(this);
        }

        mInactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }

        mBeepManager.close();
        mInactivityTimer.onPause();
        cameraManager.closeDriver();

        if (!hasSurface) {
            capturePreview.getHolder().removeCallback(this);
        }

        if (null != mScanMaskObjectAnimator && mScanMaskObjectAnimator.isStarted()) {
            mScanMaskObjectAnimator.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        super.onDestroy();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
        }
        if (!hasSurface) {
            hasSurface = true;
//            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera(holder);
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     */
    public void handleDecode(String result) {
        mInactivityTimer.onActivity();
        mBeepManager.playBeepSoundAndVibrate();

//        if (!CommonUtils.isEmpty(result) && CommonUtils.isUrl(result)) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(result));
//            startActivity(intent);
//        } else {
//            bundle.putString(ResultActivity.BUNDLE_KEY_SCAN_RESULT, result);
//            readyGo(ResultActivity.class, bundle);
//        }
    }

    private void onCameraPreviewSuccess() {
        initCrop();
        captureErrorMask.setVisibility(View.GONE);

        ViewHelper.setPivotX(captureScanMask, 0.0f);
        ViewHelper.setPivotY(captureScanMask, 0.0f);

        mScanMaskObjectAnimator = ObjectAnimator.ofFloat(captureScanMask, "scaleY", 0.0f, 1.0f);
        mScanMaskObjectAnimator.setDuration(2000);
        mScanMaskObjectAnimator.setInterpolator(new DecelerateInterpolator());
        mScanMaskObjectAnimator.setRepeatCount(-1);
        mScanMaskObjectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mScanMaskObjectAnimator.start();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
//            Log.w(TAG_LOG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager);
            }

            onCameraPreviewSuccess();
        } catch (IOException ioe) {
//            Log.w(TAG_LOG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
//            Log.w(TAG_LOG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }

    }

    private void displayFrameworkBugMessageAndExit() {
        captureErrorMask.setVisibility(View.VISIBLE);
//        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
//        builder.cancelable(true);
//        builder.title(R.string.app_name);
//        builder.content(R.string.tips_open_camera_error);
//        builder.positiveText(R.string.btn_ok);
//        builder.callback(new MaterialDialog.ButtonCallback() {
//            @Override
//            public void onPositive(MaterialDialog dialog) {
//                super.onPositive(dialog);
//                finish();
//            }
//        });
//        builder.show();
    }

    public Rect getCropRect() {
        return cropRect;
    }

    public void setCropRect(Rect cropRect) {
        this.cropRect = cropRect;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == IMAGE_PICKER_REQUEST_CODE) {
            String imagePath = data.getStringExtra(CommonImagePickerDetailActivity
                    .KEY_BUNDLE_RESULT_IMAGE_PATH);

            if (!TextUtils.isEmpty(imagePath)) {
                ImageLoader.getInstance().loadImage("file://" + imagePath, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        String resultZxing = new DecodeUtils().decodeWithZxing(loadedImage);

                        if (!TextUtils.isEmpty(resultZxing)) {
                            handleDecode(resultZxing);
                        } else {
//                            showToast(getResources().getString(R.string.tips_decode_null));
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        }
    }
}
