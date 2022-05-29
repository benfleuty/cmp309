package uk.ac.abertay.notsnapchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ImageViewerActivity extends AppCompatActivity {
    private ImageView imgTaken;
    private Button btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        imgTaken = findViewById(R.id.imgTaken);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnTouchListener((view, motionEvent) -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return false;
        });

        File filepath = this.getFilesDir();
        Uri uri = Uri.parse(filepath.toString() + "/image.jpg");
        imgTaken.setImageURI(uri);
    }
}
