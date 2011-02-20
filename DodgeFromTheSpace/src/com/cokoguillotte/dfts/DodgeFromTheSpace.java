package com.cokoguillotte.dfts;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.ParallelShapeModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.HorizontalAlign;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.objects.Area;
import com.cokoguillotte.dfts.objects.Asteroids;
import com.cokoguillotte.dfts.objects.Asteroids.Asteroid;
import com.cokoguillotte.dfts.objects.Astronautes;
import com.cokoguillotte.dfts.objects.Astronautes.Astronaute;
import com.cokoguillotte.dfts.objects.DistanceText;
import com.cokoguillotte.dfts.objects.MusicPlayer;
import com.cokoguillotte.dfts.objects.Pause;
import com.cokoguillotte.dfts.objects.SoundsPlayer;
import com.cokoguillotte.dfts.objects.SoundsPlayer.SoundsList;
import com.cokoguillotte.dfts.objects.SpaceShip;
import com.cokoguillotte.dfts.objects.SpaceShip.PlayerShip;

public class DodgeFromTheSpace extends BaseGameActivity implements IOnSceneTouchListener {

	//objets
	private Camera				mCamera;
	private PhysicsWorld		mPhysicsWorld;
	private Scene				mScene;
	
	private SpaceShip			mSpaceship;
	private Asteroids			mAsteroids;
	private Astronautes			mAstronaute;
	private SoundsPlayer 		mSounds;
	private MusicPlayer 		mMusic;
	
	private Area				mArea;
	private DistanceText		mDistanceText;
	
	private TimerHandler		mEngineAction;
	private TimerHandler		mStartGame;
	private TimerHandler		mDifficultyPlusPlus;
	private TimerHandler		mGameOverScene;
	
	private boolean				mScreenTouched;
	private boolean				mGameOver;
	private boolean 			mStart;
	public 	static int			mLife;
	private int					mOldBestDistance;
	
	private CameraScene			mPauseScene;
	private Pause				mPause;
	
