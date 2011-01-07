package com.cokoguillotte.dfts;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class DodgeFromTheSpace extends BaseGameActivity {

	//constantes
	public static final int CAMERA_WIDTH = 480;
	public static final int CAMERA_HEIGHT = 320;

	public static final float VELOCITY = 100.0f;

	//objets
	private Camera				mCamera;
	private Texture				mTextureSpaceship,
								mTextureBackground;
	private TiledTextureRegion	mTiledTextureRegionSpaceship;	//sprite anime
	private TextureRegion		mTextureRegionBackground;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(
				new EngineOptions(true, ScreenOrientation.LANDSCAPE,
						new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		mTextureSpaceship = new Texture(256, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionSpaceship = TextureRegionFactory.createTiledFromAsset(
				mTextureSpaceship, this, "spaceship.png", 0, 0, 4, 1);
		
		mTextureBackground = new Texture(1024, 512, TextureOptions.DEFAULT);
		mTextureRegionBackground = TextureRegionFactory.createFromAsset(
				mTextureBackground, this, "background.png", 0, 0);

		mEngine.getTextureManager().loadTextures(mTextureSpaceship, mTextureBackground);
	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene(1);
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
        autoParallaxBackground.addParallaxEntity(new ParallaxEntity(-5.0f,
        		new Sprite(0, CAMERA_HEIGHT - this.mTextureRegionBackground.getHeight(), this.mTextureRegionBackground)));
        scene.setBackground(autoParallaxBackground);

		//coordonnées du vaisseau
		final int posX = (CAMERA_WIDTH - this.mTiledTextureRegionSpaceship.getWidth()) / 3;
		final int posY = (CAMERA_HEIGHT - this.mTiledTextureRegionSpaceship.getHeight()) / 2;

		//creation du sprite et ajout sur la scene
		final SpaceShip spaceship = new SpaceShip(posX, posY, mTiledTextureRegionSpaceship);
		spaceship.animate(100);
		spaceship.setVelocity(0, VELOCITY);
		scene.getTopLayer().addEntity(spaceship);

		return scene;
	}

	@Override
	public void onLoadComplete() {
		//rien à faire
	}
}