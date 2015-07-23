package com.shuai.demo.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.shuai.demo.R;
import com.shuai.demo.ui.base.BaseTabFragment;
import com.shuai.demo.utils.PhotoUtils;

public class HomeFragment extends BaseTabFragment {

	private final String TAG = HomeFragment.class.getSimpleName();

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private TextView mTvTest;

	private String mImagePath;

	public HomeFragment() {
		super(R.layout.fragment_home);
	}

	@Override
	protected void onInit(View inflated) {
		mTvTest = (TextView) inflated.findViewById(R.id.textView1);
		Log.d(TAG, mTvTest.toString());

		Button btnLogin = (Button) inflated.findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});

		Button btnCarema = (Button) inflated.findViewById(R.id.btn_camera);
		btnCarema.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date());
				mImagePath = PhotoUtils.getPhotoDir(mContext)
						+ File.separatorChar + "photo_" + timeStamp + ".jpg";

				PhotoUtils.takePhoto((Activity) mContext, HomeFragment.this,
						mImagePath, REQUEST_IMAGE_CAPTURE);

			}

		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE
				&& resultCode == Activity.RESULT_OK) {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String outPath = PhotoUtils.getPhotoDir(mContext)+ File.separatorChar + "photo_" + timeStamp + "_70.jpg";
			PhotoUtils.compressPhoto(mImagePath,outPath);
			
			PhotoUtils.copyExifAttributes(mImagePath, outPath);
		}

	}

}
