package gavrysh.oleg.paintaccounting.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gavrysh.oleg.paintaccounting.Activities.FilterActivity;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Models.Author;
import gavrysh.oleg.paintaccounting.Models.Building;
import gavrysh.oleg.paintaccounting.Models.Genre;
import gavrysh.oleg.paintaccounting.Models.Painting;
import gavrysh.oleg.paintaccounting.Models.Room;
import gavrysh.oleg.paintaccounting.Models.Serve;
import gavrysh.oleg.paintaccounting.Models.Technique;
import gavrysh.oleg.paintaccounting.Models.Worker;

/**
 * Created by Oleg on 22-Nov-15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "paintings.db", TABLE_PICS = "pictures", TABLE_TECHS = "techs", TABLE_GENRES = "genres",
            TABLE_AUTHORS = "authors", TABLE_ROOMS = "rooms", TABLE_BUILDINGS = "buildings", TABLE_WORKERS = "workers", TABLE_SERVES = "serves",
            PIC_AUTHOR_ID = "author_id", PIC_NOTICE = "notice", PIC_WORKER_ID = "worker_id", BUILDING_ID = "building_id", TASK = "task", ADRESS = "adress",
            PLACE = "place", TAGS = "tags", PIC_TECH_ID = "tech", PIC_GENRE_ID = "genre", PIC_ROOM_ID = "room_id", GENERIC_NAME = "name", GENERIC_DESC = "description",
            PHONE = "phone", ID = "_id", PHOTO = "photo", PRICE = "price", YEAR = "year", LAST_REVISION = "last_revision";

    public static final String
            CREATE_WORKERS = "CREATE TABLE " + TABLE_WORKERS + " ( " + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + GENERIC_NAME + " TEXT NOT NULL, " + PHONE + " TEXT);",
            CREATE_TECHS = "CREATE TABLE " + TABLE_TECHS + " ( " + ID + "	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	" + GENERIC_NAME + "	TEXT NOT NULL,	" + GENERIC_DESC + "	TEXT);",
            CREATE_SERVES = "CREATE TABLE " + TABLE_SERVES + " (	" + ID + "	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	" + PIC_WORKER_ID + "	INTEGER NOT NULL,	" + PIC_ROOM_ID + "	INTEGER NOT NULL,	" + TASK + "	TEXT NOT NULL,	FOREIGN KEY(" + PIC_WORKER_ID + ") REFERENCES " + TABLE_WORKERS + " ( " + ID + " ),	FOREIGN KEY(" + PIC_ROOM_ID + ") REFERENCES " + TABLE_ROOMS + "(" + ID + "));",
            CREATE_ROOMS = "CREATE TABLE " + TABLE_ROOMS + " (	" + ID + "	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	" + BUILDING_ID + "	INTEGER NOT NULL,	" + GENERIC_DESC + " TEXT, " + GENERIC_NAME + "	TEXT,	FOREIGN KEY(" + BUILDING_ID + ") REFERENCES " + TABLE_BUILDINGS + "(" + ID + "));",
            CREATE_PICTURES = "CREATE TABLE " + TABLE_PICS + " (	" + ID + "	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	" + PIC_AUTHOR_ID + "	INTEGER NOT NULL,	" + PRICE + " INTEGER, " + YEAR + " INTEGER, " + GENERIC_NAME + "	TEXT NOT NULL,	" + GENERIC_DESC + "	TEXT, " + PLACE + "	TEXT,	" + PIC_NOTICE + "	TEXT,	" + TAGS + "	TEXT,	" + PIC_GENRE_ID + "	INTEGER NOT NULL,	" + PIC_TECH_ID + "	INTEGER NOT NULL,	" + PIC_ROOM_ID + "	INTEGER NOT NULL, " + LAST_REVISION + " INTEGER, " + PHOTO + " BLOB, " + "	FOREIGN KEY(" + PIC_AUTHOR_ID + ") REFERENCES " + TABLE_AUTHORS + " ( " + ID + " ),	FOREIGN KEY(" + PIC_GENRE_ID + ") REFERENCES " + TABLE_GENRES + " ( " + ID + " ),	FOREIGN KEY(" + PIC_TECH_ID + ") REFERENCES " + TABLE_TECHS + " ( " + ID + " ));",
            CREATE_GENRES = "CREATE TABLE " + TABLE_GENRES + " (	" + ID + "	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	" + GENERIC_NAME + "	TEXT NOT NULL,	" + GENERIC_DESC + "	TEXT);",
            CREATE_BUILDINGS = "CREATE TABLE " + TABLE_BUILDINGS + " (	" + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	" + GENERIC_NAME + "	TEXT NOT NULL,	" + ADRESS + "	TEXT,	" + PHONE + "	TEXT);",
            CREATE_AUTHORS = "CREATE TABLE " + TABLE_AUTHORS + " ( " + ID + "	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	" + GENERIC_NAME + " TEXT NOT NULL,	" + GENERIC_DESC + "	TEXT);";


    public static final int VERSION = 1;
    private View v;

    public DataBaseHelper(View v) {
        super(v.getContext(), DATABASE_NAME, null, VERSION);
        this.v = v;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.beginTransaction();
        db.execSQL(CREATE_WORKERS);
        db.execSQL(CREATE_TECHS);
        db.execSQL(CREATE_GENRES);
        db.execSQL(CREATE_SERVES);
        db.execSQL(CREATE_ROOMS);
        db.execSQL(CREATE_BUILDINGS);
        db.execSQL(CREATE_PICTURES);
        db.execSQL(CREATE_AUTHORS);
        //db.endTransaction();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static byte[] bitmapToBlob(Bitmap b) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, os);
        return os.toByteArray();
    }

    private static Bitmap blobToBitmap(byte[] array) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(array);
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
        }
        return null;
    }

    public Cursor execCustomSQL(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    private ContentValues getContentValues(Worker w) {
        ContentValues cv = new ContentValues();
        cv.put(GENERIC_NAME, w.name);
        cv.put(PHONE, w.phone);
        return cv;
    }

    public long insertWorker(Worker w) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        return db.insertOrThrow(TABLE_WORKERS, null, getContentValues(w));
    }

    public void updateWorker(Worker w) {
        SQLiteDatabase db = getWritableDatabase();


        db.update(TABLE_WORKERS, getContentValues(w), ID + "=?", new String[]{"" + w.id});
    }

    public Worker getWorker(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_WORKERS, null, ID + "=" + id, null, null, null, null);
        c.moveToFirst();
        return getWorker(c);
    }

    public Worker getWorker(Cursor c) {
        return new Worker(
                c.getInt(c.getColumnIndex(ID)),
                c.getString(c.getColumnIndex(GENERIC_NAME)),
                c.getString(c.getColumnIndex(PHONE))
        );
    }


    public List<Worker> getWorkers() {
        List<Worker> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_WORKERS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Worker w = getWorker(c);

                res.add(w);
            } while (c.moveToNext());
        }
        return res;
    }

    private ContentValues getContentValues(Building b) {
        ContentValues cv = new ContentValues();
        cv.put(GENERIC_NAME, b.name);
        cv.put(ADRESS, b.adress);
        return cv;
    }

    public long insertBuilding(Building b) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        return db.insertOrThrow(TABLE_BUILDINGS, null, getContentValues(b));
    }

    public void updateBuilding(Building b) {
        SQLiteDatabase db = getWritableDatabase();


        db.update(TABLE_BUILDINGS, getContentValues(b), ID + "=?", new String[]{"" + b.id});
    }

    public Building getBuilding(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_BUILDINGS, null, ID + "=" + id, null, null, null, null);
        c.moveToFirst();
        return getBuilding(c);
    }

    public Building getBuilding(Cursor c) {
        return new Building(
                c.getInt(c.getColumnIndex(ID)),
                c.getString(c.getColumnIndex(GENERIC_NAME)),
                c.getString(c.getColumnIndex(ADRESS))
        );
    }

    public List<Building> getBuildings() {
        List<Building> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_BUILDINGS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Building b = getBuilding(c);

                res.add(b);
            } while (c.moveToNext());
        }
        return res;
    }

    private ContentValues getContentValues(Room r) {
        ContentValues cv = new ContentValues();
        cv.put(GENERIC_NAME, r.name);
        cv.put(GENERIC_DESC, r.description);
        cv.put(BUILDING_ID, r.builingId);
        return cv;
    }

    public long insertRoom(Room r) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        return db.insertOrThrow(TABLE_ROOMS, null, getContentValues(r));
    }

    public void updateRoom(Room r) {
        SQLiteDatabase db = getWritableDatabase();


        db.update(TABLE_ROOMS, getContentValues(r), ID + "=?", new String[]{"" + r.id});
    }

    public Room getRoom(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_ROOMS, null, ID + "=" + id, null, null, null, null);
        c.moveToFirst();
        return getRoom(c);
    }

    public Room getRoom(Cursor c) {
        return new Room(
                c.getInt(c.getColumnIndex(ID)),
                c.getInt(c.getColumnIndex(BUILDING_ID)),
                c.getString(c.getColumnIndex(GENERIC_NAME)),
                c.getString(c.getColumnIndex(GENERIC_DESC))
        );
    }

    public List<Room> getRooms(int buildingId) {
        List<Room> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String where = null;
        if (buildingId != -1) {
            where = BUILDING_ID + " = " + buildingId;
        }
        Cursor c = db.query(TABLE_ROOMS, null, where, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Room r = getRoom(c);

                res.add(r);
            } while (c.moveToNext());
        }
        return res;
    }

    private ContentValues getContentValues(Genre g) {
        ContentValues cv = new ContentValues();
        cv.put(GENERIC_NAME, g.name);
        cv.put(GENERIC_DESC, g.description);
        return cv;
    }

    public long insertGenre(Genre g) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        return db.insertOrThrow(TABLE_GENRES, null, getContentValues(g));
    }

    public void updateGenre(Genre g) {
        SQLiteDatabase db = getWritableDatabase();

        db.update(TABLE_GENRES, getContentValues(g), ID + "=?", new String[]{"" + g.id});
    }

    public Genre getGenre(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_GENRES, null, ID + "=" + id, null, null, null, null);
        c.moveToFirst();
        return getGenre(c);
    }

    public Genre getGenre(Cursor c) {
        return new Genre(
                c.getInt(c.getColumnIndex(ID)),
                c.getString(c.getColumnIndex(GENERIC_NAME)),
                c.getString(c.getColumnIndex(GENERIC_DESC))
        );
    }

    public List<Genre> getGenres() {
        List<Genre> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_GENRES, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Genre g = getGenre(c);

                res.add(g);
            } while (c.moveToNext());
        }
        return res;
    }

    private ContentValues getContentValues(Technique t) {
        ContentValues cv = new ContentValues();
        cv.put(GENERIC_NAME, t.name);
        cv.put(GENERIC_DESC, t.description);
        return cv;
    }

    public long insertTech(Technique t) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        return db.insertOrThrow(TABLE_TECHS, null, getContentValues(t));
    }

    public void updateTech(Technique t) {
        SQLiteDatabase db = getWritableDatabase();

        db.update(TABLE_TECHS, getContentValues(t), ID + "=?", new String[]{"" + t.id});
    }

    public Technique getTech(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_TECHS, null, ID + "=" + id, null, null, null, null);
        c.moveToFirst();
        return getTech(c);
    }

    public Technique getTech(Cursor c) {
        return new Technique(
                c.getInt(c.getColumnIndex(ID)),
                c.getString(c.getColumnIndex(GENERIC_NAME)),
                c.getString(c.getColumnIndex(GENERIC_DESC))
        );
    }

    public List<Technique> getTechs() {
        List<Technique> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_TECHS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Technique t = getTech(c);
                res.add(t);
            } while (c.moveToNext());
        }
        return res;
    }

    private ContentValues getContentValues(Author a) {
        ContentValues cv = new ContentValues();
        cv.put(GENERIC_NAME, a.name);
        cv.put(GENERIC_DESC, a.description);
        return cv;
    }

    public long insertAuthor(Author a) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        return db.insertOrThrow(TABLE_AUTHORS, null, getContentValues(a));
    }

    public void updateAuthor(Author a) {
        SQLiteDatabase db = getWritableDatabase();

        db.update(TABLE_AUTHORS, getContentValues(a), ID + "=?", new String[]{"" + a.id});
    }

    public Author getAuthor(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_AUTHORS, null, ID + "=" + id, null, null, null, null);
        c.moveToFirst();
        return getAuthor(c);
    }

    public Author getAuthor(Cursor c) {
        return new Author(
                c.getString(c.getColumnIndex(GENERIC_NAME)),
                c.getString(c.getColumnIndex(GENERIC_DESC)),
                c.getInt(c.getColumnIndex(ID))
        );
    }


    public List<Author> getAuthors() {
        List<Author> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_AUTHORS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Author a = getAuthor(c);

                res.add(a);
            } while (c.moveToNext());
        }
        return res;
    }

    private ContentValues getContentValues(Serve s) {
        ContentValues cv = new ContentValues();
        cv.put(PIC_WORKER_ID, s.workerId);
        cv.put(PIC_ROOM_ID, s.roomId);
        cv.put(TASK, s.tasks);
        return cv;
    }

    public long insertServe(Serve s) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        return db.insertOrThrow(TABLE_SERVES, null, getContentValues(s));
    }

    public void updateServe(Serve s) {
        SQLiteDatabase db = getWritableDatabase();

        db.update(TABLE_SERVES, getContentValues(s), ID + "=?", new String[]{"" + s.id});
    }


    private void addInfo(Serve s) {
        s.room = getRoom(s.roomId).name;
        s.worker = getWorker(s.workerId).name;
    }

    public Serve getServe(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_SERVES, null, ID + "=" + id, null, null, null, null);
        c.moveToFirst();
        Serve s = getServe(c);
        addInfo(s);
        return s;
    }

    public Serve getServe(Cursor c) {
        Serve s = new Serve(
                c.getInt(c.getColumnIndex(ID)),
                c.getInt(c.getColumnIndex(PIC_WORKER_ID)),
                c.getInt(c.getColumnIndex(PIC_ROOM_ID)),
                c.getString(c.getColumnIndex(TASK))
        );
        addInfo(s);
        return s;
    }


    public List<Serve> getServes(int roomId, int workerId) {
        List<Serve> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_SERVES, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Serve s = getServe(c);
                if ((s.roomId == roomId || roomId == -1) && (s.workerId == workerId || workerId == -1)) {
                    res.add(s);
                }
            } while (c.moveToNext());
        }
        return res;
    }

    private ContentValues getContentValues(Painting p) {
        ContentValues cv = new ContentValues();
        cv.put(GENERIC_NAME, p.name);
        cv.put(PIC_NOTICE, p.notice);
        cv.put(TAGS, p.tags);
        cv.put(GENERIC_DESC, p.description);
        cv.put(PLACE, p.place);
        cv.put(PIC_AUTHOR_ID, p.author_id);
        cv.put(PIC_ROOM_ID, p.room_id);
        cv.put(PIC_GENRE_ID, p.genre_id);
        cv.put(PIC_TECH_ID, p.tech_id);
        cv.put(PRICE, p.price);
        cv.put(YEAR, p.year);
        cv.put(LAST_REVISION, p.lastRevisionDate);
        if (p.bitmap != null) {
            cv.put(PHOTO, bitmapToBlob(p.bitmap));
        }
        return cv;
    }

    public long insertPainting(Painting p) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        return db.insertOrThrow(TABLE_PICS, null, getContentValues(p));

    }

    public void updatePainting(Painting p) {
        SQLiteDatabase db = getWritableDatabase();

        db.update(TABLE_PICS, getContentValues(p), ID + "=?", new String[]{"" + p.id});
    }

    public Painting getPainting(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PICS, null, ID + "=" + id, null, null, null, null);
        c.moveToFirst();
        return getPainting(c);
    }

    public Painting getPainting(Cursor c) {
        return new Painting(
                c.getString(c.getColumnIndex(GENERIC_NAME)),
                c.getString(c.getColumnIndex(PIC_NOTICE)),
                c.getString(c.getColumnIndex(TAGS)),
                c.getString(c.getColumnIndex(GENERIC_DESC)),
                c.getString(c.getColumnIndex(PLACE)),
                c.getInt(c.getColumnIndex(ID)),
                c.getInt(c.getColumnIndex(PIC_AUTHOR_ID)),
                c.getInt(c.getColumnIndex(PIC_ROOM_ID)),
                blobToBitmap(c.getBlob(c.getColumnIndex(PHOTO))),
                c.getInt(c.getColumnIndex(PRICE)),
                c.getInt(c.getColumnIndex(YEAR)),
                c.getLong(c.getColumnIndex(LAST_REVISION)),
                c.getInt(c.getColumnIndex(PIC_TECH_ID)),
                c.getInt(c.getColumnIndex(PIC_GENRE_ID))
        );
    }


    public List<Painting> getPaintings(int authorId, int genreId, int techId, int roomId) {
        List<Painting> l = getPaintings(Constants.Order.none, FilterActivity.FilterInfo.NONE, ""), res = new ArrayList<>();
        for (Painting p : l) {
            if ((authorId == -1 || p.author_id == authorId) && (techId == -1 || p.tech_id == techId) && (genreId == -1 ||
                    p.genre_id == genreId) && (roomId == -1 || p.room_id == roomId)) {
                res.add(p);
            }
        }
        return res;
    }

    public List<Painting> getPaintings(Constants.Order order, FilterActivity.FilterInfo info, String search) {
        List<Painting> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String ordering = null;
        switch (order) {
            case name:
                ordering = GENERIC_NAME;
                break;
            case year:
                ordering = YEAR;
                break;
            case price:
                ordering = PRICE;
                break;
            case none:
                break;
        }
        String where = "1=1";
        if (info.photo == 1) {
            where += " and " + PHOTO + " is not null";
        } else if (info.photo == 2) {
            where += " and " + PHOTO + " is null";
        }
        if (info.minYear != -1) {
            where += " and " + YEAR + " >= " + info.minYear;
        }
        if (info.maxYear != -1) {
            where += " and " + YEAR + " <= " + info.maxYear;
        }
        if (info.maxPrice != -1) {
            where += " and " + PRICE + " <= " + info.maxPrice;
        }
        if (info.minPrice != -1) {
            where += " and " + PRICE + " >= " + info.minPrice;
        }

        where += " and ( " + GENERIC_NAME + " like '%" + search + "%'";
        where += " or " + GENERIC_DESC + " like '%" + search + "%'";
        where += " or " + PIC_NOTICE + " like '%" + search + "%'";
        where += " or " + TAGS + " like '%" + search + "%')";
        Cursor c = db.query(TABLE_PICS, null, where, null, null, null, ordering);
        if (c.moveToFirst()) {

            do {
                Painting p = getPainting(c);
                res.add(p);
            } while (c.moveToNext());
        }

        return res;
    }

    public void delete(int id, Table table) {
        SQLiteDatabase db = getWritableDatabase();
        String tableName = null;
        switch (table) {
            case AUTHORS:
                tableName = TABLE_AUTHORS;
                break;
            case PICS:
                tableName = TABLE_PICS;
                break;
            case TECHS:
                tableName = TABLE_TECHS;
                break;
            case GENRES:
                tableName = TABLE_GENRES;
                break;
            case ROOMS:
                tableName = TABLE_ROOMS;
                break;
            case BUILDINGS:
                tableName = TABLE_BUILDINGS;
                break;
            case WORKERS:
                tableName = TABLE_WORKERS;
                break;
            case SERVES:
                tableName = TABLE_SERVES;
                break;
        }
        try {
            db.delete(tableName, ID + "=?", new String[]{"" + id});
        } catch (SQLiteConstraintException e) {
            Snackbar.make(v, "Can`t be removed", Snackbar.LENGTH_SHORT).show();
        }

    }

    public enum Table {
        AUTHORS, PICS, TECHS, GENRES, ROOMS, BUILDINGS, WORKERS, SERVES
    }
}
