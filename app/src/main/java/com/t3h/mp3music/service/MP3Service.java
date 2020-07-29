package com.t3h.mp3music.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.t3h.mp3music.model.Song;
import com.t3h.mp3music.MediaController;

import java.util.ArrayList;

public class MP3Service extends Service {

    private MediaController controller;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MP3Binder(this);
    }

    private void pushNotify(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "MP3Channel";
            NotificationChannel channel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_MIN);
            manager.createNotificationChannel(channel);
        }
    }

    public void setData(ArrayList<Song> songs){
        if (controller == null) controller = new MediaController(songs, this);
    }

    public MediaController getController() {
        return controller;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MP3Binder extends Binder{
        private MP3Service service;

        public MP3Binder(MP3Service service) {
            this.service = service;
        }

        public MP3Service getService() {
            return service;
        }
    }
}
