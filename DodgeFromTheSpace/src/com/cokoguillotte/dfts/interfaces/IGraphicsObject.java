package com.cokoguillotte.dfts.interfaces;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;

import android.content.Context;

public abstract class IGraphicsObject {
	protected Context mContext;
	
	//Accesseurs
	public void setContext(Context context){
		mContext = context;
	}
	
	//Moteur graphique
	public abstract void loadResources(Engine engine);
	public abstract void loadScene(Scene scene);
}
