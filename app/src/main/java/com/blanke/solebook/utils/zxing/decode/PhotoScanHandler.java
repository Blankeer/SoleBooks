package com.blanke.solebook.utils.zxing.decode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.blanke.solebook.utils.zxing.ScanManager;
import com.google.zxing.Result;


public class PhotoScanHandler extends Handler{
	public final static int PHOTODECODEERROR=0;
	public final static int PHOTODECODEOK=1;
	ScanManager scanManager;
	public PhotoScanHandler(ScanManager scanManager) {
		this.scanManager=scanManager;
	}
	@Override
	public void handleMessage(Message message) {
		
		switch (message.what) {
		case PHOTODECODEERROR:
			scanManager.handleDecodeError((Exception)message.obj);
			break;
		case PHOTODECODEOK:
			Bundle bundle = message.getData();
			scanManager.handleDecode((Result) message.obj, bundle);
			break;
		default:
			break;
		}
	}
	
}
