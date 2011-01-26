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
	
	private Texture mTextureObject;
	private TiledTextureRegion mTiledTextureRegionAsteroid;
	private TiledTextureRegion mTiledTextureRegionAstraunaute;
	private Engine mEngine;
	private AnimatedSprite[] mFloatingObjects;
	
	private int mSpawnAstronauteRate;
	
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
		
		mTextureObject = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionAsteroid = TextureRegionFactory.createTiledFromAsset(mTextureObject, this.mContext, "asteroid.png", 0, 0, 1, 1);
		mTiledTextureRegionAstraunaute = TextureRegionFactory.createTiledFromAsset(mTextureObject, this.mContext, "astraunaute.png", 0, 64, 2, 1);
		
		mEngine.getTextureManager().loadTextures(mTextureObject);
	}

	@Override
	public void loadScene(Scene scene) {
		//creation des objets
		mSpawnAstronauteRate = 0;
		
		mFloatingObjects = new AnimatedSprite[6];
		//creation des asteroides
		for (int i = 0; i < 5; i++) {
			mFloatingObjects[i] = new AnimatedSprite(0, -100, mTiledTextureRegionAsteroid);
			mFloatingObjects[i].addShapeModifier(new LoopShapeModifier(new RotationModifier(6, 0, 360)));
			mFloatingObjects[i].setVelocity(-Consts.ASTEROID_VELOCITY, 0);
			scene.getTopLayer().addEntity(mFloatingObjects[i]);
		}
		//creation de l'astronaute
		mFloatingObjects[5] = new AnimatedSprite(0, -100, mTiledTextureRegionAstraunaute);
		mFloatingObjects[5].animate(500);
		mFloatingObjects[5].addShapeModifier(new LoopShapeModifier(new RotationModifier(6, 0, 360)));
		mFloatingObjects[5].setVelocity(-Consts.ASTEROID_VELOCITY, 0);
		scene.getTopLayer().addEntity(mFloatingObjects[5]);
		
		setSpritePositionTimeHandler(1.5f);
	}
	
	//repositionnement des sprites de facon aleatoires sur Y
	private void setSpritePositionTimeHandler(float seconds)
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

				mFloatingObjects[mSpawnAstronauteRate].setPosition(xPos, yPos);
				mSpawnAstronauteRate = (mSpawnAstronauteRate + 1) % 6;
			}
		}));
	}//createSpriteSpawnTimeHandler

}
