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


public class PhoneRegisterTask extends JsonRequest<LoginResult> {
	private final static String TAG=PhoneRegisterTask.class.getSimpleName();
	private static final String PHONE_REGISTER_URL = "";

	public PhoneRegisterTask(Context context,String phone,String verifyCode,String password,
			Listener<LoginResult> listener, ErrorListener errorListener) {
		super(Method.GET, getUrl(context,PHONE_REGISTER_URL,phone,verifyCode,Utils.md5(password)), null, listener, errorListener);
	}
	
	private static String getUrl(Context context,String url, String phone,String verifyCode,String password){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("verify_code", verifyCode));
        params.add(new BasicNameValuePair("password", password));
        
        UrlHelper.addCommonParameters(context, params);
		return url+"?"+URLEncodedUtils.format(params, "UTF-8");
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
