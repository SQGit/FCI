package net.fciapp.fciscanner;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;


@SuppressWarnings("deprecation")
public class PostService {

    public static final String TAG = "tagH";


    public static String makeRequest(String url, String json) {
        Log.e(TAG, "URL-->" + url);
        Log.e(TAG, "input-->" + json);
        InputStream is = null;

        try {
            Log.e(TAG, "inside-->");

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("apikey", "1eo7u4tig9704k2humvdywwnb4hnl2xa1jbrh7go");

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);

            Log.e("tag_", "stsL_" + httpResponse.getStatusLine());
            Log.e("tag_", "stsL_" + httpResponse.getStatusLine().getReasonPhrase());
            Log.e("tag_", "stsL_" + httpResponse.getStatusLine().getStatusCode());


            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            } else {
                Log.e(TAG, "output-->" + inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static JSONObject getStaffs(String url) throws JSONException {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;
        Log.e("tag_", "started");
        try {

            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("apikey", "1eo7u4tig9704k2humvdywwnb4hnl2xa1jbrh7go");

            DefaultHttpClient client = new DefaultHttpClient();


            HttpResponse response = null;
            String text;
            try {
                response = client.execute(httppost);

                Log.e("tag_", "stsL_" + response.getStatusLine());
                Log.e("tag_", "stsL_" + response.getStatusLine().getReasonPhrase());
                Log.e("tag_", "stsL_" + response.getStatusLine().getStatusCode());
            } catch (IOException e) {
                Log.e("INFO", e.getMessage());
            }


            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("tag_", "stsL2_" + is);

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());

        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("tag_", "stsL20ifaft_" + result);
        } catch (Exception e) {
            Log.e("tag", "Error converting result " + e.toString());
            // result = "sam";
        }

        try {
            jArray = new JSONObject(result);
            Log.e("tag_", "stsL21if_" + jArray);
        } catch (JSONException e) {
            Log.e("tag0", result);
            Log.e("tag2", "Error parsing data " + e.toString());
        }
        return jArray;

    }




    public static JSONObject getData(String url) throws JSONException {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;

        try {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("apikey", "1eo7u4tig9704k2humvdywwnb4hnl2xa1jbrh7go");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());
            result = "sam";
            is = null;
            return jArray;

        }

        if (is.equals(null)) {

            result = "sam";
            jArray = new JSONObject(result);
            return jArray;

        } else {


            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("tag", "Error converting result " + e.toString());
                result = "sam";
            }

            try {

                jArray = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("tag", result);
                Log.e("tag", jArray.toString());
                Log.e("tag", "Error parsing data " + e.toString());


            }

            return jArray;
        }

    }


    public static JSONObject getVin(String url) throws JSONException {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;

        // Download JSON data from URL
        try {


            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            //  HttpPost httppost = new HttpPost(url);
            httpGet.setHeader("X-Originating-Ip", "103.48.181.209");
            httpGet.setHeader("Accept", "application/json");
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());
            result = "sam";
            is = null;

        }


        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("tag", "Error converting result " + e.toString());
            result = "sam";
        }

        try {

            jArray = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("tag", result);
            Log.e("tag", jArray.toString());
            Log.e("tag", "Error parsing data " + e.toString());


        }

        return jArray;


    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Header adding multiple parameter>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        System.out.println(" OUTPUT -->" + result);


        return result;

    }
}
