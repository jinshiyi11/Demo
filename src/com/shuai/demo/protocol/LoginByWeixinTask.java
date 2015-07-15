package com.shuai.demo.protocol;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.shuai.demo.MyApplication;
import com.shuai.demo.data.Constants;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 通过微信登录
 * 先登录到微信，拿到微信的token，然后把微信的token传到我们自己的服务器验证，服务端到微信的服务器验证该token并返回本系统的uid和token
 */
public class LoginByWeixinTask {
	private final static String WEIXIN_LOGIN_URL="";
	private AtomicBoolean mCanceled;

	private Context mContext;
	private Listener<LoginResult> mListener;
	private ErrorListener mErrorListener;
	
	private RequestQueue mRequestQueue;

	/**
	 * 拿到微信的token后，到app的服务端验证并登陆
	 */
	private class LoginToServerTask extends JsonRequest<LoginResult> {
		private final String TAG=getClass().getSimpleName();

		public LoginToServerTask(Context context,String token) {
			super(Method.POST, WEIXIN_LOGIN_URL, getRequestBody(context,token), mListener, mErrorListener);
		}

		@Override
		protected Response<LoginResult> parseNetworkResponse(NetworkResponse response) {
			try {
	            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
	            if(Constants.DEBUG){
	                Log.d(TAG, jsonString);
	            }
	            
	            //TODO:处理返回error code的情况
	            Gson gson=new Gson();

	            LoginResult result=gson.fromJson(jsonString, LoginResult.class);
	            
	            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
	        } catch (UnsupportedEncodingException e) {
	            return Response.error(new ParseError(e));
	        } catch (Exception e) {
	            return Response.error(new ParseError(e));
	        }
		}

	}
	
	private static String getRequestBody(Context context,String token){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        
        UrlHelper.addCommonParameters(context, params);
		return URLEncodedUtils.format(params, "UTF-8");
	}

	public LoginByWeixinTask(Context context, Listener<LoginResult> listener,ErrorListener errorListener) {
		mContext=context;
		mListener=listener;
		mErrorListener=errorListener;
		
		mRequestQueue=MyApplication.getRequestQueue();
		
	}
	
	public void login(){
		loginToWeixin();
	}
	
	public void cancel(){
		mCanceled.set(true);
		if(mRequestQueue!=null)
			mRequestQueue.cancelAll(this);
	}
	
	private void loginToWeixin(){
		UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		UMWXHandler wxHandler = new UMWXHandler(mContext,Constants.APP_ID_WEIXIN,Constants.APP_SECRET_WEIXIN);
		wxHandler.addToSocialSDK();
		
		mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
		    @Override
		    public void onStart(SHARE_MEDIA platform) { 
		        //授权开始
		    }
		    @Override
		    public void onError(SocializeException e, SHARE_MEDIA platform) {
		        //授权错误
		    	if(mCanceled.get())
		    		return;
		    	mErrorListener.onErrorResponse(new ProtocolError(ProtocolError.WEIXIN_OAUTH_ERROR, "微信授权错误"));
		    }
		    @Override
		    public void onComplete(Bundle value, SHARE_MEDIA platform) {
		        //授权完成
		    	if(mCanceled.get())
		    		return;
		    	String token=null;
		    	
		    	
		    	LoginToServerTask request=new LoginToServerTask(mContext,token);
		    	request.setTag(LoginByWeixinTask.this);       
		        mRequestQueue.add(request);
		    }
		    
		    @Override
		    public void onCancel(SHARE_MEDIA platform) { 
		        //授权取消
		    	if(mCanceled.get())
		    		return;
		    	mErrorListener.onErrorResponse(new ProtocolError(ProtocolError.WEIXIN_OAUTH_ERROR, "微信授权被取消"));
		    }
		} );
	}

}
