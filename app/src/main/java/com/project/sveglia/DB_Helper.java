package com.project.sveglia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pat on 05/06/18.
 */

public class DB_Helper extends SQLiteOpenHelper {

    //informazioni db
    public static final String DB_NAME = "SVEGLIE.DB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_VIEW = "TABLE_VIEW";
    public static final String TABLE_SVEGLIE = "TABLE_SVEGLIE";

    //Colonne table_view
    public static final String ID_VIEW = "_id_view";
    public static final String TIME_VIEW = "time_view";
    public static final String NOME = "nome_view";
    public static final String BOOLEAN_DAY = "boolean_day";
    public static final String ON_OFF = "on_off";
    public static final String RITARDA = "ritarda";
    public static final String ID_SUONERIA = "id_suoneria";
    public static final String POSIZIONE_SUONERIA = "posizione_suoneria";
    public static final String TRAVEL_TO = "travel_to";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String MEZZO = "mezzo";
    public static final String ARRAY_ID_SVEGLIE = "array_id_sveglie";

    //Colonne sveglie
    public static final String ID_SVEGLIA = "_id_sveglia";
    public static final String TIME_SVEGLIA = "time_sveglia";

    //query creazione tabella view
    private static final String CREATE_TABLE_VIEW = "CREATE TABLE " +
            TABLE_VIEW + " ( " +
            ID_VIEW + " INTEGER PRIMARY KEY, " +
            TIME_VIEW + " INTEGER, " +
            NOME + " TEXT, " +
            BOOLEAN_DAY + " TEXT, " +
            ON_OFF + " INTEGER, " +
            RITARDA + " INTEGER, " +
            ID_SUONERIA + " INTEGER, " +
            POSIZIONE_SUONERIA + " INTEGER, " +
            TRAVEL_TO + " INTEGER, " +
            FROM + " TEXT, " +
            TO + " TEXT, " +
            MEZZO + " TEXT, " +
            ARRAY_ID_SVEGLIE + " TEXT " +
            ");";

    //query creazione tabella sveglie
    private static final String CREATE_TABLE_SVEGLIE = "CREATE TABLE " +
            TABLE_SVEGLIE + " ( " +
            ID_SVEGLIA + " INTEGER PRIMARY KEY, " +
            TIME_SVEGLIA + " INTEGER " +
            ");";

    public DB_Helper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_VIEW);
        db.execSQL(CREATE_TABLE_SVEGLIE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SVEGLIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW);
        onCreate(db);
    }
}
