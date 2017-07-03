package cav.musicbox.data.database;

import android.content.ContentValues;
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
    public int addPlayList(String play_list_name,int volume){
        ContentValues newValues = new ContentValues();
        newValues.put("play_list_name",play_list_name);
        newValues.put("volume",volume);
        open();
        long id =database.insert(DBHelper.ADMIN_PLAY_LIST,null,newValues);
        close();
        return (int) id;
    }

    // удалить плей лист
    public void delPlayList(int id){

    }

    // добавить трек в плей лист
    public void addTrackInPlayList(int play_list_id,String uri_file){
        ContentValues newValues = new ContentValues();
        newValues.put("type_play_list",1);
        newValues.put("play_list_id",play_list_id);
        newValues.put("uri_file",uri_file);
        open();
        database.insert(DBHelper.USED_TRACK,null,newValues);
        close();
    }

    // удалить трек из плейлиста
    public void delTrackPlayList(int id_playlist,int id_track){

    }

}
