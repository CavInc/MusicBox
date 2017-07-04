package cav.musicbox.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;

import cav.musicbox.data.managers.DataManager;
import cav.musicbox.data.storage.models.MainTrackModel;

public class MusicBoxPlayService extends Service {

    private MediaPlayer mMediaPlayer;

    private DataManager mDataManager;


    public MusicBoxPlayService() {
        mDataManager = DataManager.getInstance(getBaseContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Bundle buindle = intent.getExtras();
        //ArrayList<MainTrackModel> play_list = intent.getParcelableArrayListExtra ("PLAY_LIST");
        int pl = intent.getIntExtra("PL",0);
        ArrayList<MainTrackModel> play_list = mDataManager.getTrackInPlayList(pl);
        System.out.print(play_list.get(0).getFile());

        try {
            mMediaPlayer.setDataSource(mDataManager.getContext(), Uri.parse(play_list.get(0).getFile()));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.start();
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        releaseMP();
    }

    private void releaseMP() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
