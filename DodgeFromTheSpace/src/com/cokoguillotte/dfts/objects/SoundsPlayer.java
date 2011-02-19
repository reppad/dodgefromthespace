package com.cokoguillotte.dfts.objects;

import java.io.IOException;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.util.Debug;

import com.cokoguillotte.dfts.interfaces.IAudioObject;

public class SoundsPlayer extends IAudioObject{
	private boolean mPlay;
	
	public enum SoundsList { 	boom, 
								bonus,
							}

	private Sound mSoundBoom;
	private Sound mSoundBonus;
	
	public SoundsPlayer(boolean p){
		mPlay = p;
	}
	
	public void loadResources(Engine engine){
		SoundFactory.setAssetBasePath("sfx/");
		try {
			this.mSoundBoom = SoundFactory.createSoundFromAsset(engine.getSoundManager(), mContext, "boom.mp3");
			this.mSoundBonus = SoundFactory.createSoundFromAsset(engine.getSoundManager(), mContext, "bonus.wav");
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
	}
	
	public void play(SoundsList s){
		if(mPlay){
			switch(s){
				case boom: mSoundBoom.play(); break;
				case bonus: mSoundBonus.play(); break;
			}
		}
	}
}
