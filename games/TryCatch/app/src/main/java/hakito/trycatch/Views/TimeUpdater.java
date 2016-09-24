package hakito.trycatch.Views;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import hakito.trycatch.Game.Game;
import hakito.trycatch.Game.Logic.Helper;

/**
 * Created by Oleg on 28-Dec-15.
 */
@SuppressWarnings("ResourceType")
public class TimeUpdater extends AsyncTask<Object, Object, Object> {
    public TextView time, hint;
    public ProgressBar progressCoins;

    public TimeUpdater() {

    }

    @Override
    protected Object doInBackground(Object... params) {
        while (true) {

            if(progressCoins!=null)
            {
                progressCoins.post(new Runnable() {
                    @Override
                    public void run() {
                   progressCoins.setProgress(Game.get().getGameResult().getCoinsProgressIn100());
                    }
                });
            }
            if(hint!=null)
                hint.post(new Runnable() {
                    @Override
                    public void run() {
                        hint.setText(Game.get().getHint());
                    }
                });

            if(time!=null)

            time.post(new Runnable() {
                @Override
                public void run() {
                    time.setText(Helper.getTime((long) (Game.get().getGameResult().time * 1000)));
                }
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
