package gavrysh.oleg.paintaccounting.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import gavrysh.oleg.paintaccounting.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment implements TitleProvider, View.OnClickListener {


    public ReportsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_reports, container, false);
v.findViewById(R.id.reportBuilding).setOnClickListener(this);
        v.findViewById(R.id.reportPaintin).setOnClickListener(this);
        v.findViewById(R.id.reportRooms).setOnClickListener(this);



        return v;
    }

    void saveViewAsImage(View v)
    {
        String fileName="my.png";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);

        int w = v.getWidth();
        int h = v.getHeight();

        try {
            FileOutputStream stream = new FileOutputStream(file);
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String getTitle() {
        return "Reports";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.reportBuilding:
                saveViewAsImage(v.findViewById(R.id.reportBuilding));
                break;
            case R.id.reportPaintin:
                break;
            case R.id.reportRooms:
                break;
        }
    }
}
