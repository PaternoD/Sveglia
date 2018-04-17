package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.DirectionsApi;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private String originString = null;
    private String destinationSring = null;
    private DateTime arrival_DateTime = null;
    private TravelMode travel_Mode = TravelMode.DRIVING;
    private boolean modify_intent = false;
    private boolean arrivalTimeClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Recupero riferimenti oggetti nel layout ----------------------------
        // CardView
        CardView origin_Card_autocomplete = (CardView)findViewById(R.id.autocomp_origin_Card_ID);
        CardView destination_Card_autocomplete = (CardView)findViewById(R.id.autocomp_destination_Card_ID);

        // Button
        final Button btn_arrival_time = (Button)findViewById(R.id.btn_arrival_time_ID);
        Button btn_search_google = (Button)findViewById(R.id.btn_search_Google_ID);

        // ImageButton
        final ImageButton car_imageButton = (ImageButton)findViewById(R.id.imageButton_car_ID);
        final ImageButton train_imageButton = (ImageButton)findViewById(R.id.imageButton_transit_ID);
        final ImageButton walking_imageButton = (ImageButton)findViewById(R.id.imageButton_walking_ID);

        // ImageView
        ImageView time_imageView = (ImageView)findViewById(R.id.time_clock_Image_ID);
        ImageView arrow_ImageView = (ImageView)findViewById(R.id.arrow_imageView_ID);
        ImageView dot_ImageView = (ImageView)findViewById(R.id.dot_ImageView_ID);
        ImageView menu_Icon_ImageView = (ImageView)findViewById(R.id.menu_icon_ImageView_ID);
        ImageView location_ImageView = (ImageView)findViewById(R.id.location_ImageView_ID);
        ImageView arrow_switch_ImageView = (ImageView)findViewById(R.id.arrow_switch_ImageView_ID);

        // TextView
        final TextView origin_TextView = (TextView)findViewById(R.id.text_origin_ID);
        final TextView destination_TextView = (TextView)findViewById(R.id.text_destination_ID);

        // Recupero informazioni da activity chiamante ------------------------
        modify_intent = getIntent().getExtras().getBoolean("modify_intent");
        if(modify_intent){
            originString = getIntent().getExtras().getString("startAddress");
            destinationSring = getIntent().getExtras().getString("endAddress");

            origin_TextView.setText(originString);
            destination_TextView.setText(destinationSring);
        }

        // Recupero immagini da assegnare al layout ---------------------------
        Bitmap car_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_car_24);
        Bitmap train_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_train_24);
        Bitmap walking_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_walking_24);
        Bitmap timeClock_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_clock_48);
        Bitmap dot_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_round_filled_24);
        Bitmap dot_menu_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_menu_vertical_24);
        Bitmap location_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_marker_48);
        Bitmap arrow_switch_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_up_down_arrow_48);
        Bitmap arrow_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_left_48);

        // Setto immagini al layout -------------------------------------------
        car_imageButton.setImageBitmap(car_image);
        train_imageButton.setImageBitmap(train_image);
        walking_imageButton.setImageBitmap(walking_image);
        time_imageView.setImageBitmap(timeClock_image);
        arrow_ImageView.setImageBitmap(arrow_image);
        dot_ImageView.setImageBitmap(dot_image);
        menu_Icon_ImageView.setImageBitmap(dot_menu_image);
        location_ImageView.setImageBitmap(location_image);
        arrow_switch_ImageView.setImageBitmap(arrow_switch_image);


        // Setto visualizzazione del Layout -----------------------------------
        car_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_1));
        train_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
        walking_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
        time_imageView.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
        arrow_ImageView.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
        dot_ImageView.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
        menu_Icon_ImageView.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
        location_ImageView.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
        arrow_switch_ImageView.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));


        // Aggiungo azioni a bottoni di selezione veicolo per il viaggio ------
        car_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                car_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_1));
                train_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
                walking_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));

                car_imageButton.setBackgroundResource(R.drawable.roundcorner_white);
                train_imageButton.setBackgroundResource(R.drawable.roundcorner_trasparent);
                walking_imageButton.setBackgroundResource(R.drawable.roundcorner_trasparent);

                // Setto travel_Mode
                travel_Mode = TravelMode.DRIVING;
            }
        });

        train_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                car_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
                train_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_1));
                walking_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));

                car_imageButton.setBackgroundResource(R.drawable.roundcorner_trasparent);
                train_imageButton.setBackgroundResource(R.drawable.roundcorner_white);
                walking_imageButton.setBackgroundResource(R.drawable.roundcorner_trasparent);

                // Setto travel_Mode
                travel_Mode = TravelMode.TRANSIT;
            }
        });

        walking_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                car_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
                train_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_2));
                walking_imageButton.setImageTintList(MapsActivity.this.getColorStateList(R.color.buttonTransitButton_1));

                car_imageButton.setBackgroundResource(R.drawable.roundcorner_trasparent);
                train_imageButton.setBackgroundResource(R.drawable.roundcorner_trasparent);
                walking_imageButton.setBackgroundResource(R.drawable.roundcorner_white);

                // Setto travel_Mode
                travel_Mode = TravelMode.WALKING;
            }
        });

        // Aggiungo azione al bottone di switch (origin / destination location) --
        arrow_switch_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inverto origin e destination String --
                String temp = originString;
                originString = destinationSring;
                destinationSring = temp;

                // Recupero riferimenti layout
                // TextView
                TextView origin_TextView = (TextView)findViewById(R.id.text_origin_ID);
                TextView destination_TextView = (TextView)findViewById(R.id.text_destination_ID);

                if(originString != null && destinationSring != null) {
                    origin_TextView.setText(originString);
                    destination_TextView.setText(destinationSring);
                }
            }
        });

        // Aggiungo azione bottone per scegliere ora e data di arrivo -------
        btn_arrival_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int GOOGLE_TIME_DATE_ID = 3;

                arrivalTimeClick = true;

                setNormalTextColor(null, btn_arrival_time);

                Intent arrival_time_intent = new Intent(MapsActivity.this, TimePicker_Google.class);
                startActivityForResult(arrival_time_intent, GOOGLE_TIME_DATE_ID);
            }
        });


        // Aggiungo azioni di autocompletamento per la ricerca dell'origine e della destinazione --
        origin_Card_autocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ORIGIN_PLACE_ID = 1;

                setNormalTextColor(origin_TextView, null);

                try{
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(MapsActivity.this);
                    startActivityForResult(intent, ORIGIN_PLACE_ID);
                } catch(GooglePlayServicesRepairableException e){
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        destination_Card_autocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int DESTINATION_PLACE_ID = 2;

                setNormalTextColor(destination_TextView, null);

                try{
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(MapsActivity.this);
                    startActivityForResult(intent, DESTINATION_PLACE_ID);
                } catch(GooglePlayServicesRepairableException e){
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        // Aggiungo azione al bottone per cercare le indicazione tramite google Maps --
        btn_search_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(originString == null || destinationSring == null || arrival_DateTime == null){
                    Toast.makeText(MapsActivity.this, "Completa tutti i campi", Toast.LENGTH_LONG).show();
                    // Vedere se si riesce a colorare i campi da completare !!!!
                    if(originString == null){
                        setErrorTextColor(origin_TextView, null);
                    }
                    if(destinationSring == null){
                        setErrorTextColor(destination_TextView, null);
                    }
                    if(!arrivalTimeClick){
                        setErrorTextColor(null, btn_arrival_time);
                    }
                }else {
                    if(isHistoryTime(arrival_DateTime)){
                        Toast.makeText(MapsActivity.this, "La data non deve essere nel passato", Toast.LENGTH_LONG).show();
                    }else {
                        getDirectionResult();
                    }
                }
            }
        });

        // Aggiungo azione per annullare activity
        arrow_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("modify_result_intent", modify_intent);
                setResult(Activity.RESULT_CANCELED, resultIntent);
                MapsActivity.this.finish();
            }
        });

    }

    /**
     * Funzione invocata per ricevere i dati da StartActivityForResult()
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Recupero riferimenti layout
        // TextView
        TextView origin_TextView = (TextView)findViewById(R.id.text_origin_ID);
        TextView destination_TextView = (TextView)findViewById(R.id.text_destination_ID);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Place origin_Place = PlaceAutocomplete.getPlace(this, data);
                origin_TextView.setText(origin_Place.getAddress());
                originString = origin_Place.getAddress().toString();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Place destination_Place = PlaceAutocomplete.getPlace(this, data);
                destination_TextView.setText(destination_Place.getAddress());
                destinationSring = destination_Place.getAddress().toString();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                // Recupero dati dall'activity chiamata
                long arrival_time = data.getExtras().getLong("arrival_time");
                String res_date = getDateFormattedFromMillis(arrival_time);

                // Button
                Button btn_arrival_time = (Button)findViewById(R.id.btn_arrival_time_ID);
                btn_arrival_time.setText(res_date);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }
    }

    /**
     * Funzione che formatta l'ora da timeInMillis e setta DateTime (per la ricerca)
     * @param time
     * @return formatted date HH:mm   d - M - YYYY
     */
    private String getDateFormattedFromMillis(long time){
        String date = null;
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm   d/M/YYYY");
        calendar.setTimeInMillis(time);
        date = dateFormat.format(calendar.getTime());
        arrival_DateTime = new DateTime(calendar);

        return date;
    }

    /**
     * Funzione la quale invia la richiesta a google maps e riceve la risposta con i dati necessari
     */
    private void getDirectionResult(){

        // Recupero riferimento a recyclerView nel Layout ----------
        RecyclerView recyclerView_Data = (RecyclerView)findViewById(R.id.recyclerViewData_ID);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_Data.setLayoutManager(linearLayoutManager);

        // Recupero riferimento RelativeLayout
        RelativeLayout relativeLayout_noResult = (RelativeLayout)findViewById(R.id.relative_Layout_no_result_ID);

        // Setto la chiave Google
        GeoApiContext geoContext = new GeoApiContext().setApiKey("AIzaSyCN3jt2CpuG_SNF2uKNa5_cfPtyAMpXVVQ");

        DirectionsResult result = null;

        try{
            result = DirectionsApi.newRequest(geoContext)
                    .origin(originString)
                    .destination(destinationSring)
                    .alternatives(true)
                    .mode(travel_Mode)
                    .arrivalTime(arrival_DateTime)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(result == null){
            //Toast.makeText(MapsActivity.this, "Nessuna rotta trovata", Toast.LENGTH_LONG).show();
            recyclerView_Data.setVisibility(View.GONE);
            relativeLayout_noResult.setVisibility(View.VISIBLE);

        }else{
            //Toast.makeText(MapsActivity.this, "Alcune rotte trovate", Toast.LENGTH_LONG).show();
            recyclerView_Data.setVisibility(View.VISIBLE);
            relativeLayout_noResult.setVisibility(View.GONE);

            // Invio dati a SetGoogleMapsResult()
            SetGoogleMapsResult.setDataModelGoogleMaps(result, travel_Mode, recyclerView_Data, relativeLayout_noResult, MapsActivity.this, arrival_DateTime);
        }

    }

    /**
     * Funzione che mi dice se il tempo in input è minore del tempo corrente
     * @param time
     * @return true, is the input time is in the past
     */
    private boolean isHistoryTime(DateTime time){

        boolean res = false;

        Calendar cal = Calendar.getInstance();

        long time_1 = time.getMillis();
        long currentTime = cal.getTimeInMillis();

        if(time_1 <= currentTime){
            res = true;
        }

        return res;

    }

    /**
     * Funzione per colorare il testo dei parametri mancanti per completare la funzione desiderata
     * @param text
     * @param btn
     */
    private void setErrorTextColor(TextView text, Button btn){

        if(text != null){
            text.setTextColor(getResources().getColor(R.color.ErrorText));
        }
        if(btn != null){
            btn.setTextColor(getResources().getColor(R.color.ErrorText));
        }

    }

    /**
     * Funzione per riportare il colore del testo dei parametri mancanti normale
     * @param text
     * @param btn
     */
    private void setNormalTextColor(TextView text, Button btn){

        if(text != null){
            text.setTextColor(Color.WHITE);
        }
        if(btn != null){
            btn.setTextColor(Color.WHITE);
        }

    }
}
