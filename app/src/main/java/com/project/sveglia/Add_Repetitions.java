package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by simonerigon on 23/02/18.
 */

public class Add_Repetitions extends Activity {

    long animationTime = 400;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_repetitions);

        // Recupero dati da intent chiamante ---------------------
        boolean[] ripetition_array = getIntent().getExtras().getBooleanArray("array_giorni");

        // Recupero riferimenti layout ---------------------------
        // CheckBox
        final CheckBox checkbox_lunedì = (CheckBox)findViewById(R.id.checkBox_lunedì);
        final CheckBox checkbox_martedì = (CheckBox)findViewById(R.id.checkBox_martedì);
        final CheckBox checkbox_mercoledì = (CheckBox)findViewById(R.id.checkBox_mercoledì);
        final CheckBox checkbox_giovedì = (CheckBox)findViewById(R.id.checkBox_giovedì);
        final CheckBox checkbox_venerdì = (CheckBox)findViewById(R.id.checkBox_venerdì);
        final CheckBox checkbox_sabato = (CheckBox)findViewById(R.id.checkBox_sabato);
        final CheckBox checkbox_domenica = (CheckBox)findViewById(R.id.checkBox_domenica);

        // TextView
        TextView text_lun = (TextView)findViewById(R.id.Text_lunedì);
        TextView text_mar = (TextView)findViewById(R.id.Text_Martedì);
        TextView text_mer = (TextView)findViewById(R.id.text_mercoledì);
        TextView text_gio = (TextView)findViewById(R.id.text_giovedì);
        TextView text_ven = (TextView)findViewById(R.id.text_venerdì);
        TextView text_sab = (TextView)findViewById(R.id.text_sabato);
        TextView text_dom = (TextView)findViewById(R.id.text_domenica);

        // Button
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel_ripetition_ID);
        Button btn_save = (Button)findViewById(R.id.btn_save_ripetition_ID);

        // CardView
        CardView card_repetition = (CardView)findViewById(R.id.cardView_repetition_ID);

        // Setto animazione Entrata -------------------------------
        cardView_Animation(card_repetition);

        // Setto colori testo -------------------------------------
        text_lun.setTextColor(getResources().getColor(R.color.DefaultColorText));
        text_mar.setTextColor(getResources().getColor(R.color.DefaultColorText));
        text_mer.setTextColor(getResources().getColor(R.color.DefaultColorText));
        text_gio.setTextColor(getResources().getColor(R.color.DefaultColorText));
        text_ven.setTextColor(getResources().getColor(R.color.DefaultColorText));
        text_sab.setTextColor(getResources().getColor(R.color.DefaultColorText));
        text_dom.setTextColor(getResources().getColor(R.color.DefaultColorText));

        // setto i checkBox ---------------------------------------
        set_CheckBox_days(ripetition_array,
                checkbox_lunedì,
                checkbox_martedì,
                checkbox_mercoledì,
                checkbox_giovedì,
                checkbox_venerdì,
                checkbox_sabato,
                checkbox_domenica);

        // Aggiungo azioni bottoni cancel e save ------------------
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result_intent = new Intent();
                setResult(Activity.RESULT_CANCELED, result_intent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Add_Repetitions.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 100);


            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] res_ripetition_array = get_res_ripetition(checkbox_lunedì,
                        checkbox_martedì,
                        checkbox_mercoledì,
                        checkbox_giovedì,
                        checkbox_venerdì,
                        checkbox_sabato,
                        checkbox_domenica);

                Intent result_intent = new Intent();
                result_intent.putExtra("res_repetitions_array", res_ripetition_array);
                setResult(Activity.RESULT_OK, result_intent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Add_Repetitions.this.finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 100);

            }
        });


    }

    /**
     * Funzione per settare i checkBox con le scelte fatte dall'utente
     * @param ripetition_array
     * @param checkbox_lunedì
     * @param checkbox_martedì
     * @param checkbox_mercoledì
     * @param checkbox_giovedì
     * @param checkbox_venerdì
     * @param checkbox_sabato
     * @param checkbox_domenica
     */
    private void set_CheckBox_days(boolean[] ripetition_array, CheckBox checkbox_lunedì, CheckBox checkbox_martedì, CheckBox checkbox_mercoledì, CheckBox checkbox_giovedì, CheckBox checkbox_venerdì, CheckBox checkbox_sabato, CheckBox checkbox_domenica){
        if(ripetition_array[0]){
            checkbox_lunedì.setChecked(true);
        }
        if(ripetition_array[1]){
            checkbox_martedì.setChecked(true);
        }
        if(ripetition_array[2]){
            checkbox_mercoledì.setChecked(true);
        }
        if(ripetition_array[3]){
            checkbox_giovedì.setChecked(true);
        }
        if(ripetition_array[4]){
            checkbox_venerdì.setChecked(true);
        }
        if(ripetition_array[5]){
            checkbox_sabato.setChecked(true);
        }
        if(ripetition_array[6]){
            checkbox_domenica.setChecked(true);
        }
    }

    /**
     * Funzione che setta l'array delle scelte di ripetizione da inviare all'activity chiamante
     * @return
     * @param checkbox_lunedì
     * @param checkbox_martedì
     * @param checkbox_mercoledì
     * @param checkbox_giovedì
     * @param checkbox_venerdì
     * @param checkbox_sabato
     * @param checkbox_domenica
     */
    private boolean[] get_res_ripetition(CheckBox checkbox_lunedì, CheckBox checkbox_martedì, CheckBox checkbox_mercoledì, CheckBox checkbox_giovedì, CheckBox checkbox_venerdì, CheckBox checkbox_sabato, CheckBox checkbox_domenica){
        boolean[] res = new boolean[7];

        for(int i=0; i<7; i++){
            res[i] = false;
        }

        if(checkbox_lunedì.isChecked()){
            res[0] = true;
        }
        if(checkbox_martedì.isChecked()){
            res[1] = true;
        }
        if(checkbox_mercoledì.isChecked()){
            res[2] = true;
        }
        if(checkbox_giovedì.isChecked()){
            res[3] = true;
        }
        if(checkbox_venerdì.isChecked()){
            res[4] = true;
        }
        if(checkbox_sabato.isChecked()){
            res[5] = true;
        }
        if(checkbox_domenica.isChecked()){
            res[6] = true;
        }

        return res;
    }

    private void cardView_Animation(CardView cardView){

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 1000, 0);
        translateAnimation.setDuration(animationTime);
        translateAnimation.setFillAfter(true);
        cardView.startAnimation(translateAnimation);

    }
}
