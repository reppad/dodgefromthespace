package com.cokoguillotte.dfts;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class SpaceShip extends AnimatedSprite {

	private static final int CAMERA_HEIGHT = DodgeFromTheSpace.CAMERA_HEIGHT;
	private static final float VELOCITY = DodgeFromTheSpace.SPACESHIP_VELOCITY;

	public SpaceShip(final float pX, final float pY, final TiledTextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
	}

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
