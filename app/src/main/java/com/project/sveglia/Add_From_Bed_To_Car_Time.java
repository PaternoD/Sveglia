package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


/**
 * Created by simonerigon on 22/08/18.
 */

public class Add_From_Bed_To_Car_Time extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_from_bed_to_car_time_popup);

        // Recupero dati da intent --
        long alarm_time = getIntent().getExtras().getLong("alarm_time");
        String transit_model = getIntent().getExtras().getString("transit_model");
        String start_address = getIntent().getExtras().getString("start_address");
        String end_address = getIntent().getExtras().getString("end_address");
        String maps_direction_request = getIntent().getExtras().getString("maps_direction_request");

        // Recupero riferimenti layout --
        // Textview
        TextView textInfo = (TextView)findViewById(R.id.add_time_google_maps);

        // CardView
        CardView card_ok = (CardView)findViewById(R.id.cardView_add_time);
        CardView card_cancel = (CardView)findViewById(R.id.cardView_no_add_time);

        // button
        Button btn_ok = (Button)findViewById(R.id.btn_ok_add_time);
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel_add_time);


        String text_info = "Desideri aggiungere all'orario di partenza il tempo di preparazione 'From Bed To Car'?";

        textInfo.setText(text_info);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("ho premuto il pulsante si");

                // ----> Ricordarsi di aggiungere il tempo di preparazione al mattino, prendere da database!!!
                DB_Manager db_manager = new DB_Manager(Add_From_Bed_To_Car_Time.this);
                db_manager.open();
                long fromBedToCarTime = db_manager.getBadToCar();
                db_manager.close();

                long new_alarm_time = alarm_time - fromBedToCarTime;

                Calendar calendar = Calendar.getInstance();
                long currentTime = calendar.getTimeInMillis();

                if(new_alarm_time > currentTime) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("alarm_time", new_alarm_time);
                    resultIntent.putExtra("transit_model", transit_model);
                    resultIntent.putExtra("start_address", start_address);
                    resultIntent.putExtra("end_address", end_address);
                    resultIntent.putExtra("maps_direction_request", maps_direction_request);
                    resultIntent.putExtra("from_bed_to_car_added", "1");
                    setResult(Activity.RESULT_OK, resultIntent);
                }else{
                    Intent intent_Time_Passed = new Intent(Add_From_Bed_To_Car_Time.this, Time_Passed_Pop_Up_Google.class);
                    startActivity(intent_Time_Passed);
                }
                finish();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("ho premuto il pulsante no");

                Calendar calendar = Calendar.getInstance();
                long currentTime = calendar.getTimeInMillis();

                if(alarm_time > currentTime) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("alarm_time", alarm_time);
                    resultIntent.putExtra("transit_model", transit_model);
                    resultIntent.putExtra("start_address", start_address);
                    resultIntent.putExtra("end_address", end_address);
                    resultIntent.putExtra("maps_direction_request", maps_direction_request);
                    resultIntent.putExtra("from_bed_to_car_added", "0");
                    setResult(Activity.RESULT_OK, resultIntent);
                }else{
                    Intent intent_Time_Passed = new Intent(Add_From_Bed_To_Car_Time.this, Time_Passed_Pop_Up_Google.class);
                    startActivity(intent_Time_Passed);
                }
                finish();

            }
        });


    }
}
