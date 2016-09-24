package hakito.autosim.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLDataException;

/**
 * Created by Oleg on 29-Nov-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static class Record
    {
        public String level, car;
        public double time;

        public Record(String level, String car, double time) {
            this.level = level;
            this.car = car;
            this.time = time;
        }

        @Override
        public String toString() {
            return String.format("Best result: %.2fs", time);
        }
    }

    private static final String DBNAME="records.db", TRECORDS="records",  CNAME="car", LNAME="level", TIME="time";
    private static final int VERSION=1;
    private static final String CREATE_QUERY="CREATE TABLE " + TRECORDS + " ( "+LNAME+" TEXT, "+CNAME+" TEXT, "+TIME+" REAL, PRIMARY KEY( "+CNAME+", "+LNAME+"));";


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private ContentValues getContentValues(Record record)
    {
        ContentValues cv = new ContentValues();
        cv.put(LNAME, record.level);
        cv.put(CNAME, record.car);
        cv.put(TIME, record.time);
        return cv;
    }

    public void updateRecord(Record record)
    {
        SQLiteDatabase db = getWritableDatabase();
        if(getRecord(record.level, record.car)!=null)
        {
            db.update(TRECORDS, getContentValues(record), LNAME + " = '" + record.level + "' and '" + CNAME + " = " + record.car+"'", null);
        }
        else
        {
            db.insert(TRECORDS, null, getContentValues(record));
        }


    }

    public Record getRecord(String level, String car)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TRECORDS, null, LNAME + " = '" + level + "' and " + CNAME + " = '" + car+"'", null, null, null, null);
        if(cursor.getCount()!=1)
        {
            return null;
        }
        cursor.moveToFirst();
        Record res = new Record(cursor.getString(cursor.getColumnIndex(LNAME)),
                cursor.getString(cursor.getColumnIndex(CNAME)),
                cursor.getDouble(cursor.getColumnIndex(TIME)));
        return res;
    }
}
