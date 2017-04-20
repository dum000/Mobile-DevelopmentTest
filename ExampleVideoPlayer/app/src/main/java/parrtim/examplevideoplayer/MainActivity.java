package parrtim.examplevideoplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener {

    Button playButton;
    Button pauseButton;
    MediaPlayer mediaPlayer;
    SurfaceView canvas;
    SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.play);
        pauseButton = (Button) findViewById(R.id.pause);
        canvas = (SurfaceView) findViewById(R.id.canvas);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.me);
        holder = canvas.getHolder();

        holder.addCallback(this);
        mediaPlayer.setOnCompletionListener(this);

        pauseButton.setEnabled(false);
    }

    public void pauseFile(View view) {
        mediaPlayer.pause();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }

    public void playFile(View view) {
        mediaPlayer.start();
        pauseButton.setEnabled(true);
        playButton.setEnabled(false);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }
}
