package com.shuai.demo.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shuai.demo.MyApplication;
import com.shuai.demo.data.AccountInfo;
import com.shuai.demo.data.Config;
import com.shuai.demo.protocol.ErrorInfo;
import com.shuai.demo.protocol.LoginByAccountTask;
import com.shuai.demo.protocol.LoginResult;
import com.shuai.demo.protocol.ProtocolUtils;
import com.shuai.demo.protocol.TokenInfo;

public class AccountManager {
	private final static String TAG = AccountManager.class.getSimpleName();

	public final static int NOT_LOGIN = 0;

	/**
	 * 通过手机号密码方式登录
	 */
	public final static int LOGIN_BY_PHONE = 1;

	/**
	 * 通过uid+密码方式登录
	 */
	public final static int LOGIN_BY_UID = 2;

	private static AccountManager mSelf;
	private Context mContext;

	public static synchronized AccountManager getInstance() {
		if (mSelf != null)
			return mSelf;

		mSelf = new AccountManager();
		return mSelf;
	}

	public void init(Context context) {
		mContext = context;
	}

	public interface LoginResultListener {
		void onLoginResult(LoginResult result);
	}

	public interface LogoutListener {
		void onLogout();
	}

	private AtomicBoolean mIsLogining;
	private boolean mIsLogined;
	private AccountInfo mAccountInfo;
	private List<LoginResultListener> mLoginResultListeners = new LinkedList<LoginResultListener>();
	private List<LogoutListener> mLogoutListeners = new LinkedList<LogoutListener>();

	private AccountManager() {
	}

	public void addLoginResultListener(LoginResultListener listener) {
		mLoginResultListeners.add(listener);
	}

	public void removeLoginResultListener(LoginResultListener listener) {
		mLoginResultListeners.remove(listener);
	}

	public boolean isLogined() {
		return mIsLogined;
	}

	private void notifyLoginResult(LoginResult result) {
		if(result.isLoginSuccess())
			mIsLogined=true;
		
		for (LoginResultListener listener : mLoginResultListeners) {
			listener.onLoginResult(result);
		}
	}

	public void logout() {
		mAccountInfo = null;

		for (LogoutListener listener : mLogoutListeners) {
			listener.onLogout();
		}
	}

	/**
	 * 自动登录，发现token过期之后，重新获取token
	 */
	public void autoLogin() {
		if (mIsLogining.get()) {
			Log.d(TAG, "is logining!");
			return;
		}

		mIsLogining.set(true);
		int loginType = mAccountInfo.getLoginType();
		Request<?> request = null;
		String account = null;
		if (loginType == AccountManager.LOGIN_BY_PHONE) {
			account = mAccountInfo.getPhoneNumber();
		} else if (loginType == AccountManager.LOGIN_BY_UID) {
			account = mAccountInfo.getUid();
		}

		request = new LoginByAccountTask(mContext, loginType, account,
				mAccountInfo.getPassword(), new Listener<TokenInfo>() {

					@Override
					public void onResponse(TokenInfo tokenInfo) {
						mIsLogining.set(false);
						mAccountInfo.setToken(tokenInfo.getToken());
						notifyLoginResult(new LoginResult());
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						mIsLogining.set(false);

						ErrorInfo info = ProtocolUtils.getErrorInfo(error);
						LoginResult result = new LoginResult(
								info.getErrorCode(), info.getErrorMessage());
						notifyLoginResult(result);
					}
				});

		RequestQueue requestQueue = MyApplication.getRequestQueue();
		request.setTag(this);
		requestQueue.add(request);
	}

	public void onLoginByPhoneSuccess(String uid, String token,
			String password, String phoneNumber) {
		mAccountInfo = new AccountInfo();
		mAccountInfo.setUid(uid);
		mAccountInfo.setToken(token);
		mAccountInfo.setPassword(password);
		mAccountInfo.setLoginType(LOGIN_BY_PHONE);
		mAccountInfo.setPhoneNumber(phoneNumber);

		Config.getInstance().saveConfig();
		notifyLoginResult(new LoginResult());
	}

	public void onLoginByWeixinSuccess(String uid, String token, String password) {
		mAccountInfo = new AccountInfo();
		mAccountInfo.setUid(uid);
		mAccountInfo.setToken(token);
		mAccountInfo.setPassword(password);
		mAccountInfo.setLoginType(LOGIN_BY_UID);

		Config.getInstance().saveConfig();
		notifyLoginResult(new LoginResult());
	}

}
