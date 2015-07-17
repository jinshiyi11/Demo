package com.shuai.demo.protocol;

import org.json.JSONException;
import org.json.JSONObject;

public class ProtocolUtils {
	public static final String RESULT="result";
	
	public static ResponseError getProtocolInfo(JSONObject root) throws JSONException{
        int errorCode=root.getInt("errno");
        String errorMessage=root.getString("errmsg");
        ResponseError error=new ResponseError(errorCode, errorMessage);
        return error;
	}

}
