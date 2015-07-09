package com.shuai.demo.ui;

import android.app.Activity;
import android.os.Bundle;

import com.shuai.demo.R;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private PushAgent mPushAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //为推送服务统计启动信息
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        mPushAgent.enable();
        
        //自动更新
        UmengUpdateAgent.update(this);
        //UmengUpdateAgent.silentUpdate(this);静默下载更新
        UmengUpdateAgent.setUpdateOnlyWifi(false);
    }

}
