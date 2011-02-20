package com.cokoguillotte.dfts.objects;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.interfaces.IGraphicsObject;

public class Pause extends IGraphicsObject implements IUpdateHandler {

	private Texture mTexture;
	private TextureRegion mPausedTextureRegion;
	private Sprite mPauseSprite;
	
	@Override
	public void loadResources(Engine engine) {
		mTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mPausedTextureRegion = TextureRegionFactory.createFromAsset(mTexture, mContext, "pause.png", 0, 0);

		engine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public void loadScene(Scene scene) {
		final int x = Consts.CAMERA_WIDTH / 2 - mPausedTextureRegion.getWidth() / 2;
		final int y = Consts.CAMERA_HEIGHT / 2 - mPausedTextureRegion.getHeight() / 2;
		
		mPauseSprite = new Sprite(x, y, mPausedTextureRegion);
		scene.getTopLayer().addEntity(mPauseSprite);
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
	}

	@Override
	public void reset() {
	}
	

}
