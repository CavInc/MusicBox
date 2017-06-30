package cav.musicbox.data.managers;

import android.content.Context;

/**
 * Created by cav on 30.06.17.
 */
public class DataManager {
    private static DataManager INSTANCE = null;

    private PreferensManager mPreferensManager;

    public DataManager(Context context) {
        this.mPreferensManager = new PreferensManager();
    }

    public static DataManager getInstance(Context context) {
        if (INSTANCE==null){
            INSTANCE = new DataManager(context);
        }
        return INSTANCE;
    }

    public PreferensManager getPreferensManager() {
        return mPreferensManager;
    }
}
