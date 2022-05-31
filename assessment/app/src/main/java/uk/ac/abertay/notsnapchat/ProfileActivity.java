package uk.ac.abertay.notsnapchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

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
        String newEmail = txtEmail.getText().toString();
        String newUsername = txtUsername.getText().toString();

        Map<String, String> data = new HashMap<>();

        if (!user.get_email().equals(newEmail))
            data.put(ApiHelper.DATA_KEY_EMAIL, newEmail);

        if (!user.get_username().equals(newUsername))
            data.put(ApiHelper.DATA_KEY_USERNAME, newUsername);

        if (data.size() < 1)
            // no change
            return;

        Response.Listener<String> onSuccess = response -> {
            Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
        };

        Response.ErrorListener onError = error -> {
            Toast.makeText(this,"fail",Toast.LENGTH_SHORT).show();
        };

        ApiHelper userUpdater = new ApiHelper(
                this,
                Request.Method.PATCH,
                ExternalResources.userURL,
                data,
                onSuccess,
                onError
        );

        userUpdater.executeAsync();
    }
}
