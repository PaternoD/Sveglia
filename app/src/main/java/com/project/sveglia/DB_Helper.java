package com.project.sveglia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pat on 02/02/18.
 */

public class DB_Helper extends SQLiteOpenHelper {

    //DB informations
    public static final String DB_NAME = "SVEGLIE.DB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "SVEGLIE";

    //Colonne DB
    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String DAY = "day";

    //query creazione tabella
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + ID +
            " INTEGER PRIMARY KEY, " + TIME + " TEXT, " + DAY + " TEXT);";



    public DB_Helper (Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
