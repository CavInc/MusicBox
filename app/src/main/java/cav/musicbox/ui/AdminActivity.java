package cav.musicbox.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cav.musicbox.R;
import cav.musicbox.data.managers.DataManager;
import cav.musicbox.data.storage.models.MainTrackModel;
import cav.musicbox.data.storage.models.PlayListModel;
import cav.musicbox.ui.adapters.NoUserTrackAdapter;
import cav.musicbox.ui.adapters.PlayListHeaderAdapter;
import cav.musicbox.ui.adapters.PlayListSpecAdapter;
import cav.musicbox.utils.Func;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "ADMIN";
    private ListView mNoUsedPlayList;
    private ListView mPlayListHead;
    private ListView mSpecPlayList;

    private DataManager mDataManager;

    private ImageButton mAddPlayList;

    private TextView mPlaySpecTitle;

    private PlayListHeaderAdapter playListHeaderAdapter;
    private PlayListSpecAdapter mPlayListSpecAdapter;

    private int mPlayListIDLong = -1;
    private int mPlayListID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mDataManager = DataManager.getInstance(this);

        mAddPlayList = (ImageButton) findViewById(R.id.add_play_list);
        mAddPlayList.setOnClickListener(mAddPlayListListener);

        mPlaySpecTitle = (TextView) findViewById(R.id.spec_play_list_title);


        mPlayListHead = (ListView) findViewById(R.id.playlist_list);
        mPlayListHead.setOnItemClickListener(mPlayListHeaderListener);
        mPlayListHead.setOnItemLongClickListener(mPlayListHeaderLongListener);


        mSpecPlayList = (ListView) findViewById(R.id.spec_play_list);
        mSpecPlayList.setOnItemLongClickListener(mSpecLongListener); // удаляем запись из плей листа

        //List<MainTrackModel> noUsedModel = new ArrayList<>();
        List<MainTrackModel> noUsedModel = Func.getAllMusic(this);

        mNoUsedPlayList = (ListView) findViewById(R.id.noused_list);
        NoUserTrackAdapter noUserTrackAdapter =new NoUserTrackAdapter(this,R.layout.playlist_track_item,noUsedModel);
        mNoUsedPlayList.setAdapter(noUserTrackAdapter);
        mNoUsedPlayList.setOnItemClickListener(mNoUsedTrackListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUIPlayListHeader();
        updateUISpecPlayList(0,"UserPlayList");
    }

    AdapterView.OnItemClickListener mPlayListHeaderListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Log.d(TAG,"Click in item"+Integer.toString(position));
            PlayListModel model = (PlayListModel) adapterView.getItemAtPosition(position);
            mPlayListID = model.getId();
            updateUISpecPlayList(model.getId(),model.getTitle());
        }
    };


    AdapterView.OnItemLongClickListener mPlayListHeaderLongListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            Log.d(TAG,"Long click in item "+Integer.toString(position));
            PlayListModel model = (PlayListModel) adapterView.getItemAtPosition(position);
            Log.d(TAG, String.valueOf(model.getId())+" "+model.getTitle());
            mPlayListIDLong = model.getId();

            AlertDialog.Builder dialog = new AlertDialog.Builder(AdminActivity.this);
            dialog.setTitle("Удаляем ?");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setPositiveButton(R.string.yes, mDeleteDialogListener);
            dialog.setNegativeButton(R.string.cancel, mDeleteDialogListener);
            dialog.show();
            return true;
        }
    };

    // добавляет позицию
    AdapterView.OnItemClickListener mNoUsedTrackListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MainTrackModel model = (MainTrackModel) adapterView.getItemAtPosition(position);
            mDataManager.addTrackInPlayList(mPlayListID,model);
            updateUISpecPlayList(mPlayListID);
        }
    };

    // удаляем запись из плей листа
    AdapterView.OnItemLongClickListener mSpecLongListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            //TODO добавить диалог ?
            MainTrackModel model = (MainTrackModel) adapterView.getItemAtPosition(position);
            mDataManager.delTrackInPlayList(model.getId());
            updateUISpecPlayList(mPlayListID);
            return true;
        }
    };

    DialogInterface.OnClickListener mDeleteDialogListener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    mDataManager.delPlayList(mPlayListIDLong);
                    updateUIPlayListHeader();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
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
                    updateUIPlayListHeader();
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

    private void updateUIPlayListHeader(){
        List<PlayListModel> playlist = mDataManager.getAllPlayList();
        if (playListHeaderAdapter == null) {
            playListHeaderAdapter = new PlayListHeaderAdapter(this, R.layout.play_list_header_item, playlist);
            mPlayListHead.setAdapter(playListHeaderAdapter);
        }else{
            playListHeaderAdapter.setData(playlist);
            playListHeaderAdapter.notifyDataSetChanged();
        }
    }

    private void updateUISpecPlayList(int id,String title){
        mPlaySpecTitle.setText("Плей лист: "+title);
        updateUISpecPlayList(id);
    }

    private void updateUISpecPlayList(int id){
        List<MainTrackModel> track = mDataManager.getTrackInPlayList(id);
        if (mPlayListSpecAdapter == null) {
            mPlayListSpecAdapter = new PlayListSpecAdapter(this,R.layout.playlist_track_item,track);
            mSpecPlayList.setAdapter(mPlayListSpecAdapter);
        }else {
            mPlayListSpecAdapter.setData(track);
            mPlayListSpecAdapter.notifyDataSetChanged();
        }
    }

}
