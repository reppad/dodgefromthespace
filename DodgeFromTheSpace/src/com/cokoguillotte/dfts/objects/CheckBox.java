package com.cokoguillotte.dfts.objects;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.cokoguillotte.dfts.interfaces.IGraphicsObject;

/**
 * Objet permettant d'afficher des checkbox
 */
public class CheckBox extends IGraphicsObject implements IUpdateHandler {
	
	private Texture mTextureObject;
	private TiledTextureRegion mTiledTextureRegionCheckBox;
	private Engine mEngine;
	
	private AnimatedSprite mCheckBox;
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		// rien à faire
	}

	@Override
	public void reset() {
		// rien à faire
	}

	@Override
	public void loadResources(Engine engine) {
		mEngine = engine;
		
		mTextureObject = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTiledTextureRegionCheckBox = TextureRegionFactory.createTiledFromAsset(mTextureObject, this.mContext, "checkbox.png", 0, 0, 2, 1);
		
		mEngine.getTextureManager().loadTextures(mTextureObject);
	}

	@Override
	public void loadScene(Scene scene) {
		mCheckBox = new AnimatedSprite(0, 0, mTiledTextureRegionCheckBox);
		scene.getTopLayer().addEntity(mCheckBox);
		hide();
	}
	
	/**
	 * 
	 * @param state - true = coché ; false = décoché
	 */
	public void setState(boolean state) {
		if(state)
			mCheckBox.setCurrentTileIndex(0);
		else
			mCheckBox.setCurrentTileIndex(1);
	}
	
	/**
	 * definir la position de la checkbox
	 * @param pX - position sur l'axe X
	 * @param pY - position sur l'axe Y
	 */
	public void setPosition(float pX, float pY) {
		mCheckBox.setPosition(pX, pY);
	}
	
	/**
	 * afficher la checkbox
	 */
	public void show() {
		mCheckBox.setVisible(true);
	}
	
	/**
	 * masquer la checkbox
	 */
	public void hide() {
		mCheckBox.setVisible(false);
	}

}
