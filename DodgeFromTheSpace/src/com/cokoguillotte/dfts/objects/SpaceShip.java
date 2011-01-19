package com.cokoguillotte.dfts.objects;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.cokoguillotte.dfts.DodgeFromTheSpace;
import com.cokoguillotte.dfts.gamevar.Consts;

public class SpaceShip extends AnimatedSprite {

	public SpaceShip(float pX, float pY, float pTileWidth, float pTileHeight,
			TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTileWidth, pTileHeight, pTiledTextureRegion);
	}

	private static final int CAMERA_HEIGHT = Consts.CAMERA_HEIGHT;
	private static final float VELOCITY = Consts.SPACESHIP_VELOCITY;


	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {

		if(this.mY < 0) {
			setVelocityY(VELOCITY);
		} else if(mY + getHeight() > CAMERA_HEIGHT) {
			setVelocityY(-VELOCITY);
		}

		super.onManagedUpdate(pSecondsElapsed);
	}
	
	public void changeDirection() {
		setVelocityY(-getVelocityY());
	}

}
