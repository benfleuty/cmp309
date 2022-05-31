package uk.ac.abertay.notsnapchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    EditText txtEmail;
    EditText txtUsername;
    EditText txtPassword;
    EditText txtConfirmPassword;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getUserFields();
        setOnClickListeners();

        unpackIncomingData();
    }

    private void setUserFields() {
        txtEmail.setText(user.get_email());
        txtUsername.setText(user.get_username());
    }

    private void getUserFields() {
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
    }

    private void unpackIncomingData() {
        Intent incoming = getIntent();
        Bundle userAsBundle = incoming.getBundleExtra("user");
        try {
            user = User.parse_bundle(userAsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUserFields();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnClickListeners() {
        Button btnClose = findViewById(R.id.btnClose);
        Button btnSaveNewUserData = findViewById(R.id.btnSaveNewUserData);

        btnClose.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
            finish();
            return false;
        });

        btnSaveNewUserData.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return false;

            saveUserData();
            return false;
        });
    }

    private void saveUserData() {

    }
}
