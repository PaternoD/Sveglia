package com.project.sveglia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pat on 27/06/18.
 */

public class CustomAdapterView extends RecyclerView.Adapter <CustomAdapterView.MyViewHolder> {

    ArrayList<DataModelView> dataSet;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView timeSveglia;
        TextView nomeSveglia;
        Switch on_off;

        public MyViewHolder(View itemView) {
            super(itemView);

            timeSveglia=(TextView)itemView.findViewById(R.id.time_sveglia);
            nomeSveglia=(TextView)itemView.findViewById(R.id.nome_sveglia);
            on_off=(Switch)itemView.findViewById(R.id.on_off);

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

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        TextView time_sveglia = holder.timeSveglia;
        TextView nome_sveglia = holder.nomeSveglia;
        Switch on_off = holder.on_off;

        time_sveglia.setText(dataSet.get(position).getTime());
        nome_sveglia.setText(dataSet.get(position).getNome_sveglia());
        on_off.setChecked(dataSet.get(position).isOn_off());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    }
