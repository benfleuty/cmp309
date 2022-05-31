package uk.ac.abertay.notsnapchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class ImageViewerActivity extends Activity {
    FusedLocationProviderClient fusedLocationProviderClient;
    private Uri uri;
    private Button btnSavePhoto;

    private User user;

    public static String getImageName(String path) {
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);

        int pos = path.lastIndexOf('/');

        if (pos == -1) return path;

        return path.substring(pos + 1);
    }

    private void unpackIncomingData() {
        Intent incoming = getIntent();
        Bundle userAsBundle = incoming.getBundleExtra("user");
        try {
            user = User.parse_bundle(userAsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("", "start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        ImageView imgTaken = findViewById(R.id.imgTaken);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnSavePhoto = findViewById(R.id.btnSavePhoto);

        btnCancel.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            Intent intentMain = new Intent(this, MainActivity.class);
            intentMain.putExtra("user", User.to_bundle(user));
            startActivity(intentMain);
            overridePendingTransition(R.anim.none, R.anim.slide_out_to_bottom);
            finish();
            return false;
        });

        btnSavePhoto.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            saveImage();
            return false;
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        File filepath = getFilesDir();

        uri = Uri.parse(filepath.toString() + "/image.jpg");
        imgTaken.setImageURI(uri);
        unpackIncomingData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            user.get_id(); // result ignored
        } catch (NullPointerException e) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void saveImage() {
        requestSavePermissions();

        File f = new File(String.valueOf(uri));
        Log.e("", String.valueOf(f.exists()));

        Bitmap localImage = readLocalImage();

        File externalAppDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/NotSnapChat");

        if (!externalAppDir.exists())
            externalAppDir.mkdir();

        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        String mimeType = "image/jpg";
        ContentValues contentValues = new ContentValues();
        String image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/NotSnapChat/image.jpg";
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, getImageName(image));
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.DATA, uri.toString());

        ContentResolver contentResolver = this.getContentResolver();
        Uri contentUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        OutputStream out = null;
        try {
            out = contentResolver.openOutputStream(Objects.requireNonNull(contentUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        localImage.compress(format, quality, out);
        try {
            Objects.requireNonNull(out).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnSavePhoto.setEnabled(false);
        Toast.makeText(this, "Image successfully saved", Toast.LENGTH_SHORT).show();
    }

    private void getLocation() {
        GetLocation getLocation = new GetLocation(this);
        Thread getLocationThread = new Thread(getLocation);
        getLocationThread.start();
        try {
            getLocationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Bitmap readLocalImage() {
        return BitmapFactory.decodeFile(uri.toString());
    }

    private void requestSavePermissions() {
        int requestCode = 99;
        String[] permissionRequests = {"android.permission.WRITE_EXTERNAL_STORAGE"};
        requestPermissions(permissionRequests, requestCode);
    }
}
