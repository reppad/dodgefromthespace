package com.cokoguillotte.dfts.objects;

import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.engine.Engine;

import com.cokoguillotte.dfts.interfaces.IAudioObject;

public class MusicPlayer extends IAudioObject{
	private boolean mPlay;
	private Music mMusic;
	
	public MusicPlayer(boolean p){
		mPlay = p;
	}
	
	public void loadResources(Engine engine){
		MusicFactory.setAssetBasePath("sfx/");
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(), mContext, "musique.mp3");
			this.mMusic.setLooping(true);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play(){
		if(mMusic!=null && mPlay){
			mMusic.play();
		}
	}
}
