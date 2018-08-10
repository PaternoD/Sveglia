package com.project.sveglia;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by simonerigon on 12/04/18.
 */

public class Notification_Sound_Service extends Service {

    MediaPlayer mediaPlayer = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int alarm_music_ID = intent.getExtras().getInt("alarm_music_ID");

        Log.i("** Forground Service **", "alarm_music_ID: " + alarm_music_ID);

        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, alarm_music_ID);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }else{
            mediaPlayer.stop();

            mediaPlayer = MediaPlayer.create(this, alarm_music_ID);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        startForeground(1, new Notification());

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        Notification_Sound_Service getService() {
            // Return this instance of LocalService so clients can call public methods
            return Notification_Sound_Service.this;
        }
    }

}
