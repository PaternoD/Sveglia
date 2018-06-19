package com.project.sveglia;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by simonerigon on 13/03/18.
 */

public class CustomAdapterForMusic extends RecyclerView.Adapter<CustomAdapterForMusic.MyViewHolder> {

    private ArrayList<DataModelMusic> dataSet;
    private ImageButton btn_save_data;
    private Activity activity;
    private int previous_position_checkbox;
    public String music_name_result;
    public int music_ID_result;
    private MediaPlayer mediaPlayer;

    // dati da restituire


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView music_name;
        RadioButton select_icon;
        CardView card_music;

        public MyViewHolder(View itemView){
            super(itemView);
            music_name = (TextView)itemView.findViewById(R.id.music_name_ID);
            select_icon = (RadioButton) itemView.findViewById(R.id.radioButton_music_ID);
            card_music = (CardView)itemView.findViewById(R.id.cardView_click_music_ID);

            card_music.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    previous_position_checkbox = getAdapterPosition();
                    notifyDataSetChanged();

                    try{
                        mediaPlayer.release();
                        mediaPlayer.stop();
                    }catch(Exception e){
                        // La prima volta da errore, in quanto mediaplayer non è stato avviato
                        Log.i("MEDIAPLAYER_ERROR", "onClick: I can't stop music for the first time");
                    }

                    music_name_result = dataSet.get(previous_position_checkbox).getName();
                    music_ID_result = dataSet.get(previous_position_checkbox).getMusic_ID();

                    mediaPlayer = MediaPlayer.create(activity, music_ID_result);
                    mediaPlayer.start();

                }
            });
        }

    }

    public CustomAdapterForMusic(ArrayList<DataModelMusic> data, ImageButton btn_save, Activity activity, int listPositionMusic, String music_name, int music_ID){
        this.dataSet = data;
        this.btn_save_data = btn_save;
        this.activity = activity;
        this.previous_position_checkbox = listPositionMusic;
        this.music_name_result = music_name;
        this.music_ID_result = music_ID;
    }

    @Override
    public CustomAdapterForMusic.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_music, parent, false);

        CustomAdapterForMusic.MyViewHolder myViewHolder = new CustomAdapterForMusic.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapterForMusic.MyViewHolder holder, int listPosition){

        final TextView music_name = holder.music_name;
        final RadioButton select_icon = holder.select_icon;

        String name = dataSet.get(listPosition).getName();

        music_name.setText(name);

        select_icon.setChecked(previous_position_checkbox == listPosition);

        btn_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    mediaPlayer.stop();
                }catch(Exception e){
                    // La prima volta da errore, in quanto mediaplayer non è stato avviato
                    Log.i("MEDIAPLAYER_ERROR_BACK", "onClick: I can't stop music for the first time");
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("music_name", music_name_result);
                resultIntent.putExtra("music_ID", music_ID_result);
                resultIntent.putExtra("listPositionMusic", previous_position_checkbox);
                activity.setResult(Activity.RESULT_OK, resultIntent);
                activity.finish();

            }
        });




    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }



}
