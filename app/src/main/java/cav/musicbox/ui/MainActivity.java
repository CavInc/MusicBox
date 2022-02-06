package cav.musicbox.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.managers.DataManager;
import cav.musicbox.data.storage.models.MainTrackModel;
import cav.musicbox.data.storage.models.PlayListModel;
import cav.musicbox.services.MusicBoxPlayService;
import cav.musicbox.ui.adapters.UserPlayListAdapter;
import cav.musicbox.utils.ConstantManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MB_MAIN";
    private static final int PERMISSION_REQUEST_CODE = 345;
    private RecyclerView mRecyclerView;

    private List<MainTrackModel> mTrackData;

    private DataManager mDataManager;

    private TextView mCurrentTrack;
    private TextView mNextTrack;
    private UserPlayListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstance(this);

        mCurrentTrack = (TextView) findViewById(R.id.track_played);
        mNextTrack = (TextView) findViewById(R.id.next_track);

        mTrackData = new ArrayList<>();

        // debug data
        mTrackData.add(new MainTrackModel("Серьга", "Свет в оконце"));
        mTrackData.add(new MainTrackModel("Канлер ГИ", "Романд кардинала"));
        mTrackData.add(new MainTrackModel("Modern Talking", "Brother Lui"));
        mTrackData.add(new MainTrackModel("Cher", "Dark Lady"));
        mTrackData.add(new MainTrackModel("Cher", "Бурлеск"));
        mTrackData.add(new MainTrackModel("none", "У девушки с острова пасхи"));
        mTrackData.add(new MainTrackModel("Джйме Халид", "Девушка из Нагасаки"));
        mTrackData.add(new MainTrackModel("Отава Ё", "Ой, Дуся, ой, Маруся (казачья лезгинка)"));
        mTrackData.add(new MainTrackModel("Faun", "Federkleid"));
        mTrackData.add(new MainTrackModel("Celtic Woman", "Tír na nÓg (feat Oonagh)"));
        mTrackData.add(new MainTrackModel("Origa", "Diva"));
        mTrackData.add(new MainTrackModel("Kalafina", "Kagayakusoranoshijimaniha"));
        mTrackData.add(new MainTrackModel("Barrels of Whiskey", "The O'Reillys and the Paddyhats"));
        mTrackData.add(new MainTrackModel("Dropkick Murphys", "\"Rose Tattoo\""));
        mTrackData.add(new MainTrackModel("BOK VAN BLERK", "DE LA REY"));

        //
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 4);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.track_list);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        updateUI();
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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

    private void updateUI(){
        if (adapter == null) {
            adapter = new UserPlayListAdapter(mTrackData, mListener);
            mRecyclerView.setAdapter(adapter);
        } else {

        }
    }

    private UserPlayListAdapter.PlayListHoler.CustomClickListener mListener = new UserPlayListAdapter.PlayListHoler.CustomClickListener() {
        @Override
        public void onUserItemClickListener(int adapterPosition) {
            Log.d(TAG, String.valueOf(adapterPosition));
            UserPlayListAdapter.PlayListHoler holder = (UserPlayListAdapter.PlayListHoler) mRecyclerView.findViewHolderForAdapterPosition(adapterPosition);

            MainTrackModel track = adapter.getItem(adapterPosition);
            Log.d(TAG,track.getTrack()+" "+track.getArtist()+" "+track.getFile());
            mService.setNewTrack(track);

        }

        @Override
        public void onUserItemOnClickListener(int adapterPosition, MainTrackModel data) {
            Log.d(TAG, String.valueOf(adapterPosition) + " " + data.getTrack());

        }
    };

    private void passDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Key");
        dialog.setContentView(R.layout.key_dialog);
        final EditText keyET = (EditText) dialog.findViewById(R.id.key_dialog_edit);
        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyPass = String.valueOf(keyET.getText());
                // TODO переделать определение пароля
                if (keyPass.equals("master")) {
                    Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(adminIntent);
                    dialog.dismiss();
                }
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

    private MusicBoxPlayService mService;

    @Override
    protected void onResume() {
        super.onResume();

        //
        //ArrayList<MainTrackModel> play_list = mDataManager.getTrackInPlayList(1); // для отладки
        // запуск воспроизведения плейлиста
        // Bundle bundle =new Bundle();
        //   bundle.putSerializable("PLAY_LIST",play_list);
        // bundle.putParcelableArrayList("PLAY_LIST", (ArrayList<? extends Parcelable>) play_list);

        PendingIntent pi;
        pi = createPendingResult(ConstantManager.TASK_ID, new Intent(), 0);
        ArrayList<PlayListModel> play_list = mDataManager.getAllAdminPlayList();

        int i = 0;
        if (play_list.size()!=0){
            i = play_list.get(0).getId();
        }
        /*
        stopService(new Intent(this, MusicBoxPlayService.class));

        Intent intent = new Intent(this, MusicBoxPlayService.class);
        //  intent.putExtra("PL",bundle);
        intent.putExtra("PL", 1);
        intent.putExtra(ConstantManager.PARAM_PINTENT, pi);
        startService(intent);
        */


        if (mService==null && i!=0){
            Intent intent = new Intent(this,MusicBoxPlayService.class);
            intent.putExtra(ConstantManager.PLAY_LIST_ID,i);
            intent.putExtra(ConstantManager.PARAM_PINTENT, pi);
            startService(intent);
            bindService(intent,mConnection,BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MusicBoxPlayService.class));

    }

    // проверка и установка разрешений
    private void checkAndSetPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode = " + requestCode + ", resultCode = "
                + resultCode);

        if (resultCode==ConstantManager.CURRENT_TRACK){
            String result = data.getStringExtra(ConstantManager.PARAM_RESULT);
            mCurrentTrack.setText(getString(R.string.now_playing)+" "+result);
            result = data.getStringExtra(ConstantManager.PARAM_RESULT_NEXT);
            mNextTrack.setText(getString(R.string.next_track)+" "+result);
        }

    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicBoxPlayService.LocalBinder binder = (MusicBoxPlayService.LocalBinder) iBinder;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
