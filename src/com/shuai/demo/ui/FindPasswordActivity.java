package com.shuai.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.android.volley.RequestQueue;
import com.shuai.demo.MyApplication;
import com.shuai.demo.R;
import com.shuai.demo.data.Constants;
import com.shuai.demo.utils.Utils;

public class FindPasswordActivity extends Activity implements OnClickListener {
	private final String TAG = FindPasswordActivity.class.getSimpleName();
	
	private Button mBtnGetVerifySms;
	private Button mBtnFindPassword;
	private EditText mEtPhone;
	private EditText mEtSms;
	private EditText mEtPassword;
	
	private EventHandler mEventHandler;
	private RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_register);

		mRequestQueue=MyApplication.getRequestQueue();

		mBtnGetVerifySms = (Button) findViewById(R.id.btn_get_verify_sms);
		mBtnFindPassword = (Button) findViewById(R.id.btn_find_password);
		mEtPhone = (EditText) findViewById(R.id.et_phone);
		mEtSms = (EditText) findViewById(R.id.et_sms);
		mEtPassword = (EditText) findViewById(R.id.et_password);

		mBtnGetVerifySms.setOnClickListener(this);
		mBtnFindPassword.setOnClickListener(this);

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
		case R.id.btn_find_password:
			onBtnFindPasswordClicked();
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
	
	private void onBtnFindPasswordClicked() {
		
	}

}
