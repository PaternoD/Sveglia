package com.project.sveglia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this,"ciao",Toast.LENGTH_LONG).show();
        //secondo commit
        //terzo commit
        //quarto
        //quinto

        Button btn_add = (Button)findViewById(R.id.btn_1);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Add_Alarm.class);
                startActivity(intent);
            }
        });
    }
}
