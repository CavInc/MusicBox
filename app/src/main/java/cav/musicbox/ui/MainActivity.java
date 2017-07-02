package cav.musicbox.ui;

import android.content.CursorLoader;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.storage.models.MainTrackModel;
import cav.musicbox.ui.adapters.UserPlayListAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MB_MAIN";
    private RecyclerView mRecyclerView;

    private List<MainTrackModel> mTrackData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrackData = new ArrayList<>();

        // debug data
        mTrackData.add(new MainTrackModel("Серьга","Свет в оконце"));
        mTrackData.add(new MainTrackModel("Канлер ГИ","Романд кардинала"));
        mTrackData.add(new MainTrackModel("Modern Talking","Brother Lui"));
        mTrackData.add(new MainTrackModel("Cher","Dark Lady"));
        mTrackData.add(new MainTrackModel("Cher","Бурлеск"));
        mTrackData.add(new MainTrackModel("none","У девушки с острова пасхи"));
        mTrackData.add(new MainTrackModel("Джйме Халид","Девушка из Нагасаки"));
        mTrackData.add(new MainTrackModel("Отава Ё","Ой, Дуся, ой, Маруся (казачья лезгинка)"));
        mTrackData.add(new MainTrackModel("Faun","Federkleid"));
        mTrackData.add(new MainTrackModel("Celtic Woman","Tír na nÓg (feat Oonagh)"));
        mTrackData.add(new MainTrackModel("Origa","Diva"));
        mTrackData.add(new MainTrackModel("Kalafina","Kagayakusoranoshijimaniha"));
        mTrackData.add(new MainTrackModel("Barrels of Whiskey","The O'Reillys and the Paddyhats"));
        mTrackData.add(new MainTrackModel("Dropkick Murphys","\"Rose Tattoo\""));
        mTrackData.add(new MainTrackModel("BOK VAN BLERK","DE LA REY"));

        //
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,4);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.track_list);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        UserPlayListAdapter adapter = new UserPlayListAdapter(mTrackData,mListener);
        mRecyclerView.setAdapter(adapter);
    }

    private UserPlayListAdapter.PlayListHoler.CustomClickListener mListener = new UserPlayListAdapter.PlayListHoler.CustomClickListener() {
        @Override
        public void onUserItemClickListener(int adapterPosition) {
            Log.d(TAG, String.valueOf(adapterPosition));

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getAllMusic();
    }

    // проверяем идею потом перенести в отдельный пакет
    private void getAllMusic(){
        final Uri mediaSrc = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] from = { MediaStore.MediaColumns.TITLE };

        CursorLoader cursorLoader = new CursorLoader(this,mediaSrc, null, null, null,MediaStore.Audio.Media.TITLE);
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

            Log.d(TAG,title+" "+artist+" "+duration);
            playableUri = Uri.withAppendedPath(mediaSrc, _id);
            Log.d(TAG,playableUri.toString());
        }

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
