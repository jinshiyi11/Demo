package com.shuai.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.shuai.demo.MyApplication;
import com.shuai.demo.R;
import com.shuai.demo.protocol.LoginByAccountTask;
import com.shuai.demo.protocol.LoginByWeixinTask;
import com.shuai.demo.protocol.LoginResult;
import com.shuai.demo.protocol.PhoneRegisterTask;
import com.shuai.demo.ui.base.BaseActivity;
import com.shuai.demo.utils.Utils;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
    private TextView mTvRegister;
    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button mBtnLogin;
    
    private RequestQueue mRequestQueue;
    private LoginByWeixinTask mLoginByWeixinTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        
        mRequestQueue=MyApplication.getRequestQueue();
        
        mTvRegister=(TextView) findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);
        
        mEtAccount=(EditText) findViewById(R.id.et_account);
        mEtPassword=(EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
	protected void onDestroy() {
    	mRequestQueue.cancelAll(this);
    	if(mLoginByWeixinTask!=null)
    		mLoginByWeixinTask.cancel();
    	
		super.onDestroy();
	}

	@Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){
        case R.id.tv_register:
            Intent intent=new Intent(this,PhoneRegisterActivity.class);
            startActivity(intent);
            break;
        case R.id.btn_login:
        	loginByAccount();
        	break;
        }
    }
    
    /**
     * 通过账户密码方式登录
     */
    private void loginByAccount(){
    	String account=mEtAccount.getText().toString();
    	String password=mEtPassword.getText().toString(); 
    	
    	if (TextUtils.isEmpty(account)) {
			Utils.showShortToast(this, "请输入手机号");
			return;
		}
    	
    	if (TextUtils.isEmpty(password)) {
			Utils.showShortToast(this, "请输入密码");
			return;
		}
    	
    	LoginByAccountTask request=new LoginByAccountTask(this,account,password,new Listener<LoginResult>() {

			@Override
			public void onResponse(LoginResult arg0) {
				
			}
		},new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
			
		});
		request.setTag(this);       
        mRequestQueue.add(request);
    }
    
    /**
     * 通过微信登录
     */
    private void loginByWeixin(){
    	mLoginByWeixinTask=new LoginByWeixinTask(this, new Listener<LoginResult>() {

			@Override
			public void onResponse(LoginResult arg0) {
				
			}
		},new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
			
		});
    	
    }
    
}
