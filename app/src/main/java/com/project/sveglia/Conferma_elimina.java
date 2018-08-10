package com.project.sveglia;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Pat on 01/08/18.
 */

public class Conferma_elimina extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conferma_elimina);
        Button btn_ok = (Button) findViewById(R.id.ok_conferma_elimina);
        Button btn_annulla = (Button) findViewById(R.id.annulla_conferma_elimina);
        TextView testo_conferma_elimina = (TextView) findViewById(R.id.testo_conferma_elimina);

        String time_sveglia = getIntent().getStringExtra("time_sveglia");
        int id = getIntent().getIntExtra("id",9999);
        int position = getIntent().getIntExtra("position",9999);

        testo_conferma_elimina.setText("Confermi di eliminare la sveglia impostata alle " + time_sveglia+"?");

        btn_annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 100);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB_Manager db = new DB_Manager(getApplicationContext());
                db.open();
                Cancel_Alarm_Class.cancel_Alarm(id,getApplicationContext(),db,true);
                db.close();
                SetViewSveglie.aggiornaAdapter_rimuovi(position);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 100);
            }
        });
    }
}
