package com.cokoguillotte.dfts.objects;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.LoopShapeModifier;
import org.anddev.andengine.entity.shape.modifier.RotationModifier;
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
import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.interfaces.IGraphicsObject;

public class Asteroids extends IGraphicsObject implements IUpdateHandler {
	
	public class Asteroid extends AnimatedSprite{

		public Asteroid(float pX, float pY, float pTileWidth,
				float pTileHeight, TiledTextureRegion pTiledTextureRegion) {
			super(pX, pY, pTileWidth, pTileHeight, pTiledTextureRegion);
		}
		
	}
	
	private Texture mTextureObject;
	private TiledTextureRegion mTiledTextureRegionAsteroid;
	private TiledTextureRegion mTiledTextureRegionAstraunaute;
	private Engine mEngine;
	private PhysicsWorld mPhysicsWorld;
	private float mSeconds;
	
	private TimerHandler mSpriteTimerHandler;
	private Asteroid[] mAsteroids;
	
	private int mSpawnAstronauteRate;
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
	
		mTiledTextureRegionAsteroid = TextureRegionFactory.createTiledFromAsset(mTextureObject, this.mContext, "asteroid.png", 0, 0, 1, 1);
		mTiledTextureRegionAstraunaute = TextureRegionFactory.createTiledFromAsset(mTextureObject, this.mContext, "astraunaute.png", 0, 64, 2, 1);
		
		mEngine.getTextureManager().loadTextures(mTextureObject);
	}

	@Override
	public void loadScene(Scene scene) {
		//creation des objets
		mSpawnAstronauteRate = 0;
		mSauvetageOK = false;
		
		mAsteroids = new Asteroid[10];
		
		//creation des asteroides
		for (int i = 0; i < 10; i++) {
			mAsteroids[i] = new Asteroid(0, -100, 64, 64, mTiledTextureRegionAsteroid);
			mAsteroids[i].addShapeModifier(new LoopShapeModifier(new RotationModifier(6, 0, 360)));
			mAsteroids[i].setUpdatePhysics(false);
		
			Rectangle physicsHitBox = new Rectangle(mAsteroids[i].getX()+16,mAsteroids[i].getY()+16,32,32);
			
			Body movingBody = PhysicsFactory.createCircleBody(mPhysicsWorld, physicsHitBox, BodyType.DynamicBody, Consts.ASTEROIDS_FIXTURE_DEF);
			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(mAsteroids[i], movingBody, true, true, false, false));
			movingBody.setLinearVelocity(new Vector2(-3, 0));
			
			movingBody.setUserData(this.mAsteroids[i]);
			scene.getTopLayer().addEntity(mAsteroids[i]);

		}
		
		//creation de l'astronaute
		/*mAsteroids[5] = new AnimatedSprite(0, -100, mTiledTextureRegionAstraunaute);
		mAsteroids[5].animate(500);
		mAsteroids[5].addShapeModifier(new LoopShapeModifier(new RotationModifier(4, 0, 360)));
		mAsteroids[5].setVelocity(-Consts.ASTEROID_VELOCITY, 0);
		scene.getTopLayer().addEntity(mAsteroids[5]);
		*/
		
	}
	
	//repositionnement des sprites de facon aleatoires sur Y
	public  void setSpritePositionTimeHandler(){
		mSeconds = 1.5f;
		mSpriteTimerHandler = new TimerHandler(mSeconds, new ITimerCallback(){
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler)
			{
				pTimerHandler.reset();
				//position de depart
				final float xPos = Consts.CAMERA_WIDTH;
				final float yPos = MathUtils.random(0, (Consts.CAMERA_HEIGHT - mTiledTextureRegionAsteroid.getHeight()));

				//mFloatingObjects[mSpawnAstronauteRate].setPosition(xPos, yPos);
				
				if(mSpawnAstronauteRate<5){
					final Body body = mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(mAsteroids[mSpawnAstronauteRate]);
					body.setTransform(new Vector2(xPos/32,yPos/32), 0);
					body.setLinearVelocity(new Vector2(-3-(DistanceText.mDistance/200), 0));
				}
				
				if(mSpawnAstronauteRate==5){ mAsteroids[5].setVisible(true); }
				mSpawnAstronauteRate = (mSpawnAstronauteRate + 1) % 6;
			}
		});
		
		mEngine.registerUpdateHandler(mSpriteTimerHandler);
	}//createSpriteSpawnTimeHandler
	
	public void updateTimer() {
		if(mSeconds>0.4){
			mSeconds -= 0.1f;
			mEngine.unregisterUpdateHandler(mSpriteTimerHandler);
			mSpriteTimerHandler.setTimerSeconds(mSeconds);
			mEngine.registerUpdateHandler(mSpriteTimerHandler);
		}
	}

	public AnimatedSprite getAstraunote() {
		return mAsteroids[5];
	}
	
	public AnimatedSprite[] getAsteroids(){
		return mAsteroids;
	}

	public void setAstraunoteSaved() {
		mAsteroids[5].setVisible(false);
		mSauvetageOK = true;
	}

}
