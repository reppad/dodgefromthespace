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

	/* The categories. */
	public static final short CATEGORYBIT_SPACESHIP = 1;
	public static final short CATEGORYBIT_ASTEROIDS = 2;
	public static final short CATEGORYBIT_ASTONAUTE = 4;

	/* And what should collide with what. */
	public static final short MASKBITS_SPACESHIP = CATEGORYBIT_SPACESHIP + CATEGORYBIT_ASTEROIDS + CATEGORYBIT_ASTONAUTE;
	public static final short MASKBITS_ASTEROIDS = CATEGORYBIT_SPACESHIP + CATEGORYBIT_ASTEROIDS; // Missing: CATEGORYBIT_CIRCLE
	public static final short MASKBITS_ASTONAUTE = CATEGORYBIT_SPACESHIP + CATEGORYBIT_ASTONAUTE; // Missing: CATEGORYBIT_BOX

	public static final FixtureDef SPACESHIP_FIXTURE_DEF = PhysicsFactory.createFixtureDef(20, 0.5f, 0.5f, false, CATEGORYBIT_SPACESHIP, MASKBITS_SPACESHIP, (short)0);
	public static final FixtureDef ASTEROIDS_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f, false, CATEGORYBIT_ASTEROIDS, MASKBITS_ASTEROIDS, (short)0);
	public static final FixtureDef ASTRONAUTE_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.0f, 0.0f, false, CATEGORYBIT_ASTONAUTE, MASKBITS_ASTONAUTE, (short)0);

}
