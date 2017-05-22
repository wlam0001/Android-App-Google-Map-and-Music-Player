package edu.unc.nirjon.classlocation;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MyService extends Service implements MediaPlayer.OnPreparedListener {
    private MediaPlayer mMediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String message = intent.getStringExtra(MainActivity.SONG);

        if (message.equals("one")) {
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.cake);
        } else if (message.equals("two")) {
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.waves);
        } else if (message.equals("three")) {
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.carolina);
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(this);
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        Toast.makeText(this, " bind on service", Toast.LENGTH_LONG).show();
        return new Binder();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }


    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) mMediaPlayer.release();
    }


}
