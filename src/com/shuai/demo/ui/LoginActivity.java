package com.shuai.demo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shuai.demo.MyApplication;
import com.shuai.demo.R;
import com.shuai.demo.logic.AccountManager;
import com.shuai.demo.protocol.LoginByAccountTask;
import com.shuai.demo.protocol.ResponseError;
import com.shuai.demo.protocol.TokenInfo;
import com.shuai.demo.protocol.RegisterByWeixinTask;
import com.shuai.demo.protocol.RegisterResult;
import com.shuai.demo.ui.base.BaseActivity;
import com.shuai.demo.utils.Utils;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
    private TextView mTvRegister;
    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private LinearLayout mLlWeixinLogin;
    
    private RequestQueue mRequestQueue;
    private RegisterByWeixinTask mLoginByWeixinTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	mContext=this;
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        
        mRequestQueue=MyApplication.getRequestQueue();
        
        mTvRegister=(TextView) findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);
        
        mEtAccount=(EditText) findViewById(R.id.et_account);
        mEtPassword=(EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mLlWeixinLogin=(LinearLayout) findViewById(R.id.ll_weixin_login);
        mBtnLogin.setOnClickListener(this);
        mLlWeixinLogin.setOnClickListener(this);
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
            finish();
            break;
        case R.id.btn_login:
        	loginByAccount();
        	break;
        case R.id.ll_weixin_login:
        	loginByWeixin();
        	break;
        }
    }
    
    /**
     * 通过账户密码方式登录
     */
    private void loginByAccount(){
    	final String account=mEtAccount.getText().toString();
    	final String password=mEtPassword.getText().toString(); 
    	
    	if (TextUtils.isEmpty(account)) {
			Utils.showShortToast(this, "请输入手机号");
			return;
		}
    	
    	if (TextUtils.isEmpty(password)) {
			Utils.showShortToast(this, "请输入密码");
			return;
		}
    	
    	final String md5Password=Utils.md5(password);
    	LoginByAccountTask request=new LoginByAccountTask(this,account,md5Password,new Listener<TokenInfo>() {

			@Override
			public void onResponse(TokenInfo tokenInfo) {
				AccountManager.getInstance().onLoginByPhoneSuccess(tokenInfo.getUid(), tokenInfo.getToken(), md5Password, account);
				finish();
			}
		},new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				Utils.showShortToast(mContext, ResponseError.getErrorMessage(error));
			}
			
		});
    	
		request.setTag(this);       
        mRequestQueue.add(request);
    }
    
    /**
     * 通过微信登录
     */
    private void loginByWeixin(){
    	mLoginByWeixinTask=new RegisterByWeixinTask(this, new Listener<RegisterResult>() {

			@Override
			public void onResponse(RegisterResult result) {
				AccountManager.getInstance().onLoginByWeixinSuccess(result.getUid(), result.getToken(), result.getPassword());
				finish();
			}
		},new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				Utils.showShortToast(mContext, ResponseError.getErrorMessage(error));
			}
			
		});
    	
    }
    
}
