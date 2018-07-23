package com.project.sveglia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private DB_Manager db_manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recupero Riferimenti layout ----
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingButton_ID);
        ImageView settingButton = (ImageView) findViewById(R.id.Image_setting_ID);


        db_manager = new DB_Manager(this);
        db_manager.open();

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



    }


}
