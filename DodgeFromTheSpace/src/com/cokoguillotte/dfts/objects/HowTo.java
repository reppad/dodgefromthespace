package com.cokoguillotte.dfts.objects;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.cokoguillotte.dfts.interfaces.IGraphicsObject;

public class HowTo extends IGraphicsObject implements IUpdateHandler {
	
	public class Asteroid extends AnimatedSprite{

		public Asteroid(float pX, float pY, float pTileWidth,
				float pTileHeight, TiledTextureRegion pTiledTextureRegion) {
			super(pX, pY, pTileWidth, pTileHeight, pTiledTextureRegion);
		}
		
	}
	
	private Texture mTextureObject;
	private TextureRegion mTiledTextureRegionHowto;
	private Engine mEngine;
	private Sprite howtoSprite;
	
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
	}

	@Override
	public void reset() {
	}

	@Override
	public void loadResources(Engine engine) {
		mEngine = engine;
		mTextureObject = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionHowto = TextureRegionFactory.createFromAsset(mTextureObject, mContext, "howtoplay.png", 0, 0);
		mEngine.getTextureManager().loadTextures(mTextureObject);
	}

	@Override
	public void loadScene(Scene scene) {
		howtoSprite = new Sprite(0, 0, mTiledTextureRegionHowto);
		howtoSprite.setVisible(false);
		scene.getTopLayer().addEntity(howtoSprite);
	}
	
	public void show() {
		howtoSprite.setVisible(true);
	}
	
	public void hide() {
		howtoSprite.setVisible(false);
	}

}
