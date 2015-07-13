package com.shuai.demo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.smssdk.SMSSDK;

import com.shuai.demo.R;
import com.shuai.demo.data.Constants;
import com.shuai.demo.ui.base.BaseActivity;
import com.shuai.demo.utils.Utils;

/**
 * 手机注册界面
 */
public class PhoneRegisterActivity extends BaseActivity implements OnClickListener {
    private Button mBtnGetVerifySms;
    private Button mBtnRegister;
    private EditText mEtPhone;
    private EditText mEtSms;
    private EditText mEtPassword;
    private EditText mEtConfirmPassword;
    
    private Handler mHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);
        
        mBtnGetVerifySms=(Button) findViewById(R.id.btn_get_verify_sms);
        mBtnRegister=(Button) findViewById(R.id.btn_register);
        mEtPhone=(EditText) findViewById(R.id.et_phone);
        mEtSms=(EditText) findViewById(R.id.et_sms);
        mEtPassword=(EditText) findViewById(R.id.et_password);
        mEtConfirmPassword=(EditText) findViewById(R.id.et_confirm_password);
        
        mBtnGetVerifySms.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        
        SMSSDK.initSDK(this,Constants.SMS_APP_KEY,Constants.SMS_APP_SECRET);
    }

    @Override
    public void onClick(View v) {
        int id=mBtnGetVerifySms.getId();
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
    
    private boolean checkPhoneNumber(String phone){
        if(TextUtils.isEmpty(phone)||phone.length()!=11){
            return false;
        }else
            return true;
    }
    
    /**
     * 用户点击了获取验证码
     */
    private void onBtnGetVerifySmsClicked() {
        String phone=mEtPhone.getText().toString();
        if(!checkPhoneNumber(phone)){
            Utils.showShortToast(this, "请输入正确的手机号");
            return;
        }
        
        SMSSDK.getVerificationCode("86", phone);
    }

    /**
     * 用户点击了注册按钮
     */
    private void onBtnRegisterClicked() {
        String phone=mEtPhone.getText().toString();
        if(!checkPhoneNumber(phone)){
            Utils.showShortToast(this, "请输入正确的手机号");
            return;
        }
    }

    
    

}
