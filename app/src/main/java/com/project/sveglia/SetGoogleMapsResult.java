package com.project.sveglia;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akexorcist.googledirection.model.Step;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by simonerigon on 28/02/18.
 */

public class SetGoogleMapsResult {

    static String route_detail;
    static String duration_detail;
    static long duration_in_seconds;
    static long arrival_time_in_millis;
    static long departure_time;
    static String distance_detail;
    static String departure_detail;
    static String start_address;
    static String end_address;
    static String origin_location;
    static String destination_location;



    private static ArrayList<DataModelGoogleMaps> data;
    private static RecyclerView.Adapter adapter;
    private static RelativeLayout relativeLayout;


    public static void setDataModelGoogleMaps(DirectionsResult result, TravelMode travel_Model, RecyclerView recyclerView, RelativeLayout relLay_noResult, Activity myActivity, DateTime arrival_time, RelativeLayout relativeLayout_progressBar){

        relativeLayout = relativeLayout_progressBar;

        if(travel_Model == TravelMode.DRIVING){
            setDataModelForDriving(result, recyclerView, relLay_noResult, myActivity, arrival_time);
        } else if(travel_Model == TravelMode.TRANSIT){
            setDataModelForTransit(result, recyclerView, relLay_noResult, myActivity, arrival_time);
        } else if(travel_Model == TravelMode.WALKING){
            setDataModelForWalking(result, recyclerView, relLay_noResult, myActivity, arrival_time);
        }

    }

    private static void setDataModelForDriving(DirectionsResult result, RecyclerView recyclerView, RelativeLayout relLay_noResult, Activity myActivity, DateTime arrival_time){

        // Variabili
        int route_size = result.routes.length;

        if(route_size == 0){
            recyclerView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
            relLay_noResult.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            relLay_noResult.setVisibility(View.GONE);

            data = new ArrayList<DataModelGoogleMaps>();

            for (int i = 0; i < route_size; i++) {

                String summary = result.routes[i].summary.toString();
                route_detail = "Tramite " + summary;

                duration_detail = result.routes[i].legs[0].duration.toString();

                duration_in_seconds = result.routes[i].legs[0].duration.inSeconds;

                arrival_time_in_millis = arrival_time.getMillis();

                distance_detail = result.routes[i].legs[0].distance.toString();

                departure_detail = null;

                departure_time = 0;

                start_address = result.routes[i].legs[0].startAddress;

                end_address = result.routes[i].legs[0].endAddress;

                origin_location = "&origin=" + result.routes[i].legs[0].startLocation.lat + "," + result.routes[i].legs[0].startLocation.lng;

                destination_location = "&destination=" + result.routes[i].legs[0].endLocation.lat + "," + result.routes[i].legs[0].endLocation.lng;

                String waypoint = "";
                DirectionsStep[] step = result.routes[i].legs[0].steps;

                int stepsLenght = result.routes[i].legs[0].steps.length;
                if(stepsLenght > 5){
                    int mid = stepsLenght/2;
                    int mid_min = mid/2;
                    int mid_max = mid + mid_min;

                    waypoint =  waypoint + "&waypoints=" + step[mid_min].endLocation.lat + "," + step[mid_min].endLocation.lng;
                    waypoint =  waypoint + "&waypoints=" + step[mid].endLocation.lat + "," + step[mid].endLocation.lng;
                    waypoint =  waypoint + "&waypoints=" + step[mid_max].endLocation.lat + "," + step[mid_max].endLocation.lng;
                }else{
                    waypoint =  waypoint + "&waypoints=" + step[stepsLenght/2].endLocation.lat + "," + step[stepsLenght/2].endLocation.lng;
                }

                data.add(new DataModelGoogleMaps(route_detail, duration_detail, duration_in_seconds, arrival_time_in_millis, departure_time, distance_detail, departure_detail, start_address, end_address, waypoint, origin_location, destination_location));

            }

            adapter = new CustomAdapterDriving(data, myActivity);

            recyclerView.setAdapter(adapter);

            relativeLayout.setVisibility(View.GONE);
        }

    }

