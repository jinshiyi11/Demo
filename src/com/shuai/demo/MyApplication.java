package com.shuai.demo;

import android.app.Application;
import android.content.Context;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    
    private PushAgent mPushAgent;
    
    @Override
    public void onCreate() {
        initPush();        
    }
    
    /**
     * 初始化友盟push服务
     */
    void initPush(){
        mPushAgent = PushAgent.getInstance(this);
        //mPushAgent.setDebugMode(true);
        
//        UmengMessageHandler messageHandler = new UmengMessageHandler(){
//            @Override
//            public void dealWithCustomMessage(final Context context, final UMessage msg) {
//                new Handler(getMainLooper()).post(new Runnable() {
//                    
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        };
        
//        mPushAgent.setMessageHandler(messageHandler);
        
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                //Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

}
