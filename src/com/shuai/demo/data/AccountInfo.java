package com.shuai.demo.data;

import com.shuai.demo.logic.AccountManager;

public class AccountInfo {

	private String mUid;

	private String mPassword;
	
	private int mLoginType=AccountManager.NOT_LOGIN;

	private String mToken;
	
	private String mPhoneNumber;

	public AccountInfo() {
	}

	public String getUid() {
		return mUid;
	}

	public void setUid(String uid) {
		this.mUid = uid;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		this.mPassword = password;
	}

	public String getToken() {
		return mToken;
	}

	public void setToken(String token) {
		this.mToken = token;
	}
	
	public int getLoginType(){
		return mLoginType;
	}

	public void setLoginType(int loginType){
		mLoginType=loginType;
	}
	
	public String getPhoneNumber(){
		return mPhoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber){
		mPhoneNumber=phoneNumber;
	}
}
