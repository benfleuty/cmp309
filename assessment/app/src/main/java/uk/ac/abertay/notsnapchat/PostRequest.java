package uk.ac.abertay.notsnapchat;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PostRequest {
    HttpURLConnection connection;
    private String Url;
    private ArrayList<RestfulDataObject> RequestProperties;

    public PostRequest(String url, RestfulDataObject requestProperty) {
        Url = url;
        RequestProperties.add(requestProperty);
    }

    public PostRequest(String url, ArrayList<RestfulDataObject> requestProperties) {
        Url = url;
        RequestProperties = requestProperties;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public ArrayList<RestfulDataObject> getRequestProperties() {
        return RequestProperties;
    }

    public void setRequestProperties(ArrayList<RestfulDataObject> requestProperties) {
        RequestProperties = requestProperties;
    }

    public void addRequestProperty(String key, Object value) {
        RequestProperties.add(new RestfulDataObject(key, value));
    }

    public void Send() {
        try {
            URL url = new URL(this.Url);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            // set method
            client.setRequestMethod("POST");

            // add key value pairs
            for (int i = 0; i < RequestProperties.size(); i++) {
                RestfulDataObject temp = RequestProperties.get(i);
                client.addRequestProperty(temp.getKey(), temp.getValueAsString());
            }

            client.setDoInput(true);

            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
