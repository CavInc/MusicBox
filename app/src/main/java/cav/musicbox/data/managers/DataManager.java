package cav.musicbox.data.managers;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import cav.musicbox.data.database.DataBaseConnector;
import cav.musicbox.data.storage.models.MainTrackModel;
import cav.musicbox.data.storage.models.PlayListModel;
import cav.musicbox.utils.MusicBoxApplication;

/**
 * Created by cav on 30.06.17.
 */
public class DataManager {
    private static DataManager INSTANCE = null;

    private PreferensManager mPreferensManager;

    private DataBaseConnector mDbc;

    private Context mContext;

    public DataManager(Context context) {
        //mContext = MusicBoxApplication.getContext();
        mContext = context;

        this.mPreferensManager = new PreferensManager();
        this.mDbc = new DataBaseConnector(mContext);
    }

    public static DataManager getInstance(Context context) {
        if (INSTANCE==null){
            INSTANCE = new DataManager(context);
        }
        return INSTANCE;
    }
    public Context getContext() {
        return mContext;
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

    public ArrayList<MainTrackModel> getTrackInPlayList(int id){
        ArrayList<MainTrackModel> rec = new ArrayList<>();
        mDbc.open();
        Cursor cursor = mDbc.getTrackPlayList(id);
        while(cursor.moveToNext()){
            rec.add(new MainTrackModel(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("artist")),
                    cursor.getString(cursor.getColumnIndex("track")),
                    cursor.getString(cursor.getColumnIndex("uri_file")),
                    cursor.getInt(cursor.getColumnIndex("play_list_id"))));
        }
        mDbc.close();
        return rec;
    }

    public void addTrackInPlayList(int playlist_id,MainTrackModel data){
        mDbc.addTrackInPlayList(playlist_id,data.getFile(),data.getArtist(),data.getTrack());
    }

    // удалить трек из плейлиста
    public void delTrackInPlayList(int track_id){
        mDbc.delTrackPlayList(track_id);
    }

}
