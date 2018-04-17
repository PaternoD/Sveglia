package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by simonerigon on 22/02/18.
 */

public class TimePicker_Alarm extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timepicker_alarm);

        // Recupero riferimenti oggetti layout -----------------------
        // TimePicker
        final TimePicker timePicker_Alarm = (TimePicker)findViewById(R.id.timePicker_Alarm_ID);
        timePicker_Alarm.setIs24HourView(true);

        // Button
        Button btn_save = (Button)findViewById(R.id.btn_save_timePicker_ID);
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel_timePicker_ID);


        // Aggiungo azioni ai buttoni salva e annulla -----------------
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                TimePicker_Alarm.this.finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupero istanza calendario
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DATE);
                int hour = timePicker_Alarm.getHour();
                int min = timePicker_Alarm.getMinute();
                int second = 0;

                cal.set(year, month, day, hour, min, second);

                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String date = dateFormat.format(cal.getTime());

                String newTime = date;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newTime", newTime);
                resultIntent.putExtra("timeInMillis", cal.getTimeInMillis());
                setResult(Activity.RESULT_OK, resultIntent);
                TimePicker_Alarm.this.finish();
            }
        });


    }
}
