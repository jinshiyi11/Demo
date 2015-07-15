package com.shuai.demo.protocol;

import com.android.volley.VolleyError;

@SuppressWarnings("serial")
public class ProtocolError extends VolleyError {
	
	private final int mErrorCode;
	private final String mErrorMessage;
	
	/**
	 * 客户端保留错误码起始值
	 */
	private final static int RESERVED_ERROR_BASE=-10000;
	
	//微信授权错误
	public final static int WEIXIN_OAUTH_ERROR=RESERVED_ERROR_BASE+1;
	//微信授权被取消
	public final static int WEIXIN_OAUTH_CANCEL=RESERVED_ERROR_BASE+2;


	public ProtocolError(int errorCode,String errorMessage) {
		mErrorCode=errorCode;
		mErrorMessage=errorMessage;
	}
	
	public int getErrorCode(){
		return mErrorCode;
	}

	public String getErrorMessage(){
		return mErrorMessage;
	}

}
