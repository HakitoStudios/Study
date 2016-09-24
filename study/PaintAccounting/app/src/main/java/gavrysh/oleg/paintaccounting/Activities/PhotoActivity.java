package gavrysh.oleg.paintaccounting.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraManager;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import gavrysh.oleg.paintaccounting.PhotoEditView;
import gavrysh.oleg.paintaccounting.R;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAPTURE_CODE=1;


    private Uri fileUri;
    private  String resPath;
    private static final String PATH="PaintAccounting";
    private PhotoEditView editView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        editView = (PhotoEditView)findViewById(R.id.editView);
        findViewById(R.id.button_OK).setOnClickListener(this);

        setResult(RESULT_CANCELED);
        taktPhoto();
    }

private void taktPhoto()
{
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    fileUri = getOutputMediaFileUri();
    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
    resPath = fileUri.getPath();
    startActivityForResult(cameraIntent, CAPTURE_CODE);
}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAPTURE_CODE )
        {
            if(resultCode == RESULT_OK) {
                Bitmap result = BitmapFactory.decodeFile(resPath);
                editView.setBitmap(result);
            }
            else{
                finish();
            }
        }
    }

    Uri getOutputMediaFileUri()
    {
        return Uri.fromFile(getOutputMediaFile());
    }

    File getOutputMediaFile()
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), PATH);
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){

                return null;
            }
        }
        File imageFile;
        imageFile = new File(mediaStorageDir.getPath()+"temp" + ".jpg");
        return imageFile;

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button_OK:
                editView.crop();
                Intent intent = new Intent();
                                Bitmap bmp = editView.getBitmap();
                if(bmp==null){
                    Snackbar.make(editView, "Photo is not correct", Snackbar.LENGTH_LONG).show();
                    break;}
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, byteArray);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
