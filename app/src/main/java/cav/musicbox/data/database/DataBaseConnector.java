package cav.musicbox.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseConnector {
    private SQLiteDatabase database;
    private DBHelper mDBHelper;

    public DataBaseConnector(Context context){
        mDBHelper = new DBHelper(context,DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
    }

    public void open(){

    }
    public void close(){

    }

}
