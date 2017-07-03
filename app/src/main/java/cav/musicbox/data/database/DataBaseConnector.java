package cav.musicbox.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseConnector {
    private SQLiteDatabase database;
    private DBHelper mDBHelper;

    public DataBaseConnector(Context context){
        mDBHelper = new DBHelper(context,DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
    }

    public void open(){
        database = mDBHelper.getWritableDatabase();
    }
    public void close(){
        if (database!=null) {
            database.close();
        }
    }

    // получить все плей листы
    public Cursor getAllPlayList(){
        return database.query(DBHelper.ADMIN_PLAY_LIST,new String[]{"_id","play_list_name","volume"},null,null,null,null,null);
    }
    // получить треки плей листа
    public Cursor getTrackPlayList(int id){
        return null;
    }
    // новый плей лист
    public void addPlayList(){

    }
    // удалить плей лист
    public void delPlayList(){

    }
    // удалить трек из плейлиста
    public void delTrackPlayList(int id_playlist,int id_track){

    }

}
