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

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.widget.Toast;

import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.objects.Area;
import com.cokoguillotte.dfts.objects.CheckBox;
import com.cokoguillotte.dfts.objects.HowTo;
import com.cokoguillotte.dfts.objects.MenuText;
import com.cokoguillotte.dfts.objects.OptionsText;
import com.cokoguillotte.dfts.objects.SpaceShip;

/**
 * Menu de l'application.
 * Il permet d'afficher les différentes possibilités offerte à l'utilisateur au lancement.
 * Il permet également d'afficher les options.
 */
public class DFTSMenu extends BaseGameActivity implements IOnSceneTouchListener {
	
	public enum State {MENU , OPTION , HOWTO}

	//objets
	private Camera				mCamera;
	private SpaceShip			mSpaceship;
	private Area				mArea;
	private MenuText			mMenu;
	
	private OptionsText			mOptions;
	private CheckBox			mCheckBoxMusic,
								mCheckBoxSounds,
								mCheckBoxParticles;
	
	private HowTo				mHowTo;
	
	private boolean				mMusicEnabled,
								mSoundsEnabled,
								mParticlesEnabled;
	
	private State				mState;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT);
		
		mState = State.MENU;
		
		mSpaceship = new SpaceShip();
		mSpaceship.setContext(this);
		
		mArea = new Area();
		mArea.setContext(this);
		
		mMenu = new MenuText();
		mMenu.setContext(this);
		
		mOptions = new OptionsText();
		mOptions.setContext(this);
		
		mCheckBoxMusic = new CheckBox();
		mCheckBoxMusic.setContext(this);
		mCheckBoxSounds = new CheckBox();
		mCheckBoxSounds.setContext(this);
		mCheckBoxParticles = new CheckBox();
		mCheckBoxParticles.setContext(this);
		
		mHowTo = new HowTo();
		mHowTo.setContext(this);
		
		return new Engine(
				new EngineOptions(true, ScreenOrientation.LANDSCAPE,
						new RatioResolutionPolicy(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT), mCamera));
	}

	@Override
	public void onLoadResources() {
		mSpaceship.loadResources(mEngine);
		mArea.loadResources(mEngine);
		mMenu.loadResources(mEngine);
		mOptions.loadResources(mEngine);
		mCheckBoxMusic.loadResources(mEngine);
		mCheckBoxSounds.loadResources(mEngine);
		mCheckBoxParticles.loadResources(mEngine);
		mHowTo.loadResources(mEngine);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		
		mArea.loadScene(scene);
		
		mSpaceship.loadSceneMenu(scene);
		mMenu.loadScene(scene);
		
		mOptions.loadScene(scene);
		mCheckBoxMusic.loadScene(scene);
		mCheckBoxSounds.loadScene(scene);
		mCheckBoxParticles.loadScene(scene);
		mCheckBoxMusic.setPosition(50, (Consts.CAMERA_HEIGHT/2)-65);
		mCheckBoxSounds.setPosition(50, (Consts.CAMERA_HEIGHT/2)-15);
		mCheckBoxParticles.setPosition(50, (Consts.CAMERA_HEIGHT/2)+35);
		
		SharedPreferences settings = getSharedPreferences("dftssettings", 0);
		int bestScore = settings.getInt("best_distance", 0);
		mMenu.setBestScore(bestScore);
		
		mHowTo.loadScene(scene);
		
		scene.setOnSceneTouchListener(this);
		return scene;
	}//onLoadScene

	@Override
	public void onLoadComplete() {
		//rien à faire
	}

	/**
	 * detection d'un appui sur l'écran
	 */
	@Override
	public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
		//on s'interesse à l'endroit ou l'utilisateur releve son doigt
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			float x = pSceneTouchEvent.getX();
			float y = pSceneTouchEvent.getY();

			switch (mState) {
				//Actions dans le menu
				case MENU: {
					//Start
					if( x >= 48 && x <= 153 && y >= 98 && y <= 122) {
						Intent i = new Intent(this, DodgeFromTheSpace.class);
						startActivity(i);
					} else

					//Settings
					if( x >= 48 && x <= 190 && y >= 148 && y <= 172) {
						//chargement des options
						SharedPreferences settings = getSharedPreferences("dftssettings", 0);
						mMusicEnabled = settings.getBoolean("music_enabled", true);
						mSoundsEnabled = settings.getBoolean("sounds_enabled", true);
						mParticlesEnabled = settings.getBoolean("particles_enabled", true);
						//initialisation de l'état des checkbox
						mCheckBoxMusic.setState(mMusicEnabled);
						mCheckBoxSounds.setState(mSoundsEnabled);
						mCheckBoxParticles.setState(mParticlesEnabled);
						//affichage des options
						mState = State.OPTION;
						refresh();
					} else

					//How to play
					if( x >= 48 && x <= 268 && y >= 198 && y <= 222) {
						mState = State.HOWTO;
						refresh();
					} else

					//exit
					if( x >= 46 && x <= 120 && y >= 246 && y <= 272) {
						finish();
					}
				} break;
				//Action dans les options
				case OPTION: {
					//Music
					if( x >= 48 && x <= 188 && y >= 98 && y <= 122) {
						mMusicEnabled = !mMusicEnabled;
						mCheckBoxMusic.setState(mMusicEnabled);
					} else

					//Sounds
					if( x >= 48 && x <= 220 && y >= 148 && y <= 172) {
						mSoundsEnabled = !mSoundsEnabled;
						mCheckBoxSounds.setState(mSoundsEnabled);
					} else

					//Particles
					if( x >= 48 && x <= 265 && y >= 198 && y <= 222) {
						mParticlesEnabled = !mParticlesEnabled;
						mCheckBoxParticles.setState(mParticlesEnabled);
					} else

					//save
					if( x >= 46 && x <= 181 && y >= 246 && y <= 272) {
						Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
						//sauvegarde des options
						SharedPreferences settings = getSharedPreferences("dftssettings", 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean("music_enabled", mMusicEnabled);
						editor.putBoolean("sounds_enabled", mSoundsEnabled);
						editor.putBoolean("particles_enabled", mParticlesEnabled);
						editor.commit();
						//retour au menu
						mState = State.MENU;
						refresh();
					}
				} break;
	
				default: {
					//rien a faire
				}break;
			}//switch
			
		}
		return true;
	}
	
	//Met à jour l'affichage entre le menu ou les options
	public void refresh() {
		

		switch (mState) {
			//Actions dans le menu
			case MENU: {
				mOptions.hide();
				mCheckBoxMusic.hide();
				mCheckBoxSounds.hide();
				mCheckBoxParticles.hide();
				mHowTo.hide();
				mMenu.show();
			} break;
			//Action dans les options
			case OPTION: {
				mMenu.hide();
				mOptions.show();
				mCheckBoxMusic.show();
				mCheckBoxSounds.show();
				mCheckBoxParticles.show();
			} break;
			//Action dans le howto
			case HOWTO: {
				mHowTo.show();
			} break;
			default: {
				//rien a faire
			}break;
		}//switch
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//interception de l'appui sur "back" dans les options
		if((keyCode == KeyEvent.KEYCODE_BACK) && (mState == State.OPTION)) {
			Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
			mState = State.MENU;
			refresh();
			return true;
		} else
		//interception de l'appui sur "back" dans le howto
		if((keyCode == KeyEvent.KEYCODE_BACK) && (mState == State.HOWTO)) {
			mState = State.MENU;
			refresh();
			return true;
		}
		//propagation sinon
		return super.onKeyDown(keyCode, event);
	}
	
} //class