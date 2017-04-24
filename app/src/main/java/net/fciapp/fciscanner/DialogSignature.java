package net.fciapp.fciscanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Salman on 6/23/2016.
 */
public class DialogSignature extends Dialog {

    Activity activity;
    TextView tv_sign_hint, tv_clear, staff_tv, satffname_tv, form_tv, formno_tv, approve_tv, reject_tv;
    LinearLayout approve, reject;
    SignaturePad mSignaturePad;
    File photo;
    String staffname, s, newPath, formId, status;
    Typeface tf;
    SweetAlertDialog sweetDialog;

    public DialogSignature(Activity act) {
        super(act);
        this.activity = act;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign);


        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/asin.TTF");

        tv_sign_hint = (TextView) findViewById(R.id.tv_sign_hint);
        tv_clear = (TextView) findViewById(R.id.tv_sign_clear);
        staff_tv = (TextView) findViewById(R.id.staff);
        satffname_tv = (TextView) findViewById(R.id.staff_id);
        form_tv = (TextView) findViewById(R.id.form);
        formno_tv = (TextView) findViewById(R.id.form_id);
        approve_tv = (TextView) findViewById(R.id.approve_tv);
        reject_tv = (TextView) findViewById(R.id.reject_tv);
        approve = (LinearLayout) findViewById(R.id.approve_lv);
        reject = (LinearLayout) findViewById(R.id.reject_lv);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        formId = sharedPreferences.getString("formId", "");
        staffname = sharedPreferences.getString("staffname", "");
        satffname_tv.setText(staffname);
        formno_tv.setText(formId);

        tv_sign_hint.setTypeface(tf);
        tv_clear.setTypeface(tf);
        staff_tv.setTypeface(tf);
        satffname_tv.setTypeface(tf);
        form_tv.setTypeface(tf);
        formno_tv.setTypeface(tf);
        approve_tv.setTypeface(tf);
        reject_tv.setTypeface(tf);


        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mSignaturePad.isEmpty()) {
                    Toast.makeText(activity.getApplicationContext(), "Please Sign Before Approve", Toast.LENGTH_SHORT).show();
                } else {


                    Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                    if (addJpgSignatureToGallery(signatureBitmap)) {
                        newPath = String.valueOf(photo);
                        status = "APPROVED";
                        new UploadImageToServer(newPath).execute();
                        Log.e("tag", "sigpath" + photo);
                    } else {
                    }

                    Log.e("tag", "not empty");
                }

            }


        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSignaturePad.isEmpty()) {
                    Toast.makeText(activity.getApplicationContext(), "Please Sign Before Approve", Toast.LENGTH_SHORT).show();
                } else {

                    Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                    if (addJpgSignatureToGallery(signatureBitmap)) {
                        status = "REJECTED";
                        newPath = String.valueOf(photo);
                        new UploadImageToServer(newPath).execute();
                    } else {
                    }
                }

            }


        });

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                tv_sign_hint.setText("");
            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {

                tv_sign_hint.setText("Manager Sign");
            }
        });


        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                tv_sign_hint.setText("Manager Sign");
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }


    private boolean addSvgSignatureToGallery(String signatureSvg) {

        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    private boolean addJpgSignatureToGallery(Bitmap signatureBitmap) {

        boolean result = false;
        try {
            photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.JPG", System.currentTimeMillis()));
            saveBitmapToJPG(signatureBitmap, photo);
            scanMediaFile(photo);
            Log.e("tag", "%%%%" + photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private File getAlbumStorageDir(String signaturePad) {

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), signaturePad);
        Log.d("tag", "file" + file);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
            // file.mkdir();
        }
        return file;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        s = contentUri.toString();
        Log.e("tag", "contentUri*" + s);
        activity.sendBroadcast(mediaScanIntent);
    }

    private class UploadImageToServer extends AsyncTask<Void, Integer, String> {

        String img_path;

        public UploadImageToServer(String path) {

            this.img_path = path;

            sweetDialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#FFE64A19"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();
        }

        @Override
        protected void onPreExecute() {
            Log.e("Tag", formId);
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {

            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            Log.e("tag", "status" + formId);
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            String virtual_url = Data_Service.SERVICE_URL_NEW + "api/review";
            HttpPost httppost = new HttpPost(virtual_url);
            httppost.setHeader("apikey", "1eo7u4tig9704k2humvdywwnb4hnl2xa1jbrh7go");
            httppost.setHeader("formid", formId);
            httppost.setHeader("Action", status);
            //   httppost.setHeader("Content-type", "multipart/form-data");
            try {
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                Log.e("tag", "img: " + String.valueOf(photo));
                File sourceFile = new File(String.valueOf(photo));
                Log.e("tag", "file: " + String.valueOf(sourceFile));
                entity.addPart("fileUpload", new FileBody(photo, "images/jpeg"));
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code:" + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            dismiss();
            sweetDialog.dismiss();
            Log.e("tag", "Response from server: " + result);

            try {
                JSONObject jo = new JSONObject(result);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                if (status.equals("success")) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MESSAGE!!!")
                            .setContentText(msg)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getContext(), StaffViewEntry.class);
                                    getContext().startActivity(i);

                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("MESSAGE!!!")
                            .setContentText(msg)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
            } catch (Exception e) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("MESSAGE!!!")
                        .setContentText("Network Error \n Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                dismiss();
                            }
                        })
                        .show();
            }


        }

    }
}
