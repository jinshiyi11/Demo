package com.shuai.demo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.shuai.demo.R;
import com.umeng.message.PushAgent;

public class SplashActivity extends Activity {
    private static long SPLASH_DURATION = 2000;
    
    private Handler mHandler=new Handler();
    private Runnable mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        //为推送服务统计启动信息
        PushAgent.getInstance(this).onAppStart();
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        

        mCallback=new Runnable() {

            @Override
            public void run() {
                mCallback=null;
                startMainActivity();
            }

        };
        mHandler.postDelayed(mCallback, SPLASH_DURATION);

    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if(mCallback!=null){
            mHandler.removeCallbacks(mCallback);
        }
        super.onDestroy();
    }

}
