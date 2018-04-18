package com.project.sveglia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pat on 02/02/18.
 */

public class DB_Manager {

    private Context context;
    private DB_Helper db_helper;
    private SQLiteDatabase database;

    public DB_Manager(Context c){
        context = c;
    }

    public DB_Manager open () throws SQLException{
        db_helper = new DB_Helper(context);
        database = db_helper.getWritableDatabase();
        return this;
    }

    public void close(){
        db_helper.close();
    }

    public void insert(Long id, Long time, int suoneria, String nome_suoneria, String nome_sveglia, String ripetizioni, Boolean ritarda, Boolean travel_to, String From, String To, Boolean on_off, String mezzo ){
        //boolean ritarda, travel_to, on_off
        int ritarda_input;
        if (ritarda){
            ritarda_input=1;
        }else{
            ritarda_input=0;
        }
        int travel_to_input;
        if (travel_to){
            travel_to_input=1;
        }else{
            travel_to_input=0;
        }
        int on_off_input;
        if (on_off){
            on_off_input=1;
        }else{
            on_off_input=0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Helper.ID, id);
        contentValues.put(DB_Helper.TIME, time);
        contentValues.put(DB_Helper.SONG, suoneria);
        contentValues.put(DB_Helper.NOME_SVEGLIA, nome_sveglia);
        contentValues.put(DB_Helper.REPEAT, ripetizioni);
        contentValues.put(DB_Helper.RITARDA, ritarda_input);
        contentValues.put(DB_Helper.TRAVEL_TO, travel_to_input);
        contentValues.put(DB_Helper.FROM, From);
        contentValues.put(DB_Helper.TO, To);
        contentValues.put(DB_Helper.ON_OFF, on_off_input);
        contentValues.put(DB_Helper.TRAVEL_BY, mezzo);
        contentValues.put(DB_Helper.NOME_SUONERIA, nome_suoneria);
        database.insert(DB_Helper.TABLE_NAME,null,contentValues);
    }

    public Cursor fetch(){
        String columns[] = new String[] {DB_Helper.ID, DB_Helper.TIME, DB_Helper.SONG, DB_Helper.NOME_SVEGLIA, DB_Helper.REPEAT, DB_Helper.RITARDA, DB_Helper.TRAVEL_TO, DB_Helper.FROM, DB_Helper.TO, DB_Helper.ON_OFF, DB_Helper.TRAVEL_BY, DB_Helper.NOME_SUONERIA};
        Cursor cursor = database.query(DB_Helper.TABLE_NAME,columns,null,null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(Long id, Long time, int suoneria, String nome_suoneria, String nome_sveglia, String ripetizioni, Boolean ritarda, Boolean travel_to, String From, String To, Boolean on_off, String mezzo ){
        //boolean ritarda, travel_to, on_off
        int ritarda_input;
        if (ritarda){
            ritarda_input=1;
        }else{
            ritarda_input=0;
        }
        int travel_to_input;
        if (travel_to){
            travel_to_input=1;
        }else{
            travel_to_input=0;
        }
        int on_off_input;
        if (on_off){
            on_off_input=1;
        }else{
            on_off_input=0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Helper.ID, id);
        contentValues.put(DB_Helper.TIME, time);
        contentValues.put(DB_Helper.SONG, suoneria);
        contentValues.put(DB_Helper.NOME_SVEGLIA, nome_sveglia);
        contentValues.put(DB_Helper.REPEAT, ripetizioni);
        contentValues.put(DB_Helper.RITARDA, ritarda_input);
        contentValues.put(DB_Helper.TRAVEL_TO, travel_to_input);
        contentValues.put(DB_Helper.FROM, From);
        contentValues.put(DB_Helper.TO, To);
        contentValues.put(DB_Helper.ON_OFF, on_off_input);
        contentValues.put(DB_Helper.TRAVEL_BY, mezzo);
        contentValues.put(DB_Helper.NOME_SUONERIA, nome_suoneria);
        int i = database.update(DB_Helper.TABLE_NAME,contentValues,DB_Helper.ID + " = " + id,null);
        return i;
    }

    public void delete(int id){
        database.delete(DB_Helper.TABLE_NAME, DB_Helper.ID + "=" + id, null);
    }
}

