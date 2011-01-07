package com.cokoguillotte.dfts;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class SpaceShip extends AnimatedSprite {

	private static final int CAMERA_WIDTH = DodgeFromTheSpace.CAMERA_WIDTH;
	private static final int CAMERA_HEIGHT = DodgeFromTheSpace.CAMERA_HEIGHT;
	private static final float VELOCITY = DodgeFromTheSpace.VELOCITY;

	public SpaceShip(final float pX, final float pY, final TiledTextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(this.mX < 0) {
			this.setVelocityX(VELOCITY);
		} else if(this.mX + this.getWidth() > CAMERA_WIDTH) {
			this.setVelocityX(-VELOCITY);
		}

		if(this.mY < 0) {
			this.setVelocityY(VELOCITY);
		} else if(this.mY + this.getHeight() > CAMERA_HEIGHT) {
			this.setVelocityY(-VELOCITY);
		}

		super.onManagedUpdate(pSecondsElapsed);
	}

}
