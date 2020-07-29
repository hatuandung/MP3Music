package com.t3h.mp3music;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.t3h.mp3music.model.Song;

import java.util.ArrayList;

public class MediaController implements MediaPlayer.OnCompletionListener {

    private ArrayList<Song> songs;
    private Context context;
    private MediaPlayer mediaPlayer;
    private int index;

    public MediaController(ArrayList<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    public void create(int index) {
        release();
        this.index = index;
        Uri uri = Uri.parse(songs.get(index).getData());
        mediaPlayer = MediaPlayer.create(context, uri);
        start();
        mediaPlayer.setOnCompletionListener(this);
    }

    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void stop() {
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    public void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    public void loop(boolean isLooping) {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(isLooping);
        }
    }

    public void seek(int position) {
        if (mediaPlayer != null) mediaPlayer.seekTo(position);
    }

    public int getDuration(){
        return mediaPlayer == null? 0: mediaPlayer.getDuration();

    }

    public int getPosition(){
        return mediaPlayer == null ? 0 : mediaPlayer.getCurrentPosition();
    }

    public void change(int value){

        index +=value;
        if (index < 0){
            index = songs.size() - 1;

        }else if (index >= songs.size()){
            index = 0;
        }
        create(index);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //index++;
        change(1);

    }
}
