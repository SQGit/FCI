/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.fciapp.fciscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import net.fciapp.fciscanner.camera.CameraSource;
import net.fciapp.fciscanner.camera.CameraSourcePreview;
import net.fciapp.fciscanner.camera.GraphicOverlay;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Activity for the multi-tracker app.  This app detects barcodes and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and ID of each barcode.
 */
public final class BarcodeCaptureActivity extends AppCompatActivity {
    // constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String BarcodeObject = "Barcode";
    protected static final long TIME_DELAY = 1000;
    private static final String TAG = "tagBarcode-reader";
    // intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    Typeface tf;
    EditText et_vinscan;
    Button btn_submit;
    //SweetAlertDialog sweetDialog;
    ProgressDialog mProgressDialog;
    DbHelper dbclass;
    Context context = this;
    Button btn_flash, btn_focus;
    String make, data, check, value, duplicate;
    int position, start, end,mva;
    String aaa;
    int i = 0, j = 0;
    boolean autoFocus, useFlash;
    Handler handler = new Handler();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean dup_sts;
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    String api_key;
    //the default update interval for your text, this is in your hand , just run this sample
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    Runnable updateTextRunnable = new Runnable() {
        public void run() {


            for (BarcodeGraphic graphic : mGraphicOverlay.getGraphics()) {
                Barcode barcode = graphic.getBarcode();
                //  Log.e("tag", "0grapickbarcode:" + barcode.displayValue);

                value = barcode.displayValue.trim();
                value = value.replaceAll("\\s+", "").trim();
                value = value.replaceAll("%", "");

                // Log.e("tag", "afterchang: :" + value);

                //  Log.e("tag", value.length() + "())" + value);
                int i;
                i = value.length();

                if (value.length() == 18) {
                    data = value.substring(1);
                    i = value.length();
                    Log.e("tag", i + "<0> " + data);


                    if (!dup_sts) {
                        et_vinscan.setText(data);
                        if (sharedPreferences.getString("edit_button", "").equals("off")) {
                            btn_submit.setVisibility(View.GONE);
                            new getVin_Make(data).execute();
                            Log.e("tag", i + "data(18) " + data);
                            dup_sts = true;
                        }
                        else{
                            btn_submit.setVisibility(View.VISIBLE);
                            if (sharedPreferences.getString("sound_option", "").equals("on")) {
                                MediaPlayer true_mp3 = null;
                                try {
                                    true_mp3 = MediaPlayer.create(BarcodeCaptureActivity.this, R.raw.ass);
                                    true_mp3.start();
                                } catch (Exception e) {
                                    Log.e("tag", "s:" + e.toString());
                                }

                            }
                            dup_sts = true;
                        }

                    }


                } else if (value.length() == 17) {
                    data = value;
                    Log.e("tag", i + "<1> " + data);


                    if (!dup_sts) {
                        et_vinscan.setText(data);
                        if (sharedPreferences.getString("edit_button", "").equals("off")) {
                            btn_submit.setVisibility(View.GONE);
                            new getVin_Make(data).execute();
                            Log.e("tag", i + "data(17) " + data);
                            dup_sts = true;
                        }
                        else{
                            btn_submit.setVisibility(View.VISIBLE);
                            if (sharedPreferences.getString("sound_option", "").equals("on")) {
                                MediaPlayer true_mp3 = null;
                                try {
                                    true_mp3 = MediaPlayer.create(BarcodeCaptureActivity.this, R.raw.ass);
                                    true_mp3.start();
                                } catch (Exception e) {
                                    Log.e("tag", "s:" + e.toString());
                                }

                            }
                            dup_sts = true;
                        }

                    }

                } else {

                    Log.e("tag", i + "<2> " + data);

                }


            }

            handler.postDelayed(this, TIME_DELAY);
        }
    };
    // helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.barcode_capture);

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) findViewById(R.id.graphicOverlay);

        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        et_vinscan = (EditText) findViewById(R.id.edit_text);
        btn_submit = (Button) findViewById(R.id.submit);
        btn_flash = (Button) findViewById(R.id.flash);
        btn_focus = (Button) findViewById(R.id.focus);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BarcodeCaptureActivity.this);

        // read parameters from the intent used to launch the activity.
        autoFocus = getIntent().getBooleanExtra(AutoFocus, false);
        useFlash = getIntent().getBooleanExtra(UseFlash, false);
        //  Log.e("tag", "" + autoFocus + useFlash);

        //  Log.e(TAG, "barcode oncreate");

        dbclass = new DbHelper(context);


        Intent asdf = getIntent();
        position = asdf.getIntExtra("pos", 0);
        start = asdf.getIntExtra("start", 0);
        end = asdf.getIntExtra("end", 0);
        mva = asdf.getIntExtra("mva", 0);
        Log.e("tag","sl: "+mva);
        check = asdf.getStringExtra("make");
        aaa = String.valueOf(position);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(true, useFlash);
            Log.e(TAG, "permission accepted");
        } else {
            requestCameraPermission();
        }

        et_vinscan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("tag",count+" before "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("tag",count+" textchange "+s);
                if(s.length() ==17){

                    new getVin_Make(s.toString()).execute();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("tag"," changed "+s);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_vinscan.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter Vin Number", Toast.LENGTH_LONG).show();
                } else if (et_vinscan.getText().toString().trim().length() < 17) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Vin Number", Toast.LENGTH_LONG).show();
                } else {
                    String vin = et_vinscan.getText().toString().toUpperCase().trim();
                    new getVin_Make(vin).execute();
                }
            }
        });

        btn_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!useFlash) {
                    useFlash = true;
                    mPreview.stop();
                    createCameraSource(autoFocus, useFlash);
                    startCameraSource();
                    btn_flash.setText("Flash Off");
                    i = 1;
                } else {

                    useFlash = false;
                    mPreview.stop();
                    createCameraSource(autoFocus, useFlash);
                    startCameraSource();
                    btn_flash.setText("Flash On");
                    i = 0;
                }
            }
        });


        btn_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoFocus == true) {
                    autoFocus = false;
                    mPreview.stop();
                    createCameraSource(autoFocus, useFlash);
                    startCameraSource();
                    btn_focus.setText("Focus On");
                    j = 1;
                } else {
                    autoFocus = true;
                    mPreview.stop();
                    createCameraSource(autoFocus, useFlash);
                    startCameraSource();
                    j = 0;
                    btn_focus.setText("Focus Off");
                }
            }
        });


        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        Snackbar.make(mGraphicOverlay, "Tap to capture. Pinch/Stretch to zoom",
                Snackbar.LENGTH_LONG)
                .show();


        for (BarcodeGraphic graphic : mGraphicOverlay.getGraphics()) {
            Barcode barcode = graphic.getBarcode();
            Log.e("tag", "000asdfioe:" + barcode.displayValue);
        }


        handler.post(updateTextRunnable);


    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        //  Log.e(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean b = scaleGestureDetector.onTouchEvent(e);

        boolean c = gestureDetector.onTouchEvent(e);

        return b || c || super.onTouchEvent(e);
    }

    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        Log.e(TAG, "create Camera Source");

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {

            Log.e(TAG, "Detector dependencies are not yet available.");

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.e(TAG, getString(R.string.low_storage_error));
            }
        } else {

            Log.e(TAG, "else barcode detector working");
        }


        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f);

        // make sure that auto focus is an available option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(
                    autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
        }

        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
        Log.e("tag", "resume barcode Source");
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("tag", "pause barcode Source");
        if (mPreview != null) {
            mPreview.stop();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("tag", "destroy barcode Source");
        if (mPreview != null) {
            mPreview.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus, false);
            boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.

        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        Log.e("tag", "start camera service");

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }


    private boolean onTap(float rawX, float rawY) {

        Log.e("tag", "click on tap");
        // Find tap point in preview frame coordinates.
        int[] location = new int[2];
        mGraphicOverlay.getLocationOnScreen(location);
        float x = (rawX - location[0]) / mGraphicOverlay.getWidthScaleFactor();
        float y = (rawY - location[1]) / mGraphicOverlay.getHeightScaleFactor();

        // Find the barcode whose center is closest to the tapped point.
        Barcode best = null;
        float bestDistance = Float.MAX_VALUE;
        for (BarcodeGraphic graphic : mGraphicOverlay.getGraphics()) {
            Barcode barcode = graphic.getBarcode();
            Log.e("tag", "0grapickbarcode:" + barcode.displayValue);
            if (barcode.getBoundingBox().contains((int) x, (int) y)) {
                // Exact hit, no need to keep looking.
                best = barcode;
                Log.e("tag", "1bestbarco:" + best.displayValue);
                break;
            }
            float dx = x - barcode.getBoundingBox().centerX();
            float dy = y - barcode.getBoundingBox().centerY();
            float distance = (dx * dx) + (dy * dy);  // actually squared distance
            if (distance < bestDistance) {
                best = barcode;
                Log.e("tag", "2bess barco:" + best.displayValue);
                bestDistance = distance;
            }
        }

        if (best != null) {

            Log.e("tag", "3.barcodevalu :" + best.displayValue);


            value = best.displayValue.trim();
            Log.e("tag", value.length() + "())" + value);
            int i;
            i = value.length();

            if (value.length() == 18) {
                data = value.substring(1);
                i = value.length();
                Log.e("tag", i + "<0> " + data);

                new getVin_Make(data).execute();

            } else if (value.length() == 17) {
                data = value;
                Log.e("tag", i + "<1> " + data);

                new getVin_Make(data).execute();

            } else {

                //  Toast.makeText(getApplicationContext(), "Please Adjust Your Camera and Try Again", Toast.LENGTH_LONG).show();
                Log.e("tag", i + "<2> " + data);

                Intent intent = getIntent();
                BarcodeCaptureActivity.this.finish();
                startActivity(intent);
            }


            return true;
        }

        return false;
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e("tag", "calling tap");
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
        }
    }

    class getVin_Make extends AsyncTask<String, Void, String> {
        String vin_no;
        int pos;

        //https://api.edmunds.com/api/vehicle/v2/vins/3G5DB03E32S518612?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9
        //https://api.edmunds.com/api/vehicle/v2/vins/2G1FC3D33C9165616?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9
        // String web_p1 = "https://api.edmunds.com/api/vehicle/v2/vins/";
        //String web_p2 = "?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9";

        String api_key = "bka9rsmkb8snppu7efzqtjfs";

        String web_p1 = "https://api.edmunds.com/api/v1/vehicle/vin/";
        String web_p2 = "/configuration?api_key=";
        String old_api = "26svh9z83ybumwkc3a45bkhu";

        public getVin_Make(String vinno) {

            this.vin_no = vinno;

        }

        protected void onPreExecute() {


            mProgressDialog = new ProgressDialog(BarcodeCaptureActivity.this);
            mProgressDialog.setTitle("Loading..");
            mProgressDialog.setMessage("Please wait");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            String kl = "https://api.edmunds.com/api/vehicle/v2/vins/2G1FC3D33C9165616?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9";

            //   https://api.edmunds.com/api/vehicle/v2/makes?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9
            try {
                //  vin_no = "2G1FC3D33C9165616";
                String virtual_url = web_p1 + vin_no + web_p2+api_key;
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
                mProgressDialog.dismiss();
            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String jsonStr) {

            //  Log.e("tag", "<-----outttttt---->" + jsonStr);
            mProgressDialog.dismiss();
            super.onPostExecute(jsonStr);
            Log.e("tag", "service: " + jsonStr);

            //  Toast.makeText(getApplicationContext(), jsonStr, Toast.LENGTH_LONG).show();
            try {
                JSONObject jo = new JSONObject(jsonStr);

                if (jo.has("make")) {
                    if (!(jo.getString("make").isEmpty())) {

                        if (sharedPreferences.getString("sound_option", "").equals("on")) {
                            MediaPlayer true_mp3 = null;
                            try {
                                true_mp3 = MediaPlayer.create(BarcodeCaptureActivity.this, R.raw.ass);
                                true_mp3.start();
                            } catch (Exception e) {
                                Log.e("tag", "s:" + e.toString());
                            }

                        }

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
                            dbclass.updateIntoDB(position, vin_no, name, String.valueOf(start), String.valueOf(end), "1/2", "1/2",String.valueOf(mva));
                        } else {
                            dbclass.insertIntoDB(position, vin_no, name, String.valueOf(start), String.valueOf(end), "1/2", "1/2",String.valueOf(mva));
                        }


                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("make", name);
                        editor.putString("vv_make" + position, name);
                        editor.commit();



/*
                        Intent i = new Intent(getApplicationContext(), __Staff.class);
                        startActivity(i);*/

                        BarcodeCaptureActivity.this.finish();

                    }
                } else {


                    if (sharedPreferences.getString("sound_option", "").equals("on")) {
                        MediaPlayer false_mp3 = null;
                        try {
                            false_mp3 = MediaPlayer.create(BarcodeCaptureActivity.this, R.raw.off);
                            false_mp3.start();
                        } catch (Exception e) {
                            Log.e("tag", "s:" + e.toString());
                        }

                    }


                    if (jo.has("message")) {
                        if (jo.getString("message").contains("Information not found for this Squish VIN")) {
                            //  stopScan();
                            Toast.makeText(getApplicationContext(), "Information Not Found on Our Database.Please Try Again.", Toast.LENGTH_LONG).show();
                            BarcodeCaptureActivity.this.finish();
                        } else {
                            //stopScan();
                            BarcodeCaptureActivity.this.finish();
                            Toast.makeText(getApplicationContext(), "Incorrect VIN Number", Toast.LENGTH_LONG).show();
                        }
                    } else {

                        //stopScan();

                        BarcodeCaptureActivity.this.finish();

                        Toast.makeText(getApplicationContext(), "Incorrect VIN Number", Toast.LENGTH_LONG).show();
                    }
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                BarcodeCaptureActivity.this.finish();

            }

        }

    }


}
