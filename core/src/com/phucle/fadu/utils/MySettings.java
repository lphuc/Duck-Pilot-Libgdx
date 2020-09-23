package com.phucle.fadu.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/*
 * Created by Phuc Engineer on 10/11/2018.
 */
public class MySettings {
    public static final String KM_TRAVELED = "km";
    public static final String CURRENT_SPEED = "current_speed";
    public static final String MAX_SCORE = "max_score";
    public static final String STAGE = "stage_travelled";
    public static final String PREFS_NAME = "dupi";

    private static MySettings instance;

    public static MySettings getInstance() {
        if (instance == null) {
            instance = new MySettings();
        }
        return instance;
    }

    public Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

}
