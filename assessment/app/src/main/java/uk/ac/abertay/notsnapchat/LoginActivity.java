package uk.ac.abertay.notsnapchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private final ArrayList<String> errors = new ArrayList<>();
    Toast currentToast;
    Toast errorToast;

    private void showToast(String text) {
        currentToast.cancel();
        currentToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
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

        btnSignIn.setOnClickListener(view -> signUp());
        btnLogin.setOnClickListener(view -> login());
    }

    private boolean isLoginInformationValid() {
        errors.clear();

        if (errorToast != null)
            errorToast.cancel();

        boolean isValid;

        // check email validity
        isValid = isEmailValid();

        // check password validity
        isValid = isPasswordValid() && isValid;

        return isValid;
    }

    private boolean isEmailValid() {
        String strEmail = getEmail();
        boolean result = !TextUtils.isEmpty(strEmail) && Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
        if (result) return true;
        errors.add(getResources().getString(R.string.invalid_email));
        return false;
    }

    private boolean isPasswordValid() {
        String strPassword = getPassword();
        boolean isEmpty = TextUtils.isEmpty(strPassword);
        if (isEmpty)
            errors.add(getResources().getString(R.string.invalid_password));

        return !isEmpty;
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
        boolean success = isLoginInformationValid();
        if (checkLoginFailure(success)) return;

        showToast("Registering with " + getEmail() + "/" + getPassword());
        // todo try sign up
    }

    private boolean checkLoginFailure(boolean success) {
        if (!success) {
            StringBuilder outputBuilder = new StringBuilder();
            for (String error : errors) {
                outputBuilder.append(error).append("\n");
            }
            errorToast = Toast.makeText(this, outputBuilder.toString().trim(), Toast.LENGTH_SHORT);
            errorToast.show();
            return true;
        }

        return false;
    }

    private void login() {
        boolean success = isLoginInformationValid();

        if (Debug.isDebuggerConnected())
            success = true;

        if (checkLoginFailure(success)) return;


        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        Response.Listener<String> onLoginPOSTSuccess = response -> {
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
                        showToast("We do not recognise that username/password combo!");
                        return;
                    default:
                        showToast("Something unexpected happened!");
                        return;
                }

                // get user from data in from API
                responseData = responseData.getJSONObject("data");
                JSONObject user = responseData.getJSONObject("user");

                // send user data to next activity
                Bundle bundleUserData = new Bundle();

                bundleUserData.putInt("id", user.getInt("id"));
                bundleUserData.putString("email", user.getString("email"));
                bundleUserData.putString("username", user.getString("username"));

                Intent intentMain = new Intent(this, MainActivity.class);
                intentMain.putExtra("user", bundleUserData);
                startActivity(intentMain);

                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        Response.ErrorListener onLoginPOSTError = error -> showToast(error.toString());

        Map<String, String> data = new HashMap<>();

        if (Debug.isDebuggerConnected()) {
            data.put(ApiHelper.DATA_KEY_EMAIL, getEmailDebug());
            data.put(ApiHelper.DATA_KEY_PASSWORD, getPasswordDebug());
        } else {
            data.put(ApiHelper.DATA_KEY_EMAIL, getEmail());
            data.put(ApiHelper.DATA_KEY_PASSWORD, getPassword());
        }

        ApiHelper postTryLogin = new ApiHelper(
                this,
                Request.Method.POST,
                ExternalResources.loginUserURL,
                data,
                onLoginPOSTSuccess,
                onLoginPOSTError
        );

        postTryLogin.executeAsync();
    }

}
