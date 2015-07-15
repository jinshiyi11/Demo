package com.shuai.demo.protocol;

import com.google.gson.annotations.SerializedName;

/**
 * 登陆成功后返回的数据
 */
public class LoginResult {
	
	@SerializedName("uid")
	private String mUid;
	
	@SerializedName("token")
	private String mToken;
	
	public String getUid() {
		return mUid;
	}
	public void setUid(String uid) {
		this.mUid = uid;
	}
	public String getToken() {
		return mToken;
	}
	public void setToken(String token) {
		this.mToken = token;
	}

}
