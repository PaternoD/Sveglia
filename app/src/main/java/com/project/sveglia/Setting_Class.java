package com.project.sveglia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by simonerigon on 12/07/18.
 */

public class Setting_Class extends Activity {

    long fromBedToCar;
    long ringTone_Duration;
    long interval_duration;
    int repetition_time;

    DB_Manager db_manager;

    Switch sensorSwitch;

    boolean activeSensor = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        db_manager = new DB_Manager(Setting_Class.this);
        db_manager.open();

        // Recupero riferimenti layout ---------------------------
        // CardView
        CardView cardFromBedToCar = (CardView)findViewById(R.id.Card_From_Bed_To_Car_ID);
        CardView cardRingToneDuration = (CardView)findViewById(R.id.Card_RingTone_Duration_ID);
        CardView cardSleepDuration = (CardView)findViewById(R.id.Card_Sleep_Duration_ID);

        // TextView
        TextView fromBedToCarTexView = (TextView)findViewById(R.id.TextView_FBTC_ID);
        TextView ringToneTextView = (TextView)findViewById(R.id.TextView_RingTone_Duration_ID);
        TextView textView_sensor_active = (TextView)findViewById(R.id.TexView_sensor_active);

        // Switch
        sensorSwitch = (Switch)findViewById(R.id.switch_sensor_ID);

        // RelativeLayout
        RelativeLayout relativeLayout_sensor_active = (RelativeLayout) findViewById(R.id.relativeLayout_sensor_active);

        // ImageButton
        ImageButton imageButton_arrow_back = (ImageButton)findViewById(R.id.arrow_image_button_setting);
        ImageButton imageButton_info = (ImageButton)findViewById(R.id.info_image_button_setting);

        // Setto i parametri di default presi da database --------
        int fromBedToCardDefaultValue = convertLongToInteger(db_manager.getBadToCar());
        int ringToneDurationDefaultValue = convertLongToInteger(db_manager.getDurataSuoneria());
        boolean sensorDefaultValue = db_manager.getSensoriOn();

        fromBedToCarTexView.setText(fromBedToCardDefaultValue + " minuti");
        ringToneTextView.setText(ringToneDurationDefaultValue + " minuti");

        sensorSwitch.setChecked(sensorDefaultValue);
        if(sensorSwitch.isChecked()){
            activeSensor = true;
            relativeLayout_sensor_active.setVisibility(View.VISIBLE);
            String sensor = db_manager.getSensoriOpzione();
            if(sensor.equals("ritarda")){
                textView_sensor_active.setText("Azione Sensore: Ritarda Allarme");
            }else{
                textView_sensor_active.setText("Azione Sensore: Elimina Allarme");
            }

        }else{
            activeSensor = false;
            relativeLayout_sensor_active.setVisibility(View.GONE);
        }

        Bitmap arrow_image = BitmapFactory.decodeResource(Setting_Class.this.getResources(), R.drawable.icons8_left_48);
        Bitmap info_image = BitmapFactory.decodeResource(Setting_Class.this.getResources(), R.drawable.information_outline_24);
        imageButton_arrow_back.setImageBitmap(arrow_image);
        imageButton_arrow_back.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        imageButton_info.setImageBitmap(info_image);
        imageButton_info.setImageTintList(ColorStateList.valueOf(Color.WHITE));



        // Aggiungo azione al bottone From Bed To Car ------------
        cardFromBedToCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fromBedToCarID = 10;

                fromBedToCar = db_manager.getBadToCar();

                Intent fromBedToCarIntent = new Intent(Setting_Class.this, From_Bed_To_Car_PopUp.class);
                fromBedToCarIntent.putExtra("fromBedToCarValue", fromBedToCar);
                startActivityForResult(fromBedToCarIntent, fromBedToCarID);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // Aggiungo azione al bottone RingTone Duration -----------
        cardRingToneDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ringToneID = 11;

                ringTone_Duration = db_manager.getDurataSuoneria();

                Intent ringToneIntent = new Intent(Setting_Class.this, RingTone_Setting_PopUp.class);
                ringToneIntent.putExtra("ringTone_Duration", ringTone_Duration);
                startActivityForResult(ringToneIntent, ringToneID);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // Aggiungo azione al bottone Sleep Duration --------------
        cardSleepDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sleepDurationID = 12;

                interval_duration = db_manager.getRitardaMinuti();
                repetition_time = db_manager.getRitardaVolte();


