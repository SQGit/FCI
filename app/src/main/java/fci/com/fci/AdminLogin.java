package fci.com.fci;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 11-06-2016.
 */
public class AdminLogin extends AppCompatActivity {
    ImageView submit;
    TextView tv_header,content;
    EditText et_phone, et_pass;
    Typeface tf;
    String str_phone, str_pass;
    public String URL_LOGIN = Data_Service.SERVICE_URL_NEW + "admin/login";
    SweetAlertDialog sweetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_login_1);
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        submit = (ImageView) findViewById(R.id.submit_iv);
        tv_header = (TextView) findViewById(R.id.tv_header);
        content = (TextView) findViewById(R.id.content);

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pass = (EditText) findViewById(R.id.et_password);
        tv_header.setTypeface(tf);
        et_phone.setTypeface(tf);
        et_pass.setTypeface(tf);
        content.setTypeface(tf);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_phone = et_phone.getText().toString();
                str_pass = et_pass.getText().toString();
                if (Util.Operations.isOnline(AdminLogin.this)) {
                    if (!(str_phone.isEmpty())) {
                        if (!(str_pass.isEmpty()))
                        {
                            if(str_phone.equals("0987654321"))
                            {
                                new staffLogin_Task().execute();
                            }
                            else
                            {
                                new SweetAlertDialog(AdminLogin.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("WARNING MESSAGE!!!")
                                        .setContentText("Incorrect Phone Number")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "Password cannot be blank", Toast.LENGTH_LONG).show();
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Phone Number cannot be blank", Toast.LENGTH_LONG).show();

                    }


                } else {
                    new SweetAlertDialog(AdminLogin.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("No Internet Connectivity")
                            .show();
                }
            }
        });


        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {








                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(i);
                AdminLogin.this.finish();

            }
        });


    }


    class staffLogin_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(AdminLogin.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#5DB2EF"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("Phone", str_phone);
                jsonObject.accumulate("Password", str_pass);
                json = jsonObject.toString();
                return jsonStr = PostService.makeRequest(URL_LOGIN, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("tag", "<-----resultlogin---->" + s);
            sweetDialog.dismiss();
            super.onPostExecute(s);


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                if (status.equals("success")) {
                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                    startActivity(i);
                    AdminLogin.this.finish();
                } else {
                    new SweetAlertDialog(AdminLogin.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(msg)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
            catch (Exception e) {

            }


        }

    }
}
