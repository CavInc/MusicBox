package cav.musicbox.data.managers;

import android.content.SharedPreferences;

import cav.musicbox.utils.MusicBoxApplication;

/**
 * Created by cav on 30.06.17.
 */
public class PreferensManager {

    private SharedPreferences mSharedPreferences;

    public PreferensManager(){
        this.mSharedPreferences = MusicBoxApplication.getSharedPreferences();
    }
}
