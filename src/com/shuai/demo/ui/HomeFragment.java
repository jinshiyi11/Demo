package com.shuai.demo.ui;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shuai.demo.R;
import com.shuai.demo.ui.base.BaseTabFragment;

public class HomeFragment extends BaseTabFragment {

	private final String TAG=HomeFragment.class.getSimpleName();
    private TextView mTvTest;
    
    public HomeFragment() {
		super(R.layout.fragment_home);
	}

    @Override
	protected void onInit(View inflated) {
        mTvTest=(TextView) inflated.findViewById(R.id.textView1);
        Log.d(TAG, mTvTest.toString());
    }
    
}
