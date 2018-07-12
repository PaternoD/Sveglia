package com.project.sveglia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
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


    DB_Manager db;

    private static ArrayList<DataModelView> data;
    private static RecyclerView.Adapter adapter;

    public void setViewSveglie(RecyclerView recyclerView, Context c){


        db = new DB_Manager(c);
        db.open();
        data = new ArrayList<DataModelView>();

        ArrayList<String> array_nomi = db.getAllNameView();
        ArrayList<String> array_time = db.getAllTimeView();
        ArrayList<String> array_on_off = db.getAllOn_Off();
        ArrayList<String> array_repetitions_day = db.getAllRepetitionsDay();
        ArrayList<String> array_ritarda = db.getAllRitarda();
        ArrayList<String> array_id_suoneria = db.getAllIDsuoneria();
        ArrayList<String> array_pos_suoneria = db.getAllPosSuoneria();
        ArrayList<String> array_travel_to = db.getAllTravelTO();
        ArrayList<String> array_from = db.getAllFrom();
        ArrayList<String> array_to = db.getAllTo();
        ArrayList<String> array_mezzo = db.getAllMezzo();
        ArrayList<String> array_id = db.getAllID();


        for(int i=0;i<db.numberOfRows();i++) {
            String time = array_time.get(i);
            String name = array_nomi.get(i);
            String on_off_string = array_on_off.get(i);
            String repetitions_day_string = array_repetitions_day.get(i);
            String id_string = array_id.get(i);


            int id= Integer.parseInt(id_string);

            boolean on_off = true;
            boolean [] repetitions_day_array;
            repetitions_day_array = new boolean[7];
//2 if che mi settano lo switch
            if (on_off_string.equals("1")) {
                on_off = true;
            }
            if (on_off_string.equals("0")) {
                on_off = false;
            }
//ciclo che mi restituisce il vettore di booleani
            for (int j=0;j<7;j++){
                boolean day=true;
                Character character;
                character = repetitions_day_string.charAt(j);

                if (character.equals((Character)'0')){
                    day=false;
                }
                if (character.equals((Character)'1')){
                    day=true;
                }
                repetitions_day_array[j]=day;
            }

            //get ritarda
            boolean ritarda= true;
            String ritarda_str = array_ritarda.get(i);
            Character character = ritarda_str.charAt(0);
            if (character.equals((Character)'0'))ritarda=false;
            if (character.equals((Character)'1'))ritarda=true;

            //get id suoneria
            int id_suoneria;
            String id_str = array_id_suoneria.get(i);
            id_suoneria = Integer.parseInt(id_string);

            //get posizione suoneria

            int pos_suoneria;
            String pos_str = array_pos_suoneria.get(i);
            pos_suoneria = Integer.parseInt(pos_str);

            //get travel to, from e to

            boolean travel_to=false;
            String from=null;
            String to = null;
            String mezzo=null;
            String travel_str = array_travel_to.get(i);
            Character  tr = travel_str.charAt(0);
            if (tr.equals((Character)'0'))travel_to=false;
            if (tr.equals((Character)'1'))travel_to=true;
            if(travel_to){
                from=array_from.get(i);
                to=array_to.get(i);
                mezzo=array_mezzo.get(i);
            }


            DataModelView view = new DataModelView(id, getFormattedTimeFromMillis(time), name,repetitions_day_array, on_off, ritarda,id_suoneria,
                    pos_suoneria,travel_to,from,to,mezzo);

            data.add(view);


        }
        adapter = new CustomAdapterView(data,db,c);
        recyclerView.setAdapter(adapter);



    }

    public static void aggiornaAdapter(int pos){
        data.remove(pos);
        adapter.notifyDataSetChanged();

        System.out.println("AGGIORNATO---------------------------");

    }





    /**
     * Funzione che formatta il tempo in input (milliseconds) in HH:mm
     * @param timeString
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
