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
import android.widget.Toast;

import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.objects.Area;
import com.cokoguillotte.dfts.objects.MenuText;
import com.cokoguillotte.dfts.objects.SpaceShip;

public class DFTSMenu extends BaseGameActivity implements IOnSceneTouchListener {

	//objets
	private Camera				mCamera;
	
	private SpaceShip			mSpaceship;
	private Area				mArea;
	
	private MenuText			mMenu;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT);
		
		mSpaceship = new SpaceShip();
		mSpaceship.setContext(this);
		
		mArea = new Area();
		mArea.setContext(this);
		
		mMenu = new MenuText();
		mMenu.setContext(this);
		
		return new Engine(
				new EngineOptions(true, ScreenOrientation.LANDSCAPE,
						new RatioResolutionPolicy(Consts.CAMERA_WIDTH, Consts.CAMERA_HEIGHT), mCamera));
	}

	@Override
	public void onLoadResources() {
		mSpaceship.loadResources(this.mEngine);
		mArea.loadResources(this.mEngine);
		mMenu.loadResources(this.mEngine);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		
		mArea.loadScene(scene);
		
		mSpaceship.loadSceneMenu(scene);
		mMenu.loadScene(scene);
		
		scene.setOnSceneTouchListener(this);
		return scene;
	}//onLoadScene

	@Override
	public void onLoadComplete() {
		//rien à faire
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			float x = pSceneTouchEvent.getX();
			float y = pSceneTouchEvent.getY();
			
			//Start
			if( x >= 48 && x <= 153 && y >= 98 && y <= 122) {
				Intent i = new Intent(this, DodgeFromTheSpace.class);
				startActivity(i);
			} else
				
			//Settings
			if( x >= 48 && x <= 190 && y >= 148 && y <= 172) {
				Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
			} else
				
			//How to play
			if( x >= 48 && x <= 268 && y >= 198 && y <= 222) {
				Toast.makeText(this, "How to play", Toast.LENGTH_SHORT).show();
			} else
				
			//exit
			if( x >= 48 && x <= 116 && y >= 248 && y <= 270) {
				finish();
			}
		}
		return true;
	}
	
} //class