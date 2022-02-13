package cav.musicbox.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import cav.musicbox.R;
import cav.musicbox.data.managers.DataManager;
import cav.musicbox.data.storage.models.MainTrackModel;
import cav.musicbox.utils.ConstantManager;

public class MusicBoxPlayService extends Service {

    private static final String TAG = "MB SERVICE";
    private MediaPlayer mMediaPlayer;

    private DataManager mDataManager;

    private ArrayList<MainTrackModel> play_list;
    private int currentTrackId = -1;

    private PendingIntent pi;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        public  MusicBoxPlayService getService() {
            return MusicBoxPlayService.this;
        }
    }


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
       // throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Bundle buindle = intent.getExtras();
        //ArrayList<MainTrackModel> play_list = intent.getParcelableArrayListExtra ("PLAY_LIST");
        int pl = intent.getIntExtra(ConstantManager.PLAY_LIST_ID,0);
        pi = intent.getParcelableExtra(ConstantManager.PARAM_PINTENT);
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
        Log.d(TAG,"DESTROY");
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
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mDataManager.getContext(), Uri.parse(track));
            mMediaPlayer.prepare();
            mMediaPlayer.setVolume(0.7f,0.7f);
            mMediaPlayer.setScreenOnWhilePlaying(true); // не дает уснуть во премя воспроизведениея ?
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNextTrack(){
        currentTrackId +=1;
        Log.d(TAG,"Track #"+currentTrackId);
        if (currentTrackId>play_list.size()-1) currentTrackId = 0;
        Log.d(TAG,"Track file "+play_list.get(currentTrackId).getFile());

        //int nextTrackId = (currentTrackId + 1) > (play_list.size() -1 ) ? 0: currentTrackId + 1 ;
        int nextTrackId = getNextTrackId();

        Log.d(TAG,"Track Next: "+nextTrackId);

        // возвращяем текущий трек
        sendMessageInTrack(currentTrackId,nextTrackId);


        return play_list.get(currentTrackId).getFile();
    }

    //получаем номер следующего трека
    private int getNextTrackId(){
        return (currentTrackId + 1) > (play_list.size() -1 ) ? 0: currentTrackId + 1 ;
    }

    // передаем данные от треках в активити
    private void sendMessageInTrack(int currentTrackId,int nextTrackId){
        Intent intent = new Intent().putExtra(ConstantManager.PARAM_RESULT,play_list.get(currentTrackId).getTrack()+" "+
                play_list.get(currentTrackId).getArtist())
                .putExtra(ConstantManager.PARAM_RESULT_NEXT,play_list.get(nextTrackId).getTrack()+" "+
                        play_list.get(nextTrackId).getArtist());
        try {
            pi.send(MusicBoxPlayService.this,ConstantManager.CURRENT_TRACK,intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNewTrack(MainTrackModel track){
        //play_list.set(currentTrackId+1,track);
        play_list.add(currentTrackId + 1 ,track);

        int nextTrackId = getNextTrackId();

        sendMessageInTrack(currentTrackId,nextTrackId);
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

    class PlayerTask implements Runnable{

        @Override
        public void run() {


        }
    }

}
