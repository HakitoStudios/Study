package gavrysh.oleg.paintaccounting.Models;

import android.graphics.Bitmap;

/**
 * Created by Oleg on 16-Nov-15.
 */
public class Painting extends Idable{
    public String name, notice, tags, description, place;
    public int author_id, room_id, tech_id, genre_id, price, year;
    public long lastRevisionDate;

    public Bitmap bitmap;

    public Painting()
    {
        super();

    }

    public Painting(String name, String notice, String tags, String description, String place, int id, int author_id, int room_id, Bitmap bitmap, int price, int year, long lastRevisionDate, int tech, int genre) {
        super(id);
        this.price = price;
        this.year = year;

        this.lastRevisionDate = lastRevisionDate;
        this.bitmap = bitmap;
        this.name = name;
        this.notice = notice;
        this.tags = tags;
        this.description = description;
        this.place = place;

        this.room_id = room_id;
        this.author_id = author_id;

        this.tech_id=tech;
        this.genre_id=genre;
    }

}
