package com.shuai.demo.protocol;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

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
import com.shuai.demo.utils.Utils;


/**
 * 通过用户名密码方式登录
 */
public class LoginByAccountTask extends JsonRequest<LoginResult> {
	private final static String TAG=LoginByAccountTask.class.getSimpleName();
	private static final String ACCOUNT_LOGIN_URL = "";

	public LoginByAccountTask(Context context,String account,String password,Listener<LoginResult> listener, ErrorListener errorListener) {
		super(Method.POST, ACCOUNT_LOGIN_URL, getRequestBody(context,account,password), listener, errorListener);
	}
	
	private static String getRequestBody(Context context,String account,String password){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("password", Utils.md5(password)));
        
        UrlHelper.addCommonParameters(context, params);
		return URLEncodedUtils.format(params, "UTF-8");
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
