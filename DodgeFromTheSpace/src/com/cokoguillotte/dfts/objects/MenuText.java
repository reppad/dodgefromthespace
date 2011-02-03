package com.cokoguillotte.dfts.objects;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;

import android.content.Intent;
import android.graphics.Color;

import com.cokoguillotte.dfts.DodgeFromTheSpace;
import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.interfaces.IGraphicsText;

public class MenuText extends IGraphicsText {

	private Texture mFontTexture;
	private Font mFont;
	
	private Texture mFontTextureItem;
	private Font mFontItem;
	
	private ChangeableText mTitre;
	private ChangeableText mStart;
	private ChangeableText mSetting;
	private ChangeableText mHowToPlay;
	private ChangeableText mQuit;
	

	@Override
	public void setText(String text) {
		//nothing to do
	}

	@Override
	public void loadResources(Engine engine) {
		FontFactory.setAssetBasePath("font/");
		this.mFontTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(mFontTexture, mContext, "starjedi.ttf", 32, true, Color.WHITE);
		this.mFontTextureItem = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFontItem = FontFactory.createFromAsset(mFontTextureItem, mContext, "starjedi.ttf", 28, true, Color.WHITE);
		
		engine.getTextureManager().loadTextures(this.mFontTexture, this.mFontTextureItem);
		engine.getFontManager().loadFonts(this.mFont, this.mFontItem);
	}

	@Override
	public void loadScene(Scene scene) {
		mTitre = new ChangeableText(15, 5, this.mFont, "Dodge From The Space", "Dodge From The Space".length());
		scene.getTopLayer().addEntity(mTitre);

		mStart = new ChangeableText(50, (Consts.CAMERA_HEIGHT/2)-75, this.mFontItem,
				"Start", "Start".length());
		mStart.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mStart.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mStart);
		scene.registerTouchArea(mStart);

		mSetting = new ChangeableText(50, (Consts.CAMERA_HEIGHT/2)-25, this.mFontItem,
				"Settings", "Settings".length());
		mSetting.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mSetting.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mSetting);

		mHowToPlay = new ChangeableText(50, (Consts.CAMERA_HEIGHT/2)+25, this.mFontItem,
				"How to play", "How to play".length());
		mHowToPlay.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mHowToPlay.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mHowToPlay);

		mQuit = new ChangeableText(50, (Consts.CAMERA_HEIGHT/2)+75, this.mFontItem,
				"Exit", "Exit".length());
		mQuit.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mQuit.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mQuit);

		scene.setTouchAreaBindingEnabled(true);
	}

}
