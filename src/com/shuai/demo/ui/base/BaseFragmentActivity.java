package com.shuai.demo.ui.base;

import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * 增加友盟统计，所有需要使用Activity的地方都应该使用此类做基类
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
