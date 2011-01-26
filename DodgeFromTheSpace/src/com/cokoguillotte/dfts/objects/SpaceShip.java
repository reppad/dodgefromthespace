package com.cokoguillotte.dfts.objects;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
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
	
	private Texture mTextureSpaceship;
	private TiledTextureRegion mTiledTextureRegionSpaceship;
	private Engine mEngine;
	private AnimatedSprite mSpaceship;
	
	private int mEngineForce;

	@Override
	public void loadResources(Engine engine) {
		mEngine = engine;
		mEngineForce = 0;
		
		mTextureSpaceship = new Texture(256, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionSpaceship = TextureRegionFactory.createTiledFromAsset(mTextureSpaceship, this.mContext, "bugdroid.png", 0, 0, 8, 1);
		mEngine.getTextureManager().loadTextures(mTextureSpaceship);
	}

	@Override
	public void loadScene(Scene scene) {
		//coordonnées du vaisseau
		final int sSPosX = (Consts.CAMERA_WIDTH - mTiledTextureRegionSpaceship.getWidth()) / 3;
		final int sSPosY = (Consts.CAMERA_HEIGHT - mTiledTextureRegionSpaceship.getHeight()) / 2;
		
		//creation du sprite et ajout sur la scene
		mSpaceship = new AnimatedSprite(sSPosX, sSPosY, 48, 48, mTiledTextureRegionSpaceship);
		mSpaceship.animate(100);
		//spaceship.setVelocity(0, Consts.SPACESHIP_VELOCITY);
		
		scene.getTopLayer().addEntity(mSpaceship);
	}
	
	public void createPhysics(PhysicsWorld physicsWorld){
		final Body body;
		body = PhysicsFactory.createCircleBody(physicsWorld, mSpaceship, BodyType.DynamicBody, Consts.FIXTURE_DEF);
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
		final Body body = physicsWorld.getPhysicsConnectorManager().findBodyByShape(mSpaceship);
		body.setLinearVelocity(new Vector2(0, -mEngineForce));
	}
	
	public void speedUpEngine(){
		if(mEngineForce<4){
			mEngineForce++;
		}
	}
	
	public void speedDownEngine(){
		if(mEngineForce>-4){
			mEngineForce--;
		}
	}
	
	public void turnOffEngine(){
		mEngineForce = 0;
	}
	
}