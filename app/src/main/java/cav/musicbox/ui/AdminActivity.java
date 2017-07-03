package cav.musicbox.ui;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    private static final String TAG = "ADMIN";
    private ListView mNoUsedPlayList;
    private ListView mPlayListHead;

    private DataManager mDataManager;

    private ImageButton mAddPlayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mDataManager = DataManager.getInstance(this);

        mAddPlayList = (ImageButton) findViewById(R.id.add_play_list);
        mAddPlayList.setOnClickListener(mAddPlayListListener);


        mPlayListHead = (ListView) findViewById(R.id.playlist_list);
        mPlayListHead.setOnItemLongClickListener(mPlayListHeaderLongListener);

        List<PlayListModel> playlist = mDataManager.getAllPlayList();
        PlayListHeaderAdapter playListHeaderAdapter = new PlayListHeaderAdapter(this,R.layout.play_list_header_item,playlist);
        mPlayListHead.setAdapter(playListHeaderAdapter);


        //List<MainTrackModel> noUsedModel = new ArrayList<>();
        List<MainTrackModel> noUsedModel = Func.getAllMusic(this);

        mNoUsedPlayList = (ListView) findViewById(R.id.noused_list);
        NoUserTrackAdapter noUserTrackAdapter =new NoUserTrackAdapter(this,R.layout.playlist_track_item,noUsedModel);
        mNoUsedPlayList.setAdapter(noUserTrackAdapter);

    }

    AdapterView.OnItemLongClickListener mPlayListHeaderLongListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            Log.d(TAG,"Long click in item "+Integer.toString(position));
            return false;
        }
    };

    View.OnClickListener mAddPlayListListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(AdminActivity.this);
            dialog.setTitle("Добавить плейлист");
            dialog.setContentView(R.layout.add_playlist_dialog);
            final EditText keyET = (EditText) dialog.findViewById(R.id.title_playlist_dialog);
            Button okButton = (Button) dialog.findViewById(R.id.ok_button);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDataManager.addPlayList(String.valueOf(keyET.getText()),0);
                    dialog.dismiss();
                }
            });

            Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };


}
