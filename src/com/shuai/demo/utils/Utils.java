package com.shuai.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class Utils {
    private static final String TAG=Utils.class.getSimpleName();
    
    public static void showShortToast(Context context,String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    
    public static void showLongToast(Context context,String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 在浏览器中打开url
     * @param context
     * @param url
     */
    public static void openBrowser(Context context,String url){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


}
