package com.project.sveglia;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;

import org.joda.time.chrono.AssembledChronology;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by simonerigon on 13/03/18.
 */

public class getMusicData {

    private static ArrayList<DataModelMusic> data;
    private static RecyclerView.Adapter adapter;

    public static void getMusicDataForAlarm(Context context, RecyclerView recyclerView,ImageButton btn_save, Activity myActivity, int listPositionMusic, String music_name, int alarm_music_ID){

        Field[] fields = R.raw.class.getFields();

        data = new ArrayList<DataModelMusic>();

        if(fields != null) {
            for(int i=0; i<fields.length; i++){
                if(fields[i].getName().contains("mp3_")){

                    String musicNameForID = fields[i].getName();
                    String name = getMusicName(fields[i].getName());
                    int music_ID = context.getResources().getIdentifier(musicNameForID, "raw", context.getPackageName());

                    DataModelMusic dataset = new DataModelMusic(name, music_ID);

                    data.add(dataset);

                }

            }

            adapter = new CustomAdapterForMusic(data, btn_save, myActivity, listPositionMusic, music_name, alarm_music_ID);
            recyclerView.setAdapter(adapter);

        }

    }

    private static String getMusicName(String music){

        String[] split = music.split("_");

        String res = "";
        for(int i=1; i<split.length; i++){
            res += " " + split[i];
        }

        return res;
    }

    public static String[] getDefaultMusic(Context context){

        Field[] fields = R.raw.class.getFields();

        boolean defaultMusic = true;
        int count = 0;
        String name = "";
        int music_ID = 0;

        if(fields != null){
            for(int i=0; i<fields.length && defaultMusic; i++){
                if(fields[i].getName().contains("mp3_")){
                    count++;
                    if(count == 3){
                        defaultMusic = false;
                        String nameForID = fields[i].getName();
                        name = getMusicName(fields[i].getName());
                        music_ID = context.getResources().getIdentifier(nameForID, "raw", context.getPackageName());
                    }
                }

            }
        }

        String[] res = new String[2];

        res[0] = name;
        res[1] = "" + music_ID;

        return res;

    }
}
