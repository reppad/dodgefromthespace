package com.cokoguillotte.dfts.interfaces;

import org.anddev.andengine.engine.Engine;

import android.content.Context;

public abstract class IAudioObject {
	protected Context mContext;
	
	public void setContext(Context context){
		mContext = context;
	}
	
	public abstract void loadResources(Engine engine);
}
