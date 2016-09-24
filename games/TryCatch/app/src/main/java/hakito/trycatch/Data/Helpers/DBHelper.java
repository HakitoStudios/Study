package hakito.trycatch.Data.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

import hakito.trycatch.Data.Content;
import hakito.trycatch.Data.Models.Good;
import hakito.trycatch.Data.Models.Player;
import hakito.trycatch.Data.Models.Record;

/**
 * Created by Oleg on 12-Dec-15.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final int VERSION = 1;
    static final String DATABASE = "db_results", TABLE_RESULTS = "results", ID = "_id", LEVEL_ID = "level_id", STARS = "stars",
            TABLE_PLAYER = "player", COINS = "coins", BONUS = "bonus", COUNT = "count", TABLE_BONUSES = "bonuses";
    static final String CREATE_RESULTS = "create table " + TABLE_RESULTS + "( " + LEVEL_ID + " integer primary key, " + STARS + " integer " + " ) ";
    static final String CREATE_PLAYER = "create table " + TABLE_PLAYER + " ( " + COINS + " integer" + " ) ";
    static final String CREATE_BONUSES = "create table " + TABLE_BONUSES + " ( " + BONUS + " text ) ";

    public DBHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RESULTS);
        db.execSQL(CREATE_PLAYER);
        db.execSQL(CREATE_BONUSES);

        db.insert(TABLE_BONUSES, null, getContentValues(Content.goods.get(1)));
        db.insert(TABLE_BONUSES, null, getContentValues(Content.goods.get(1)));

        db.insert(TABLE_PLAYER, null, getContentValues(new Player(0)));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    ContentValues getContentValues(Good g) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BONUS, g.getType().toString());
        return contentValues;
    }

    ContentValues getContentValues(Player player) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COINS, player.getCoins());
        return contentValues;
    }

    public void savePlayer(Player player) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_PLAYER, getContentValues(player), null, null);

        db.delete(TABLE_BONUSES, null, null);

        for (int i = 0; i < player.getBonuses().size(); i++) {
            db.insert(TABLE_BONUSES, null, getContentValues(player.getBonuses().get(i)));
        }

    }

    public Player getPlayer() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_PLAYER, null, null, null, null, null, null);
        cursor.moveToFirst();

        Player player = getPlayer(cursor);
        Cursor bonusCursor = db.query(TABLE_BONUSES, null, null, null, null, null, null);
        if (bonusCursor.moveToFirst()) {
            do {
                player.addBonus(getBonus(bonusCursor.getString(bonusCursor.getColumnIndex(BONUS))));
            } while (bonusCursor.moveToNext());
        }
        return player;
    }

    private Good getBonus(String s) {
        Good.GoodType goodType = Good.GoodType.valueOf(s);
        for (int i = 0; i < Content.goods.size(); i++) {
            Good g = Content.goods.get(i);
            if (g.getType() == goodType)
                return g;
        }
        return null;
    }

    private Player getPlayer(Cursor c) {
        return new Player(c.getInt(c.getColumnIndex(COINS)));
    }

    ContentValues getContentValues(Record r) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LEVEL_ID, r.level);
        contentValues.put(STARS, r.stars);
        return contentValues;
    }

    public void writeRecord(Record r) {
        if(getRecord(r.level)==null || getRecord(r.level).stars < r.stars) {
            ContentValues cv = getContentValues(r);
            try {
                getWritableDatabase().insertOrThrow(TABLE_RESULTS, null, cv);
            } catch (Exception e) {
                getWritableDatabase().update(TABLE_RESULTS, cv, LEVEL_ID + "=" + r.level, null);
            }
        }
    }

    Record getRecord(Cursor c) {
        return new Record(c.getInt(c.getColumnIndex(LEVEL_ID)), c.getInt(c.getColumnIndex(STARS)));
    }

    public List<Record> getAllRecords()
    {
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESULTS, null, null, null, null, null, null, null);
        List<Record> res = new ArrayList<>(cursor.getCount());
        if(cursor.moveToFirst())
        {
            do {
                res.add(getRecord(cursor));
            }while(cursor.moveToNext());
        }
        db.close();
        return res;
    }

    public Record getRecord(int level) {
        Cursor cursor = getWritableDatabase().query(TABLE_RESULTS, null, LEVEL_ID + "=" + level, null, null, null, null);
        if (!cursor.moveToFirst()) return null;
        return getRecord(cursor);
    }

    public int getStarsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RESULTS, new String[]{"sum(" + STARS + ") as " + STARS}, null, null, null, null, null);
        if (!c.moveToFirst()) return 0;

        return c.getInt(c.getColumnIndex(STARS));
    }
}
