package uk.ac.abertay.notsnapchat;

import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
            signUp();
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

    private String getEmailDebug() {
        return "test@test.com";
    }

    private String getPasswordDebug() {
        return "test";
    }

    private String getEmail() {
        return ((EditText) findViewById(R.id.txtEmail)).getText().toString();
    }

    private String getPassword() {
        return ((EditText) findViewById(R.id.txtPassword)).getText().toString();
    }

    private void signUp() {
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

        showToast("Registering with " + getEmail() + "/" + getPassword());
        // todo try sign up
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
        success = true;
        if (!success) {
            showToast(errors);
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        Response.Listener<String> postSuccess = response -> {
            JSONObject jResponse;
            try {
                jResponse = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            try {
                int status = jResponse.getInt("status");
                switch (status) {
                    case 200:
                        // success
                        showToast("success");
                        break;
                    case 400:
                        // error with the inputs
                        showToast("There is an issue with your email/password!");
                        return;
                    case 500:
                        showToast("There is an issue with our server!");
                        return;
                    default:
                        // todo error handling
                        showToast("Something unexpected happened!");
                        return;
                }

                JSONObject responseData = jResponse.getJSONObject("response");
                status = responseData.getInt("status");

                switch (status) {
                    case 200:
                        // success
                        break;
                    case 404:
                        // invalid u/p combo
                        showToast("We do not recognise that username/password combination!");
                        return;
                    default:
                        showToast("Something unexpected happened!");
                        return;
                }

                // get user id
                responseData = responseData.getJSONObject("data");

                // todo save user locally

                startActivity(new Intent(this, MainActivity.class));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        Response.ErrorListener postError = error -> {
            showToast(error.toString());
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ExternalResources.loginUser, postSuccess, postError) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(POST_EMAIL, getEmailDebug());
                params.put(POST_PASSWORD, getPasswordDebug());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}
