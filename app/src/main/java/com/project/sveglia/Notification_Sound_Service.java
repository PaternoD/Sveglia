package com.project.sveglia;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by simonerigon on 12/04/18.
 */

public class Notification_Sound_Service extends Service {

    MediaPlayer mediaPlayer = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int alarm_music_ID = intent.getExtras().getInt("alarm_music_ID");

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

        return START_STICKY;
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
        return null;
    }

}
