package com.project.sveglia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Pat on 05/06/18.
 */

public class DB_Manager {

    private Context context;
    private DB_Helper db_helper;
    private SQLiteDatabase database;
    private SQLiteDatabase databaseRead;

    public DB_Manager(Context c){
        context = c;
    }

    public DB_Manager open () throws SQLException {
        db_helper = new DB_Helper(context);
        database = db_helper.getWritableDatabase();
        databaseRead = db_helper.getReadableDatabase();
        return this;
    }

    public void close(){
        db_helper.close();
    }


    //funzione di appoggio per convertire vettore di boolean in string per inserirle nel DB
    public String bool_to_string(boolean[] array){
        String str = "";
        for(int i=0;i<7;i++){
            if(array[i]){
                str=str+"1";
            }else{
                str=str+"0";
            }
        }
        System.out.println(str);
        return str;
    }

    //funz di appoggio per convertire stringa in bool_day del db
    //in un array di booleani
    //DA VERIFICARE FUNZIONAMENTO!!!!!
    public boolean [] string_to_bool(String str){
        boolean [] array  = new boolean[7];
        for(int i=0;i<7;i++){
            if(str.charAt(i)=='0'){
                array[i]=false;
            }
            if(str.charAt(i)=='1'){
                array[i]=true;
            }
        }
        return array;
    }

    public void insert_view(int id,
                            Long time,
                            String nome,
                            boolean [] boolean_day,
                            String on_off,
                            int ritarda,
                            int id_suoneria,
                            int posizione_suoneria,
                            int travel_to,
                            String from,
                            String to,
                            String mezzo
                            //String array_id
    ){
        System.out.println("inizio funzione");
        ContentValues cv = new ContentValues();
        cv.put(DB_Helper.ID_VIEW,id);
        cv.put(DB_Helper.TIME_VIEW,time);
        cv.put(DB_Helper.NOME, nome);
        cv.put(DB_Helper.BOOLEAN_DAY, bool_to_string(boolean_day));
        cv.put(DB_Helper.ON_OFF,on_off);
        cv.put(DB_Helper.RITARDA,ritarda);
        cv.put(DB_Helper.ID_SUONERIA,id_suoneria);
        cv.put(DB_Helper.POSIZIONE_SUONERIA,posizione_suoneria);
        cv.put(DB_Helper.TRAVEL_TO,travel_to);
        cv.put(DB_Helper.GOOGLE_FROM, from);
        cv.put(DB_Helper.GOOGLE_TO, to);
        cv.put(DB_Helper.MEZZO,mezzo);
        //cv.put(DB_Helper.ARRAY_ID_SVEGLIE,array_id);
        database.insert(DB_Helper.TABLE_VIEW,null,cv);
    }



    public void insert_repetition_id(int id, Vector<Integer> vector){
        String str="";
        str = vector.toString();
        System.out.println(id);
        String sql = "UPDATE TABLE_VIEW "  +
                " SET array_id_sveglie = '"+ str +"' " +
                " WHERE _id = "+ id +";";
        database.execSQL(sql);
    }


    public void insert_sveglia(int id, Long time){
        ContentValues cv = new ContentValues();
        cv.put(DB_Helper.ID_SVEGLIA, id);
        cv.put(DB_Helper.TIME_SVEGLIA, time);
        database.insert(DB_Helper.TABLE_SVEGLIE, null, cv);
    }


    public void delete_view(int id){
        database.delete(DB_Helper.TABLE_VIEW, DB_Helper.ID_VIEW + " = " + id, null);
    }

    public void delete_sveglia(int id){
        database.delete(DB_Helper.TABLE_SVEGLIE, DB_Helper.ID_SVEGLIA + " = " + id, null);
    }

    public Cursor getData(int id){
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from TABLE_VIEW where _id = "+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = db_helper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,db_helper.TABLE_VIEW);
        return numRows;
    }

    public ArrayList<String>getAllTimeView(){
        ArrayList<String> array_time = new ArrayList<>();


        SQLiteDatabase db= db_helper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM TABLE_VIEW", null);

        res.moveToFirst();

        while (!res.isAfterLast()){
            array_time.add(res.getString(res.getColumnIndex(db_helper.TIME_VIEW)));
            res.moveToNext();
        }

        for (int i=0; i<array_time.size();i++){
            System.out.println(array_time.get(i));
        }

        return array_time;

    }

    public ArrayList<String>getAllNameView(){
        ArrayList<String> array_name = new ArrayList<>();

        Cursor res = databaseRead.rawQuery("SELECT * FROM TABLE_VIEW", null);
        res.moveToFirst();

        while (!res.isAfterLast()){
            array_name.add(res.getString(res.getColumnIndex(db_helper.NOME)));
            res.moveToNext();
        }

        return array_name;

    }

    public ArrayList<String>getAllOn_Off(){
        ArrayList<String> array_on = new ArrayList<>();

        SQLiteDatabase db= db_helper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from TABLE_VIEW", null);
        res.moveToFirst();

        while (!res.isAfterLast()){
            array_on.add(res.getString(res.getColumnIndex(db_helper.ON_OFF)));
            res.moveToNext();
        }

        return array_on;

    }

    public ArrayList<String>getAllRepetitionsDay(){
        ArrayList<String> array_repetitions = new ArrayList<>();

        SQLiteDatabase db= db_helper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from TABLE_VIEW", null);
        res.moveToFirst();

        while (!res.isAfterLast()){
            array_repetitions.add(res.getString(res.getColumnIndex(db_helper.BOOLEAN_DAY)));
            res.moveToNext();
        }

        return array_repetitions;

    }
}


