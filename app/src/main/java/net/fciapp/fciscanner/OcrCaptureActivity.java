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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import net.fciapp.fciscanner.ocr_camera.CameraSource1;
import net.fciapp.fciscanner.ocr_camera.CameraSourcePreview1;
import net.fciapp.fciscanner.ocr_camera.GraphicOverlay1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Activity for the multi-tracker app.  This app detects text and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and contents of each TextBlock.
 */
public final class OcrCaptureActivity extends AppCompatActivity {
    private static final String TAG = "OcrCaptureActivity";

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    protected static final long TIME_DELAY = 1000;

    // Constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String TextBlockObject = "String";

    private CameraSource1 mCameraSource;
    private CameraSourcePreview1 mPreview;
    private GraphicOverlay1<OcrGraphic> mGraphicOverlay;

    // Helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    Typeface tf;
    EditText et_vinscan;
    Button btn_submit;
    DbHelper dbclass;
    Context context = this;

    Handler handler=new Handler();

    String make, data, check,value;
    int position, start, end;
    String aaa;
    ProgressDialog mProgressDialog;

    Button btn_flash,btn_focus;
    int i =0,j=0;
    boolean autoFocus,useFlash;



    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.vin_capture);

        mPreview = (CameraSourcePreview1) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay1<OcrGraphic>) findViewById(R.id.graphicOverlay);


        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        et_vinscan = (EditText) findViewById(R.id.edit_text);
        btn_submit = (Button) findViewById(R.id.submit);

        btn_flash = (Button) findViewById(R.id.flash);
        btn_focus = (Button) findViewById(R.id.focus);


        Intent asdf = getIntent();
        position = asdf.getIntExtra("pos", 0);
        start = asdf.getIntExtra("start", 0);
        end = asdf.getIntExtra("end", 0);
        check = asdf.getStringExtra("make");
        aaa = String.valueOf(position);

        autoFocus = getIntent().getBooleanExtra(AutoFocus, false);
        useFlash = getIntent().getBooleanExtra(UseFlash, false);

        dbclass = new DbHelper(context);

        // read parameters from the intent used to launch the activity.
        // autoFocus = true;//getIntent().getBooleanExtra(AutoFocus, false);
        // useFlash = false;//getIntent().getBooleanExtra(UseFlash, false);

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }

        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        Snackbar.make(mGraphicOverlay, "Tap to capture. Pinch/Stretch to zoom",
                Snackbar.LENGTH_LONG)
                .show();



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



        btn_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(useFlash == false) {

                    useFlash = true;
                    mPreview.stop();
                    createCameraSource(autoFocus, useFlash);
                    startCameraSource();
                    btn_flash.setText("Flash Off");
                    i =1;
                }
                else{

                    useFlash = false;

                    mPreview.stop();
                    createCameraSource(autoFocus, useFlash);
                    startCameraSource();
                    btn_flash.setText("Flash On");
                    i =0;
                }
            }
        });


        btn_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoFocus == true) {
                    autoFocus = false;
                    mPreview.stop();
                    createCameraSource(autoFocus, useFlash);
                    startCameraSource();
                    btn_focus.setText("Focus On");
                    j =1;
                }
                else{
                    autoFocus = true;
                    mPreview.stop();
                    createCameraSource(autoFocus, useFlash);
                    startCameraSource();
                    j =0;
                    btn_focus.setText("Focus Off");
                }
            }
        });


        handler.post(updateTextRunnable);



    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

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




    Runnable updateTextRunnable=new Runnable(){
        public void run() {

            //gestureDetector = new GestureDetector(getApplicationContext(), new CaptureGestureListener());

           // new CaptureGestureListener();

           /* x:996.2217
            10-15 16:55:01.189 28201-28201/com.sqi.fciapp E/tag: y:249.65326
*/

           // float ff = 999.2217f;
           // float ft = 245.65326f;



          //  boolean sad  = onTap(ff,ft);

           // Log.e("tag","bool:"+sad);



            //Toast.makeText(getApplicationContext(),"value"+value,Toast.LENGTH_LONG).show();

          //  Log.e("tag","vl:"+value);




            for (OcrGraphic graphic : mGraphicOverlay.getGraphics()) {

                TextBlock text = null;

                if (graphic != null) {
                    text = graphic.getTextBlock();
                    value = text.getValue().trim();

                    Log.e("tag","befor::"+value);

                    //value = value.replaceAll("\\s+","").trim();
                    //value = value.replaceAll("[\\w\\s\\-\\_\\<.*?>]","");

                    value = value.replaceAll("\\s+","").trim();
                    value = value.replaceAll("%","");
                    Log.e("tag","after::"+value);

                    if (!value.trim().isEmpty() &&  value.length() == 17 ) {
                        Log.e("tag","aaa::"+value);
                        et_vinscan.setText(value);
                    }
                }

            }

            handler.postDelayed(this, TIME_DELAY);
        }
    };



    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // A text recognizer is created to find text.  An associated processor instance
        // is set to receive the text recognition results and display graphics for each text block
        // on screen.
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        textRecognizer.setProcessor(new OcrDetectorProcessor(mGraphicOverlay));

        if (!textRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the text recognizer to detect small pieces of text.
        mCameraSource =
                new CameraSource1.Builder(getApplicationContext(), textRecognizer)
                .setFacing(CameraSource1.CAMERA_FACING_BACK)
                //.setRequestedPreviewSize(1280, 1024)
                .setRequestedPreviewSize(350, 350)
                .setRequestedFps(2.0f)
                      //  .setRequestedFps(15.0f)
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
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
            // We have permission, so create the camerasource
            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus,false);
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

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // Check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

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

    /**
     * onTap is called to capture the first TextBlock under the tap location and return it to
     * the Initializing Activity.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the activity is ending.
     */
    private boolean onTap(float rawX, float rawY) {

        Log.e("tag","x:"+rawX);
        Log.e("tag","y:"+rawY);

        OcrGraphic graphic = mGraphicOverlay.getGraphicAtLocation(rawX, rawY);


        TextBlock text = null;
        if (graphic != null) {
            text = graphic.getTextBlock();
            if (text != null && text.getValue() != null) {




                value = text.getValue().trim();
                Log.e("tag","val:"+value);

                Log.e("tag",value.length()+"())"+value);
                int i;
                i = value.length();

                if (value.length() == 18) {
                    data = value.substring(1);
                    i = value.length();
                    Log.e("tag", i + "<0> " + data);

                    et_vinscan.setText(data);

                } else if (value.length() == 17) {
                    data = value;
                    Log.e("tag", i + "<1> " + data);

                    et_vinscan.setText(data);

                } else {

                    Toast.makeText(getApplicationContext(), "Please Adjust Your Camera and Try Again", Toast.LENGTH_LONG).show();
                    Log.e("tag", i + "<2> " + data);

                }















                /*Intent data = new Intent();
                data.putExtra(TextBlockObject, text.getValue());
                setResult(CommonStatusCodes.SUCCESS, data);
                finish();*/
            }
            else {
                Log.d(TAG, "text data is null");
            }
        }
        else {
            Log.d(TAG,"no text detected");
        }
        return text != null;
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e("tag","beforeontap");
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
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
        String web_p1 = "https://api.edmunds.com/api/vehicle/v2/vins/";
        String web_p2 = "?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9";

        public getVin_Make(String vinno) {

            this.vin_no = vinno;

        }

        protected void onPreExecute() {


            mProgressDialog = new ProgressDialog(OcrCaptureActivity.this);
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
                mProgressDialog.dismiss();
            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String jsonStr) {

            Log.e("tag", "<-----outttttt---->" + jsonStr);
            mProgressDialog.dismiss();
            super.onPostExecute(jsonStr);
            Log.e("tag", "service: " + jsonStr);

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

                        OcrCaptureActivity.this.finish();

                    }
                } else {
                    if (jo.has("message")) {
                        if (jo.getString("message").contains("Information not found for this Squish VIN")) {
                            //  stopScan();
                            Toast.makeText(getApplicationContext(), "Information Not Found on Our Database.Please Try Again.", Toast.LENGTH_LONG).show();
                            OcrCaptureActivity.this.finish();
                        } else {
                            //stopScan();
                            OcrCaptureActivity.this.finish();
                            Toast.makeText(getApplicationContext(), "Incorrect VIN Number", Toast.LENGTH_LONG).show();
                        }
                    } else {

                        //stopScan();

                        OcrCaptureActivity.this.finish();

                        Toast.makeText(getApplicationContext(), "Incorrect VIN Number", Toast.LENGTH_LONG).show();
                    }
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                OcrCaptureActivity.this.finish();

            }

        }

    }






}
