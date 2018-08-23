package com.project.sveglia;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.PlaceAutocompleteType;
import com.google.maps.model.TravelMode;
import com.google.maps.DirectionsApi;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private String originString = null;
    private String originPosition = null;
    private String destinationSring = null;
    private String destinationPosition = null;
    private DateTime arrival_DateTime = null;
    private TravelMode travel_Mode = TravelMode.DRIVING;
    private boolean modify_intent = false;
    RecyclerView recyclerView_Data;
    RelativeLayout relativeLayout_ProgressBar;
    private boolean isNullTime = true;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionsGranted = false;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCAL_PERMISSION_REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        // Recupero riferimenti oggetti nel layout ----------------------------
        // CardView
        CardView origin_Card_autocomplete = (CardView)findViewById(R.id.autocomp_origin_Card_ID);
        CardView destination_Card_autocomplete = (CardView)findViewById(R.id.autocomp_destination_Card_ID);
        CardView btn_arrival_time = (CardView) findViewById(R.id.Arrival_time_Card);
        CardView card_Current_Location = (CardView)findViewById(R.id.Card_CurrentLocation);

        // Button
        //final Button btn_arrival_time = (Button)findViewById(R.id.btn_arrival_time_ID);
        Button btn_search_google = (Button)findViewById(R.id.btn_search_Google_ID);

        // ImageButton
        final ImageButton car_imageButton = (ImageButton)findViewById(R.id.imageButton_car_ID);
        final ImageButton train_imageButton = (ImageButton)findViewById(R.id.imageButton_transit_ID);
        final ImageButton walking_imageButton = (ImageButton)findViewById(R.id.imageButton_walking_ID);
        ImageButton info_Maps = (ImageButton)findViewById(R.id.info_maps_id);
        ImageButton imageButton_current_location = (ImageButton)findViewById(R.id.ImageButton_Current_View);

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
        final TextView departure_textView = (TextView)findViewById(R.id.text_arrival_time_ID);

        // RecyclerView
        final RecyclerView recyclerView_Data = (RecyclerView)findViewById(R.id.recyclerViewData_ID);

        // Relative Layout
        relativeLayout_ProgressBar = (RelativeLayout)findViewById(R.id.progressBar_Relative_Layout_ID);

        // Progress Bar
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar2);

        // Setto visualizzazione "relativeLayout_ProgressBar" e progressBar ---
        relativeLayout_ProgressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.CircleRepetitionAlarm), android.graphics.PorterDuff.Mode.MULTIPLY);

        // Recupero informazioni da activity chiamante ------------------------
        modify_intent = getIntent().getExtras().getBoolean("modify_intent");
        if(modify_intent){
            originString = getIntent().getExtras().getString("startAddress");
            originPosition = originString;
            destinationSring = getIntent().getExtras().getString("endAddress");
            destinationPosition = destinationSring;

            origin_TextView.setText(originString);
            destination_TextView.setText(destinationSring);
        }else{
            // Recupero posizione device --
            getLocationPermission();
            getCurrentLocation();
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
        Bitmap info_image = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.information_outline_24);
        Bitmap currentLocation = BitmapFactory.decodeResource(MapsActivity.this.getResources(), R.drawable.icons8_target_24);

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
        info_Maps.setImageBitmap(info_image);
        imageButton_current_location.setImageBitmap(currentLocation);

        arrow_ImageView.setImageTintList(ColorStateList.valueOf(Color.WHITE));

        // Aggiungo azioni a bottoni di selezione veicolo per il viaggio ------
        car_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                car_imageButton.setBackgroundResource(R.drawable.roundcorner_trasparent);
                train_imageButton.setBackgroundResource(R.drawable.roundcorner_trasparent);
                walking_imageButton.setBackgroundResource(R.drawable.roundcorner_white);

                // Setto travel_Mode
                travel_Mode = TravelMode.WALKING;
            }
        });

        // Aggiungo azione bottone current Location --
        card_Current_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        // Aggiungo azione al bottone di switch (origin / destination location) --
        arrow_switch_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(originString == null || destinationSring == null){
                    Log.i("INVERT POSITION MAP", "Non posso invertire le posizioni in quanto alcune non sono state ancora settate");
                }else{
                    // inverto origin e destination String --
                    String temp = originString;
                    String temp_1 = originPosition;
                    originString = destinationSring;
                    originPosition = destinationPosition;
                    destinationSring = temp;
                    destinationPosition = temp_1;

                    // Recupero riferimenti layout
                    // TextView
                    TextView origin_TextView = (TextView)findViewById(R.id.text_origin_ID);
                    TextView destination_TextView = (TextView)findViewById(R.id.text_destination_ID);

                    origin_TextView.setText(originString);
                    destination_TextView.setText(destinationSring);

                    System.out.println("ORIGIN POSITION = " + originPosition);
                    System.out.println("DESTINATION POSITION = " + destinationPosition);
                }
            }
        });

        // Aggiungo azione bottone per scegliere ora e data di arrivo -------
        btn_arrival_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int GOOGLE_TIME_DATE_ID = 3;

                setNormalTextColor(departure_textView, null);

                Intent arrival_time_intent = new Intent(MapsActivity.this, TimePicker_Google.class);
                startActivityForResult(arrival_time_intent, GOOGLE_TIME_DATE_ID);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

        //bottone tutorial
        info_Maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this,Tutorial_TravelTo.class);
                startActivity(intent);
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

        // Aggiungo azione al bottone per cercare le indicazioni tramite google Maps --
        btn_search_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetGoogleMapsResult.clearRecyclerView();

                if(originString == null || destinationSring == null || isNullTime == true){
                    Toast.makeText(MapsActivity.this, "Completa tutti i campi", Toast.LENGTH_LONG).show();
                    // Vedere se si riesce a colorare i campi da completare !!!!
                    if(originString == null){
                        setErrorTextColor(origin_TextView, null);
                    }
                    if(destinationSring == null){
                        setErrorTextColor(destination_TextView, null);
                    }
                    if(isNullTime){
                        setErrorTextColor(departure_textView, null);
                    }
                }else {
                    if(isHistoryTime(arrival_DateTime)){
                        Toast.makeText(MapsActivity.this, "La data non deve essere nel passato", Toast.LENGTH_LONG).show();
                    }else {
                        // resetto recyclerView
                        relativeLayout_ProgressBar.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getDirectionResult();
                            }
                        }, 1500);

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

    @Override
    public void onBackPressed(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("modify_result_intent", modify_intent);
        setResult(Activity.RESULT_CANCELED, resultIntent);
        super.onBackPressed();
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
                originPosition = origin_Place.getAddress().toString();
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
                destinationPosition = destination_Place.getAddress().toString();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Azioni nel caso l'intent non restituisca nulla
            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                isNullTime = false;
                // Recupero dati dall'activity chiamata
                long arrival_time = data.getExtras().getLong("arrival_time");
                String res_date = getDateFormattedFromMillis(arrival_time);

                // Button
                TextView btn_arrival_time = (TextView)findViewById(R.id.text_arrival_time_ID);
                btn_arrival_time.setText(res_date);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                isNullTime = true;
                // Azioni nel caso l'intent non restituisca nulla
            }
        }
        if (requestCode == 8) {
            if (resultCode == Activity.RESULT_OK) {

                // Recupero dati da intent --
                long alarm_time = data.getExtras().getLong("alarm_time");
                String transit_model = data.getExtras().getString("transit_model");
                String start_address = data.getExtras().getString("start_address");
                String end_address = data.getExtras().getString("end_address");
                String maps_direction_request = data.getExtras().getString("maps_direction_request");
                String from_bed_to_car_added = data.getExtras().getString("from_bed_to_car_added");

                Intent resultIntent = new Intent();
                resultIntent.putExtra("alarm_time", alarm_time);
                resultIntent.putExtra("transit_model", transit_model);
                resultIntent.putExtra("start_address", start_address);
                resultIntent.putExtra("end_address", end_address);
                resultIntent.putExtra("maps_direction_request", maps_direction_request);
                resultIntent.putExtra("from_bed_to_car_added", from_bed_to_car_added);
                setResult(Activity.RESULT_OK, resultIntent);
                MapsActivity.this.finish();


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
        DateFormat dateFormat = new SimpleDateFormat("HH:mm   d/M/yyyy");
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
        recyclerView_Data = (RecyclerView)findViewById(R.id.recyclerViewData_ID);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_Data.setLayoutManager(linearLayoutManager);

        // Recupero riferimento RelativeLayout
        RelativeLayout relativeLayout_noResult = (RelativeLayout)findViewById(R.id.relative_Layout_no_result_ID);

        // Setto la chiave Google
        GeoApiContext geoContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyCN3jt2CpuG_SNF2uKNa5_cfPtyAMpXVVQ")
                .build();

        DirectionsResult result = null;

        try{
            result = DirectionsApi.newRequest(geoContext)
                    .origin(originPosition)
                    .destination(destinationPosition)
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
            relativeLayout_ProgressBar.setVisibility(View.GONE);
            relativeLayout_noResult.setVisibility(View.VISIBLE);

        }else{
            //Toast.makeText(MapsActivity.this, "Alcune rotte trovate", Toast.LENGTH_LONG).show();
            recyclerView_Data.setVisibility(View.VISIBLE);
            relativeLayout_noResult.setVisibility(View.GONE);

            // Invio dati a SetGoogleMapsResult()
            SetGoogleMapsResult.setDataModelGoogleMaps(result, travel_Mode, recyclerView_Data, relativeLayout_noResult, MapsActivity.this, arrival_DateTime, relativeLayout_ProgressBar);
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
            text.setTextColor(getResources().getColor(R.color.DefaultColorText));
        }
        if(btn != null){
            btn.setTextColor(getResources().getColor(R.color.DefaultColorText));
        }

    }

    /**
     * Funzione per ricavare la posizione del device
     */
    private void getCurrentLocation(){
        Log.d("MAPS_ACTIVITY", "getCurrentLocation: getting the device current location.");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
        System.out.println("getLocationPermission: " + mLocationPermissionsGranted);

        try{
            if(mLocationPermissionsGranted){
                com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        if(task.isSuccessful()){
                            Log.d("MAPS_ACTIVITY", "onComplete: Found Current Location");
                            Location currentLocation = (Location)task.getResult();
                            if(currentLocation != null) {
                                originString = "Origine: la tua posizione";
                                originPosition = currentLocation.getLatitude() + "," + currentLocation.getLongitude();

                                TextView origin_TextView = (TextView) findViewById(R.id.text_origin_ID);
                                origin_TextView.setText(originString);

                                System.out.println("****** Current Location : Lat: " + currentLocation.getLatitude() + ", long: " + currentLocation.getLongitude());
                            }
                        }else{
                            Log.d("MAPS_ACTIVITY", "onComplete: Current location is null");
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("MAPS_ACTIVITY", "getCurrentLocation: SecurityException: " + e.getMessage());
        }


    }

    /**
     * Funzione per controllare se abbiamo il permesso di calcolare la posizione del device, in caso non
     * avessimo il permesso verrà chiesto all'utente di se desidera consentire la ricerca della posizione
     * del dispositivo.
     */
    private void getLocationPermission(){
        Log.d("MAPS_ACTIVITY", "getLocationPermission: getting location permission");
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(this, permission, LOCAL_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permission, LOCAL_PERMISSION_REQUEST_CODE);
        }
    }

}
