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

public class Tutorial_AddSveglia extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tutorial_addsveglia);

        ViewFlipper tutorial = (ViewFlipper) findViewById(R.id.view_tutorial_addsveglia);

        Bitmap bit1 = BitmapFactory.decodeResource(Tutorial_AddSveglia.this.getResources(),R.drawable.add_sveglia_1);
        ImageView slide1 = (ImageView) findViewById(R.id.tutorial_add_sveglia_1);
        slide1.setImageBitmap(bit1);

        Bitmap bit2 = BitmapFactory.decodeResource(Tutorial_AddSveglia.this.getResources(),R.drawable.add_sveglia_2);
        ImageView slide2 = (ImageView) findViewById(R.id.tutorial_add_sveglia_2);
        slide2.setImageBitmap(bit2);

        Bitmap bit3 = BitmapFactory.decodeResource(Tutorial_AddSveglia.this.getResources(),R.drawable.add_sveglia_3);
        ImageView slide3 = (ImageView) findViewById(R.id.tutorial_add_sveglia_3);
        slide3.setImageBitmap(bit3);

        Bitmap bit4 = BitmapFactory.decodeResource(Tutorial_AddSveglia.this.getResources(),R.drawable.add_sveglia_4);
        ImageView slide4 = (ImageView) findViewById(R.id.tutorial_add_sveglia_4);
        slide4.setImageBitmap(bit4);


        slide1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tutorial.showNext();
            }
        });
        slide2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tutorial.showNext();

            }
        });

        slide3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tutorial.showNext();
            }
        });



        slide4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Tutorial_AddSveglia.this.finish();
            }
        });

    }
}
