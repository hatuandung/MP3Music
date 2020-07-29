package com.t3h.mp3music.model;

import android.net.Uri;
import android.provider.MediaStore;

public class Artist extends BaseModel {
    @FieldInfo(columnName = MediaStore.Audio.Artists._ID)
    private int id;
    @FieldInfo(columnName = MediaStore.Audio.Artists.ARTIST)
    private String name;
    @FieldInfo(columnName = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
    private int numOfAlbum;
    @FieldInfo(columnName = MediaStore.Audio.Artists.NUMBER_OF_TRACKS)
    private int numOfTrack;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumOfAlbum() {
        return numOfAlbum;
    }

    public int getNumOfTrack() {
        return numOfTrack;
    }

    @Override
    public Uri getContentUri() {
        return MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    }
}
