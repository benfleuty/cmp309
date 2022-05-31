package uk.ac.abertay.notsnapchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btnClose = findViewById(R.id.btnClose);

        btnClose.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            Intent intentMain = new Intent(this,MainActivity.class);
            startActivity(intentMain);
            finish();
            return false;
        });
    }
}
