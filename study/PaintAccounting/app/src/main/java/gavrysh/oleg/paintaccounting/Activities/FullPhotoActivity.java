package gavrysh.oleg.paintaccounting.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import gavrysh.oleg.paintaccounting.R;

public class FullPhotoActivity extends AppCompatActivity {

    public static final String PHOTO_TAG = "photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);

        ImageView imageView = (ImageView)findViewById(R.id.image);

        if(getIntent().getExtras()!=null)
        {
            if(getIntent().getExtras().containsKey(PHOTO_TAG))
            {
                byte[] b = getIntent().getExtras().getByteArray(PHOTO_TAG);
                Bitmap bitmap =  BitmapFactory.decodeByteArray(b, 0, b.length);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
