package com.cokoguillotte.dfts;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.objects.Area;
import com.cokoguillotte.dfts.objects.Asteroids;
import com.cokoguillotte.dfts.objects.SpaceShip;

public class DodgeFromTheSpace extends BaseGameActivity implements IOnSceneTouchListener {

	//objets
	private Camera				mCamera;
	private Texture				mTextureSpaceship;
	private TiledTextureRegion	mTiledTextureRegionSpaceship;
	
	private SpaceShip			mSpaceship;
	private Asteroids			mAsteroids;
	private Area				mArea;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT);
		
		mAsteroids = new Asteroids();
		mAsteroids.setContext(this);
		
		mArea = new Area();
		mArea.setContext(this);
		
		return new Engine(
				new EngineOptions(true, ScreenOrientation.LANDSCAPE,
						new RatioResolutionPolicy(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT), mCamera));
	}

	@Override
	public void onLoadResources() {
		
		mAsteroids.loadResources(this.mEngine);
		mArea.loadResources(this.mEngine);
		
		mTextureSpaceship = new Texture(256, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionSpaceship = TextureRegionFactory.createTiledFromAsset(mTextureSpaceship, this, "bugdroid.png", 0, 0, 8, 1);
		
		mEngine.getTextureManager().loadTextures(mTextureSpaceship);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		
		mArea.loadScene(scene);

		//coordonnées du vaisseau
		final int sSPosX = (Consts.CAMERA_WIDTH - mTiledTextureRegionSpaceship.getWidth()) / 3;
		final int sSPosY = (Consts.CAMERA_HEIGHT - mTiledTextureRegionSpaceship.getHeight()) / 2;

		//creation du sprite et ajout sur la scene
		mSpaceship = new SpaceShip(sSPosX, sSPosY, 48, 48, mTiledTextureRegionSpaceship);
		mSpaceship.animate(100);
		mSpaceship.setVelocity(0, Consts.SPACESHIP_VELOCITY);
		scene.getTopLayer().addEntity(mSpaceship);
		
		mAsteroids.loadScene(scene);
		
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
				mSpaceship.changeDirection();
			}
		});
		return false;
	}
	
} //class