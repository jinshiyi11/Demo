package com.shuai.demo.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;

import com.shuai.demo.data.AccountInfo;
import com.shuai.demo.data.Config;
import com.shuai.demo.protocol.LoginResult;

public class AccountManager {
	
	public final static int NOT_LOGIN=0;
	
	/**
	 * 通过手机号密码方式登录
	 */
	public final static int LOGIN_BY_PHONE=1;
	
	/**
	 * 通过uid+密码方式登录
	 */
	public final static int LOGIN_BY_UID=2;
	
	
	private static AccountManager mSelf;
	private Context mContext;

	public static synchronized AccountManager getInstance(){
		if(mSelf!=null )
			return mSelf;
		
		mSelf=new AccountManager();
		return mSelf;
	}
	
	public void init(Context context){
		mContext=context;
	}
	
	public interface LoginResultListener{
		void onLoginResult(LoginResult result);
	}
	
	public interface LogoutListener{
		void onLogout();
	}
	
	private AtomicBoolean mIsLogining;
	private AccountInfo mAccountInfo;
	private List<LoginResultListener> mLoginResultListeners=new LinkedList<LoginResultListener>();
	private List<LogoutListener> mLogoutListeners=new LinkedList<LogoutListener>();
	
	private AccountManager() {
	}
	
	public void addLoginResultListener(LoginResultListener listener){
		mLoginResultListeners.add(listener);
	}
	
	public void removeLoginResultListener(LoginResultListener listener){
		mLoginResultListeners.remove(listener);
	}
	
	public boolean isLogin(){
		return mAccountInfo!=null;
	}
	
	public void login(){
		if(mIsLogining.get())
			return;
		
		mIsLogining.set(true);
		
	}
	
	private void notifyLoginResult(LoginResult result){
		for(LoginResultListener listener:mLoginResultListeners){
			listener.onLoginResult(result);
		}
	}
	
	public void logout(){
		mAccountInfo=null;
		
		for(LogoutListener listener:mLogoutListeners){
			listener.onLogout();
		}
	}
	
	/**
	 * 发现token过期之后，重新获取token
	 */
	public void refreshToken(){
		
	}
	
	public void onLoginByPhoneSuccess(String uid,String token,String password,String phoneNumber){
		mAccountInfo=new AccountInfo();
		mAccountInfo.setUid(uid);
		mAccountInfo.setToken(token);
		mAccountInfo.setPassword(password);
		mAccountInfo.setLoginType(LOGIN_BY_PHONE);
		mAccountInfo.setPhoneNumber(phoneNumber);
		
		Config.getInstance().saveConfig();
		notifyLoginResult(new LoginResult());
	}
	
	public void onLoginByWeixinSuccess(String uid,String token,String password){
		mAccountInfo=new AccountInfo();
		mAccountInfo.setUid(uid);
		mAccountInfo.setToken(token);
		mAccountInfo.setPassword(password);
		mAccountInfo.setLoginType(LOGIN_BY_UID);
		
		Config.getInstance().saveConfig();
		notifyLoginResult(new LoginResult());
	}

}
