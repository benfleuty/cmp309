package uk.ac.abertay.notsnapchat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createButtonListeners();
    }

    private void createButtonListeners() {
        Button btnSignIn = (Button) findViewById(R.id.btnSignUp);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnSignIn.setOnClickListener(view -> {
            signIn();
        });
        btnLogin.setOnClickListener(view -> {
            login();
        });
    }

    private boolean isLoginInformationValid() {
        boolean isValid = true;

        // check email validity
        isValid &= isEmailValid();

        // check password validity
        isValid &= isPasswordValid();

        return isValid;
    }

    private boolean isEmailValid() {
        String strEmail = getEmail();
        return (!TextUtils.isEmpty(strEmail) && Patterns.EMAIL_ADDRESS.matcher(strEmail).matches());
    }

    private boolean isPasswordValid() {
        String strPassword = getPassword();
        return !TextUtils.isEmpty(strPassword);
    }

    private String getEmail() {
        return ((EditText) findViewById(R.id.txtEmail)).getText().toString();
    }

    private String getPassword() {
        return ((EditText) findViewById(R.id.txtPassword)).getText().toString();
    }

    private void signIn() {
        // todo input validation
        boolean success = true;
        String errors = "";
        if (!isEmailValid()) {
            success = false;
            errors += " Invalid email!";
        }
        if (!isPasswordValid()) {
            success = false;
            errors += " Invalid password!";

        }

        if (!success) {
            Toast.makeText(this, errors, Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, ("Logging in with " + getEmail() + "/" + getPassword()), Toast.LENGTH_LONG).show();
        // todo try sign in
    }

    private void login() {
        // todo input validation
        
        // todo try log in
    }

}
