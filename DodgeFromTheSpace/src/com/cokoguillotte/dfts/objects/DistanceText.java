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

import com.cokoguillotte.dfts.interfaces.IGraphicsText;

public class DistanceText extends IGraphicsText {

	private Texture mFontTexture;
	private Font mFont;
	private ChangeableText mDistanceText;
	private int mDistance;

	@Override
	public void setText(String text) {
		mDistanceText.setText(text);
	}
	
	public void incDistance() {
		mDistanceText.setText("Distance: " + String.valueOf(++mDistance));
	}

	@Override
	public void loadResources(Engine engine) {
		FontFactory.setAssetBasePath("font/");
		this.mFontTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(mFontTexture, mContext, "starjedi.ttf", 20, true, Color.WHITE);
		
		engine.getTextureManager().loadTexture(this.mFontTexture);
		engine.getFontManager().loadFont(this.mFont);
	}

	@Override
	public void loadScene(Scene scene) {
		mDistanceText = new ChangeableText(15, 5, this.mFont, "Distance: 0", "Distance: XXXXX".length());
		mDistanceText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mDistanceText.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mDistanceText);
		mDistance = 0;
	}

}
