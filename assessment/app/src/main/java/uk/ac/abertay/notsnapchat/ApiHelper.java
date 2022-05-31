package uk.ac.abertay.notsnapchat;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ApiHelper {

    public static final String DATA_KEY_EMAIL = "email";
    public static final String DATA_KEY_USERNAME = "username";
    public static final String DATA_KEY_PASSWORD = "password";

    private final RequestQueue requestQueue;
    private final Response.Listener<String> onSuccessCallback;
    private final Response.ErrorListener onErrorCallback;
    private final int requestType;
    private final String url;
    private final Map<String, String> data;

    /**
     * @param context - this
     * @param requestType - Request.Method.GET/POST/etc.
     * @param url - Absolute URL of API endpoint
     * @param data - Data to send as a Map
     * @param successCallback - Function to call on success
     * @param errorCallback - Function to call on failure
     */
    public ApiHelper(Context context, int requestType, String url, Map<String, String> data, Response.Listener<String> successCallback, Response.ErrorListener errorCallback) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestType = requestType;
        this.url = url;
        this.data = data;
        this.onSuccessCallback = successCallback;
        this.onErrorCallback = errorCallback;
    }

    public void executeAsync() {

        StringRequest stringRequest = new StringRequest(requestType, url, onSuccessCallback, onErrorCallback) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                for (String key : data.keySet()) {
                    String value = data.get(key);
                    params.put(key, value);
                }

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
