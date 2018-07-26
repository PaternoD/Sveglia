package com.project.sveglia;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

/**
 * Created by Pat on 25/07/18.
 */

public class Tutorial_Setting extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tutorial_settings);

        ViewFlipper tutorial = (ViewFlipper) findViewById(R.id.tutorial_settings_view);

        Bitmap bit1 = BitmapFactory.decodeResource(Tutorial_Setting.this.getResources(),R.drawable.settings_1);
        ImageView slide1 = (ImageView) findViewById(R.id.tutorial_settings_1);
        slide1.setImageBitmap(bit1);

        Bitmap bit2 = BitmapFactory.decodeResource(Tutorial_Setting.this.getResources(),R.drawable.settings_2);
        ImageView slide2 = (ImageView) findViewById(R.id.tutorial_settings_2);
        slide2.setImageBitmap(bit2);



        slide1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tutorial.showNext();
            }
        });


        slide2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Tutorial_Setting.this.finish();
            }
        });

    }
}
