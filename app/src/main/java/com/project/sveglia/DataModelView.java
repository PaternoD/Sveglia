package com.project.sveglia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Pat on 27/06/18.
 */

public class DataModelView {

    int id;
    String time;
    String nome_sveglia;
    boolean on_off;
    boolean [] repetitions_day;
    boolean ritarda;
    int id_suoneria;
    int posizione_suoneria;
    boolean travel_to;
    String from;
    String to;
    String mezzo;
    long time_in_millis;

    public DataModelView(int id, String time, String nome_sveglia, boolean [] repetitions_day, boolean on_off, boolean ritarda,
                         int id_suoneria, int posizione_suoneria, boolean travel_to, String from, String to,
                         String mezzo, long time_in_millis) {
        this.id=id;
        this.time=time;
        this.nome_sveglia=nome_sveglia;
        this.on_off=on_off;
        this.repetitions_day=repetitions_day;
        this.ritarda=ritarda;
        this.id_suoneria=id_suoneria;
        this.posizione_suoneria=posizione_suoneria;
        this.travel_to=travel_to;
        this.from=from;
        this.to=to;
        this.mezzo=mezzo;
        this.time_in_millis=time_in_millis;
    }

    public int getId(){return id;}

    public String getTime(){
        return time;
    }

    public String getNome_sveglia(){
        return nome_sveglia;
    }

    public boolean isOn_off() { return on_off; }

    public boolean [] getRepetitions_day(){ return repetitions_day; }

    public boolean getRitarda(){return ritarda; }

    public int getId_suoneria(){return id_suoneria;}

    public int getPosizione_suoneria(){return posizione_suoneria;}

    public boolean getTravelTo(){return travel_to;}

    public String getFrom(){return from;}

    public String getTo(){return to;}

    public String getMezzo(){return mezzo;}

    public String getDataSveglia(){return getDayTimeFromMillis(time_in_millis);}

    /**
     * Funzione per ricavare il giorno (es. Lunedì  23-04-2018) da time in millis in input
     * @param time
     * @return current date (es. Lunedì  23-04-2018)
     */
    private String getDayTimeFromMillis(long time){

        Calendar cal = Calendar.getInstance();
        if(time != 0) {
            cal.setTimeInMillis(time);
        }

        DateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
        String dayOfYear = dateFormat.format(cal.getTime());


        String res = dayOfYear;

        return res;
    }



}

