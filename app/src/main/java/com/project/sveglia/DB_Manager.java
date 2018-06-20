package com.project.sveglia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Vector;

/**
 * Created by Pat on 05/06/18.
 */

public class DB_Manager {

    private Context context;
    private DB_Helper db_helper;
    private SQLiteDatabase database;

    public DB_Manager(Context c){
        context = c;
    }

    public DB_Manager open () throws SQLException {
        db_helper = new DB_Helper(context);
        database = db_helper.getWritableDatabase();
        return this;
    }

    public void close(){
        db_helper.close();
    }

    public void insert_view(int id,
                            Long time,
                            String nome,
                            String boolean_day,
                            //int on_off,
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
        cv.put(DB_Helper.BOOLEAN_DAY, boolean_day);
        //cv.put(DB_Helper.ON_OFF,on_off);
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

        String sql = "UPDATE TABLE_VIEW "  +
                " SET array_id_sveglie = '"+ str +"' " +
                " WHERE _id_view = "+ id +";";
        database.execSQL(sql);
    }


    public void insert_sveglia(int id, Long time){
        ContentValues cv = new ContentValues();
        cv.put(DB_Helper.ID_SVEGLIA, id);
        cv.put(DB_Helper.TIME_SVEGLIA, time);
        database.insert(DB_Helper.TABLE_SVEGLIE, null, cv);
    }

    public Cursor fetch_view(){
        String columns [] = new String[]{   DB_Helper.ID_VIEW, DB_Helper.TIME_VIEW,
                DB_Helper.BOOLEAN_DAY, DB_Helper.ON_OFF,
                DB_Helper.RITARDA, DB_Helper.ID_SUONERIA,
                DB_Helper.POSIZIONE_SUONERIA, DB_Helper.TRAVEL_TO,
                DB_Helper.GOOGLE_FROM, DB_Helper.GOOGLE_TO,
                DB_Helper.MEZZO, DB_Helper.ARRAY_ID_SVEGLIE};
        Cursor cursor = database.query(DB_Helper.TABLE_VIEW,columns,null,null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_sveglie(){
        String columns [] = new String[]{DB_Helper.ID_SUONERIA, DB_Helper.TIME_SVEGLIA,};
        Cursor cursor = database.query(DB_Helper.TABLE_SVEGLIE,columns,null,null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
/*
    public int update_sveglia(int id, String time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Helper.ID_SVEGLIA, id);
        contentValues.put(DB_Helper.TIME_SVEGLIA, time);
        int i = database.update(DB_Helper.TABLE_SVEGLIE,contentValues,DB_Helper.ID_SVEGLIA + " = " + id,null);
        return i;
    }
**/

    public void delete_view(int id){
        database.delete(DB_Helper.TABLE_VIEW, DB_Helper.ID_VIEW + " = " + id, null);
    }

    public void delete_sveglia(int id){
        database.delete(DB_Helper.TABLE_SVEGLIE, DB_Helper.ID_SVEGLIA + " = " + id, null);
    }
}


