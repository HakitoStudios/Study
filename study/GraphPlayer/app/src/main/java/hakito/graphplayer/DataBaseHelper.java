package hakito.graphplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.style.TtsSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Oleg on 24-Dec-15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE="main", TABLE_ALARMS="alarms", LABEL="label", TIME="time",
            SONGS="songs", DAYS="days", IS_NIGHT="night", ID="_id", GRAPH = "graph",
    DURATION="duration", ENABLED="enabled";
    public static final int VERSION=1;
    public static final String CREATE_ALARMS="create table " + TABLE_ALARMS + " ( " + ID + " integer primary key autoincrement," + LABEL + " text, " + TIME + " integer, " + DAYS + " text, " + SONGS + " text, " + IS_NIGHT + " integer, " + GRAPH + " text, " + DURATION + " integer," + ENABLED + " integer " + " )";
    private Context c;

    public DataBaseHelper(Context c)
    {

        super(c, DATABASE, null, VERSION);

        this.c = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALARMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void updateOrInsert(Alarm a)
    {
        SQLiteDatabase   d = getWritableDatabase();

        if(a.id == -1)
        {
            d.insert(TABLE_ALARMS, null, getContentValues(a));
        }
        else
        {
            d.update(TABLE_ALARMS, getContentValues(a), ID + "=" + a.id, null);
        }
        d.close();
    }

    private  Alarm getAlarm(Cursor c)
    {
        return new Alarm(
                c.getInt(c.getColumnIndex(ID)),
                c.getString(c.getColumnIndex(LABEL)),
                c.getLong(c.getColumnIndex(TIME)),
                TextConverter.getDays(c.getString(c.getColumnIndex(DAYS))),
                c.getString(c.getColumnIndex(SONGS)),
                c.getInt(c.getColumnIndex(IS_NIGHT)) == 1,
                TextConverter.getGraph(c.getString(c.getColumnIndex(GRAPH))),
                c.getLong(c.getColumnIndex(DURATION)),
                c.getInt(c.getColumnIndex(ENABLED))==1
        );
    }

    public Alarm getAlarm(int index)
    {
        Cursor c = getReadableDatabase().query(TABLE_ALARMS, null, ID+"="+index, null, null, null, null);
        c.moveToFirst();
        return getAlarm(c);
    }

    private ContentValues getContentValues(Alarm a)
    {
        ContentValues c = new ContentValues();
        c.put(LABEL, a.getLabel());
        c.put(TIME, a.getTime());
        c.put(SONGS, a.getSongs());
        c.put(DAYS, a.getDaysString());
        c.put(IS_NIGHT, a.isNight());
        c.put(GRAPH, a.getGraphString());
        c.put(DURATION, a.getDuration());
        c.put(ENABLED, a.isEnabled());
        return c;
    }

    public List<Alarm> getAlarms()
    {
        List<Alarm> res = new ArrayList<>();
        SQLiteDatabase d = getReadableDatabase();
        Cursor c = d.query(TABLE_ALARMS, null, null, null, null, null, null);
        if(c.moveToFirst())
            do {
                res.add(getAlarm(c));
            }
            while (c.moveToNext());
        d.close();
        return res;
    }


    public void remove(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ALARMS, ID + "=" + id, null);
        db.close();
    }


    public static AlarmBroadcastReciever reciever;

    public void setEnabled(int id, boolean enabled)
    {
        if(enabled && (reciever==null || reciever.released))
        {
            Alarm alarm = getAlarm(id);

            reciever = new AlarmBroadcastReciever();
            reciever.SetAlarm(c, alarm);
        }
        ContentValues cv = new ContentValues();
        cv.put(ENABLED, enabled);

        getWritableDatabase().update(TABLE_ALARMS, cv, ID + "=" + id, null);
    }
}
