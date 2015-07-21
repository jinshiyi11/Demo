package com.shuai.demo.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.shuai.demo.R;
import com.shuai.demo.ui.base.BaseFragmentActivity;
import com.umeng.message.PushAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseFragmentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    
    /**
     * 上次按下back按钮的时间
     */
    private long mLastBackPressedTime;

    //推送
    private PushAgent mPushAgent;

    private ViewPager mViewPager;
    private TabHost mTabHost;
    private TabsAdapter mTabsAdapter;

    public class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener,
            ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
        private final Fragment[] mFragments = new Fragment[4];

        final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args, boolean b) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            if (mFragments[position] == null) {
                mFragments[position] = Fragment.instantiate(mContext, info.clss.getName(), info.args);
            }
            return mFragments[position];
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            //Log.v(TAG, "setPrimaryItem" + position);
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	mContext=this;
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

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this);

        createTabs();

        //防止回收被隐藏的page
        mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount());
    }
    
    @Override
    public void onBackPressed() {
    	if (!getSupportFragmentManager().popBackStackImmediate()) {
    		if(System.currentTimeMillis()-mLastBackPressedTime>2000){
    	        mLastBackPressedTime=System.currentTimeMillis();
    	        Toast.makeText(mContext, R.string.one_more_exit, Toast.LENGTH_SHORT).show();
    	    }else{
    	        super.onBackPressed();
    	    }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 创建maintab
     */
    private void createTabs() {
        String[] tabIds = { "home", "category", "topic", "my" };
        int[] titles = { R.string.tab_home, R.string.tab_category, R.string.tab_topic, R.string.tab_user };
        int[] icons = { R.drawable.tab_home, R.drawable.tab_category, R.drawable.tab_topic, R.drawable.tab_user, };
        Class<?>[] fragments = { HomeFragment.class, CategoryFragment.class, TopicFragment.class,
                UserFragment.class };

        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < tabIds.length; i++) {
            View tab = inflater.inflate(R.layout.tab_with_icon, null);
            tab.setBackgroundColor(0xfff);
            TextView title = (TextView) tab.findViewById(R.id.tab_title);
            title.setText(titles[i]);
            title.setTextColor(getResources().getColorStateList(R.color.main_tab_textcolor));
            ImageView iv = (ImageView) tab.findViewById(R.id.tab_icon);
            iv.setImageResource(icons[i]);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabIds[i]).setIndicator(tab), fragments[i], null, false);

        }
    }

}
