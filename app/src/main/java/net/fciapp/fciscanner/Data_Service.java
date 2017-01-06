package net.fciapp.fciscanner;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Salman on 6/21/2016.
 */
public class Data_Service {

   // static final String SERVICE_URL = "http://fci.sqindia.net/";
   // static final String SERVICE_URL_NEW = "http://androidtesting.newlogics.in/";


    static final String SERVICE_URL_NEW = "http://ec2-54-174-246-193.compute-1.amazonaws.com/";


    public static boolean isNetworkAvailable(Context c1) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c1.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
