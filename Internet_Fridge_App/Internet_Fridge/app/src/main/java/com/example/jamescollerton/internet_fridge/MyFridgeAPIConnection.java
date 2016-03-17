package com.example.jamescollerton.internet_fridge;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by JamesCollerton on 06/03/2016.
 *
 * This class handles calls to the API written in Node.js. It is async so will need to be handled
 * with callbacks in order to get the returned string. When executed it will call doInBackground,
 * then onPostExecute.
 *
 */
public class MyFridgeAPIConnection extends AsyncTask<String, String, String> {

    HomeScreen parentScreen;

    public MyFridgeAPIConnection(HomeScreen parentScreen) {
        super();
        this.parentScreen = parentScreen;
        // do stuff
    }
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

            try
            {
                // Load CAs from an InputStream
                // (could be from a resource or ByteArrayInputStream or ...)
                CertificateFactory cf = CertificateFactory.getInstance("X.509");

                // My CRT file that I put in the assets folder
                // I got this file by following these steps:
                // * Go to https://littlesvr.ca using Firefox
                // * Click the padlock/More/Security/View Certificate/Details/Export
                // * Saved the file as littlesvr.crt (type X.509 Certificate (PEM))
                // The MainActivity.context is declared as:
                // public static Context context;
                // And initialized in MainActivity.onCreate() as:
                // MainActivity.context = getApplicationContext();
                InputStream caInput = new BufferedInputStream(parentScreen.getAssets().open("localhost.crt"));
                Certificate ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // Create an SSLContext that uses our TrustManager
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);

                // Tell the URLConnection to use a SocketFactory from our SSLContext
                URL url = new URL(APIURL);
                HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                conn.setSSLSocketFactory(context.getSocketFactory());
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();

                String contentAsString = convertInputStreamToString(is);
                System.out.println(contentAsString);

                return "Testing";
            }
            catch (Exception ex)
            {
                System.out.println("Here");
                System.out.println(ex.getMessage());
                return null;
            }

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

