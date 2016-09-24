package hakito.trycatch.Mosaic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Path;
import android.graphics.Region;
import android.support.v7.graphics.drawable.DrawableUtils;

import java.util.List;

import hakito.trycatch.Data.Helpers.DBHelper;
import hakito.trycatch.Data.Models.Record;

/**
 * Created by Oleg on 08-Jan-16.
 */
public class MosaicBuilder {
    private Context context;

    public MosaicBuilder(Context context) {
        this.context = context;
    }

    public Bitmap getMosaic()
    {
        DBHelper dbHelper = new DBHelper(context);
        List<Record> stars = dbHelper.getAllRecords();
        Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(bitmap);


        return bitmap;
    }


}
