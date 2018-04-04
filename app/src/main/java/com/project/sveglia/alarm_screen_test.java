package com.project.sveglia;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by simonerigon on 03/04/18.
 */

public class alarm_screen_test extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_screen);

        final int NOT_ID = getIntent().getExtras().getInt("notification_ID");

        // recupero riferimenti layout -------------------
        // Button
        Button btn_cancel = (Button)findViewById(R.id.btn_termina_alarm_ID);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(NOT_ID);
                finish();
            }
        });


    }
}
