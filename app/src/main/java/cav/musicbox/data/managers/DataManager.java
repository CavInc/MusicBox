package cav.musicbox.data.managers;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import cav.musicbox.data.database.DataBaseConnector;
import cav.musicbox.data.storage.models.PlayListModel;

/**
 * Created by cav on 30.06.17.
 */
public class DataManager {
    private static DataManager INSTANCE = null;

    private PreferensManager mPreferensManager;

    private DataBaseConnector mDbc;

    public DataManager(Context context) {
        this.mPreferensManager = new PreferensManager();
        this.mDbc = new DataBaseConnector(context);
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
    public DataBaseConnector getDataBaseConnector(){
        return this.mDbc;
    }

    public ArrayList<PlayListModel> getAllPlayList(){
        ArrayList<PlayListModel> rec = new ArrayList<>();
        mDbc.open();
        Cursor cursor = mDbc.getAllPlayList();
        while (cursor.moveToNext()){
            rec.add(new PlayListModel(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("play_list_name")),
                    cursor.getInt(cursor.getColumnIndex("volume"))));
        }
        mDbc.close();
        return rec;
    }

    public void addPlayList(String play_list_name,int volume){
        mDbc.addPlayList(play_list_name,volume);
    }

    public void delPlayList(int id){
        mDbc.delPlayList(id);

    }

}
