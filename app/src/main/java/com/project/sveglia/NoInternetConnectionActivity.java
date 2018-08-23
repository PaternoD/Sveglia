package com.project.sveglia;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by simonerigon on 17/04/18.
 */

public class NoInternetConnectionActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet_connection);

        // Recupero riferimenti layout ------------------
        // TextView
        TextView no_internet_conn_textView = (TextView)findViewById(R.id.add_time_google_maps);

        // Button
        Button btn_no_conn = (Button)findViewById(R.id.btn_no_conn_ID);

        // Setto testo nel layout -----------------------
        String text_no_internet = "Non puoi usare questa funzione. Nessuna connesione a internet.";
        no_internet_conn_textView.setText(text_no_internet);

        // Setto azione Bottone -------------------------
        btn_no_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoInternetConnectionActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
