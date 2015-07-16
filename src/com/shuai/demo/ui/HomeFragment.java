package com.shuai.demo.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
        
        Button btnLogin=(Button) inflated.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				startActivity(intent);
			}
		});
    }
    
}
