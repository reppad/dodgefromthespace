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
		
		return true;
	}
	
} //class