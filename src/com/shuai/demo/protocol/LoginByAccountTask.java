package com.shuai.demo.protocol;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.shuai.demo.data.Constants;
import com.shuai.demo.logic.AccountManager;


/**
 * 通过用户名密码方式登录
 */
public class LoginByAccountTask extends JsonRequest<TokenInfo> {
	private final static String TAG=LoginByAccountTask.class.getSimpleName();
	private static final String ACCOUNT_LOGIN_URL = "login";

	public LoginByAccountTask(Context context,int loginType,String account,String password,Listener<TokenInfo> listener, ErrorListener errorListener) {
		super(Method.POST, ACCOUNT_LOGIN_URL,getBody(context,loginType,account,password), listener, errorListener);
	}
	
	private static String getBody(Context context,int loginType, String account,String password){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		if(loginType==AccountManager.LOGIN_BY_PHONE){
			params.add(new BasicNameValuePair("type", "phone"));
	        params.add(new BasicNameValuePair("username", account));
	        params.add(new BasicNameValuePair("password", password));
		}else{
			params.add(new BasicNameValuePair("type", "weixin"));
	        params.add(new BasicNameValuePair("uid", account));
	        params.add(new BasicNameValuePair("password", password));
		}
        
        UrlHelper.addCommonParameters(context, params);
		return URLEncodedUtils.format(params, "UTF-8");
	}

	@Override
	protected Response<TokenInfo> parseNetworkResponse(NetworkResponse response) {
		try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if(Constants.DEBUG){
                Log.d(TAG, jsonString);
            }
            
            JSONObject root=new JSONObject(jsonString);
            ErrorInfo error=ProtocolUtils.getProtocolInfo(root);
            if(error.getErrorCode()!=0){
            	return Response.error(error);
            }
            
            String resultJson=root.get(ProtocolUtils.RESULT).toString();
            Gson gson=new Gson();

            TokenInfo result=gson.fromJson(resultJson, TokenInfo.class);
            
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
	}

}
