package cav.musicbox.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

import cav.musicbox.data.storage.models.MainTrackModel;

/**
 * Created by cav on 03.07.17.
 */
public class Func {

    private static final String TAG = "FUNC";

    // проверяем идею потом перенести в отдельный пакет
    public static ArrayList<MainTrackModel> getAllMusic(Context context){
        ArrayList<MainTrackModel> rec= new ArrayList<>();

        final Uri mediaSrc = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        CursorLoader cursorLoader = new CursorLoader(context,mediaSrc, null, null, null,MediaStore.Audio.Media.TITLE);
        Cursor cursor = cursorLoader.loadInBackground();

        Uri playableUri = null;

        int ic = cursor.getCount();
        for (int i=0;i<ic;i++){
            cursor.moveToPosition(i);
            String _id = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));

            // Дополнительная информация
            String title = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));
            int duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));

            //Log.d(TAG,title+" "+artist+" "+duration);
            playableUri = Uri.withAppendedPath(mediaSrc, _id);
           // Log.d(TAG,playableUri.toString());
            rec.add(new MainTrackModel(artist,title,playableUri.toString()));
        }

        return rec;

        /*
        MediaPlayer mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(this,playableUri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }


}
