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
import com.google.gson.JsonObject;
import com.shuai.demo.data.Constants;

/**
 * 使用手机号+验证码注册
 */
public class RegisterByPhoneTask extends JsonRequest<RegisterResult> {
	private final static String TAG=RegisterByPhoneTask.class.getSimpleName();
	private static final String PHONE_REGISTER_URL = "register";

	public RegisterByPhoneTask(Context context,String phone,String verifyCode,String password,
			Listener<RegisterResult> listener, ErrorListener errorListener) {
		super(Method.POST, PHONE_REGISTER_URL,getBody(context,phone,verifyCode,password), listener, errorListener);
	}
	
	private static String getBody(Context context, String phone,String verifyCode,String password){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("type", "phone"));
        params.add(new BasicNameValuePair("username", phone));
        params.add(new BasicNameValuePair("verify_code", verifyCode));
        params.add(new BasicNameValuePair("password", password));
        
        UrlHelper.addCommonParameters(context, params);
		return URLEncodedUtils.format(params, "UTF-8");
	}

	@Override
	protected Response<RegisterResult> parseNetworkResponse(NetworkResponse response) {
		try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if(Constants.DEBUG){
                Log.d(TAG, jsonString);
            }
            
            JSONObject root=new JSONObject(jsonString);
            ResponseError error=ProtocolUtils.getProtocolInfo(root);
            if(error.getErrorCode()!=0){
            	return Response.error(error);
            }
            
            String resultJson=root.get(ProtocolUtils.RESULT).toString();
            Gson gson=new Gson();
            RegisterResult result=gson.fromJson(resultJson, RegisterResult.class);
            
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
	}

}
