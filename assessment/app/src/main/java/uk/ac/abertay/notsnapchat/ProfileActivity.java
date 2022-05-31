package uk.ac.abertay.notsnapchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setOnClickListeners();

        unpackIncomingData();
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
    private void setOnClickListeners() {
        Button btnClose = findViewById(R.id.btnClose);

        btnClose.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
            finish();
            return false;
        });
    }
}
