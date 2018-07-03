package com.project.sveglia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Pat on 27/06/18.
 */

public class SetViewSveglie {

    String time_sveglia;
    String nome_sveglia;
    boolean on_off;

    DB_Manager db;

    private static ArrayList<DataModelView> data;
    private static RecyclerView.Adapter adapter;
    private static RelativeLayout relativeLayout;

    public void setViewSveglie(RecyclerView recyclerView, Context c){

        db = new DB_Manager(c);
        db.open();
        data = new ArrayList<DataModelView>();

        ArrayList<String> array_nomi = db.getAllNameView();

        ArrayList<String> array_time = db.getAllTimeView();
        ArrayList<String> array_on_off = db.getAllOn_Off();


        for(int i=0;i<db.numberOfRows();i++) {
            String time = array_time.get(i);
            String name = array_nomi.get(i);
            //String on_off_string = array_on_off.get(i);
            boolean on_off = true;

/*
            if (on_off_string.equals("1")) {
                on_off = true;
            }
            if (on_off_string.equals("0")) {
                on_off = false;
            }
*/

            DataModelView view = new DataModelView(array_time.get(i), array_nomi.get(i), on_off);

            data.add(view);

            //data.add(new DataModelView("secondo","nome2",true));

            //data.add(new DataModelView("primo","nome",true));

            for (int j = 0; j < 1; j++) {
                data.get(j).toooString();
            }

        }
        adapter = new CustomAdapterView(data);
        recyclerView.setAdapter(adapter);

    }
}
