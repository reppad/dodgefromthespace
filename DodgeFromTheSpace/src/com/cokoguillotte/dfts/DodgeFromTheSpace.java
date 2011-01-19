package com.cokoguillotte.dfts;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.objects.Area;
import com.cokoguillotte.dfts.objects.Asteroids;
import com.cokoguillotte.dfts.objects.SpaceShip;

public class DodgeFromTheSpace extends BaseGameActivity implements IOnSceneTouchListener {

	//objets
	private Camera				mCamera;
	
	private SpaceShip			mSpaceship;
	private Asteroids			mAsteroids;
	private Area				mArea;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT);
		
		mAsteroids = new Asteroids();
		mAsteroids.setContext(this);
		
		mSpaceship = new SpaceShip();
		mSpaceship.setContext(this);
		
		mArea = new Area();
		mArea.setContext(this);
		
		return new Engine(
				new EngineOptions(true, ScreenOrientation.LANDSCAPE,
						new RatioResolutionPolicy(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT), mCamera));
	}

	@Override
	public void onLoadResources() {
		mAsteroids.loadResources(this.mEngine);
		mSpaceship.loadResources(this.mEngine);
		mArea.loadResources(this.mEngine);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		
		mArea.loadScene(scene);
		
		mAsteroids.loadScene(scene);
		mSpaceship.loadScene(scene);
		
		//TODO collisions (surement pas a faire ici)
//		scene.registerUpdateHandler(new IUpdateHandler() {
//			
//			@Override
//			public void reset() { }
//
//			@Override
//			public void onUpdate(final float pSecondsElapsed) {
//				if(spaceship.collidesWith(asteroid)) {
//					
//				}
//			}
//		});
		
		scene.setOnSceneTouchListener(this);
		return scene;
	}//onLoadScene

	@Override
	public void onLoadComplete() {
		//rien à faire
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
//				mSpaceship.changeDirection();
			}
		});
		return false;
	}
	
} //class