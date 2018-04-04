package com.project.sveglia;

/**
 * Created by simonerigon on 13/03/18.
 */

public class DataModelMusic {

    String name;
    int music_ID;

    public DataModelMusic(String name, int music_ID){

        this.name = name;
        this.music_ID = music_ID;

    }

    public int getMusic_ID() {
        return music_ID;
    }

    public void setMusic_ID(int music_ID) {
        this.music_ID = music_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
