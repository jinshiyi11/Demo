package com.shuai.demo.data;


public class Config {
	
	private static Config mSelf;

	private Config() {
	}
	
	public static Config getInstance(){
		if(mSelf!=null)
			return mSelf;
		
		mSelf=new Config();
		return mSelf;
	}
	
	public void loadConfig(){
		
	}
	
	public void saveConfig(){
		
	}

}
