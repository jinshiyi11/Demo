package com.shuai.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class Utils {
    private static final String TAG=Utils.class.getSimpleName();

    /**
     * 在浏览器中打开url
     * @param context
     * @param url
     */
    void openBrowser(Context context,String url){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


}
