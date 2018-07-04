package com.project.sveglia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Pat on 27/06/18.
 */

public class DataModelView {

    String time;
    String nome_sveglia;
    boolean on_off;
    boolean [] repetitions_day;

    public DataModelView(String time, String nome_sveglia, boolean on_off, boolean [] repetitions_day){
        this.time=time;
        this.nome_sveglia=nome_sveglia;
        this.on_off=on_off;
        this.repetitions_day=repetitions_day;
    }


    public String getTime(){
        return time;
    }

    public String getNome_sveglia(){
        return nome_sveglia;
    }

    public boolean isOn_off() { return on_off; }

    public boolean [] getRepetitions_day(){ return repetitions_day; }


}

