package gavrysh.oleg.paintaccounting.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import gavrysh.oleg.paintaccounting.Activities.StringEnterActivity;
import gavrysh.oleg.paintaccounting.Data.DataBaseHelper;
import gavrysh.oleg.paintaccounting.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SQLFragment extends Fragment implements View.OnClickListener, TitleProvider {

    private static final String REPORT_HTML="<html>\n" +
            "<head>\n" +
            "<title>Report</title>\n" +
            "</head>\n" +
            "<style>\n" +
            "    #date{\n" +
            "    margin:30;\n" +
            "    float:right;\n" +
            "    }\n" +
            "    #title\n" +
            "    {\n" +
            "    text-align:center;\n" +
            "    font-size:3em;\n" +
            "    font-family:sans-serif;\n" +
            "    }\n" +
            "    #description\n" +
            "    {\n" +
            "\n" +
            "    }\n" +
            "    #table\n" +
            "    {\n" +
            "\n" +
            "    }\n" +
            "    #header\n" +
            "    {\n" +
            "        font-weight:bold;\n" +
            "    }\n" +
            "    table\n" +
            "    {\n" +
            "    border-collapse:collapse;\n" +
            "    }\n" +
            "    td\n" +
            "    {\n" +
            "\n" +
            "    border: 2px solid black;\n" +
            "\n" +
            "    }\n" +
            "    td, tr\n" +
            "    {\n" +
            "\n" +
            "    }\n" +
            "</style>\n" +
            "<body>\n" +
            "<div id=\"title\">Report</div>\n" +
            "<div id=\"description\">%s</div>\n" +
            "<div id=\"table\">\n" +
            "<table>\n" +
            "   %s\n" +
            "</table>\n" +
            "\n" +
            "</div>\n" +
            "<hr>\n" +
            "<div id=\"date\">%s</div>\n" +
            "</body>\n" +
            "</html>";

    GridLayout grid;
    EditText text;
    String[][] data;
    private String description;
private static final int PATH_CODE=1;

    public SQLFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_sql, container, false);
        v.findViewById(R.id.buttonRun).setOnClickListener(this);
        grid=(GridLayout)v.findViewById(R.id.grid);
        text= (EditText)v.findViewById(R.id.textSQL);
        return v;
    }

    View getTextView(String text, int row, int column, boolean header)
    {
        TextView v;
        if(header)
            v = (TextView)getActivity().getLayoutInflater().inflate(R.layout.table_header, grid, false);
            else
            v = (TextView)getActivity().getLayoutInflater().inflate(R.layout.table_cell, grid, false);

        v.setText(text);
        GridLayout.LayoutParams p = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(column));
        p.setMargins(5, 5, 5, 5);
        v.setLayoutParams(p);
        return v;
    }

    void runSQL()
    {
        try {
            description="";
            DataBaseHelper h = new DataBaseHelper(grid);
            Cursor c = h.execCustomSQL(text.getText().toString());
            data = new String[c.getCount()+1][c.getColumnCount()];
            grid.removeAllViews();
            grid.setColumnCount(c.getColumnCount());
            grid.setRowCount(c.getCount() + 1);
            for(int i=0;i < c.getColumnCount(); i++)
            {
                grid.addView(getTextView(c.getColumnName(i), 0, i, true));
                data[0][i] = c.getColumnName(i);
            }

            int r=1;
            if(c.moveToFirst()) {
                do
                {
                    for(int i=0;i < c.getColumnCount(); i++) {
                        grid.addView(getTextView(c.getString(i), r, i, false));
                        data[r][i] = c.getString(i);
                    }
                        r++;
                }while(c.moveToNext());
            }
        }
        catch (Exception e)
        {

        }
    }

    public void query(String query, String description)
    {

        text.setText(query);
        runSQL();
        this.description = description;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonRun:
        runSQL();
                break;

        }
    }

    private void writeHTML(String html, String path)
    {
        File fileName = null;
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdDir = android.os.Environment.getExternalStorageDirectory();
            fileName = new File(sdDir, "reports/"+path+".html");
        } else {
            fileName = getActivity().getFilesDir();
        }
        if (!fileName.exists())
            fileName.getParentFile().mkdirs();

        final File tf = fileName;
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(fileName);
            final File filePath = fileName;
        f.write(html.getBytes());
            f.flush();
            f.close();
            Snackbar.make(getView(), "Report saved", Snackbar.LENGTH_LONG).show();
    } catch (FileNotFoundException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void doSaving(String path)
    {
        if(data!=null)
        {
            String table="", date=Calendar.getInstance().getTime().toString();
            table+="<tr id=\"header\">";
            for (int i=0; i < data[0].length; i++)
            {
                table+="<td>" + data[0][i] + "</td>";
            }
            table+="</tr>";
            for (int i=1; i < data.length; i++)
            {
                table+="<tr>";
                for (int c=0;c<data[i].length;c++)
                {
                    table+="<td>" + data[i][c] + "</td>";
                }
                table+="</tr>";
            }
            String res = String.format(REPORT_HTML, description, table, date);
            writeHTML(res, path);
        }
    }

    public void save()
    {
      startActivityForResult(new Intent(getActivity(), StringEnterActivity.class), PATH_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == PATH_CODE)
            {
                doSaving(data.getStringExtra(StringEnterActivity.EXTRA_FILENAME));
            }
        }
    }

    void saveViewAsImage(View v)
    {
        String fileName=System.currentTimeMillis() % 100000+".png";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);

        View con = getActivity().getLayoutInflater().inflate(R.layout.report_layout, null, false);

        //((FrameLayout) con.findViewById(R.id.frame)).addView(v);
        ((TextView)con.findViewById(R.id.textTitle)).setText("hghy");
        v=con;
        int w = v.getWidth();
        int h = v.getHeight();

        try {
            FileOutputStream stream = new FileOutputStream(file);
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            v.draw(canvas);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getTitle() {
        return "SQL query";
    }
}
