package cav.musicbox.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.managers.DataManager;
import cav.musicbox.data.storage.models.MainTrackModel;
import cav.musicbox.data.storage.models.PlayListModel;
import cav.musicbox.ui.adapters.NoUserTrackAdapter;
import cav.musicbox.ui.adapters.PlayListHeaderAdapter;
import cav.musicbox.utils.Func;

public class AdminActivity extends AppCompatActivity {

    private ListView mNoUsedPlayList;
    private ListView mPlayListHead;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mDataManager = DataManager.getInstance(this);

        mPlayListHead = (ListView) findViewById(R.id.playlist_list);
        List<PlayListModel> playlist = mDataManager.getAllPlayList();
        PlayListHeaderAdapter playListHeaderAdapter = new PlayListHeaderAdapter(this,R.layout.play_list_header_item,playlist);
        mPlayListHead.setAdapter(playListHeaderAdapter);


        //List<MainTrackModel> noUsedModel = new ArrayList<>();
        List<MainTrackModel> noUsedModel = Func.getAllMusic(this);

        mNoUsedPlayList = (ListView) findViewById(R.id.noused_list);
        NoUserTrackAdapter noUserTrackAdapter =new NoUserTrackAdapter(this,R.layout.playlist_track_item,noUsedModel);
        mNoUsedPlayList.setAdapter(noUserTrackAdapter);

    }
}
