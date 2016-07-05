package fci.com.fci;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarScan extends Activity implements ZXingScannerView.ResultHandler {
    Context context = this;
    String aaa;
    int position;
    String make;
    private ZXingScannerView mScannerView;
    public HashMap<Integer,String> vin_make = new HashMap<>();
    public ArrayList<Integer> v_pos = new ArrayList<>();
    public ArrayList<String> v_mk = new ArrayList<>();
    SweetAlertDialog sweetDialog;
    DbHelper dbclass;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content vie
        Intent asdf = getIntent();
        position = asdf.getIntExtra("pos", 0);
        aaa = String.valueOf(position);
        dbclass = new DbHelper(context);


    }


    @Override
    public void onResume() {
        super.onResume();
      // Register ourselves as a handler for scan results.
        if(isStoragePermissionGranted()) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
        else {
            mScannerView.setResultHandler(this);

            mScannerView.startCamera();

        }
        // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(Result rawResult) {
        Log.e("tag", rawResult.getText()); // Prints scan results
        Log.e("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        String dddd = rawResult.getText();
        new getVin_Make(dddd).execute();
        StaffAddEntry staff = new StaffAddEntry();
        staff.vin_make.put(position, rawResult.getText());
        staff.v_pos.add(position);
        staff.v_mk.add(rawResult.getText());
        if (!(rawResult.getText().isEmpty())) {
            int i = 0;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("data", rawResult.getText());
            editor.putString("pos", aaa);
            editor.putString("make", make);
            editor.putString("vv_vin"+position, rawResult.getText());
            editor.commit();
        }
    }


    class getVin_Make extends AsyncTask<String, Void, String> {

        String vin_no;
        int pos;

        //https://api.edmunds.com/api/vehicle/v2/vins/2G1FC3D33C9165616?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9

        String web_p1 = "https://api.edmunds.com/api/vehicle/v2/vins/";

        String web_p2 = "?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9";

        public getVin_Make(String vinno) {

            this.vin_no = vinno;
            // this.pos = pos;

        }

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";

            String  kl = "https://api.edmunds.com/api/vehicle/v2/vins/2G1FC3D33C9165616?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9";

            try {
              //  vin_no = "2G1FC3D33C9165616";
                String virtual_url = web_p1 + vin_no + web_p2;

                JSONObject jsonobject = PostService.getVin(kl);

                Log.d("tag", "" + jsonobject.toString());

                if (jsonobject.toString() == "sam") {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops!")
                            .setContentText("Try Check your Network")
                            .setConfirmText("OK")
                            .show();
                }

                json = jsonobject.toString();

                return json;
            } catch (Exception e) {
                Log.e("InputStream", "" + e.getLocalizedMessage());
                jsonStr = "";
            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----rerseres---->" + jsonStr);
            mScannerView.stopCamera();

            super.onPostExecute(jsonStr);
            try {
                JSONObject jo = new JSONObject(jsonStr);
                if (!(jo.getString("make").isEmpty())) {
                    String msg = jo.getString("make");
                    Log.e("tag", "<>" + msg);
                    JSONObject data = new JSONObject(msg);
                    String name = data.getString("name");
                    Log.e("tag", "<>" + name +vin_no+name);
                    dbclass.insertIntoDB(position,vin_no,name);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("make", name);
                    editor.putString("vv_make"+position,name);
                    editor.commit();
                    finish();


                } else if (!(jo.getString("make").isEmpty())) {
                    String status = jo.getString("status");
                    Log.e("tag", "<>" + status);
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }



    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("tag", "Permission is granted");
                return true;
            }
            else
            {

                Log.e("tag", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("tag", "Permission is granted");
            return true;
        }


    }



}