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

public class Tutorial_TravelTo extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tutorial_travel_to);

        ViewFlipper tutorial = (ViewFlipper) findViewById(R.id.view_tutorial_travel_to);

        Bitmap bit1 = BitmapFactory.decodeResource(Tutorial_TravelTo.this.getResources(),R.drawable.travel_to_1);
        ImageView slide1 = (ImageView) findViewById(R.id.tutorial_travel_to_1);
        slide1.setImageBitmap(bit1);

        Bitmap bit2 = BitmapFactory.decodeResource(Tutorial_TravelTo.this.getResources(),R.drawable.travel_to_2);
        ImageView slide2 = (ImageView) findViewById(R.id.tutorial_travel_to_2);
        slide2.setImageBitmap(bit2);

        Bitmap bit3 = BitmapFactory.decodeResource(Tutorial_TravelTo.this.getResources(),R.drawable.travel_to_3);
        ImageView slide3 = (ImageView) findViewById(R.id.tutorial_travel_to_3);
        slide3.setImageBitmap(bit3);

        Bitmap bit4 = BitmapFactory.decodeResource(Tutorial_TravelTo.this.getResources(),R.drawable.travel_to_4);
        ImageView slide4 = (ImageView) findViewById(R.id.tutorial_travel_to_4);
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
                Tutorial_TravelTo.this.finish();
            }
        });

    }
}
