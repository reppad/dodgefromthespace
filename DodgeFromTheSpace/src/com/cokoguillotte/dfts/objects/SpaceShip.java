package com.cokoguillotte.dfts.objects;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.anddev.andengine.entity.particle.modifier.AlphaInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorInitializer;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.RotationInitializer;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.particle.modifier.VelocityInitializer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.interfaces.IGraphicsObject;


public class SpaceShip extends IGraphicsObject implements IUpdateHandler {
	
	public class PlayerShip extends AnimatedSprite{

		public PlayerShip(float pX, float pY, float pTileWidth,
				float pTileHeight, TiledTextureRegion pTiledTextureRegion) {
			super(pX, pY, pTileWidth, pTileHeight, pTiledTextureRegion);
		}
		
	}
	
	private Texture mTextureSpaceship;
	private TiledTextureRegion mTiledTextureRegionSpaceship;
	private TiledTextureRegion mTiledTextureRegionExplosion;
	private Engine mEngine;
	private PlayerShip mSpaceship;
	private AnimatedSprite mExplosion;
	
	private Texture mTextureParticule;
	private TextureRegion mParticleTextureRegion;
	private CircleOutlineParticleEmitter mParticleEmitter;
	private ParticleSystem mParticleSystem;
	
	private int mEngineForce;
	private boolean mForceChanged;
	private boolean mFireEnabled;
	
	@Override
	public void loadResources(Engine engine) {
		mEngine = engine;
		mEngineForce = 0;
		mForceChanged = false;
		
		mTextureSpaceship = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionSpaceship = TextureRegionFactory.createTiledFromAsset(mTextureSpaceship, this.mContext, "bugdroid.png", 0, 0, 8, 1);
		mTiledTextureRegionExplosion = TextureRegionFactory.createTiledFromAsset(mTextureSpaceship, this.mContext, "sprite_explodeoptimized.png", 0, 32, 6, 3);
		mEngine.getTextureManager().loadTextures(mTextureSpaceship);
		
		if(mFireEnabled){
			this.mTextureParticule = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.mParticleTextureRegion = TextureRegionFactory.createFromAsset(this.mTextureParticule, mContext, "particle_fire.png", 0, 0);
			engine.getTextureManager().loadTexture(this.mTextureParticule);
		}
	}

	@Override
	public void loadScene(Scene scene) {
		//coordonnées du vaisseau
		final int sSPosX = ((Consts.CAMERA_WIDTH - mTiledTextureRegionSpaceship.getWidth()) / 2 );
		final int sSPosY = (Consts.CAMERA_HEIGHT - mTiledTextureRegionSpaceship.getHeight()) / 2;
		
		//creation du sprite et ajout sur la scene
		mSpaceship = new PlayerShip(sSPosX, sSPosY, 48, 48, mTiledTextureRegionSpaceship);
		mSpaceship.animate(100);
		//spaceship.setVelocity(0, Consts.SPACESHIP_VELOCITY);
		
		scene.getTopLayer().addEntity(mSpaceship);
		
		if(mFireEnabled){
			mParticleEmitter = new CircleOutlineParticleEmitter(0, 0, 5);
			mParticleSystem = new ParticleSystem(mParticleEmitter, 5, 25, 30, this.mParticleTextureRegion);
			
			mParticleSystem.addParticleInitializer(new ColorInitializer(1, 1, 1));
			mParticleSystem.addParticleInitializer(new AlphaInitializer(0));
			mParticleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			mParticleSystem.addParticleInitializer(new VelocityInitializer(-2, 2, -20, -10));
			mParticleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
			mParticleSystem.addParticleModifier(new ScaleModifier(0.4f, 1.0f, 0, 5));
			mParticleSystem.addParticleModifier(new ColorModifier(1, 1, 1, 0.5f, 1, 0, 0, 1));
			mParticleSystem.addParticleModifier(new ColorModifier(1, 1, 0.5f, 1, 0, 1, 1, 2));
			mParticleSystem.addParticleModifier(new AlphaModifier(0, 1, 0, 0.3f));
			mParticleSystem.addParticleModifier(new AlphaModifier(1, 0, 1, 2));
			mParticleSystem.addParticleModifier(new ExpireModifier(2, 2));
			
			mParticleSystem.setParticlesSpawnEnabled(false);
	
			scene.getTopLayer().addEntity(mParticleSystem);
		}
	}
	
	public void loadSceneMenu(Scene scene) {
		//coordonnées du vaisseau
		final int sSPosX = Consts.CAMERA_WIDTH - 150;
		final int sSPosY = (Consts.CAMERA_HEIGHT - mTiledTextureRegionSpaceship.getHeight()) / 2;
		
		//creation du sprite et ajout sur la scene
		mSpaceship = new PlayerShip(sSPosX, sSPosY, 48, 48, mTiledTextureRegionSpaceship);
		mSpaceship.animate(100);
		
		scene.getTopLayer().addEntity(mSpaceship);
	}
	
	public void createPhysics(PhysicsWorld physicsWorld){
		final Body body;
		body = PhysicsFactory.createCircleBody(physicsWorld, mSpaceship, BodyType.DynamicBody, Consts.SPACESHIP_FIXTURE_DEF);
		body.setUserData(this.mSpaceship);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(mSpaceship, body, true, true, false, false));
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public void applyEngineForce(PhysicsWorld physicsWorld) {
		if(mForceChanged){
			final Body body = physicsWorld.getPhysicsConnectorManager().findBodyByShape(mSpaceship);
			body.setLinearVelocity(new Vector2(0, -mEngineForce));
			mForceChanged = false;
		}
	}
	
	public void speedUpEngine(){
		if(mSpaceship.getY()>5){
			if(mEngineForce<4){
				mEngineForce++;
				mForceChanged = true;
			}
		}else{
			mEngineForce = 0;
			mForceChanged = true;
		}
	}
	
	public void speedDownEngine(){
		if(mSpaceship.getY()<(Consts.CAMERA_HEIGHT-64)){
			if(mEngineForce>-4){
				mEngineForce--;
				mForceChanged = true;
			}
		}else{
			mEngineForce = 0;
			mForceChanged = true;
		}
	}
	
	public void turnOffEngine(){
		mEngineForce = 0;
	}

	public AnimatedSprite getSpaceShip() {
		return mSpaceship;
	}
	
	public void updateFireEffect(){
		if(mFireEnabled){
			mParticleEmitter.setCenter(mSpaceship.getX(), mSpaceship.getY()+mSpaceship.getHeight());
		}
	}
	
	public void setOnFire(boolean active){
		if(mFireEnabled){
			mParticleSystem.setParticlesSpawnEnabled(active);
		}
	}

	public void setEffect(boolean b) {
		mFireEnabled = b;
	}

	public void die(Scene scene) {
		mExplosion = new AnimatedSprite(mSpaceship.getX(), mSpaceship.getY(), 75, 95, mTiledTextureRegionExplosion);
		mExplosion.animate(50, false);
		scene.getTopLayer().addEntity(mExplosion);
		mSpaceship.setVisible(false);
	}
	
}