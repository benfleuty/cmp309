package uk.ac.abertay.notsnapchat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    final String POST_EMAIL = "email";
    final String POST_PASSWORD = "password";
    final String POST_USERNAME = "username";

    Toast currentToast;

    private void showToast(String text){
        currentToast.cancel();
        currentToast = Toast.makeText(this,text,Toast.LENGTH_LONG);
        currentToast.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createButtonListeners();
        currentToast = new Toast(this);
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
            showToast(errors);
            return;
        }

        showToast("Registering in with " + getEmail() + "/" + getPassword());
        // todo try sign in
    }

    private void login() {
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
            showToast(errors);
            return;
        }

        //showToast("Logging in with " + getEmail() + "/" + getPassword());

        // todo try log in

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ExternalResources.loginUser,
                response -> Toast.makeText(LoginActivity.this, response.trim(), Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(POST_EMAIL, getEmail());
                params.put(POST_PASSWORD, getPassword());
                return params;
            }
        };

        requestQueue.add(stringRequest);

        return;
//
//        RestfulDataObject email = new RestfulDataObject("email", getEmail());
//        RestfulDataObject password = new RestfulDataObject("password", getPassword());
//
//        ArrayList<RestfulDataObject> data = new ArrayList<>(email, password);
//
//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme("https")
//                .authority(ExternalResources.loginUser);
//
//        PostRequest postRequest = new PostRequest(ExternalResources.loginUser, data);
//        postRequest.Send();
    }

}
