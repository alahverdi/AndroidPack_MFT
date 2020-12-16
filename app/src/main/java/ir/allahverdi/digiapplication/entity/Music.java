package ir.allahverdi.digiapplication.entity;

import android.graphics.drawable.Drawable;

public class Music {

    private int id;
    private int musicImage;
    private String songName;

    public Music(int id, int imageID, String songName) {
        this.id = id;
        this.musicImage = imageID;
        this.songName = songName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMusicImage() {
        return musicImage;
    }

    public void setMusicImage(int musicImage) {
        this.musicImage = musicImage;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