                Intent sleepDurationIntent = new Intent(Setting_Class.this, Sleep_Duration_PopUp.class);
                sleepDurationIntent.putExtra("interval_duration", interval_duration);
                sleepDurationIntent.putExtra("repetition_time", repetition_time);
                startActivityForResult(sleepDurationIntent, sleepDurationID);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        // Aggiungo azione allo switch del sensore ----------------
        sensorSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sensorSwitch_ID = 13;

                if(sensorSwitch.isChecked()){

                    Intent sensorIntent = new Intent(Setting_Class.this, Sensor_Setting_PopUp.class);
                    startActivityForResult(sensorIntent, sensorSwitch_ID);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }else{
                    db_manager.setSensori_on(false);
                    activeSensor = false;

                    relativeLayout_sensor_active.setVisibility(View.GONE);
                }
            }
        });

        // Aggiungo opzione per modificare opzioine sensore -------
        relativeLayout_sensor_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sensorSwitch_ID = 13;

                Intent sensorIntent = new Intent(Setting_Class.this, Sensor_Setting_PopUp.class);
                startActivityForResult(sensorIntent, sensorSwitch_ID);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // Aggiungo azione alla freccia in alto per tornare alla lista sveglie --
        imageButton_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting_Class.this.finish();
            }
        });

        //tutorial
        imageButton_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting_Class.this,Tutorial_Setting.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Funzione invocata per ricevere i dati da StartActivityForResult()
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Ottengo risposta da PopUp FrombedToCar ------
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                fromBedToCar = data.getExtras().getLong("FromBedToCarResult");

                db_manager = new DB_Manager(Setting_Class.this);
                db_manager.open();

                db_manager.setBadToCar(fromBedToCar);

                int fromBedToCar_Value = convertLongToInteger(fromBedToCar);

                // Recupero riferimento layout ---------
                TextView fromBedToCarTexView = (TextView)findViewById(R.id.TextView_FBTC_ID);
                fromBedToCarTexView.setText(fromBedToCar_Value + " minuti");
                db_manager.close();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }

        // Ottengo risposta da PopUp RingTone ------
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                ringTone_Duration = data.getExtras().getInt("ringTone_Duration");

                db_manager = new DB_Manager(Setting_Class.this);
                db_manager.open();

                db_manager.setDurataSuoneria(ringTone_Duration);

                int ringTone_Duration_Value = convertLongToInteger(ringTone_Duration);

                // Recupero riferimento layout ---------
                TextView ringToneTextView = (TextView)findViewById(R.id.TextView_RingTone_Duration_ID);
                ringToneTextView.setText(ringTone_Duration_Value + " minuti");
                db_manager.close();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }

        // Ottengo risposta da PopUp SleepDuration ------
        if (requestCode == 12) {
            if (resultCode == Activity.RESULT_OK) {
                interval_duration = data.getExtras().getLong("interval_duration");
                repetition_time = data.getExtras().getInt("repetition_time");

                db_manager = new DB_Manager(Setting_Class.this);
                db_manager.open();

                db_manager.setRitardaMinuti(interval_duration);
                db_manager.setRitardaVolte((repetition_time));
                db_manager.close();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }

        // Ottengo risposta da Sensor_Setting_PopUp ------
        if (requestCode == 13) {

            RelativeLayout relativeLayout_sensor_active = (RelativeLayout) findViewById(R.id.relativeLayout_sensor_active);

            if (resultCode == Activity.RESULT_OK) {
                String sensor_active = data.getExtras().getString("sensor_active");

                relativeLayout_sensor_active.setVisibility(View.VISIBLE);

                activeSensor = true;

                TextView textView_sensor_active = (TextView)findViewById(R.id.TexView_sensor_active);
                textView_sensor_active.setText("Azione Sensore: " + sensor_active + " Allarme");

            }
            if (resultCode == Activity.RESULT_CANCELED) {

                if(activeSensor){
                    relativeLayout_sensor_active.setVisibility(View.VISIBLE);
                    System.out.println("sensor_active_true: " + activeSensor);
                }else{
                    relativeLayout_sensor_active.setVisibility(View.GONE);
                    sensorSwitch.setChecked(false);
                    System.out.println("sensor_active_false: " + activeSensor);
                }
            }
        }
    }

    /**
     * Funzione per trasformare il dato in ingresso (Long) in intero
     * @param time
     * @return Integer
     */
    private int convertLongToInteger(Long time){

        int  res = (int) (time / 60000);

        return res;
    }

}
