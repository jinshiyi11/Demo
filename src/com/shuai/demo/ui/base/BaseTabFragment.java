package com.shuai.demo.ui.base;

import com.shuai.demo.R;

import android.R.integer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;

/**
 * 用于优化放在ViewPager中的Fragment，在切换到该Fragment对应的tab时才inflate
 */
public abstract class BaseTabFragment extends Fragment {

	private Handler mHandler = new Handler();

	private boolean mInitialized;
	private ViewStub mViewStub;
	private int mLayoutResource;

	public BaseTabFragment(int layoutResource) {
		mLayoutResource = layoutResource;
	}

	@Override
	final public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_stub, container, false);

		mViewStub = (ViewStub) view.findViewById(R.id.viewStub);

		mViewStub.setLayoutResource(mLayoutResource);
		mViewStub.setOnInflateListener(new OnInflateListener() {

			@Override
			public void onInflate(ViewStub stub, View inflated) {
				onInit(inflated);
			}
		});
		return view;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		if(isVisibleToUser && !mInitialized){
			mInitialized=true;
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					mViewStub.setVisibility(View.VISIBLE);					
				}
			});
		}
	}
	
	protected abstract void onInit(View inflated);

}
