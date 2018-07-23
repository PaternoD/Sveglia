package com.project.sveglia;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by simonerigon on 23/07/18.
 */

public class Time_Passed_Pop_Up_Google extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_passed_pop_up_layout);


        // Ottengo riferimenti layout --------------------------
        // TextView
        TextView info_time = (TextView)findViewById(R.id.info_text_time_passed_google_ID);

        // Button
        Button btn_ok = (Button)findViewById(R.id.btn_ok_time_passed_google);

        String text = "L'orario di partenza è nel passato.";
        info_time.setText(text);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time_Passed_Pop_Up_Google.this.finish();
            }
        });

    }
}
