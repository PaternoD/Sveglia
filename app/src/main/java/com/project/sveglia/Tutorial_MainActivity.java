package com.project.sveglia;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

/**
 * Created by Pat on 25/07/18.
 */

public class Tutorial_MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tutorial_mainactivity);

        ViewFlipper tutorial = (ViewFlipper) findViewById(R.id.view_tutorial);

        Bitmap bit1 = BitmapFactory.decodeResource(Tutorial_MainActivity.this.getResources(),R.drawable.main_1);
        ImageView slide1 = (ImageView) findViewById(R.id.tutorial1);
        slide1.setImageBitmap(bit1);

        Bitmap bit2 = BitmapFactory.decodeResource(Tutorial_MainActivity.this.getResources(),R.drawable.main_2);
        ImageView slide2 = (ImageView) findViewById(R.id.tutorial2);
        slide2.setImageBitmap(bit2);

        Bitmap bit3 = BitmapFactory.decodeResource(Tutorial_MainActivity.this.getResources(),R.drawable.main_3);
        ImageView slide3 = (ImageView) findViewById(R.id.tutorial3);
        slide3.setImageBitmap(bit3);

        Bitmap bit4 = BitmapFactory.decodeResource(Tutorial_MainActivity.this.getResources(),R.drawable.main_4);
        ImageView slide4 = (ImageView) findViewById(R.id.tutorial4);
        slide4.setImageBitmap(bit4);

        Bitmap bit5 = BitmapFactory.decodeResource(Tutorial_MainActivity.this.getResources(),R.drawable.main_5);
        ImageView slide5 = (ImageView) findViewById(R.id.tutorial5);
        slide5.setImageBitmap(bit5);

        Bitmap bit6 = BitmapFactory.decodeResource(Tutorial_MainActivity.this.getResources(),R.drawable.main_6);
        ImageView slide6 = (ImageView) findViewById(R.id.tutorial6);
        slide6.setImageBitmap(bit6);


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
                tutorial.showNext();
            }
        });
        slide5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tutorial.showNext();
            }
        });

        slide6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Tutorial_MainActivity.this.finish();
            }
        });

    }
}
