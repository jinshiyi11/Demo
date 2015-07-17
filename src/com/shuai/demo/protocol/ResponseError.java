package com.shuai.demo.protocol;

import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

@SuppressWarnings("serial")
public class ResponseError extends VolleyError {
	private static final String TAG=ResponseError.class.getSimpleName();
	
	private final int mErrorCode;
	private final String mErrorMessage;
	
	/**
	 * 客户端保留错误码起始值
	 */
	private final static int RESERVED_ERROR_BASE=-10000;
	
	//微信授权错误
	public final static int ERROR_WEIXIN_OAUTH_ERROR=RESERVED_ERROR_BASE+1;
	
	//微信授权被取消
	public final static int ERROR_WEIXIN_OAUTH_CANCEL=RESERVED_ERROR_BASE+2;
	
	/**
	 * 网络连接异常
	 */
	public final static int ERROR_NERWORK_CONNECTION=RESERVED_ERROR_BASE+3;
	
	/**
	 * 返回的数据有问题，如缺字段或字段数据格式不对等
	 */
	public final static int ERROR_DATA=RESERVED_ERROR_BASE+4;
	
	/**
	 * 服务器异常，若服务端返回502
	 */
	public final static int ERROR_SERVER=RESERVED_ERROR_BASE+5;


	public ResponseError(int errorCode,String errorMessage) {
		mErrorCode=errorCode;
		mErrorMessage=errorMessage;
	}
	
	public int getErrorCode(){
		return mErrorCode;
	}

	public String getErrorMessage(){
		return mErrorMessage;
	}

	public static String getErrorMessage(VolleyError error) {
		Log.e(TAG, error.toString());
		if(error instanceof ResponseError){
			ResponseError responseError=(ResponseError)error;
			switch (responseError.getErrorCode()) {
			case ERROR_WEIXIN_OAUTH_ERROR:
				return "微信授权错误";
			case ERROR_WEIXIN_OAUTH_CANCEL:
				return "微信授权被取消";
			default:
				return responseError.getErrorMessage();
			}
		}else if(error instanceof NetworkError){
			return "网络未连接";
		}else if(error instanceof TimeoutError){
			return "网络连接超时";
		}else if(error instanceof ServerError){
			return "服务异常";
		}else if(error instanceof ParseError){
			return "数据异常";
		}else{
			return "未知错误";
		}
		
	}

}
