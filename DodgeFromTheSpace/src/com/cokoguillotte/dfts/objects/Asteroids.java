package com.cokoguillotte.dfts.objects;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.MathUtils;

import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.interfaces.IGraphicsObject;

public class Asteroids extends IGraphicsObject implements IUpdateHandler {
	
	private Texture mTextureAsteroid;
	private TiledTextureRegion mTiledTextureRegionAsteroid;
	private Engine mEngine;
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadResources(Engine engine) {
		mEngine = engine;
		
		mTextureAsteroid = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionAsteroid = TextureRegionFactory.createTiledFromAsset(mTextureAsteroid, this.mContext, "asteroid.png", 0, 0, 1, 1);
		mEngine.getTextureManager().loadTextures(mTextureAsteroid);
	}

	@Override
	public void loadScene(Scene scene) {
		//creation des asteroids
		createSpriteSpawnTimeHandler(1.5f);
	}
	
	//creation de sprites positionnes de facon aléatoires
	private void createSpriteSpawnTimeHandler(float seconds)
	{
		@SuppressWarnings("unused")
		TimerHandler spriteTimerHandler;
		mEngine.registerUpdateHandler(spriteTimerHandler = new TimerHandler(seconds, new ITimerCallback()
		{
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler)
			{
				pTimerHandler.reset();
				//position de depart
				final float xPos = Consts.CAMERA_WIDTH;
				final float yPos = MathUtils.random(0, (Consts.CAMERA_HEIGHT - mTiledTextureRegionAsteroid.getHeight()));

				final AnimatedSprite asteroid = new AnimatedSprite(xPos, yPos, mTiledTextureRegionAsteroid);
				asteroid.addShapeModifier(new LoopShapeModifier(new RotationModifier(6, 0, 360)));
				asteroid.setVelocity(-Consts.ASTEROID_VELOCITY, 0);
				mEngine.getScene().getTopLayer().addEntity(asteroid);
			}
		}));
	}//createSpriteSpawnTimeHandler

}