	private Texture mFontTexture;
	private Font mFont;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT);
		
		SharedPreferences settings = getSharedPreferences("dftssettings", 0);
		boolean musicEnabled = settings.getBoolean("music_enabled", true);
		boolean soundsEnabled = settings.getBoolean("sounds_enabled", true);
		boolean particlesEnabled = settings.getBoolean("particles_enabled", true);
		
		mAsteroids = new Asteroids();
		mAsteroids.setContext(this);
		
		mSpaceship = new SpaceShip();
		mSpaceship.setContext(this);
		mSpaceship.setEffect(particlesEnabled);
		
		mSounds = new SoundsPlayer(soundsEnabled);
		mSounds.setContext(this);
		
		mMusic = new MusicPlayer(musicEnabled);
		mMusic.setContext(this);
		
		mArea = new Area();
		mArea.setContext(this);
		
		mDistanceText = new DistanceText();
		mDistanceText.setContext(this);
		
		mAstronaute = new Astronautes();
		mAstronaute.setContext(this);
		
		mPause = new Pause();
		mPause.setContext(this);
		
		EngineOptions eo = new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT), mCamera);
		eo.setNeedsSound(true);
		eo.setNeedsMusic(true);

		return new Engine(eo);
	}

	@Override
	public void onLoadResources() {
		mAsteroids.loadResources(this.mEngine);
		mSpaceship.loadResources(this.mEngine);
		mArea.loadResources(this.mEngine);
		mDistanceText.loadResources(this.mEngine);
		mAstronaute.loadResources(this.mEngine);
		mSounds.loadResources(this.mEngine);
		mMusic.loadResources(this.mEngine);
		mPause.loadResources(this.mEngine);
		
		this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this, "starjedi.ttf", 32, true, Color.WHITE);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);
	}

	@Override
	public Scene onLoadScene() {
		mScene = new Scene(1);
		mStart = false;
		mScreenTouched = false;
		mGameOver = false;
		mLife = 4;
		
		mPauseScene = new CameraScene(1, this.mCamera);
		mPause.loadScene(mPauseScene);
		mPauseScene.setBackgroundEnabled(false);
		
		//this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		this.mPhysicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
		mAsteroids.registerPhysicsWorld(mPhysicsWorld);
		mAstronaute.registerPhysicsWorld(mPhysicsWorld);
		
		mArea.loadScene(mScene);
		mDistanceText.loadScene(mScene);
		mAsteroids.loadScene(mScene);
		mSpaceship.loadScene(mScene);
		mSpaceship.createPhysics(mPhysicsWorld);
		mAstronaute.loadScene(mScene);
		
		mScene.registerUpdateHandler(this.mPhysicsWorld);
		
		mEngineAction = new TimerHandler(0.1f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				if(!mGameOver && mStart){
					if(mScreenTouched){
						mSpaceship.speedUpEngine();
						mSpaceship.setOnFire(true);
						mSpaceship.applyEngineForce(mPhysicsWorld);
					}else{
						mSpaceship.speedDownEngine();
						mSpaceship.applyEngineForce(mPhysicsWorld);
						mSpaceship.setOnFire(false);
						//mSpaceship.turnOffEngine();
					}
					//increment distance
					mDistanceText.incDistance();
					mDistanceText.updateHud(mLife);
					
					if(mLife<1){
						mGameOver = true;
						playGameOver();
					}
					
					if(mSpaceship!=null){
						mSpaceship.updateFireEffect();
					}
				}
			}
		});
		
		mStartGame = new TimerHandler(3f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				final Text text = new Text(Consts.CAMERA_WIDTH/2, Consts.CAMERA_HEIGHT/2-45, mFont, "Go !", HorizontalAlign.CENTER);
				text.addShapeModifier(
						new SequenceShapeModifier(
								new ParallelShapeModifier(
										new AlphaModifier(0.5f, 0.0f, 1.0f),
										new ScaleModifier(2, 0.0f, 10.0f)
								),
								new AlphaModifier(0.1f, 1.0f, 0.0f)
						)
				);
				text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				mScene.getTopLayer().addEntity(text);
				mStart = true;
				mAsteroids.setSpritePositionTimeHandler();
				mAstronaute.setSpritePositionTimeHandler(6f);
				mScene.unregisterUpdateHandler(mStartGame);
				mScene.registerUpdateHandler(mDifficultyPlusPlus);
			}
		});
		
		mDifficultyPlusPlus = new TimerHandler(10f, true, new ITimerCallback() {
				public void onTimePassed(TimerHandler pTimerHandler) {
					mAsteroids.updateTimer();
				}
		});
		
		mGameOverScene = new TimerHandler(5f, true, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) {
				finish();
			}
		});
		
		
		mPhysicsWorld.setContactListener(new ContactListener(){
			@Override
			public void beginContact(Contact contact) {
				if(!mGameOver && mStart){
					if (contact.getFixtureA().getBody().getUserData() != null){
						if (contact.getFixtureA().getBody().getUserData() instanceof Asteroid && 
							contact.getFixtureB().getBody().getUserData() instanceof PlayerShip){
							//Log.d("Test","Hitted !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							mSpaceship.die(mScene);
							mSounds.play(SoundsList.boom);
							mGameOver = true;
							playGameOver();
						}
						if (contact.getFixtureA().getBody().getUserData() instanceof Astronaute && 
								contact.getFixtureB().getBody().getUserData() instanceof PlayerShip){
							if(!mAstronaute.isSaved()){
								mAstronaute.setAstraunoteSaved();
								mSounds.play(SoundsList.bonus);
							}
						}
					}
				}
			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
			}
			
		});
		
		/*scene.registerUpdateHandler(new IUpdateHandler() {
		
			@Override
			public void reset() { }
			
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				if(!mGameOver){
					if(mSpaceship.getSpaceShip().collidesWith(mAsteroids.getAstraunote())) {
						mAsteroids.setAstraunoteSaved();
					}
					
					AnimatedSprite []asteroids = mAsteroids.getAsteroids();
					for(int i = 0; i<5;i++){
						if(mSpaceship.getSpaceShip().collidesWith(asteroids[i])) {
							//Mort du vaisseau !!!
							mSpaceship.die(scene);
							mGameOver = true;
						}
					}
				}
			}
		});*/
		
		mScene.setOnSceneTouchListener(this);
		return mScene;
	}//onLoadScene

	@Override
	public void onLoadComplete() {
		runOnUpdateThread(new Runnable() {
            public void run() {
        		mScene.registerUpdateHandler(mEngineAction);
        		mScene.registerUpdateHandler(mStartGame);
        		mMusic.play();
            }
		});

	}
	
	private void playGameOver(){
		final Text text = new Text(Consts.CAMERA_WIDTH/2-100, Consts.CAMERA_HEIGHT/2-35, mFont, "Game over", HorizontalAlign.CENTER);
		text.addShapeModifier(
				new SequenceShapeModifier(
						new ParallelShapeModifier(
								new AlphaModifier(0.5f, 0.0f, 1.0f),
								new ScaleModifier(2, 0.0f, 2.0f)
						)
				)
		);
		text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mScene.getTopLayer().addEntity(text);
		mScene.registerUpdateHandler(mGameOverScene);
		
		//Enregistrement du new best score si besoin
		if(DistanceText.mDistance > mOldBestDistance){
			SharedPreferences settings = getSharedPreferences("dftssettings", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("best_distance", DistanceText.mDistance);
			editor.commit();
		}
	}
	

	@Override
	public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(mPhysicsWorld != null && !mGameOver){
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
	


	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(this.mEngine.isRunning()) {
				this.mScene.setChildScene(this.mPauseScene, false, true, true);
				this.mEngine.stop();
			} else {
				this.mScene.clearChildScene();
				this.mEngine.start();
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}
	
} //class