package com.shuai.demo;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

public class MyApplication extends Application {
	private static final String TAG = MyApplication.class.getSimpleName();

	private PushAgent mPushAgent;
	
	/**
     * 网络异步请求对象,用于协议数据的获取
     */
    private static RequestQueue mRequestQueue;
    
    public static RequestQueue getRequestQueue() {
        if(mRequestQueue!=null)
            return mRequestQueue;
        else {
            //不应该为null
            throw new NullPointerException("mRequestQueue is null!");
        }
    }


	@Override
	public void onCreate() {
		initPush();

		// 初始化网络异步请求对象
		mRequestQueue = Volley.newRequestQueue(this);

		initImageLoader();
	}

	/**
	 * 初始化友盟push服务
	 */
	void initPush() {
		mPushAgent = PushAgent.getInstance(this);
		// mPushAgent.setDebugMode(true);

		// UmengMessageHandler messageHandler = new UmengMessageHandler(){
		// @Override
		// public void dealWithCustomMessage(final Context context, final
		// UMessage msg) {
		// new Handler(getMainLooper()).post(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
		// Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
		// }
		// });
		// }
		// };

		// mPushAgent.setMessageHandler(messageHandler);

		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				// Toast.makeText(context, msg.custom,
				// Toast.LENGTH_LONG).show();
			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}
	
	private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(1 * 1024 * 1024)
                .denyCacheImageMultipleSizesInMemory()
                //.discCacheSize(10 * 1024 * 1024)
                .discCacheFileCount(150)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

}
