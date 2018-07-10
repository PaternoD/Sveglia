package com.project.sveglia;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DB_Manager db_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recupero Riferimenti layout ----
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingButton_ID);

        db_manager = new DB_Manager(this);
        db_manager.open();
/*
            boolean[]b = new boolean[7];
            db_manager.insert_view(999999999,Long.valueOf(32423),"", b,"1",1,0,1,1,null,null,null);
*/
        //boolean[]b = new boolean[7];
        //db_manager.insert_view(999999999,Long.valueOf(32423),"", b,"1",1,0,1,1,null,null,null);
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


    }
}
