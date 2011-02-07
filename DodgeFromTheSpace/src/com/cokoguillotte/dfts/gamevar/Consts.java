package com.cokoguillotte.dfts.gamevar;

import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Consts {
	//constantes
	public static final int CAMERA_WIDTH = 480;
	public static final int CAMERA_HEIGHT = 320;

	public static final float SPACESHIP_VELOCITY = 100.0f;
	public static final float ASTEROID_VELOCITY = 80.0f;
	public static final int	ASTEROID_MAX_NUMBER = 5;
	
	public static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

}
