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

    public void insert(int id, String time, String day){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Helper.ID, id);
        contentValues.put(DB_Helper.TIME, time);
        contentValues.put(DB_Helper.DAY, day);
        database.insert(DB_Helper.TABLE_NAME,null,contentValues);
    }

    public Cursor fetch(){
        String columns[] = new String[] {DB_Helper.ID, DB_Helper.TIME, DB_Helper.DAY};
        Cursor cursor = database.query(DB_Helper.TABLE_NAME,columns,null,null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(int id, String time, String day){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Helper.ID, id);
        contentValues.put(DB_Helper.TIME, time);
        contentValues.put(DB_Helper.DAY, day);
        int i = database.update(DB_Helper.TABLE_NAME,contentValues,DB_Helper.ID + " = " + id,null);
        return i;
    }

    public void delete(int id){
        database.delete(DB_Helper.TABLE_NAME, DB_Helper.ID + "=" + id, null);
    }
}

