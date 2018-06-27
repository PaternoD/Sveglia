package com.project.sveglia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Pat on 27/06/18.
 */

public class DataModelView {

    long time;
    String nome_sveglia;
    boolean on_off;
    boolean [] repetitions_day;

    public DataModelView(long time, String nome_sveglia, boolean on_off, boolean [] repetitions_day){
        this.time=time;
        this.nome_sveglia=nome_sveglia;
        this.on_off=on_off;
        this.repetitions_day=repetitions_day;
    }

    public String getTime(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String res = dateFormat.format(cal.getTime());

        return res;
    }

}
