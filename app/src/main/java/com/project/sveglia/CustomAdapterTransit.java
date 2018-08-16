package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by simonerigon on 01/03/18.
 */

public class CustomAdapterTransit extends RecyclerView.Adapter<CustomAdapterTransit.MyViewHolder> {

    private ArrayList<DataModelGoogleMaps> dataSet;
    private Activity myActivity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // Recupero riferimenti layout ----------------------------------
        TextView route_detail;
        TextView duration_detail;
        TextView departure_detail;
        TextView btn_save_data;


        public MyViewHolder(View itemView){

            super(itemView);
            route_detail = (TextView)itemView.findViewById(R.id.detail_transit_ruote_ID);
            duration_detail = (TextView)itemView.findViewById(R.id.detail_transit_duration_ID);
            departure_detail = (TextView)itemView.findViewById(R.id.detail_transit_departure_ID);
            btn_save_data = (TextView)itemView.findViewById(R.id.detail_transit_save_ID);

        }
    }

    public CustomAdapterTransit(ArrayList<DataModelGoogleMaps> data, Activity myActivity){
        this.dataSet = data;
        this.myActivity = myActivity;
    }

    @Override
    public CustomAdapterTransit.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_transit, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapterTransit.MyViewHolder holder, final int listPosition) {

        TextView route_detail_text = holder.route_detail;
        TextView duration_detail_text = holder.duration_detail;
        TextView departure_detail_text = holder.departure_detail;
        TextView btn_save = holder.btn_save_data;

        route_detail_text.setText(dataSet.get(listPosition).getRoute_detail());
        duration_detail_text.setText(dataSet.get(listPosition).getTime_to_travel());
        departure_detail_text.setText(dataSet.get(listPosition).getDeparture());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DB_Manager db_manager = new DB_Manager(myActivity);
                db_manager.open();

                long fromBedToCarTime = db_manager.getBadToCar();

                long alarm_Time = dataSet.get(listPosition).getDeparture_time() - fromBedToCarTime;

                String start_address_detail = dataSet.get(listPosition).start_address;
                String end_address_detail = dataSet.get(listPosition).end_address;

                Calendar calendar = Calendar.getInstance();
                long currentTime = calendar.getTimeInMillis();

                if(alarm_Time > currentTime) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("alarm_time", alarm_Time);
                    resultIntent.putExtra("transit_model", "TRANSIT");
                    resultIntent.putExtra("start_address", start_address_detail);
                    resultIntent.putExtra("end_address", end_address_detail);
                    myActivity.setResult(Activity.RESULT_OK, resultIntent);
                    myActivity.finish();
                }else{
                    Intent intent_Time_Passed = new Intent(myActivity, Time_Passed_Pop_Up_Google.class);
                    myActivity.startActivity(intent_Time_Passed);
                }db_manager.close();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
