package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by simonerigon on 16/07/18.
 */

public class Sensor_Setting_PopUp extends Activity {

    long animationTime = 400;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_setting_layout);

        DB_Manager db_manager = new DB_Manager(Sensor_Setting_PopUp.this);
        db_manager.open();

        // recupero riferimenti layout ---------------
        //CardView
        CardView cardView_sensor = (CardView)findViewById(R.id.cardView_sensor_setting);

        // RadioGroup
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.RadioGroup_sensor_setting);

        // RadioButton
        RadioButton radioButton_ritarda = (RadioButton)findViewById(R.id.radioButton_ritarda_sensor_setting);
        RadioButton radioButton_elimina = (RadioButton)findViewById(R.id.radioButton_elimina_sensor_setting);

        // Button
        Button btn_save = (Button)findViewById(R.id.btn_save_sensor_setting);
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel_sensor_setting);

        cardView_Animation(cardView_sensor);

        // Setto radioButton di default con informazioni del database --
        String radioButton_default_value = db_manager.getSensoriOpzione();

        if(radioButton_default_value.equals("ritarda")){
            radioButton_ritarda.setChecked(true);
        }else{
            radioButton_elimina.setChecked(true);
        }



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButton_checked = radioGroup.getCheckedRadioButtonId();
                String res_sensor_active;

                db_manager.setSensori_on(true);

                RadioButton radioButton = (RadioButton)findViewById(radioButton_checked);

                String radioButtonValue = radioButton.getText().toString();

                if(radioButtonValue.equals("Ritarda")){
                    db_manager.setSensoriOpzione("ritarda");
                    res_sensor_active = "Ritarda";
                    //System.out.println("Opzione sensore: " + radioButtonValue);
                }else{
                    db_manager.setSensoriOpzione("elimina");
                    res_sensor_active = "Elimina";
                    //System.out.println("Opzione sensore: " + radioButtonValue);
                }

                Intent resIntent = new Intent();
                resIntent.putExtra("sensor_active", res_sensor_active);
                setResult(Activity.RESULT_OK, resIntent);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Sensor_Setting_PopUp.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 100);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resIntent);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Sensor_Setting_PopUp.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 100);

            }
        });
    }

    private void cardView_Animation(CardView cardView){

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 1000, 0);
        translateAnimation.setDuration(animationTime);
        translateAnimation.setFillAfter(true);
        cardView.startAnimation(translateAnimation);

    }
}
