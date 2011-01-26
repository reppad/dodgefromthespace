package com.cokoguillotte.dfts;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.badlogic.gdx.math.Vector2;
import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.objects.Area;
import com.cokoguillotte.dfts.objects.Asteroids;
import com.cokoguillotte.dfts.objects.DistanceText;
import com.cokoguillotte.dfts.objects.SpaceShip;

public class DodgeFromTheSpace extends BaseGameActivity implements IOnSceneTouchListener {

	//objets
	private Camera				mCamera;
	private PhysicsWorld		mPhysicsWorld;
	
	private SpaceShip			mSpaceship;
	private Asteroids			mAsteroids;
	private Area				mArea;
	private DistanceText		mDistanceText;
	
	private TimerHandler		mEngineAction;
	private boolean				mScreenTouched;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT);
		
		mAsteroids = new Asteroids();
		mAsteroids.setContext(this);
		
		mSpaceship = new SpaceShip();
		mSpaceship.setContext(this);
		
		mArea = new Area();
		mArea.setContext(this);
		
		mDistanceText = new DistanceText();
		mDistanceText.setContext(this);
		
		return new Engine(
				new EngineOptions(true, ScreenOrientation.LANDSCAPE,
						new RatioResolutionPolicy(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT), mCamera));
	}

	@Override
	public void onLoadResources() {
		mAsteroids.loadResources(this.mEngine);
		mSpaceship.loadResources(this.mEngine);
		mArea.loadResources(this.mEngine);
		mDistanceText.loadResources(this.mEngine);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		
		mScreenTouched = false;
		
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		
		mArea.loadScene(scene);
		
		mAsteroids.loadScene(scene);
		mSpaceship.loadScene(scene);
		mSpaceship.createPhysics(mPhysicsWorld);
		mDistanceText.loadScene(scene);
		
		scene.registerUpdateHandler(this.mPhysicsWorld);
		
		
		mEngineAction = new TimerHandler(0.1f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				if(mScreenTouched){
					mSpaceship.speedUpEngine();
					mSpaceship.applyEngineForce(mPhysicsWorld);
				}else{
					mSpaceship.speedDownEngine();
					mSpaceship.applyEngineForce(mPhysicsWorld);
					//mSpaceship.turnOffEngine();
				}
				//increment distance
				mDistanceText.incDistance();
			}
		});
		
		scene.registerUpdateHandler(mEngineAction);
		
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
	public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(mPhysicsWorld != null){
			if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
				mScreenTouched = true;
				return true;
			}else if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
				mScreenTouched = false;
				return true;
			}
		}
		return true;
	}
	
} //class