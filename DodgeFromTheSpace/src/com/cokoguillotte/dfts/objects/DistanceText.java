package com.cokoguillotte.dfts.objects;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Color;

import com.cokoguillotte.dfts.gamevar.Consts;
import com.cokoguillotte.dfts.interfaces.IGraphicsText;

public class DistanceText extends IGraphicsText {

	private Texture mFontTexture;
	private Font mFont;
	private ChangeableText mDistanceText;
	public static int mDistance;

	private Texture mHeartTexture;
	private TiledTextureRegion mHeartTextureRegion;
	private TiledSprite hearts[];

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
		this.mHeartTexture = new Texture(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mFont = FontFactory.createFromAsset(mFontTexture, mContext, "starjedi.ttf", 20, true, Color.WHITE);
		
		TextureRegionFactory.setAssetBasePath("gfx/");
		this.mHeartTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mHeartTexture, mContext, "life.png", 0, 0, 2 , 1);
		
		engine.getTextureManager().loadTextures(this.mFontTexture, mHeartTexture);
		engine.getFontManager().loadFont(this.mFont);
	}

	@Override
	public void loadScene(Scene scene) {
		mDistanceText = new ChangeableText(15, 5, this.mFont, "Distance: 0", "Distance: XXXXX".length());
		mDistanceText.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mDistanceText.setAlpha(0.8f);
		scene.getTopLayer().addEntity(mDistanceText);
		
		hearts = new TiledSprite[3];
		hearts[0] = new TiledSprite(Consts.CAMERA_WIDTH-128 ,5 ,mHeartTextureRegion.clone());
		hearts[1] = new TiledSprite(Consts.CAMERA_WIDTH-96 ,5 ,mHeartTextureRegion.clone());
		hearts[2] = new TiledSprite(Consts.CAMERA_WIDTH-64 ,5 ,mHeartTextureRegion.clone());
		
		scene.getTopLayer().addEntity(hearts[0]);
		scene.getTopLayer().addEntity(hearts[1]);
		scene.getTopLayer().addEntity(hearts[2]);
		mDistance = 0;
	}
	
	public void updateHud(int life){
		if(life<3){
			hearts[0].setCurrentTileIndex(1);
		}
		if(life<2){
			hearts[1].setCurrentTileIndex(1);
		}
		if(life<1){
			hearts[2].setCurrentTileIndex(1);
		}
	}

}
