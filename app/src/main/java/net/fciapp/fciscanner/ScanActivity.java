package net.fciapp.fciscanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixotech.android.scanner.library.ScannerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ScanActivity extends ScannerActivity {

    String aaa;
    int position, start, end;
    String make, data, check;
    DbHelper dbclass;
    Context context = this;
    int i;
    EditText et_vinscan;
    Button btn_submit;
    LinearLayout lt;
    SweetAlertDialog sweetDialog;
    Typeface tf;
    private TextView vinTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addLayout(R.layout.activity_scan);
        vinTextView = (TextView) findViewById(R.id.vinTextView);

        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        et_vinscan = (EditText) findViewById(R.id.edit_text);
        btn_submit = (Button) findViewById(R.id.submit);

        et_vinscan.setTypeface(tf);
        btn_submit.setTypeface(tf);

        lt = (LinearLayout) findViewById(R.id.layout);
        lt.setVisibility(View.GONE);

        Intent asdf = getIntent();
        position = asdf.getIntExtra("pos", 0);
        start = asdf.getIntExtra("start", 0);
        end = asdf.getIntExtra("end", 0);
        check = asdf.getStringExtra("make");
        aaa = String.valueOf(position);
        dbclass = new DbHelper(context);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_vinscan.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Vin Number", Toast.LENGTH_LONG).show();
                } else if (et_vinscan.getText().length() < 17) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Vin Number", Toast.LENGTH_LONG).show();
                } else {
                    String vin = et_vinscan.getText().toString().toUpperCase();
                    new getVin_Make(vin).execute();
                }

            }
        });

        lt.setVisibility(View.VISIBLE);




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("tag_", "scan_ondestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("tag_", "scan_onstop");
    }

    @Override
    public void handleDecode(String s) {
//        vibrate();
        //  vinTextView.setText(s);
        Log.e("tag", "" + s);
        // startScan();


        if (!(s.equals(null))) {

     /*       data = s;

            if (s.length() == 18) {
                data = data.substring(1);
                i = data.length();
                Log.e("tag", i + "<0> " + data);

                new getVin_Make(data).execute();

            } else if (i == 17) {
                data = s;
                Log.e("tag", i + "<1> " + data);

                new getVin_Make(data).execute();

            } else {

                Toast.makeText(getApplicationContext(), "Please Adjust Your Camera and Try Again", Toast.LENGTH_LONG).show();
                Log.e("tag", i + "<2> " + data);

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }*/


            //finish();


            new getVin_Make(s).execute();


            StaffAddEntry staff = new StaffAddEntry();
            staff.vin_make.put(position, s);
            staff.v_pos.add(position);
            staff.v_mk.add(s);


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("data", s);
            editor.putString("pos", aaa);
            editor.putString("make", make);
            editor.putString("vv_vin" + position, s);
            editor.commit();


        }

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("tag", "Permission is granted");
                return true;
            } else {

                Log.e("tag", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("tag", "Permission is granted");
            return true;
        }


    }

    class getVin_Make extends AsyncTask<String, Void, String> {
        String vin_no;
        int pos;

        //https://api.edmunds.com/api/vehicle/v2/vins/3G5DB03E32S518612?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9
        //https://api.edmunds.com/api/vehicle/v2/vins/2G1FC3D33C9165616?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9
        String web_p1 = "https://api.edmunds.com/api/vehicle/v2/vins/";
        String web_p2 = "?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9";

        public getVin_Make(String vinno) {

            this.vin_no = vinno;

        }

        protected void onPreExecute() {
            super.onPreExecute();
            // mScannerView.stopCamera();

            sweetDialog = new SweetAlertDialog(ScanActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#5DB2EF"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();

        }

        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String kl = "https://api.edmunds.com/api/vehicle/v2/vins/2G1FC3D33C9165616?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9";

            //   https://api.edmunds.com/api/vehicle/v2/makes?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9
            try {
                //  vin_no = "2G1FC3D33C9165616";
                String virtual_url = web_p1 + vin_no + web_p2;
                Log.e("tag", "<---urlll--->" + vin_no);
                Log.e("tag", "<---urlll--->" + virtual_url);
                JSONObject jsonobject = PostService.getVin(virtual_url);
                Log.d("tag", "" + jsonobject.toString());
                if (jsonobject.toString() == "sam") {
                    new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.WARNING_TYPE)
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
                sweetDialog.dismiss();
            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String jsonStr) {

            Log.e("tag", "<-----outttttt---->" + jsonStr);
            sweetDialog.dismiss();
            super.onPostExecute(jsonStr);
            Log.e("tag", "service: " + jsonStr);
            stopScan();
            //  Toast.makeText(getApplicationContext(), jsonStr, Toast.LENGTH_LONG).show();
            try {
                JSONObject jo = new JSONObject(jsonStr);

                if (jo.has("make")) {
                    if (!(jo.getString("make").isEmpty())) {
                        Log.e("tag", "<---1111--->");
                        // stopScan();
                        String msg = jo.getString("make");
                        String msg1 = jo.getString("model");
                        Log.e("tag", "<>" + msg + msg1);
                        JSONObject data = new JSONObject(msg);
                        String name = data.getString("name");

                        JSONObject data1 = new JSONObject(msg1);
                        String name1 = data1.getString("name");

                        Log.e("tag", "<>" + name + vin_no + name1);

                        name = name + " / " + name1;

                        Log.e("tag___", "<check>" + check);


                        if (check.length() > 0) {
                            dbclass.updateIntoDB(position, vin_no, name, String.valueOf(start), String.valueOf(end), "1/2", "1/2");
                        } else {
                            dbclass.insertIntoDB(position, vin_no, name, String.valueOf(start), String.valueOf(end), "1/2", "1/2");
                        }


                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("make", name);
                        editor.putString("vv_make" + position, name);
                        editor.commit();



/*
                        Intent i = new Intent(getApplicationContext(), __Staff.class);
                        startActivity(i);*/
                        ScanActivity.this.finish();

                    }
                } else {
                    if (jo.has("message")) {
                        if (jo.getString("message").contains("Information not found for this Squish VIN")) {
                            //  stopScan();
                            Toast.makeText(getApplicationContext(), "Information Not Found on Our Database.Please Try Again.", Toast.LENGTH_LONG).show();
                            ScanActivity.this.finish();
                        } else {
                            //stopScan();
                            ScanActivity.this.finish();
                            Toast.makeText(getApplicationContext(), "Incorrect VIN Number", Toast.LENGTH_LONG).show();
                        }
                    } else {

                        //stopScan();
                        ScanActivity.this.finish();
                        Toast.makeText(getApplicationContext(), "Incorrect VIN Number", Toast.LENGTH_LONG).show();
                    }
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ScanActivity.this.finish();

            }

        }

    }


}
