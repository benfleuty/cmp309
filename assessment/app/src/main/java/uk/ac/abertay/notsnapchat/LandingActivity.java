package uk.ac.abertay.notsnapchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        TextView txt = findViewById(R.id.txtWelcomeMessage);
        String msg = getString(R.string.landing_welcome) + " " + getString(R.string.app_name);
        txt.setText(msg);

        switch (userLoggedInStatus()) {
            case LoggedIn:
                // todo start main app activity
                break;
            case NotLoggedIn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case SessionExpired:
                // todo start login activity with session expired message
                break;
            default:
                // todo error message
                break;
        }

    }

    private LoginStatus userLoggedInStatus() {
        // if the user has logged in before, they will have an auth key
        // todo check for auth key with database

        // if the user's key is valid then they can log in
        // todo return user log in

        // if the user's key is invalid then the session has expired
        // todo return expired session

        // otherwise the user has no auth key and therefore has not logged in before
        return LoginStatus.NotLoggedIn;
    }

    enum LoginStatus {
        LoggedIn,
        NotLoggedIn,
        SessionExpired
    }


}