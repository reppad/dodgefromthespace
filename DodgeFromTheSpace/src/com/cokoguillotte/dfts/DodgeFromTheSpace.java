package com.cokoguillotte.dfts;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.ParallelShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.MathUtils;

public class DodgeFromTheSpace extends BaseGameActivity implements IOnSceneTouchListener {

	//constantes
	public static final int CAMERA_WIDTH = 480;
	public static final int CAMERA_HEIGHT = 320;

	public static final float SPACESHIP_VELOCITY = 100.0f;
	public static final float ASTEROID_VELOCITY = 80.0f;

	//objets
	private Camera				mCamera;
	private Texture				mTextureSpaceship,
								mTextureAsteroid,
								mTextureBackground;
	private TiledTextureRegion	mTiledTextureRegionSpaceship,
								mTiledTextureRegionAsteroid;
	private TextureRegion		mTextureRegionBackground0, mTextureRegionBackground1, mTextureRegionBackground2;
	private SpaceShip			mSpaceship;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(
				new EngineOptions(true, ScreenOrientation.LANDSCAPE,
						new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
	}

	@Override
	public void onLoadResources() {
		mTextureSpaceship = new Texture(256, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTextureAsteroid = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionSpaceship = TextureRegionFactory.createTiledFromAsset(mTextureSpaceship, this, "bugdroid.png", 0, 0, 8, 1);
		mTiledTextureRegionAsteroid = TextureRegionFactory.createTiledFromAsset(mTextureAsteroid, this, "asteroid.png", 0, 0, 1, 1);
		
		mTextureBackground = new Texture(512, 1024, TextureOptions.DEFAULT);
		mTextureRegionBackground0 = TextureRegionFactory.createFromAsset(mTextureBackground, this, "fond0.png", 0, 0);
		mTextureRegionBackground1 = TextureRegionFactory.createFromAsset(mTextureBackground, this, "fond1.png", 0, 320);
		mTextureRegionBackground2 = TextureRegionFactory.createFromAsset(mTextureBackground, this, "fond2.png", 0, 640);

		mEngine.getTextureManager().loadTextures(mTextureSpaceship, mTextureAsteroid, mTextureBackground);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		
		//background
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
        autoParallaxBackground.addParallaxEntity(new ParallaxEntity(-1.0f,
        		new Sprite(0, 0, 480, 320, mTextureRegionBackground0)));
        autoParallaxBackground.addParallaxEntity(new ParallaxEntity(-4.0f,
        		new Sprite(0, 0, 480, 320, mTextureRegionBackground1)));
        autoParallaxBackground.addParallaxEntity(new ParallaxEntity(-8.0f,
        		new Sprite(0, 0, 480, 320, mTextureRegionBackground2)));
        scene.setBackground(autoParallaxBackground);

		//coordonnées du vaisseau
		final int sSPosX = (CAMERA_WIDTH - mTiledTextureRegionSpaceship.getWidth()) / 3;
		final int sSPosY = (CAMERA_HEIGHT - mTiledTextureRegionSpaceship.getHeight()) / 2;

		//creation du sprite et ajout sur la scene
		mSpaceship = new SpaceShip(sSPosX, sSPosY, 48, 48, mTiledTextureRegionSpaceship);
		mSpaceship.animate(100);
		mSpaceship.setVelocity(0, SPACESHIP_VELOCITY);
		scene.getTopLayer().addEntity(mSpaceship);
		
		//creation des asteroids
		createSpriteSpawnTimeHandler(1.5f);
		
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

	//creation de sprites positionnes de facon aléatoires
	private void createSpriteSpawnTimeHandler(float seconds)
	{
		@SuppressWarnings("unused")
		TimerHandler spriteTimerHandler;
		getEngine().registerUpdateHandler(spriteTimerHandler = new TimerHandler(seconds, new ITimerCallback()
		{
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler)
			{
				pTimerHandler.reset();
				//position de depart
				final float xPos = CAMERA_WIDTH;
				final float yPos = MathUtils.random(0, (CAMERA_HEIGHT - mTiledTextureRegionAsteroid.getHeight()));

				final Asteroid asteroid = new Asteroid(xPos, yPos, mTiledTextureRegionAsteroid);
				asteroid.addShapeModifier(new LoopShapeModifier(new RotationModifier(6, 0, 360)));
				asteroid.setVelocity(-ASTEROID_VELOCITY, 0);
				mEngine.getScene().getTopLayer().addEntity(asteroid);
			}
		}));
	}//createSpriteSpawnTimeHandler

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