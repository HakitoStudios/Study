package hakito.graphplayer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Interpolator;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class Player extends AsyncTask<Player.PlayerInfo, Float, Object> {

    public static void playDefault(Context context)
    {
        PlayerInfo playerInfo = new PlayerInfo(new float[]{ 1,1}, 10000);
        new Player(context).execute(playerInfo);
    }

    MediaPlayer mediaPlayer;
    Context context;
    int[] songs;
    int songindex=0;
    long startTime;



    public Player(Context context) {
        this.context = context;

        startTime   = System.currentTimeMillis();
        songs = new int[]{R.raw.song1, R.raw.song2};
        mediaPlayer = MediaPlayer.create(context, songs[songindex]);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               // mediaPlayer.release();
                if(mediaPlayer.isPlaying())
                mediaPlayer.stop();
                songindex++;
                mediaPlayer =  MediaPlayer.create(Player.this.context, songs[songindex%songs.length]);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.start();
            }
        });
    }

    @Override
    protected Object doInBackground(PlayerInfo... params) {
        Intent intent = new Intent(context, CancelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        long duration = params[0].duration;
        float[] pp = params[0].points;
        Interpolator interpolator = new Interpolator(1, pp.length);

        for (int i=0;i<pp.length;i++)
        {
            interpolator.setKeyFrame(i, (int)(((float)i / (pp.length-1)) * 1000), new float[]{pp[i]} );
        }
        mediaPlayer.start();
        while (true)
        {

            int p = (int)(1000*(float)(System.currentTimeMillis() - startTime) / duration);
            if(p>1000)
            {
                if(mediaPlayer.isPlaying())
                mediaPlayer.stop();
                return null;
            }
            float[] r = new float[1];
            interpolator.timeToValues(p, r);
            float volume=r[0];
            mediaPlayer.setVolume(volume, volume);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //return null;

    }



    public static class PlayerInfo
    {
        float[] points;
        long duration;

        public PlayerInfo(float[] points, long duration) {
            this.points = points;
            this.duration = duration;
        }
    }
}
