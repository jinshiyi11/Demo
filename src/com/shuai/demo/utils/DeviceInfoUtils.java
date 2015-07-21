package com.shuai.demo.utils;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class DeviceInfoUtils {

	public static String getUniqueID(Context context){    
	    String deviceId = "";
	    TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    if (mTelephony.getDeviceId() != null){
	        deviceId = mTelephony.getDeviceId(); 
	    }else{
	         deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID); 
	    }
	    return deviceId;
	}
}
