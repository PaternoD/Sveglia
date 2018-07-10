package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by simonerigon on 21/02/18.
 */

public class Add_Alarm_Name extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm_name);

        // Recupero riferimenti nel layout -----------------------
        // Button
        Button btn_cancel = (Button)findViewById(R.id.btn_alarm_name_cancel_ID);
        Button btn_save = (Button)findViewById(R.id.btn_alarm_name_save_ID);

        // EditText
        final EditText edit_alarm_name = (EditText)findViewById(R.id.AlarmName_ID);

        // Recupero Extra da intent ------------------------------
        String old_name = getIntent().getExtras().getString("old_name");
        edit_alarm_name.setText(old_name, null);

        // Assegno azioni ai bottoni -----------------------------
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultintent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultintent);
                Add_Alarm_Name.this.finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // recupero nuovo nome
                String new_name = edit_alarm_name.getText().toString();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_name", new_name);
                setResult(Activity.RESULT_OK, resultIntent);
                Add_Alarm_Name.this.finish();
            }
        });
    }
}
