package cav.musicbox.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import cav.musicbox.R;
import cav.musicbox.data.managers.DataManager;
import cav.musicbox.data.storage.models.MainTrackModel;

public class MusicBoxPlayService extends Service {

    private static final String TAG = "MB SERVICE";
    private MediaPlayer mMediaPlayer;

    private DataManager mDataManager;

    private ArrayList<MainTrackModel> play_list;
    private int currentTrackId = -1;


    public MusicBoxPlayService() {
        mDataManager = DataManager.getInstance(getBaseContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(mCompletionListener);

        //
        /*
        Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher);
        Notification notification;
        if (Build.VERSION.SDK_INT < 16)
            notification = builder.getNotification();
        else
            notification = builder.build();
        startForeground(777, notification);
        */
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
        play_list = mDataManager.getTrackInPlayList(pl);

        if (play_list!=null) {
            playTrack(getNextTrack());
        }
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
        if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
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

    private void playTrack(String track){

        try {
            mMediaPlayer.setDataSource(mDataManager.getContext(), Uri.parse(track));
            mMediaPlayer.prepare();
            mMediaPlayer.setVolume(0,50);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.start();
    }

    private String getNextTrack(){
        currentTrackId +=1;
        Log.d(TAG,"Track #"+currentTrackId);
        if (currentTrackId>play_list.size()-1) currentTrackId = 0;
        return play_list.get(currentTrackId).getTrack();
    }

    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Log.d(TAG,"TRACK COMPLETE");
            if (!mediaPlayer.isPlaying()){
                playTrack(getNextTrack());
            }
        }
    };
}
