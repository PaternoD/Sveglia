package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by simonerigon on 13/03/18.
 */

public class add_alarm_music extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_music_layuot);

        // Recupero Extra da intent ------------------------------
        int listPositionMusic = getIntent().getExtras().getInt("listPositionMusic");
        String music_name = getIntent().getExtras().getString("music_name");
        int alarm_music_ID = getIntent().getExtras().getInt("alarm_music_ID");

        // Recupero riferimenti layout ---------------------------
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecLay_music_ID);

        // ImageButton
        ImageButton btn_save_music = (ImageButton) findViewById(R.id.image_button_music_ID);

        // Recupero immagini -------------------------------------
        Bitmap arrowLeft = BitmapFactory.decodeResource(add_alarm_music.this.getResources(), R.drawable.icons8_left_48);

        // Assegno immagine a imageButton ------------------------
        btn_save_music.setImageBitmap(arrowLeft);
        btn_save_music.setImageTintList(getResources().getColorStateList(R.color.White));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        getMusicData.getMusicDataForAlarm(add_alarm_music.this, recyclerView, btn_save_music, add_alarm_music.this, listPositionMusic, music_name, alarm_music_ID);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    @Override
    public void onBackPressed() {
        Intent result_intent = new Intent();
        setResult(Activity.RESULT_CANCELED, result_intent);
        


        super.onBackPressed();
    }
}
