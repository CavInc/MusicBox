package cav.musicbox.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcelable;
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
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.managers.DataManager;
import cav.musicbox.data.storage.models.MainTrackModel;
import cav.musicbox.services.MusicBoxPlayService;
import cav.musicbox.ui.adapters.UserPlayListAdapter;
import cav.musicbox.utils.ConstantManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MB_MAIN";
    private RecyclerView mRecyclerView;

    private List<MainTrackModel> mTrackData;

    private DataManager mDataManager;

    private TextView mCurrentTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstance(this);

        mCurrentTrack = (TextView) findViewById(R.id.track_played);

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

        UserPlayListAdapter adapter = new UserPlayListAdapter(mTrackData, mListener);
        mRecyclerView.setAdapter(adapter);
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

    private UserPlayListAdapter.PlayListHoler.CustomClickListener mListener = new UserPlayListAdapter.PlayListHoler.CustomClickListener() {
        @Override
        public void onUserItemClickListener(int adapterPosition) {
            Log.d(TAG, String.valueOf(adapterPosition));

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

        /*
        stopService(new Intent(this, MusicBoxPlayService.class));

        Intent intent = new Intent(this, MusicBoxPlayService.class);
        //  intent.putExtra("PL",bundle);
        intent.putExtra("PL", 1);
        intent.putExtra(ConstantManager.PARAM_PINTENT, pi);
        startService(intent);
        */
        if (mService==null){
            Intent intent = new Intent(this,MusicBoxPlayService.class);
            intent.putExtra(ConstantManager.PLAY_LIST_ID,1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode = " + requestCode + ", resultCode = "
                + resultCode);
        if (resultCode==ConstantManager.CURRENT_TRACK){
            String result = data.getStringExtra(ConstantManager.PARAM_RESULT);
            mCurrentTrack.setText(getString(R.string.now_playing)+" "+result);
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
