package com.shuai.demo.ui;

import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.TextView;

import com.shuai.demo.R;

public class HomeFragment extends Fragment {
    private final String TAG=HomeFragment.class.getSimpleName();
    private ViewStub mViewStub;
    private TextView mTvTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_stub, container, false);
        
        mViewStub=(ViewStub) view.findViewById(R.id.viewStub);
        
        mViewStub.setLayoutResource(R.layout.fragment_home);
        mViewStub.setOnInflateListener(new OnInflateListener() {
            
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                OnInit(inflated);
            }
        });
        return view;        
    }

    protected void OnInit(View inflated) {
        mTvTest=(TextView) inflated.findViewById(R.id.textView1);
        Log.d(TAG, mTvTest.toString());
    }
    
}
