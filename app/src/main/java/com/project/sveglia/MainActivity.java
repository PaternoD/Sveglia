package com.project.sveglia;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DB_Manager db_manager;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    final String[] from = new String[]{DB_Helper.ID_VIEW, DB_Helper.TIME_VIEW, DB_Helper.BOOLEAN_DAY};
    final int[] to = new int[] { R.id._idView, R.id.timeView, R.id.booleanDayView};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_vuota);

        db_manager = new DB_Manager(this);
        db_manager.open();
        Cursor cursor = db_manager.fetch_view();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.view_sveglia, cursor, from, to,0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id_TV = (TextView) view.findViewById(R.id._idView);
                String id = id_TV.getText().toString();
                Intent modify_intent = new Intent(getApplicationContext(),Add_Alarm.class);
                modify_intent.putExtra("id", id);
                modify_intent.putExtra("isModifyAlarm",true);
                startActivity(modify_intent);

            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, Add_Alarm.class);
            add_mem.putExtra("isModifyAlarm", false);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }
}
