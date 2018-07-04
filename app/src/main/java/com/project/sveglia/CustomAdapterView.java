package com.project.sveglia;

import android.annotation.SuppressLint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pat on 27/06/18.
 */

public class CustomAdapterView extends RecyclerView.Adapter <CustomAdapterView.MyViewHolder> {

    ArrayList<DataModelView> dataSet;
    boolean [] repetitions_day;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView timeSveglia;
        TextView nomeSveglia;
        Switch on_off;
        CardView lun;
        CardView mar;
        CardView mer;
        CardView gio;
        CardView ven;
        CardView sab;
        CardView dom;
        RelativeLayout giorni_ripetizioni;


        public MyViewHolder(View itemView) {
            super(itemView);

            timeSveglia=(TextView)itemView.findViewById(R.id.time_sveglia);
            nomeSveglia=(TextView)itemView.findViewById(R.id.nome_sveglia);
            on_off=(Switch)itemView.findViewById(R.id.on_off);
            lun=(CardView)itemView.findViewById(R.id.Lun_Circle);
            mar=(CardView)itemView.findViewById(R.id.Mar_Circle);
            mer=(CardView)itemView.findViewById(R.id.Mer_Circle);
            gio=(CardView)itemView.findViewById(R.id.Gio_Circle);
            ven=(CardView)itemView.findViewById(R.id.Ven_Circle);
            sab=(CardView)itemView.findViewById(R.id.Sab_Circle);
            dom=(CardView)itemView.findViewById(R.id.Dom_Circle);
            giorni_ripetizioni=(RelativeLayout)itemView.findViewById(R.id.giorni_ripetizione);

        }
    }


    public CustomAdapterView(ArrayList<DataModelView> dataSet){
        this.dataSet=dataSet;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_sveglia, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        TextView time_sveglia = holder.timeSveglia;
        TextView nome_sveglia = holder.nomeSveglia;
        Switch on_off = holder.on_off;
        CardView lun = holder.lun;
        CardView mar = holder.mar;
        CardView mer = holder.mer;
        CardView gio = holder.gio;
        CardView ven = holder.ven;
        CardView sab = holder.sab;
        CardView dom = holder.dom;

        time_sveglia.setText(dataSet.get(position).getTime());
        nome_sveglia.setText(dataSet.get(position).getNome_sveglia());
        on_off.setChecked(dataSet.get(position).isOn_off());

        repetitions_day = dataSet.get(position).getRepetitions_day();

        boolean repetitions= false;

        for(int j=0;j<7;j++){
            if(repetitions_day[j]){
                repetitions=true;
            }
        }

        if(repetitions){
            if (repetitions_day[0]) lun.setBackgroundResource(R.drawable.color_day_active);
            if(!repetitions_day[0]) lun.setBackgroundResource(R.drawable.color_day_nonattivo);
            if (repetitions_day[1]) mar.setBackgroundResource(R.drawable.color_day_active);
            if(!repetitions_day[1]) mar.setBackgroundResource(R.drawable.color_day_nonattivo);
            if (repetitions_day[2]) mer.setBackgroundResource(R.drawable.color_day_active);
            if(!repetitions_day[2]) mer.setBackgroundResource(R.drawable.color_day_nonattivo);
            if (repetitions_day[3]) gio.setBackgroundResource(R.drawable.color_day_active);
            if(!repetitions_day[3]) gio.setBackgroundResource(R.drawable.color_day_nonattivo);
            if (repetitions_day[4]) ven.setBackgroundResource(R.drawable.color_day_active);
            if(!repetitions_day[4]) ven.setBackgroundResource(R.drawable.color_day_nonattivo);
            if (repetitions_day[5]) sab.setBackgroundResource(R.drawable.color_day_active);
            if(!repetitions_day[5]) sab.setBackgroundResource(R.drawable.color_day_nonattivo);
            if (repetitions_day[6]) dom.setBackgroundResource(R.drawable.color_day_active);
            if(!repetitions_day[6]) dom.setBackgroundResource(R.drawable.color_day_nonattivo);
        }
        else if(!repetitions){
            RelativeLayout giorni_ripetizioni = holder.giorni_ripetizioni;
            giorni_ripetizioni.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    }
