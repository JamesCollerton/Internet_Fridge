package com.example.jamescollerton.internet_fridge;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JamesCollerton on 06/03/2016.
 *
 * This class handles calls to the API written in Node.js. It is async so will need to be handled
 * with callbacks in order to get the returned string. When executed it will call doInBackground,
 * then onPostExecute.
 *
 */
public class MyFridgeAPIConnection extends AsyncTask<String, String, String> {

    /**
     *
     * This is the async task that is called by default with MyFridgeAPIConnection.execute(). It
     * takes the first parameter that is fed in and then uses that to launch downloadContent.
     * downloadContent throws an IOException if it fails, which can be caught and handled.
     *
     * @param params List of the parameters that are fed into the API call (URL)
     * @return String response from the API call.
     *
     * TODO: Replace the error string with something defined elsewhere.
     *
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            return downloadContent(params[0]);
        } catch (IOException e) {
            return "Unable to retrieve data. URL may be invalid.";
        }
    }

    /**
     *
     * At the minute this doesn't do anything, but later will be used to print something to do
     * with the call having finished.
     *
     * @param result Result parameter for use in logging.
     *
     * TODO: Decide on a postExecute function.
     */
    @Override
    protected void onPostExecute(String result) {

    }

    /**
     *
     * This is used to actually put calls to the API. Declares the InputStream, makes the connection
     * and opens it, sets timeouts and call type, then connects and gets the response code. Next
     * gets the input stream (the data). Finally converts the input stream to a string and returns
     * it. Later this will be deserialized into .JSON.
     *
     * @param APIURL The string that forms the API URL string.
     * @return String response from the API Call.
     * @throws IOException Can be caught when downloadContent is called with problems in API call.
     *
     * TODO: Change GET to an argument from the function call.
     * TODO: Do something with the response number.
     *
     */
    private String downloadContent(String APIURL) throws IOException {

        InputStream is = null;

        try {

            URL url = new URL(APIURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
//            int response = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = convertInputStreamToString(is);
            System.out.println(contentAsString);
            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     *
     * The API returns an InputStream which needs to be converted to a string. This takes the
     * string, reads it into a buffered reader, makes a string out of it and returns it.
     *
     * @param stream The input stream from the API that needs to be converted to a string.
     * @return The input stream from the API converted to string.
     * @throws IOException To be caught if the stream can't be converted.
     *
     */
    public String convertInputStreamToString(InputStream stream) throws IOException {

        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();

    }
}

