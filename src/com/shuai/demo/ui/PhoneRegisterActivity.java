package com.shuai.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shuai.demo.MyApplication;
import com.shuai.demo.R;
import com.shuai.demo.data.Constants;
import com.shuai.demo.logic.AccountManager;
import com.shuai.demo.protocol.ProtocolUtils;
import com.shuai.demo.protocol.RegisterByPhoneTask;
import com.shuai.demo.protocol.RegisterResult;
import com.shuai.demo.ui.base.BaseActivity;
import com.shuai.demo.utils.Utils;

/**
 * 手机注册界面
 */
public class PhoneRegisterActivity extends BaseActivity implements
		OnClickListener {
	private final String TAG = PhoneRegisterActivity.class.getSimpleName();

	private Button mBtnGetVerifySms;
	private Button mBtnRegister;
	private EditText mEtPhone;
	private EditText mEtSms;
	private EditText mEtPassword;
	private EditText mEtConfirmPassword;

	private Handler mHandler = new Handler();
	private EventHandler mEventHandler;
	private RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_register);
		
		mRequestQueue=MyApplication.getRequestQueue();

		mBtnGetVerifySms = (Button) findViewById(R.id.btn_get_verify_sms);
		mBtnRegister = (Button) findViewById(R.id.btn_register);
		mEtPhone = (EditText) findViewById(R.id.et_phone);
		mEtSms = (EditText) findViewById(R.id.et_sms);
		mEtPassword = (EditText) findViewById(R.id.et_password);
		mEtConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);

		mBtnGetVerifySms.setOnClickListener(this);
		mBtnRegister.setOnClickListener(this);

		SMSSDK.initSDK(this, Constants.SMS_APP_KEY, Constants.SMS_APP_SECRET);

		mEventHandler = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {
				super.afterEvent(event, result, data);

				if (result == SMSSDK.RESULT_COMPLETE) {
					// 回调完成
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						// 提交验证码成功
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						// 获取验证码成功
					} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
						// 返回支持发送验证码的国家列表
					}
				}else{
					//result == SMSSDK.RESULT_ERROR
					Log.e(TAG, data.toString());
					
				}
			}

		};

		SMSSDK.registerEventHandler(mEventHandler);
	}

	@Override
	protected void onDestroy() {
		if (mEventHandler != null) {
			SMSSDK.unregisterEventHandler(mEventHandler);
		}
		
		mRequestQueue.cancelAll(this);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_get_verify_sms:
			onBtnGetVerifySmsClicked();
			break;
		case R.id.btn_register:
			onBtnRegisterClicked();
			break;
		default:
			break;
		}
	}

	private boolean checkPhoneNumber(String phone) {
		if (TextUtils.isEmpty(phone) || phone.length() != 11) {
			return false;
		} else
			return true;
	}

	/**
	 * 用户点击了获取验证码
	 */
	private void onBtnGetVerifySmsClicked() {
		String phone = mEtPhone.getText().toString();
		if (!checkPhoneNumber(phone)) {
			Utils.showShortToast(this, "请输入正确的手机号");
			return;
		}

		SMSSDK.getVerificationCode("86", phone);
	}

	/**
	 * 用户点击了注册按钮
	 */
	private void onBtnRegisterClicked() {
		final String phone = mEtPhone.getText().toString();
		if (!checkPhoneNumber(phone)) {
			Utils.showShortToast(this, "请输入正确的手机号");
			return;
		}

		String verifyCode = mEtSms.getText().toString();
		if (TextUtils.isEmpty(verifyCode)) {
			Utils.showShortToast(this, "请输入短信验证码");
			return;
		}

		String password = mEtPassword.getText().toString();
		if (TextUtils.isEmpty(password)) {
			Utils.showShortToast(this, "请输入密码");
			return;
		}

		String confirmPassword = mEtConfirmPassword.getText().toString();
		if (TextUtils.isEmpty(confirmPassword)) {
			Utils.showShortToast(this, "两次输入的密码不匹配");
			return;
		}

		RegisterByPhoneTask request=new RegisterByPhoneTask(this,phone,verifyCode,Utils.md5(password),new Listener<RegisterResult>() {

			@Override
			public void onResponse(RegisterResult result) {
				AccountManager.getInstance().onLoginByPhoneSuccess(result.getUid(), result.getToken(), result.getPassword(), phone);
				finish();
			}
		},new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				Utils.showShortToast(PhoneRegisterActivity.this, ProtocolUtils.getErrorInfo(error).getErrorMessage());
			}
			
		});
		request.setTag(this);       
        mRequestQueue.add(request);
	}

}