    private static void setDataModelForTransit(DirectionsResult result, RecyclerView recyclerView, RelativeLayout relLay_noResult, Activity myActivity, DateTime arrival_time){

        // Variabili
        int route_size = result.routes.length;

        if(route_size == 0){
            recyclerView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
            relLay_noResult.setVisibility(View.VISIBLE);
        }else {

            try {
                recyclerView.setVisibility(View.VISIBLE);
                relLay_noResult.setVisibility(View.GONE);

                data = new ArrayList<DataModelGoogleMaps>();

                for (int i = 0; i < route_size; i++) {

                    DateTime departure_DateTime = result.routes[i].legs[0].departureTime;
                    int departure_hour = departure_DateTime.getHourOfDay();
                    int departure_min = departure_DateTime.getMinuteOfHour();
                    DateTime arrival_DateTime = result.routes[i].legs[0].arrivalTime;
                    int arrival_hour = arrival_DateTime.getHourOfDay();
                    int arrival_min = arrival_DateTime.getMinuteOfHour();

                    long duration_in_seconds = result.routes[i].legs[0].duration.inSeconds;

                    if (duration_in_seconds > 86400) {
                        long arrival_time_in_millis = arrival_DateTime.getMillis();
                        long departure_time_in_millis = arrival_time_in_millis - (duration_in_seconds * 1000);
                        String departure_day = getDay(departure_time_in_millis);
                        String arrival_day = getDay(arrival_time_in_millis);

                        route_detail = departure_hour + ":" + departure_min + " (" + departure_day + ") " + " - " + arrival_hour + ":" + arrival_min + " (" + arrival_day + ")";
                    } else {
                        route_detail = departure_hour + ":" + departure_min + " - " + arrival_hour + ":" + arrival_min;
                    }

                    duration_detail = result.routes[i].legs[0].duration.toString();

                    departure_time = departure_DateTime.getMillis();

                    distance_detail = null;

                    start_address = result.routes[i].legs[0].startAddress;

                    end_address = result.routes[i].legs[0].endAddress;

                    boolean firstTime = true;
                    int stepsLength = result.routes[i].legs[0].steps.length;
                    int index_departure = -1;

                    for (int j = 0; j < stepsLength; j++) {
                        String string_transit_mode = result.routes[i].legs[0].steps[j].travelMode.toString();
                        if (string_transit_mode.equals("transit") && firstTime == true) {
                            index_departure = j;
                            firstTime = false;
                        }
                    }

                    if (index_departure >= 0) {
                        String departure_name = result.routes[i].legs[0].steps[index_departure].transitDetails.departureStop.name.toString();
                        DateTime departure_transit_time = result.routes[i].legs[0].steps[index_departure].transitDetails.departureTime.toDateTime();
                        int departure_transit_hour = departure_transit_time.getHourOfDay();
                        int departure_transit_min = departure_transit_time.getMinuteOfHour();

                        departure_detail = departure_transit_hour + ":" + departure_transit_min + " from " + departure_name;
                    } else {
                        departure_detail = "nessuna informazione";
                    }

                    String googleMapsRequest = "";

                    data.add(new DataModelGoogleMaps(route_detail, duration_detail, duration_in_seconds, arrival_time_in_millis, departure_time, distance_detail, departure_detail, start_address, end_address, googleMapsRequest, "", ""));


                }


                adapter = new CustomAdapterTransit(data, myActivity);

                recyclerView.setAdapter(adapter);

                relativeLayout.setVisibility(View.GONE);
            }catch(Exception e){
                String TAG = "GoogleMapsResponse";
                Log.e(TAG, "setDataModelForTransit: <ToDateTime>", e);

                Toast.makeText(myActivity, "Risposta di Google Maps errata, prova a cambiare origine o destinazione", Toast.LENGTH_LONG).show();

                relativeLayout.setVisibility(View.GONE);
            }

        }
    }

    private static void setDataModelForWalking(DirectionsResult result, RecyclerView recyclerView, RelativeLayout relLay_noResult, Activity myActivity, DateTime arrival_time){

        // Variabili
        int route_size = result.routes.length;

        if(route_size == 0){
            recyclerView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
            relLay_noResult.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            relLay_noResult.setVisibility(View.GONE);

            data = new ArrayList<DataModelGoogleMaps>();

            for (int i = 0; i < route_size; i++) {

                String summary = result.routes[i].summary.toString();
                route_detail = "Tramite " + summary;

                duration_detail = result.routes[i].legs[0].duration.toString();

                duration_in_seconds = result.routes[i].legs[0].duration.inSeconds;

                arrival_time_in_millis = arrival_time.getMillis();

                distance_detail = result.routes[i].legs[0].distance.toString();

                departure_detail = null;

                departure_time = 0;

                start_address = result.routes[i].legs[0].startAddress;

                end_address = result.routes[i].legs[0].endAddress;

                origin_location = "&origin=" + result.routes[i].legs[0].startLocation.lat + "," + result.routes[i].legs[0].startLocation.lng;

                destination_location = "&destination=" + result.routes[i].legs[0].endLocation.lat + "," + result.routes[i].legs[0].endLocation.lng;

                String waypoint = "";
                DirectionsStep[] step = result.routes[i].legs[0].steps;

                int stepsLenght = result.routes[i].legs[0].steps.length;
                if(stepsLenght > 5){
                    int mid = stepsLenght/2;
                    int mid_min = mid/2;
                    int mid_max = mid + mid_min;

                    waypoint =  waypoint + "&waypoints=" + step[mid_min].endLocation.lat + "," + step[mid_min].endLocation.lng;
                    waypoint =  waypoint + "&waypoints=" + step[mid].endLocation.lat + "," + step[mid].endLocation.lng;
                    waypoint =  waypoint + "&waypoints=" + step[mid_max].endLocation.lat + "," + step[mid_max].endLocation.lng;
                }else{
                    waypoint =  waypoint + "&waypoints=" + step[stepsLenght/2].endLocation.lat + "," + step[stepsLenght/2].endLocation.lng;
                }

                data.add(new DataModelGoogleMaps(route_detail, duration_detail, duration_in_seconds, arrival_time_in_millis, departure_time, distance_detail, departure_detail, start_address, end_address, waypoint, origin_location, destination_location));

            }


            adapter = new CustomAdapterWalking(data, myActivity);

            recyclerView.setAdapter(adapter);

            relativeLayout.setVisibility(View.GONE);
        }

    }

    /**
     * Funzione che restituisce la stringa del giorno del tempo in millisecondi in input
     * @param time_in_millis
     * @return day string
     */
    private static String getDay(long time_in_millis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time_in_millis);

        DateTime dateTime = new DateTime(calendar);
        int day = dateTime.getDayOfWeek();

        String dayString = "null";

        switch (day){
            case 1: dayString = "Lun";
                    break;
            case 2: dayString = "Mar";
                    break;
            case 3: dayString = "Mer";
                    break;
            case 4: dayString = "Gio";
                    break;
            case 5: dayString = "Ven";
                    break;
            case 6: dayString = "Sab";
                    break;
            case 7: dayString = "Dom";
                    break;
        }

        return dayString;
    }

    public static void clearRecyclerView(){
        if(data != null) {
            data.clear();
            adapter.notifyDataSetChanged();
        }
    }
}
