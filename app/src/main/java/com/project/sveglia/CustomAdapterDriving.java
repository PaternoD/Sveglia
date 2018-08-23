package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by simonerigon on 28/02/18.
 */

public class CustomAdapterDriving extends RecyclerView.Adapter<CustomAdapterDriving.MyViewHolder> {

    private ArrayList<DataModelGoogleMaps> dataSet;
    private Activity myActivity;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // Recupero riferimenti layout ----------------------------------
        TextView route_detail;
        TextView duration_detail;
        TextView distance_detail;
        TextView btn_save_data;


        public MyViewHolder(View itemView){

            super(itemView);
            route_detail = (TextView)itemView.findViewById(R.id.time_sveglia);
            duration_detail = (TextView)itemView.findViewById(R.id.detail_driving_duration_ID);
            distance_detail = (TextView)itemView.findViewById(R.id.detail_driving_distance_ID);
            btn_save_data = (TextView)itemView.findViewById(R.id.detail_driving_save_ID);

        }

    }

    public CustomAdapterDriving(ArrayList<DataModelGoogleMaps> data, Activity myActivity){
        this.dataSet = data;
        this.myActivity = myActivity;
    }

    @Override
    public CustomAdapterDriving.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_driving, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView route_detail_text = holder.route_detail;
        TextView duration_detail_text = holder.duration_detail;
        TextView distance_detail_text = holder.distance_detail;
        TextView btn_save = holder.btn_save_data;

        route_detail_text.setText(dataSet.get(listPosition).getRoute_detail());
        duration_detail_text.setText(dataSet.get(listPosition).getTime_to_travel());
        distance_detail_text.setText(dataSet.get(listPosition).getDistance());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long duration_in_seconds = dataSet.get(listPosition).getTime_to_travel_in_seconds();
                long arrival_time_in_millis = dataSet.get(listPosition).getArrival_time();

                long alarm_Time = getTime(listPosition, duration_in_seconds, arrival_time_in_millis);

                String start_address_detail = dataSet.get(listPosition).start_address;
                String end_address_detail = dataSet.get(listPosition).end_address;


                String origin_loc = dataSet.get(listPosition).getOrigin_location();
                String dest_loc = dataSet.get(listPosition).getDestination_location();
                String waypoint = dataSet.get(listPosition).getGoogleMapsRequest();

                System.out.println("Path_" + listPosition + ": " + origin_loc + " - " + dest_loc + " - " + waypoint);

                String maps_direction_request = "https://www.google.com/maps/dir/?api=1" + origin_loc + dest_loc + waypoint + "&travelmode=driving&dir_action=navigate";

                    /*
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("alarm_time", alarm_Time);
                    resultIntent.putExtra("transit_model", "DRIVING");
                    resultIntent.putExtra("start_address", start_address_detail);
                    resultIntent.putExtra("end_address", end_address_detail);
                    resultIntent.putExtra("maps_direction_request", maps_direction_request);
                    resultIntent.putExtra("from_bed_to_car_added", true);
                    myActivity.setResult(Activity.RESULT_OK, resultIntent);
                    myActivity.finish();
                    */

                int ACTIVITY_ID = 8;

                Intent add_time_google_maps = new Intent(myActivity, Add_From_Bed_To_Car_Time.class);
                add_time_google_maps.putExtra("alarm_time", alarm_Time);
                add_time_google_maps.putExtra("transit_model", "DRIVING");
                add_time_google_maps.putExtra("start_address", start_address_detail);
                add_time_google_maps.putExtra("end_address", end_address_detail);
                add_time_google_maps.putExtra("maps_direction_request", maps_direction_request);
                myActivity.startActivityForResult(add_time_google_maps, ACTIVITY_ID);




            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    /**
     * Funzione che mi restituisce l'orario di partenza per i viaggi in cui viene utilizzato google maps
     * @param listPosition
     * @param duration_in_seconds
     * @param arrival_time_in_millis
     * @return departure_time
     */
    private long getTime(int listPosition, long duration_in_seconds, long arrival_time_in_millis){
        long res = 0;

        long time = arrival_time_in_millis - (duration_in_seconds * 1000);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        res = cal.getTimeInMillis();
        return res;
    }

}
