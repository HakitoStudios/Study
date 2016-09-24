package hakito.graphplayer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmBroadcastReciever extends BroadcastReceiver {

    public boolean released;
    public Alarm alarm;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
//Осуществляем блокировку
        wl.acquire();

//Здесь можно делать обработку.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)) {

            msgStr.append("Одноразовый будильник: ");
        }
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        //Player.playDefault(context);
        if(alarm!=null)
        new Player(context).execute(new Player.PlayerInfo(alarm.getGraph(), alarm.getDuration()));
        msgStr.append(formatter.format(new Date()));

        //Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

        wl.release();
    }

    final public static String ONE_TIME = "onetime";

    public void SetAlarm(Context context, Alarm alarm) {
        this.alarm = alarm;
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReciever.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);//Задаем параметр интента
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //Устанавливаем интервал срабатывания в 5 секунд.
        Toast.makeText(context, "" + alarm.getTime(), Toast.LENGTH_LONG).show();

        Calendar cal = Calendar.getInstance();
        TextConverter.MTime time = TextConverter.getTimeInt(alarm.getTime());
        cal.set(Calendar.HOUR_OF_DAY, (int)time.h);
        cal.set(Calendar.MINUTE, (int) time.m);
        long q = cal.getTimeInMillis() - System.currentTimeMillis();
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarm.getDuration() * 3, pi);
    }

    public void CancelAlarm(Context context) {
        released = true;
        Intent intent = new Intent(context, AlarmBroadcastReciever.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);//Отменяем будильник, связанный с интентом данного класса
    }

    public void setOnetimeTimer(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReciever.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);//Задаем параметр интента
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}
