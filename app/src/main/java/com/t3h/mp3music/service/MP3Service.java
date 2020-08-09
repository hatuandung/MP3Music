package com.t3h.mp3music.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.t3h.mp3music.R;
import com.t3h.mp3music.model.Song;
import com.t3h.mp3music.MediaController;

import java.util.ArrayList;

public class MP3Service extends Service {

    private MediaController controller;
    private RemoteViews remoteViews;

    private final String ACTION_NEXT = "action.Next";
    private final String ACTION_PREV = "action.PREV";
    private final String ACTION_PLAY = "action.PLAY";
    private final String ACTION_CLOSE = "action.CLOSE";


    @Override
    public void onCreate() {
        super.onCreate();
        initRemoteViews();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_CLOSE);
        intentFilter.addAction(ACTION_NEXT);
        intentFilter.addAction(ACTION_PLAY);
        intentFilter.addAction(ACTION_PREV);
        registerReceiver(receiver, intentFilter);
    }

    private void initRemoteViews() {
        remoteViews = new RemoteViews(getPackageName(), R.layout.ui_notification);
        registerAction(R.id.img_stop, ACTION_CLOSE);
        registerAction(R.id.img_next, ACTION_NEXT);
        registerAction(R.id.img_pre, ACTION_PREV);
        registerAction(R.id.img_play, ACTION_PLAY);

    }

    private void registerAction(@IdRes int id, String action) {
        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(id, pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MP3Binder(this);
    }

    private void pushNotify(Song song) {
        Intent intent = new Intent(this, getClass());
        startService(intent);


        String channelId = "MP3Channel";
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_MIN);
            manager.createNotificationChannel(channel);
        }
        remoteViews.setTextViewText(R.id.txt_songname, song.getTitle());
        remoteViews.setTextViewText(R.id.txt_artist, song.getArtist());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

        builder.setSmallIcon(R.drawable.ic_song);
        builder.setCustomContentView(remoteViews);
        startForeground(2997, builder.build());
    }

    public void setData(final ArrayList<Song> songs) {
        if (controller == null) controller = new MediaController(songs, this) {
            @Override
            public void create(int index) {
                super.create(index);
                pushNotify(songs.get(index));
            }
        };
    }

    public MediaController getController() {
        return controller;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MP3Binder extends Binder {
        private MP3Service service;

        public MP3Binder(MP3Service service) {
            this.service = service;
        }

        public MP3Service getService() {
            return service;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_CLOSE:
                    break;
                case ACTION_NEXT:
                    controller.change(1);
                    break;
                case ACTION_PLAY:
                    if (controller.isPlaying()) {
                        controller.pause();
                    } else controller.start();
                    break;
                case ACTION_PREV:
                    controller.change(-1);
                    break;
            }
        }
    };
}
