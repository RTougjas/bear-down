package ee.steffi.beardown.model;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import ee.steffi.beardown.R;
import ee.steffi.beardown.view.MainActivity;

/**
 * Created by rain on 4/26/16.
 */
public class ClientTask extends AsyncTask<ValueObject, Void, Void> {

    private HttpURLConnection urlConnection;
    private URL url;

    public ClientTask() throws IOException {
        super();
    }

    @Override
    protected Void doInBackground(ValueObject... params) {

        if(params.length == 1) {
            this.url = params[0].getURL();

            try {

                JSONObject json = new JSONObject();

                json.put("correctCode", params[0].getCorrectCode());
                json.put("wrong_attempts", params[0].getWrongAttempts());
                json.put("type", params[0].getType());
                json.put("time", params[0].getTime());
                json.put("pad", params[0].getPadStatus());

                String message = json.toString();

                this.urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setFixedLengthStreamingMode(json.toString().length());

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(message.getBytes());

                out.flush();
                out.close();

                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                while ((line = rd.readLine()) != null) {
                    System.out.println(line);

                }
                rd.close();

                //params[0].reset();

                if(urlConnection.getResponseCode() == 200) {

                    params[0].reset(ValueObject.STATUS_SUCCESS);

                }
                else {

                    params[0].reset(ValueObject.STATUS_FAIL);

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

        }

        return null;
    }
}
