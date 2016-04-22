package com.blanke.solebook.core.feedback;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVPersistenceUtils;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.feedback.Comment;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.avos.avoscloud.feedback.Resources;
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseSwipeBackActivity;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.utils.ResUtils;
import com.blanke.solebook.utils.StatusBarCompat;
import com.zhy.changeskin.SkinManager;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by blanke on 16-3-24.
 */
public class FeedActivity extends BaseSwipeBackActivity {
    FeedbackAgent agent;
    ListView feedbackListView;
    Button sendButton;
    EditText feedbackInput;
    EditText contact;
    FeedbackThread thread;
    FeedActivity.FeedbackListAdapter adapter;
    FeedbackThread.SyncCallback syncCallback;
    ImageView imageButton;
    AtomicBoolean animating = new AtomicBoolean(false);
    private static final int IMAGE_REQUEST = 111;
    public static final FeedActivity.ImageCache cache;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avoscloud_feedback_activity_conversation);
        this.setupActionBar();
        SkinManager.getInstance().register(this);
        EventBus.getDefault().register(this);
        this.agent = new FeedbackAgent(this);
        this.adapter = new FeedbackListAdapter(this);
        this.thread = this.agent.getDefaultThread();
        this.feedbackListView = (ListView) this.findViewById(Resources.id.avoscloud_feedback_thread_list(this));
        this.feedbackListView.setAdapter(this.adapter);
        this.sendButton = (Button) this.findViewById(Resources.id.avoscloud_feedback_send(this));
        this.imageButton = (ImageView) this.findViewById(Resources.id.avoscloud_feedback_add_image(this));
        this.feedbackInput = (EditText) this.findViewById(Resources.id.avoscloud_feedback_input(this));
        applyTheme(null);
        this.syncCallback = new FeedbackThread.SyncCallback() {
            public void onCommentsSend(List<Comment> comments, AVException e) {
                LogUtil.avlog.d("send new comments");
                FeedActivity.this.adapter.notifyDataSetChanged();
            }

            public void onCommentsFetch(List<Comment> comments, AVException e) {
                LogUtil.avlog.d("fetch new comments");
                FeedActivity.this.adapter.notifyDataSetChanged();
            }
        };
        this.sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FeedActivity.this.sendButton.setOnClickListener((View.OnClickListener) null);
                String feedbackText = FeedActivity.this.feedbackInput.getText().toString();
                FeedActivity.this.feedbackInput.setText("");
                if (!AVUtils.isBlankString(feedbackText)) {
                    FeedActivity.this.thread.add(new Comment(feedbackText));
                    FeedActivity.this.adapter.notifyDataSetChanged();
                    FeedActivity.this.feedbackListView.setSelection(FeedActivity.this.feedbackListView.getAdapter().getCount());
                    FeedActivity.this.smoothScrollToBottom();
                    FeedActivity.this.thread.sync(FeedActivity.this.syncCallback);
                }

                FeedActivity.this.sendButton.setOnClickListener(this);
            }
        });
        this.imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    FeedActivity.this.startActivityForResult(Intent.createChooser(intent, FeedActivity.this.getResources().getString(Resources.string.avoscloud_feedback_select_image(FeedActivity.this))), IMAGE_REQUEST);
                } else {
                    intent = new Intent("android.intent.action.OPEN_DOCUMENT");
                    intent.addCategory("android.intent.category.OPENABLE");
                    intent.setType("image/*");
                    FeedActivity.this.startActivityForResult(intent, IMAGE_REQUEST);
                }

            }
        });
        this.feedbackInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    FeedActivity.this.feedbackListView.setSelection(FeedActivity.this.feedbackListView.getAdapter().getCount());
                    FeedActivity.this.smoothScrollToBottom();
                }

            }
        });
        this.feedbackInput.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TranslateAnimation a1;
                AlphaAnimation a2;
                AnimationSet set;
                TranslateAnimation a3;
                AlphaAnimation a4;
                AnimationSet set2;
                if (AVUtils.isBlankString(s.toString()) && !FeedActivity.this.animating.get() && FeedActivity.this.imageButton.getVisibility() == View.INVISIBLE) {
                    a1 = new TranslateAnimation(0.0F, 0.0F, 0.0F, 100.0F);
                    a2 = new AlphaAnimation(1.0F, 0.2F);
                    set = new AnimationSet(true);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.addAnimation(a1);
                    set.addAnimation(a2);
                    set.setDuration(300L);
                    set.setRepeatMode(2);
                    set.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                            FeedActivity.this.animating.set(true);
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            FeedActivity.this.animating.set(false);
                            FeedActivity.this.sendButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    FeedActivity.this.sendButton.startAnimation(set);
                    FeedActivity.this.imageButton.setVisibility(View.VISIBLE);
                    a3 = new TranslateAnimation(0.0F, 0.0F, -50.0F, 0.0F);
                    a4 = new AlphaAnimation(0.2F, 1.0F);
                    set2 = new AnimationSet(true);
                    set2.setInterpolator(new AccelerateDecelerateInterpolator());
                    set2.addAnimation(a3);
                    set2.addAnimation(a4);
                    set2.setDuration(250L);
                    set2.setRepeatMode(2);
                    set2.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            FeedActivity.this.imageButton.setVisibility(View.VISIBLE);
                        }
                    });
                    FeedActivity.this.imageButton.startAnimation(set2);
                } else if (!AVUtils.isBlankString(s.toString()) && !FeedActivity.this.animating.get() && FeedActivity.this.sendButton.getVisibility() == View.INVISIBLE) {
                    a1 = new TranslateAnimation(0.0F, 0.0F, 0.0F, -100.0F);
                    a2 = new AlphaAnimation(1.0F, 0.2F);
                    set = new AnimationSet(true);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.addAnimation(a1);
                    set.addAnimation(a2);
                    set.setDuration(300L);
                    set.setRepeatMode(2);
                    set.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                            FeedActivity.this.animating.set(true);
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            FeedActivity.this.imageButton.setVisibility(View.INVISIBLE);
                            FeedActivity.this.animating.set(false);
                        }
                    });
                    FeedActivity.this.imageButton.startAnimation(set);
                    FeedActivity.this.sendButton.setVisibility(View.VISIBLE);
                    a3 = new TranslateAnimation(0.0F, 0.0F, 50.0F, 0.0F);
                    a4 = new AlphaAnimation(0.2F, 1.0F);
                    set2 = new AnimationSet(true);
                    set2.setInterpolator(new AccelerateDecelerateInterpolator());
                    set2.addAnimation(a3);
                    set2.addAnimation(a4);
                    set2.setDuration(250L);
                    set2.setRepeatMode(2);
                    set2.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            FeedActivity.this.sendButton.setVisibility(View.VISIBLE);
                        }
                    });
                    FeedActivity.this.sendButton.startAnimation(set2);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.contact = (EditText) this.findViewById(Resources.id.avoscloud_feedback_contact(this));
        if (this.agent.isContactEnabled()) {
            this.contact.setVisibility(View.VISIBLE);
            this.contact.setText(this.thread.getContact());
            this.contact.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (!AVUtils.isBlankString(s.toString())) {
                        FeedActivity.this.thread.setContact(s.toString());
                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        } else {
            this.contact.setVisibility(View.GONE);
        }

        this.thread.sync(this.syncCallback);
    }

    public void setStatusBarColor() {
        int c = SkinManager.getInstance().getResourceManager().getColor(Constants.RES_COLOR_STATUSBAR);
        StatusBarCompat.setStatusBarColor(this, c);
    }

    public void setEditColor() {
        int c = SkinManager.getInstance().getResourceManager().getColor(Constants.RES_COLOR_TEXT);
        feedbackInput.setTextColor(c);
        feedbackInput.setHintTextColor(c);
    }

    @Subscriber(tag = Constants.EVENT_THEME_CHANGE)
    public void applyTheme(Object o) {
        setStatusBarColor();
        setEditColor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
        EventBus.getDefault().unregister(this);
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @TargetApi(19)
    private static String getPath(Context context, Uri uri) {
        boolean isKitKat = Build.VERSION.SDK_INT >= 19;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            String docId;
            String[] split;
            String type;
            if (isExternalStorageDocument(uri)) {
                docId = DocumentsContract.getDocumentId(uri);
                split = docId.split(":");
                type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else {
                if (isDownloadsDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    Uri split1 = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId).longValue());
                    return getDataColumn(context, split1, (String) null, (String[]) null);
                }

                if (isMediaDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    split = docId.split(":");
                    type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, "_id=?", selectionArgs);
                }
            }
        } else {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, (String) null, (String[]) null);
            }

            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = new String[]{"_data"};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, (String) null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow("_data");
                String var8 = cursor.getString(column_index);
                return var8;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }

        return null;
    }

    @TargetApi(19)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (IMAGE_REQUEST == requestCode && resultCode == -1 && data.getData() != null) {
            Uri uri = data.getData();
            String filePath = getPath(this, uri);

            try {
                LogUtil.avlog.d("img picked:" + filePath);
                File e = new File(filePath);
                this.thread.add(new Comment(e));
                this.adapter.notifyDataSetChanged();
                this.feedbackListView.setSelection(this.feedbackListView.getAdapter().getCount());
                this.smoothScrollToBottom();
                this.thread.sync(this.syncCallback);
                this.feedbackInput.setText("");
            } catch (AVException var7) {
                var7.printStackTrace();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(ResUtils.getResString(this, R.string.navigation_feedback));
//        ActionBar actionBar = this.getActionBar();
//        if (actionBar != null) {
//            actionBar.setCustomView(Resources.layout.avoscloud_feedback_thread_actionbar(this));
//            actionBar.setDisplayShowCustomEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(false);
//            actionBar.setDisplayShowTitleEnabled(false);
////            View backButton = actionBar.getCustomView().findViewById(Resources.id.avoscloud_feedback_actionbar_back(this));
////            backButton.setOnClickListener(new View.OnClickListener() {
////                public void onClick(View v) {
////                    FeedActivity.this.onBackPressed();
////                    FeedActivity.this.finish();
////                }
////            });
//        }

    }

    private void smoothScrollToBottom() {
        this.feedbackListView.post(new Runnable() {
            public void run() {
                FeedActivity.this.feedbackListView.smoothScrollToPosition(FeedActivity.this.feedbackListView.getAdapter().getCount());
            }
        });
    }

    static {
        cache = new FeedActivity.ImageCache(AVOSCloud.applicationContext);
    }

    public static class ImageCache {
        LruCache<String, Bitmap> bitmapCache;
        static final int cacheSize = 20;
        Context context;

        public ImageCache(Context context) {
            this.context = context;
            this.bitmapCache = new LruCache(20);
        }

        static String getFileName(String fileUrl) {
            Uri uri = Uri.parse(fileUrl);
            return uri.getLastPathSegment();
        }

        public static File getCacheFile(String fileName) {
            File imgCacheDir = new File(AVOSCloud.applicationContext.getExternalCacheDir(), "img");
            if (!imgCacheDir.exists()) {
                imgCacheDir.mkdirs();
            }

            return new File(imgCacheDir, getFileName(fileName));
        }

        static File getCacheThumbnailFile(String fileName) {
            File imgCacheDir = new File(AVOSCloud.applicationContext.getExternalCacheDir(), "img");
            if (!imgCacheDir.exists()) {
                imgCacheDir.mkdirs();
            }

            return new File(imgCacheDir, getFileName(fileName) + ".tn");
        }

        public Bitmap getImage(String key) {
            Bitmap cacheBitmap = (Bitmap) this.bitmapCache.get(key);
            if (cacheBitmap == null) {
                cacheBitmap = BitmapFactory.decodeFile(getCacheThumbnailFile(key).getAbsolutePath());
            }

            return cacheBitmap;
        }

        public Bitmap setImage(String key, byte[] data) {
            FileOutputStream os = null;
            FileOutputStream thumbnailOS = null;
            Object imageData = null;
            Bitmap thumbnail = null;

            try {
                Bitmap e = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(data, 0, data.length), 150, 150, false);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                e.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData1 = baos.toByteArray();
                os = new FileOutputStream(getCacheFile(key), true);
                os.write(data);
                thumbnailOS = new FileOutputStream(getCacheThumbnailFile(key), true);
                thumbnailOS.write(imageData1);
                thumbnail = BitmapFactory.decodeByteArray(imageData1, 0, imageData1.length);
            } catch (FileNotFoundException var13) {
                var13.printStackTrace();
            } catch (IOException var14) {
                var14.printStackTrace();
            } finally {
                AVPersistenceUtils.closeQuietly(os);
                AVPersistenceUtils.closeQuietly(thumbnailOS);
            }

            return thumbnail;
        }
    }

    public class ViewHolder {
        TextView content;
        TextView timestamp;
        ImageView image;

        public ViewHolder() {
        }
    }

    public class FeedbackListAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;

        public FeedbackListAdapter(Context context) {
            this.mContext = context;
            this.inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return FeedActivity.this.thread.getCommentsList().size();
        }

        public Object getItem(int position) {
            return FeedActivity.this.thread.getCommentsList().get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final FeedActivity.ViewHolder holder;
            if (convertView == null) {
                if (this.getItemViewType(position) == 0) {
                    convertView = this.inflater.inflate(Resources.layout.avoscloud_feedback_user_reply(FeedActivity.this), (ViewGroup) null);
                } else {
                    convertView = this.inflater.inflate(Resources.layout.avoscloud_feedback_dev_reply(FeedActivity.this), (ViewGroup) null);
                }

                holder = FeedActivity.this.new ViewHolder();
                holder.content = (TextView) convertView.findViewById(Resources.id.avoscloud_feedback_content(FeedActivity.this));
                holder.timestamp = (TextView) convertView.findViewById(Resources.id.avoscloud_feedback_timestamp(FeedActivity.this));
                holder.image = (ImageView) convertView.findViewById(Resources.id.avoscloud_feedback_image(FeedActivity.this));
                convertView.setTag(holder);
            } else {
                holder = (FeedActivity.ViewHolder) convertView.getTag();
            }

            final Comment comment = (Comment) this.getItem(position);
            if (comment.getAttachment() != null && comment.getAttachment().getUrl() != null) {
                holder.content.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
                final View.OnClickListener imageOnClickListener = new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setDataAndType(Uri.fromFile(FeedActivity.ImageCache.getCacheFile(comment.getAttachment().getUrl())), "image/*");
                        FeedActivity.this.startActivity(intent);
                    }
                };
                Bitmap attachmentCache = FeedActivity.cache.getImage(comment.getAttachment().getUrl());
                if (attachmentCache != null) {
                    holder.image.setImageBitmap(attachmentCache);
                    holder.image.setOnClickListener(imageOnClickListener);
                } else {
                    holder.image.setOnClickListener((View.OnClickListener) null);
                    comment.getAttachment().getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, AVException e) {
                            if (e == null) {
                                Bitmap attachmentCache = FeedActivity.cache.setImage(comment.getAttachment().getUrl(), data);
                                holder.image.setImageBitmap(attachmentCache);
                                holder.image.setOnClickListener(imageOnClickListener);
                            }

                        }
                    });
                }
            } else {
                holder.content.setVisibility(View.VISIBLE);
                holder.content.setText(comment.getContent());
                holder.image.setVisibility(View.GONE);
            }

            if (Math.abs(comment.getCreatedAt().getTime() - System.currentTimeMillis()) < 10000L) {
                holder.timestamp.setText(FeedActivity.this.getResources().getString(Resources.string.avoscloud_feedback_just_now(FeedActivity.this)));
            } else {
                holder.timestamp.setText(DateUtils.getRelativeTimeSpanString(comment.getCreatedAt().getTime(), System.currentTimeMillis() - 1L, 0L, 524288));
            }
            int c = SkinManager.getInstance().getResourceManager().getColor(Constants.RES_COLOR_TEXT_BACKGROUND);
            convertView.setBackgroundColor(c);
            return convertView;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public int getItemViewType(int position) {
            Comment comment = (Comment) this.getItem(position);
            return comment.getCommentType().equals(Comment.CommentType.USER) ? 0 : 1;
        }
    }
}

