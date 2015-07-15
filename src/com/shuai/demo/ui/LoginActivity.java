package com.shuai.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shuai.demo.R;
import com.shuai.demo.ui.base.BaseActivity;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
    private TextView mTvRegister;
    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        mTvRegister=(TextView) findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);
        
        mEtAccount=(EditText) findViewById(R.id.et_account);
        mEtPassword=(EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
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
    	
    	
    }
    
    /**
     * 通过微信登录
     */
    private void loginByWeixin(){
    	
    }
    
}
