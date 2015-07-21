package com.shuai.demo.protocol;

import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.shuai.demo.data.Constants;
import com.shuai.demo.utils.AppUtils;

/**
 * 协议url辅助拼接类
 */
public class UrlHelper {	
	private static String getUrl(String relativePath, List<BasicNameValuePair> params) {
		StringBuilder builder = new StringBuilder(Constants.SERVER_ADDRESS);
		builder.append("/").append(relativePath);
		String query = params == null ? "" : URLEncodedUtils.format(params, "UTF-8");
		if (query.length() != 0)
			builder.append("?").append(query);

		return builder.toString();
	}
	
	/**
	 * 增加公共参数，如版本号
	 * @param params
	 */
	public static void addCommonParameters(Context context,List<BasicNameValuePair> params){
		params.add(new BasicNameValuePair("version", AppUtils.getVersionName(context)));
	    params.add(new BasicNameValuePair("protocol_version", Constants.PROTOCOL_VERSION));
	    params.add(new BasicNameValuePair("channel", AppUtils.getChannel(context)));
	    params.add(new BasicNameValuePair("device", Constants.DEVICE_INFO));
	}

}
