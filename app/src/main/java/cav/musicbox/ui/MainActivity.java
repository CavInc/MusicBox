package cav.musicbox.ui;

import android.app.Dialog;
import android.content.CursorLoader;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_menu:
                //TODO диалог о вводе пароля.
                passDialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private UserPlayListAdapter.PlayListHoler.CustomClickListener mListener = new UserPlayListAdapter.PlayListHoler.CustomClickListener() {
        @Override
        public void onUserItemClickListener(int adapterPosition) {
            Log.d(TAG, String.valueOf(adapterPosition));

        }
    };

    private void passDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Key");
        dialog.setContentView(R.layout.key_dialog);
        final EditText keyET = (EditText) dialog.findViewById(R.id.key_dialog_edit);
        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

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
