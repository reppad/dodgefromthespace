package com.cokoguillotte.dfts.objects;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;

import android.graphics.Color;

import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.interfaces.IGraphicsText;

/**
 * Objet permettant d'afficher la partie textuelle du menu des options
 */
public class OptionsText extends IGraphicsText {
	
	private Texture mFontTextureItem;
	private Font mFontItem;
	
	private ChangeableText mMusic;
	private ChangeableText mSounds;
	private ChangeableText mParticles;
	private ChangeableText mSave;

	@Override
	public void setText(String text) {
		//nothing to do
	}

	@Override
	public void loadResources(Engine engine) {
		FontFactory.setAssetBasePath("font/");
		mFontTextureItem = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mFontItem = FontFactory.createFromAsset(mFontTextureItem, mContext, "starjedi.ttf", 28, true, Color.WHITE);
		
		engine.getTextureManager().loadTextures(mFontTextureItem);
		engine.getFontManager().loadFonts(mFontItem);
	}

	@Override
	public void loadScene(Scene scene) {

		mMusic = new ChangeableText(100, (Consts.CAMERA_HEIGHT/2)-75, this.mFontItem,
				"Music", "Music".length());
		mMusic.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mMusic.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mMusic);

		mSounds = new ChangeableText(100, (Consts.CAMERA_HEIGHT/2)-25, this.mFontItem,
				"Sounds", "Sounds".length());
		mSounds.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mSounds.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mSounds);

		mParticles = new ChangeableText(100, (Consts.CAMERA_HEIGHT/2)+25, this.mFontItem,
				"Particles", "Particles".length());
		mParticles.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mParticles.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mParticles);
		
		mSave = new ChangeableText(100, (Consts.CAMERA_HEIGHT/2)+75, this.mFontItem,
				"Save", "Save".length());
		mSave.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mSave.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mSave);
		
		hide();
	}
	
	/**
	 * Afficher les options
	 */
	public void show() {
		mMusic.setVisible(true);
		mSounds.setVisible(true);
		mParticles.setVisible(true);
		mSave.setVisible(true);
	}
	
	/**
	 * Masquer les options
	 */
	public void hide() {
		mMusic.setVisible(false);
		mSounds.setVisible(false);
		mParticles.setVisible(false);
		mSave.setVisible(false);
	}

}
