package com.project.sveglia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Pat on 27/06/18.
 */

public class SetViewSveglie {

    String time_sveglia;
    String nome_sveglia;
    boolean on_off;

    DB_Manager db;

    private static ArrayList<DataModelView> data;
    private static RecyclerView.Adapter adapter;
    private static RelativeLayout relativeLayout;

    public void setViewSveglie(RecyclerView recyclerView, Context c){

        db = new DB_Manager(c);
        db.open();
        data = new ArrayList<DataModelView>();

        ArrayList<String> array_nomi = db.getAllNameView();

        ArrayList<String> array_time = db.getAllTimeView();
        ArrayList<String> array_on_off = db.getAllOn_Off();


        for(int i=0;i<db.numberOfRows();i++) {
            String time = array_time.get(i);
            String name = array_nomi.get(i);
            String on_off_string = array_on_off.get(i);
            boolean on_off = true;


            if (on_off_string=="1") {
                on_off = true;
            }
            if (on_off_string=="0") {
                on_off = false;
            }




            DataModelView view = new DataModelView(getFormattedTimeFromMillis(array_time.get(i)), array_nomi.get(i), on_off);

            data.add(view);


        }
        adapter = new CustomAdapterView(data);
        recyclerView.setAdapter(adapter);

    }

    /**
     * Funzione che formatta il tempo in input (milliseconds) in HH:mm
     * @param time
     * @return formatted time (HH:mm)
     */
    private String getFormattedTimeFromMillis(String timeString){

        Long timeLong = Long.parseLong(timeString);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeLong);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String res = dateFormat.format(cal.getTime());

        return res;
    }



}
