package com.project.sveglia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AndroidException;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private DB_Manager db_manager;
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recupero Riferimenti layout ----
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingButton_ID);
        ImageView settingButton = (ImageView) findViewById(R.id.Image_setting_ID);

        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        SetViewSveglie set_sveglie = new SetViewSveglie();
        set_sveglie.setViewSveglie(recyclerView, this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_mem = new Intent(MainActivity.this, Add_Alarm.class);
                add_mem.putExtra("isModifyAlarm", false);
                startActivity(add_mem);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(MainActivity.this, Setting_Class.class);
                startActivity(settingIntent);
            }
        });

        //bottone info
        ImageButton infoButton = findViewById(R.id.infoMain_ID);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,Tutorial_MainActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
