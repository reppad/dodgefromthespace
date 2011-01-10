package com.cokoguillotte.dfts;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Asteroid extends AnimatedSprite {

	public Asteroid(final float pX, final float pY, final TiledTextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(this.mX < 0) {
			//TODO supprimer le sprite ou le repositionner au debut
		}

		super.onManagedUpdate(pSecondsElapsed);
	}

}
