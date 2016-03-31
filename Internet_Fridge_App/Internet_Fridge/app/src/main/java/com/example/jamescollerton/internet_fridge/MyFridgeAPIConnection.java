package com.example.jamescollerton.internet_fridge;

import android.app.Activity;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * MyFridgeAPIConnection
 *
 * This class handles calls to the API written in Node.js. It is async so will need to be handled
 * with callbacks in order to get the returned string. When executed it will call doInBackground,
 * then onPostExecute.
 *
 */
public class MyFridgeAPIConnection extends AsyncTask<String, String, String> {

    /**
     *
     * Parent screen is used so that we know which activity the API connection is called from.
     * Delegate is used along with the AsyncResponse interface in order to make the user wait for the
     * API to return success or failure and to continue.
     *
     */
    private Activity parentScreen;
    public AsyncResponse delegate = null;

    /**
     *
     * This is used in order to return something from the async task so we know if the API
     * connection has been successful or not.
     *
     */
    public interface AsyncResponse {
        void processFinish(String output);
    }

    /**
     *
     * This is the constructor for the API connection. It takes in the parent screen the connection
     * was launched from, and also the delegation interface. This is so we can take in a callback
     * argument and so some stuff with the result of the API connection.
     *
     * @param parentScreen The activity the API connection was launched from.
     * @param delegate So we can use a callback function and do something with the response.
     *
     */
    public MyFridgeAPIConnection(Activity parentScreen, AsyncResponse delegate) {

        super();
        this.parentScreen = parentScreen;
        this.delegate = delegate;

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
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            return downloadContent(params[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return parentScreen.getResources().getString(R.string.errorMessageAPIConnectionRetrieveURLFailed);
        }
    }

    /**
     *
     * This is used to pass the output of the doInBackground function to the AsyncResponse interface
     * so that we can have it returned in the callback.
     *
     * @param result The string returned from downloadContent.
     *
     */
    @Override
    protected void onPostExecute(String result) {

        delegate.processFinish(result);

    }

    /**
     *
     * This is used to actually put calls to the API. Finds the certificate to match to the incoming
     * HTTPS connection, then creates a keystore to add the certificate to, makes a trust manager
     * factory entry to add the certificate to. Finally creates an SSL context to add the certificate
     * to to use in the HTTPS connection.
     *
     * Next it creates the URL from the input string, adds the SSL context to use the certificate and
     * puts the request type. Finally connects, takes in the input stream and converts it to a string.
     *
     * @param APIURL The string that forms the API URL string.
     * @return String response from the API Call.
     * @throws IOException Can be caught when downloadContent is called with problems in API call.
     *
     * TODO: Change GET to an argument from the function call.
     * TODO: Do something with the response number.
     *
     */
    public String downloadContent(String APIURL) throws IOException {

        InputStream is = null;
        String contentAsString = null;

        try {

            try
            {

                System.setProperty("jsse.enableSNIExtension", "false");
                Certificate ca = createServerCertificate();
                KeyStore keyStore = createKeyStore(ca);
                TrustManagerFactory tmf = createTrustManagerFactory(keyStore);
                SSLContext context = createSSLContext(tmf);
                URL url = new URL(APIURL);

                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setSSLSocketFactory(context.getSocketFactory());
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                is = urlConnection.getInputStream();
                contentAsString = convertInputStreamToString(is);
                System.out.println(contentAsString);

            }
            catch (Exception ex)
            {
                ErrorDialog errorDialog = new ErrorDialog(parentScreen, parentScreen.getResources().getString(R.string.errorMessageAPIConnectionAPIConnectionFailed));
            }

        } finally {
            if (is != null) {
                is.close();
            }
        }

        return(contentAsString);
    }

    /**
     *
     * This is used to create the certificate for the server. It opens up the certificate from the
     * assets folder, reads it in, creates a certificate inside the program and returns it.
     *
     * @return Certificate generated from the certificate file in assets.
     */
    private Certificate createServerCertificate() {

        Certificate ca = null;

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(parentScreen.getAssets().open("localhost.crt"));
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }
        } catch (Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(parentScreen, parentScreen.getResources().getString(R.string.errorMessageAPIConnectionOpenCertificateFailed));
        }

        return(ca);
    }

    /**
     *
     * This is used to create the KeyStore with the certificate added in. It takes in the certificate
     * as an argument, then opens up a keyStore and adds the certificate.
     *
     * @param ca The certificate read in from the assets folder.
     * @return The keystore with the added certificate.
     *
     */
    private KeyStore createKeyStore(Certificate ca){

        KeyStore keyStore = null;

        try {
            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
        } catch(Exception e){
            ErrorDialog errorDialog = new ErrorDialog(parentScreen, parentScreen.getResources().getString(R.string.errorMessageAPIConnectionKeystoreInitialisationFailed));
        }

        return(keyStore);

    }

    /**
     *
     * Used to create a trust manager with the Keystore in. The trust manager is then set in the
     * SSL context of the HTTPS connection, which means the certificate can be trusted.
     *
     * @param keyStore Keystore with the certificate in.
     * @return TrustManagerFactory with the Keystore in.
     *
     */
    private TrustManagerFactory createTrustManagerFactory(KeyStore keyStore){

        TrustManagerFactory tmf = null;

        try {
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        } catch (Exception e){
            ErrorDialog errorDialog = new ErrorDialog(parentScreen, parentScreen.getResources().getString(R.string.errorMessageAPIConnectionTrustManagerFactoryInitialisationFailed));
        }

        return(tmf);
    }

    /**
     *
     * This creates the SSLContext to be used with the URL connection. The context is created and
     * then has the TrustManagerFactory added to it. The allHostsValid function is so that the
     * certificate name doesn't need to match the connection name (which is super dangerous) but is
     * fine for internal testing. Finally the context is installed into the connection.
     *
     * @param tmf The trust manager factory with the certificate added in.
     * @return The SSL context with the trust manager factory with accepted certificate in.
     *
     */
    private SSLContext createSSLContext(TrustManagerFactory tmf){

        SSLContext context = null;

        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch(Exception e){
            ErrorDialog errorDialog = new ErrorDialog(parentScreen, parentScreen.getResources().getString(R.string.errorMessageAPIConnectionSSLContextInitialisationFailed));
        }

        return(context);
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

