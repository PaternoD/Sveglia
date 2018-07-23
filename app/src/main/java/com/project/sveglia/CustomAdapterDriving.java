package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
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

                Calendar calendar = Calendar.getInstance();
                long currentTime = calendar.getTimeInMillis();

                if(alarm_Time > currentTime){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("alarm_time", alarm_Time);
                    resultIntent.putExtra("transit_model", "DRIVING");
                    resultIntent.putExtra("start_address", start_address_detail);
                    resultIntent.putExtra("end_address", end_address_detail);
                    myActivity.setResult(Activity.RESULT_OK, resultIntent);
                    myActivity.finish();
                }else{
                    Intent intent_Time_Passed = new Intent(myActivity, Time_Passed_Pop_Up_Google.class);
                    myActivity.startActivity(intent_Time_Passed);
                }



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

        // ----> Ricordarsi di aggiungere il tempo di preparazione al mattino, prendere da database!!!
        DB_Manager db_manager = new DB_Manager(myActivity);
        db_manager.open();

        long fromBedToCarTime = db_manager.getBadToCar();

        long time = arrival_time_in_millis - (duration_in_seconds * 1000) - fromBedToCarTime;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        res = cal.getTimeInMillis();

        return res;
    }

}
