package cav.musicbox.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cav on 30.06.17.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1 ;
    public static final String DATABASE_NAME = "musicbox.db3";

    public static final String ADMIN_PLAY_LIST = "";
    public static final String USED_TRACK = "USED_TRACK"; // треки в плей листах


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updatedDB(db,0,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updatedDB(db,oldVersion,newVersion);
    }

    private void updatedDB(SQLiteDatabase db,int oldVersion,int newVersion){
        if (oldVersion<1){
            db.execSQL("create table "+USED_TRACK+
                    "(_id not null primary key AUTOINCREMENT,"+
                    " type_play_list integer default 0,"+ // тип плей листа 0 - пользовательский 1 -админский
                    " play_list_id integer default 0"+
                    " uri_file text)");
            db.execSQL("create table "+ADMIN_PLAY_LIST+
                    "(_id not null primary key AUTOINCREMENT,"+
                    " play_list_name text,"+
                    " volume integer)");


            db.execSQL("insert inti "+ADMIN_PLAY_LIST+"(_id,play_list) values (0,'UserPlayList')");

        }

    }

}
