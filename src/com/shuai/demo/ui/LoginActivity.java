package com.shuai.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shuai.demo.R;
import com.shuai.demo.ui.base.BaseActivity;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
    private TextView mTvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        mTvRegister=(TextView) findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){
        case R.id.tv_register:
            Intent intent=new Intent(this,PhoneRegisterActivity.class);
            startActivity(intent);
            break;
        }
    }
    
}
