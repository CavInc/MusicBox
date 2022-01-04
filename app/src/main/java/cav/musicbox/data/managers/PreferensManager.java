package cav.musicbox.data.managers;

import android.content.SharedPreferences;

import cav.musicbox.utils.MusicBoxApplication;

/**
 * Created by cav on 30.06.17.
 */
public class PreferensManager {

    private static final String CURRENT_PLAY_LIST = "CURRENT_PLAY_LIST";
    private SharedPreferences mSharedPreferences;

    public PreferensManager(){
        this.mSharedPreferences = MusicBoxApplication.getSharedPreferences();
    }

    // получаем текущий плейлист админа
    public int getCurrentPlayList(){
        return mSharedPreferences.getInt(CURRENT_PLAY_LIST,-1);
    }

    public void setCurrentPlayList(int playList){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CURRENT_PLAY_LIST,playList);
        editor.apply();
    }
}
