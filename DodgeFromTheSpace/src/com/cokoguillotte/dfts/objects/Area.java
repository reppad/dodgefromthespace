package com.cokoguillotte.dfts.objects;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import com.cokoguillotte.dfts.interfaces.IGraphicsObject;

public class Area extends IGraphicsObject{
	
	private Texture mTextureBackground;
	private TextureRegion 	mTextureRegionBackground0, 
							mTextureRegionBackground1,
							mTextureRegionBackground2;
	

	@Override
	public void loadResources(Engine engine) {
		mTextureBackground = new Texture(1024, 1024, TextureOptions.DEFAULT);
		TextureRegionFactory.setAssetBasePath("gfx/");
		mTextureRegionBackground0 = TextureRegionFactory.createFromAsset(mTextureBackground, this.mContext, "fond0.png", 0, 0);
		mTextureRegionBackground1 = TextureRegionFactory.createFromAsset(mTextureBackground, this.mContext, "fond1.png", 0, 320);
		/*mTextureRegionBackground2 = TextureRegionFactory.createFromAsset(mTextureBackground, this.mContext, "fond2.png", 0, 640);*/

		engine.getTextureManager().loadTextures(mTextureBackground);
	}

	@Override
	public void loadScene(Scene scene) {
		//background
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
        autoParallaxBackground.addParallaxEntity(new ParallaxEntity(0f,
        		new Sprite(0, 0, 480, 320, mTextureRegionBackground0)));
        autoParallaxBackground.addParallaxEntity(new ParallaxEntity(-4.0f,
        		new Sprite(0, 0, 480, 320, mTextureRegionBackground1)));
        /*autoParallaxBackground.addParallaxEntity(new ParallaxEntity(-8.0f,
        		new Sprite(0, 0, 1024, 320, mTextureRegionBackground2)));*/
        scene.setBackground(autoParallaxBackground);
	}

}
