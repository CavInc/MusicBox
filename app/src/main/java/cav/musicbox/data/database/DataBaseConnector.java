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
        return database.query(DBHelper.ADMIN_PLAY_LIST,
                new String[]{"_id","play_list_name","volume","selected"},null,null,null,null,"_id");
    }
    // получить все плей листы кроме пользовательского
    public Cursor getAdminPlayList(){
        return database.query(DBHelper.ADMIN_PLAY_LIST,new String[]{"_id","play_list_name","volume","selected"},"_id<>0",null,null,null,null);
    }

    // получить пользовательский трек лист
    public Cursor getUserPlayList(){
        return database.query(DBHelper.ADMIN_PLAY_LIST,new String[]{"_id","play_list_name","volume","selected"},"_id = 0",null,null,null,null);
    }

    // получить треки пользовательского плей листа
    public Cursor getUserPlayListTrack(){
        return database.query(DBHelper.USED_TRACK,new String[]{},"",null,null,null,null,null);
    }

    // установить активный плей лист



    // получить треки плей листа
    public Cursor getTrackPlayList(int id){
        return database.query(DBHelper.USED_TRACK,new String[]{"_id","uri_file","artist","track","play_list_id"},"play_list_id="+Integer.toString(id),null,null,null,null);
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
        open();
        //database.execSQL("PRAGMA foreign_keys = ON;");
        database.delete(DBHelper.ADMIN_PLAY_LIST,"_id="+id,null);
        database.delete(DBHelper.USED_TRACK,"play_list_id="+id,null);
        close();

    }

    // добавить трек в плей лист
    public void addTrackInPlayList(int play_list_id,String uri_file,String artist,String track){
        ContentValues newValues = new ContentValues();
        newValues.put("type_play_list",1);
        newValues.put("play_list_id",play_list_id);
        newValues.put("uri_file",uri_file);
        newValues.put("artist",artist);
        newValues.put("track",track);
        open();
        database.insert(DBHelper.USED_TRACK,null,newValues);
        close();
    }

    // удалить трек из плейлиста
    public void delTrackPlayList(int id_track){
        open();
        database.delete(DBHelper.USED_TRACK,"_id="+id_track,null);
        close();
    }

}
