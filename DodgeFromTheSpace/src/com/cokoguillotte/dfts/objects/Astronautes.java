package com.cokoguillotte.dfts.objects;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.ParallelShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
import org.anddev.andengine.entity.shape.modifier.ScaleModifier;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.cokoguillotte.dfts.DodgeFromTheSpace;
import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.interfaces.IGraphicsObject;

public class Astronautes extends IGraphicsObject implements IUpdateHandler {
	
	public class Astronaute extends AnimatedSprite{

		public Astronaute(float pX, float pY, float pTileWidth,
				float pTileHeight, TiledTextureRegion pTiledTextureRegion) {
			super(pX, pY, pTileWidth, pTileHeight, pTiledTextureRegion);
		}
		
	}
	
	private Texture mTextureObject;
	private TiledTextureRegion mTiledTextureRegionAstraunaute;
	private Engine mEngine;
	
	private PhysicsWorld mPhysicsWorld;
	private Astronaute mAstronaute;
	
	private boolean mSauvetageOK;
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public void registerPhysicsWorld(PhysicsWorld physicsWorld){
		this.mPhysicsWorld = physicsWorld;
	}

	@Override
	public void loadResources(Engine engine) {
		mEngine = engine;
		mTextureObject = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionAstraunaute = TextureRegionFactory.createTiledFromAsset(mTextureObject, this.mContext, "astraunaute.png", 0, 0, 2, 1);
		mEngine.getTextureManager().loadTextures(mTextureObject);
	}

	@Override
	public void loadScene(Scene scene) {
		//creation des objets
		mSauvetageOK = false;
		
		mAstronaute = new Astronaute(0, -100, 64, 64, mTiledTextureRegionAstraunaute);
		mAstronaute.animate(500);
		
		mAstronaute.addShapeModifier(new LoopShapeModifier(new RotationModifier(6, 0, 360)));
		mAstronaute.setUpdatePhysics(false);
		
		Body movingBody = PhysicsFactory.createBoxBody(mPhysicsWorld, mAstronaute, BodyType.DynamicBody, Consts.ASTRONAUTE_FIXTURE_DEF);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mAstronaute, movingBody, true, true, false, false));
		movingBody.setLinearVelocity(new Vector2(-3, 0));
		
		movingBody.setUserData(mAstronaute);
		scene.getTopLayer().addEntity(mAstronaute);		
	}
	
	public void setSpritePositionTimeHandler(float seconds)
	{
		@SuppressWarnings("unused")
		TimerHandler spriteTimerHandler;
		mEngine.registerUpdateHandler(spriteTimerHandler = new TimerHandler(seconds, new ITimerCallback()
		{
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler){
				
				pTimerHandler.reset();
				//position de depart
				
				if(!mSauvetageOK){
					DodgeFromTheSpace.mLife = DodgeFromTheSpace.mLife-1;
				}
				
				mSauvetageOK = false;
				
				final float xPos = Consts.CAMERA_WIDTH;
				final float yPos = MathUtils.random(0, (Consts.CAMERA_HEIGHT - mTiledTextureRegionAstraunaute.getHeight()));

				final Body body = mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(mAstronaute);
				body.setAwake(true);
				body.setTransform(new Vector2(xPos/32,yPos/32), 0);
				body.setLinearVelocity(new Vector2(-3, 0));
				mAstronaute.setVisible(true);
			}
		}));
	}//createSpriteSpawnTimeHandler

	public AnimatedSprite getAstraunote() {
		return mAstronaute;
	}
	
	public void setAstraunoteSaved() {
		mAstronaute.setVisible(false);
		final Body body = mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(mAstronaute);
		//body.setTransform(new Vector2(Consts.CAMERA_WIDTH,100), 0);
		body.setLinearVelocity(new Vector2(0, 0));
		body.setAwake(false);
		
		mSauvetageOK = true;
	}

	public boolean isSaved() {
		return mSauvetageOK;
	}

}
