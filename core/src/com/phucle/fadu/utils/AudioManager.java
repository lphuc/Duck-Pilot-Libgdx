package com.phucle.fadu.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private static AudioManager instance;
    private Music music;


    // singleton: prevent instantiation from other classes
    private AudioManager() {
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void play(Sound sound, float vol) {
        if (sound != null)
            sound.play(vol);
    }

    public void play(String path) {
        if (music != null)
            music.dispose();
        music = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.setLooping(true);
        music.setVolume(1.0f);
        music.play();

    }

    public void pause() {
        if (music != null)
            music.pause();
    }

    public void resume() {
        if (music != null)
            music.play();
    }

    public void stopMusic() {
        if (music != null) music.stop();
    }

}