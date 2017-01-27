package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 11-06-2016.
 */
public class AdminLogin extends AppCompatActivity {
    public String URL_LOGIN = Data_Service.SERVICE_URL_NEW + "admin/login";
    ImageView submit;
    TextView tv_header,content;
    EditText et_phone, et_pass;
    Typeface tf;
    String str_phone, str_pass;
    SweetAlertDialog sweetDialog;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

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


        setupUI(findViewById(R.id.top));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                str_phone = et_phone.getText().toString().trim();
                str_pass = et_pass.getText().toString().trim();
                if (Util.Operations.isOnline(AdminLogin.this)) {

                    /*Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                    startActivity(i);
                    AdminLogin.this.finish();*/

                    if (!(str_phone.isEmpty())) {
                        if (!(str_pass.isEmpty()))
                        {
                            if(str_phone.equals("0987654321"))
                            {

                                if (str_pass.equals("12345z")) {

                                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                    startActivity(i);
                                    AdminLogin.this.finish();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }
                                //new adminLogin_Task().execute();
                            }
                            else
                            {

                                Toast.makeText(getApplicationContext(), "Incorrect Phone Number", Toast.LENGTH_SHORT).show();
                            }
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "Password cannot be blank", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Phone Number cannot be blank", Toast.LENGTH_LONG).show();

                    }




                } else {
                    new SweetAlertDialog(AdminLogin.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText("No Internet Connectivity.\nPlease Try Again Later.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                    startActivity(i);
                                    AdminLogin.this.finish();
                                }
                            })
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

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(AdminLogin.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(i);
        AdminLogin.this.finish();

    }

    class adminLogin_Task extends AsyncTask<String, Void, String> {

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
                new SweetAlertDialog(AdminLogin.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("WARNING MESSAGE!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                AdminLogin.this.finish();
                            }
                        })
                        .show();

            }


        }

    }
}
