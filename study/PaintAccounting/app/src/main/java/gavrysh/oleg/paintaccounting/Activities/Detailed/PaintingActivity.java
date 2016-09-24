package gavrysh.oleg.paintaccounting.Activities.Detailed;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.ByteArrayOutputStream;
import java.sql.SQLDataException;
import java.util.Calendar;
import java.util.Date;

import gavrysh.oleg.paintaccounting.Activities.BaseActivity;
import gavrysh.oleg.paintaccounting.Activities.FullPhotoActivity;
import gavrysh.oleg.paintaccounting.Activities.PhotoActivity;
import gavrysh.oleg.paintaccounting.Activities.SelectActivity;
import gavrysh.oleg.paintaccounting.Constants;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;

import gavrysh.oleg.paintaccounting.Fragments.PaintingsFragment;
import gavrysh.oleg.paintaccounting.Models.Painting;
import gavrysh.oleg.paintaccounting.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaintingActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final int PHOTO_CODE = 1, AUTHOR_CODE = 2, ROOM_CODE = 3, GENRE_CODE = 4, TECH_CODE = 5;
    public static final String EXTRA_INIT_INFO = "info";

    private int authorId, roomId, genreId, techId;
    private long lastRevision;


    Bitmap bitmap;

    TextView author, room, genre, tech, tlastRevision;
    EditText name, descr, place, tags, notice, year, price;
    ImageView image;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_painting);

        findViewById(R.id.buttonPhoto).setOnClickListener(this);

        tlastRevision = (TextView) findViewById(R.id.tLastRevision);
        tlastRevision.setOnClickListener(this);

        descr = (EditText) findViewById(R.id.textDescription);
        place = (EditText) findViewById(R.id.textPlace);
        tags = (EditText) findViewById(R.id.textTags);
        notice = (EditText) findViewById(R.id.textNotices);
        name = (EditText) findViewById(R.id.textTask);
        image = (ImageView) findViewById(R.id.image);

        tech = (TextView) findViewById(R.id.textTech);
        genre = (TextView) findViewById(R.id.textGenre);
        room = (TextView) findViewById(R.id.textRoom);
        author = (TextView) findViewById(R.id.textAuthor);

        year = (EditText) findViewById(R.id.textYear);
        price = (EditText) findViewById(R.id.textPrice);

        genre.setOnClickListener(this);
        tech.setOnClickListener(this);
        author.setOnClickListener(this);
        room.setOnClickListener(this);

        findViewById(R.id.buttonAuthor).setOnClickListener(this);
        findViewById(R.id.buttonRoom).setOnClickListener(this);
        findViewById(R.id.buttonGenre).setOnClickListener(this);
        findViewById(R.id.buttonTech).setOnClickListener(this);

        findViewById(R.id.imageButton).setOnClickListener(this);
        tryFill();
        setTitle("Painting");
    }


    void tryFill() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.EXTRA_ID)) {
            id = getIntent().getExtras().getInt(Constants.EXTRA_ID);
            fillViews();

        } else {
            authorId = roomId = id = genreId = techId = -1;
            lastRevision = Calendar.getInstance().getTimeInMillis();
            fillLastRevision();
            year.setText(""+Calendar.getInstance().get(Calendar.YEAR));

            if (getIntent().hasExtra(EXTRA_INIT_INFO)) {
                PaintingsFragment.InitPicInfo info = (PaintingsFragment.InitPicInfo) getIntent().getSerializableExtra(EXTRA_INIT_INFO);
                switch (info.field) {
                    case SelectActivity.AUTHOR:
                        authorId = info.id;
                        fillAuthor();
                        break;
                    case SelectActivity.ROOM:
                        roomId = info.id;
                        fillRoom();
                        break;
                }
            }
        }
    }

    void fillLastRevision()
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(lastRevision);
        tlastRevision.setText(String.format("%2s.%2s.%s", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)).replace(' ', '0'));
    }

    void fillGenre() {
        try {
            DataBaseHelper h = new DataBaseHelper(name);
            genre.setText(h.getGenre(genreId).name);
            genre.setEnabled(true);
        } catch (Exception e) {
            genre.setEnabled(false);
        }
    }

    void fillTech() {
        try {
            DataBaseHelper h = new DataBaseHelper(name);
            tech.setText(h.getTech(techId).name);
            tech.setEnabled(true);
        } catch (Exception e) {
            tech.setEnabled(false);
        }
    }

    void fillRoom() {
        try {
            DataBaseHelper h = new DataBaseHelper(name);
            room.setText(h.getRoom(roomId).name);
            room.setEnabled(true);
        } catch (Exception e) {
            room.setEnabled(false);
        }
    }

    void fillAuthor() {
        try {
            DataBaseHelper h = new DataBaseHelper(name);
            author.setText(h.getAuthor(authorId).name);
            author.setEnabled(true);
        } catch (Exception e) {
            author.setEnabled(false);

        }
    }

    void fillViews() {
        DataBaseHelper h = new DataBaseHelper(name);
        Painting p = h.getPainting(id);

        genreId = p.genre_id;
        fillGenre();

        techId = p.tech_id;
        fillTech();

        authorId = p.author_id;
        fillAuthor();

        roomId = p.room_id;
        fillRoom();

        lastRevision = p.lastRevisionDate;
        fillLastRevision();


        price.setText("" + p.price);
        year.setText("" + p.year);
        name.setText(p.name);
        descr.setText(p.description);
        place.setText(p.place);
        tags.setText(p.tags);
        notice.setText(p.notice);
        if (p.bitmap != null) {
            image.setImageBitmap(p.bitmap);
        }
    }

    protected void apply() throws SQLException {
        Painting p = new Painting();


        DataBaseHelper h = new DataBaseHelper(name);


        if (bitmap != null) {
            p.bitmap = bitmap;
        }
        try {
            p.price = Integer.parseInt(price.getText().toString());
        } catch (Exception e) {

        }

        try {
            p.year = Integer.parseInt(year.getText().toString());
        } catch (Exception e) {

        }


        p.id = id;
        p.tech_id = techId;
        p.lastRevisionDate = lastRevision;
        p.genre_id = genreId;
        p.author_id = authorId;
        p.room_id = roomId;
        p.name = name.getText().toString();
        p.description = descr.getText().toString();
        p.place = place.getText().toString();
        p.tags = tags.getText().toString();
        p.notice = notice.getText().toString();
        if (id == -1) {
            h.insertPainting(p);
        } else {
            h.updatePainting(p);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_CODE) {

                byte[] b = data.getByteArrayExtra(MediaStore.EXTRA_OUTPUT);
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                image.setImageBitmap(bitmap);

            } else if (requestCode == AUTHOR_CODE) {
                authorId = data.getIntExtra(SelectActivity.EXTRA_ID, -1);
                fillAuthor();
            } else if (requestCode == ROOM_CODE) {
                roomId = data.getIntExtra(SelectActivity.EXTRA_ID, -1);
                fillRoom();
            } else if (requestCode == GENRE_CODE) {
                genreId = data.getIntExtra(SelectActivity.EXTRA_ID, -1);
                fillGenre();
            } else if (requestCode == TECH_CODE) {
                techId = data.getIntExtra(SelectActivity.EXTRA_ID, -1);
                fillTech();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPhoto:
                startActivityForResult(new Intent(this, PhotoActivity.class), PHOTO_CODE);
                break;
            case R.id.buttonAuthor:
                Intent intenta = new Intent(this, SelectActivity.class);
                intenta.putExtra(SelectActivity.EXTRA_TYPE, SelectActivity.AUTHOR);
                startActivityForResult(intenta, AUTHOR_CODE);
                break;
            case R.id.buttonRoom:
                Intent intentr = new Intent(this, SelectActivity.class);
                intentr.putExtra(SelectActivity.EXTRA_TYPE, SelectActivity.ROOM);
                startActivityForResult(intentr, ROOM_CODE);
                break;
            case R.id.buttonGenre:
                Intent intentg = new Intent(this, SelectActivity.class);
                intentg.putExtra(SelectActivity.EXTRA_TYPE, SelectActivity.GENRE);
                startActivityForResult(intentg, GENRE_CODE);
                break;
            case R.id.buttonTech:
                Intent intentt = new Intent(this, SelectActivity.class);
                intentt.putExtra(SelectActivity.EXTRA_TYPE, SelectActivity.TECH);
                startActivityForResult(intentt, TECH_CODE);
                break;
            case R.id.imageButton:
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ((BitmapDrawable) image.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(this, FullPhotoActivity.class);
                intent.putExtra(FullPhotoActivity.PHOTO_TAG, byteArray);
                startActivity(intent);
                break;


            case R.id.textGenre:
                Intent intentsg = new Intent(this, GenreActivity.class);
                intentsg.putExtra(Constants.EXTRA_ID, genreId);
                try {
                    startActivity(intentsg);
                } catch (Exception e) {
                }
                break;
            case R.id.textTech:
                Intent intentst = new Intent(this, TechActivity.class);
                intentst.putExtra(Constants.EXTRA_ID, techId);
                try {
                    startActivity(intentst);
                } catch (Exception e) {
                }
                break;
            case R.id.textRoom:
                Intent intentsr = new Intent(this, RoomActivity.class);
                intentsr.putExtra(Constants.EXTRA_ID, roomId);
                try {
                    startActivity(intentsr);
                } catch (Exception e) {
                }
                break;
            case R.id.textAuthor:
                Intent intentsa = new Intent(this, AuthorActivity.class);
                intentsa.putExtra(Constants.EXTRA_ID, authorId);
                try {
                    startActivity(intentsa);
                } catch (Exception e) {
                }
                break;
            case R.id.tLastRevision:
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(lastRevision);
                new DatePickerDialog(this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        lastRevision = c.getTimeInMillis();

        fillLastRevision();
    }
}
