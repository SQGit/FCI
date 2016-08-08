package fci.com.fci;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 11-06-2016.
 */
public class AdminStaffCreate extends AppCompatActivity {
    ImageView submit;
    EditText et_name, et_phone, et_pin1, et_pin2, et_pin3, et_pin4, et_repin1, et_repin2, et_repin3, et_repin4;
    String str_pin, str_repin, str_name, str_phone,str_address;
    LinearLayout lt_logout;
    SweetAlertDialog sweetDialog;
    Typeface tf;
    TextView tv_header, tv_pass_txt, tv_repass_txt;
    int sss;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_createstaff);

        Intent get_intent = getIntent();

        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        submit = (ImageView) findViewById(R.id.submit_iv);

        tv_header = (TextView) findViewById(R.id.tv_header);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pin1 = (EditText) findViewById(R.id.et_pin1);
        et_pin2 = (EditText) findViewById(R.id.et_pin2);
        et_pin3 = (EditText) findViewById(R.id.et_pin3);
        et_pin4 = (EditText) findViewById(R.id.et_pin4);
        et_repin1 = (EditText) findViewById(R.id.et_repin1);
        et_repin2 = (EditText) findViewById(R.id.et_repin2);
        et_repin3 = (EditText) findViewById(R.id.et_repin3);
        et_repin4 = (EditText) findViewById(R.id.et_repin4);
        lt_logout = (LinearLayout) findViewById(R.id.layout_logout);

        tv_pass_txt = (TextView) findViewById(R.id.tv_password);
        tv_repass_txt = (TextView) findViewById(R.id.tv_repassword);

        sss = get_intent.getIntExtra("sts",0);
        Log.d("tag",""+status);
        if(sss == 0){
            et_phone.setEnabled(true);
            Log.d("tag","fromdash");
            str_address = Data_Service.SERVICE_URL + "staff/add";
            tv_header.setText("Create Staff");
        }
        else if(sss == 1){
            String name = get_intent.getStringExtra("name");
            String phone = get_intent.getStringExtra("phone");
            String pass = get_intent.getStringExtra("pass");
            str_address = Data_Service.SERVICE_URL + "staff/update";
            Log.d("tag","fromUpdate");
            et_name.setText(name);
            et_phone.setText(phone);
            et_phone.setEnabled(false);
            et_pin1.requestFocus();
            tv_header.setText("Update Staff");
        }

        tv_header.setTypeface(tf);
        et_name.setTypeface(tf);
        et_phone.setTypeface(tf);
        tv_pass_txt.setTypeface(tf);
        tv_repass_txt.setTypeface(tf);




        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sss == 0){
                    Intent i=new Intent(getApplicationContext(),AdminDashboard.class);
                    startActivity(i);
                    AdminStaffCreate.this.finish();
                }
                else {
                    Intent i=new Intent(getApplicationContext(),AdminStaffEdit.class);
                    startActivity(i);
                    AdminStaffCreate.this.finish();
                }



            }
        });




        lt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "logout");
            }
        });


       /* et_pin1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_pin2.requestFocus();
                    if (et_pin1.getText().length() == 1) {
                        et_pin2.requestFocus();
                    } else {
                        et_pin1.requestFocus();
                    }
                }
                return false;
            }
        });*/


        et_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_pin1.requestFocus();
                }
                return false;
            }
        });


        et_pin1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin1.getText().length() == 1) {
                    et_pin2.requestFocus();
                } else {
                    et_pin1.requestFocus();
                }
                return false;
            }
        });

        et_pin2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin2.getText().length() == 1) {
                    et_pin3.requestFocus();
                } else {
                    et_pin2.requestFocus();
                }
                return false;
            }
        });

        et_pin3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin3.getText().length() == 1) {
                    et_pin4.requestFocus();
                } else {
                    et_pin3.requestFocus();
                }
                return false;
            }
        });

        et_pin4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin4.getText().length() == 1) {
                    // pin = pin1.getText().toString()+pin2.getText().toString()+pin3.getText().toString()+pin4.getText().toString();
                    //Toast.makeText(getApplicationContext(),pin,Toast.LENGTH_LONG).show();
                    et_repin1.requestFocus();
                } else {
                    et_pin4.requestFocus();
                }
                return false;
            }
        });


        et_repin1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_repin1.getText().length() == 1) {
                    et_repin2.requestFocus();
                } else {
                    et_repin1.requestFocus();
                }
                return false;
            }
        });

        et_repin2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_repin2.getText().length() == 1) {
                    et_repin3.requestFocus();
                } else {
                    et_repin2.requestFocus();
                }
                return false;
            }
        });

        et_repin3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_repin3.getText().length() == 1) {
                    et_repin4.requestFocus();
                } else {
                    et_repin3.requestFocus();
                }
                return false;
            }
        });

        et_repin4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et_pin4.getText().length() == 1) {
                    str_pin = et_pin1.getText().toString() + et_pin2.getText().toString() + et_pin3.getText().toString() + et_pin4.getText().toString();
                    str_repin = et_repin1.getText().toString() + et_repin2.getText().toString() + et_repin3.getText().toString() + et_repin4.getText().toString();
                    submit.requestFocus();
                } else {
                    et_repin4.requestFocus();
                }
                return false;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_name = et_name.getText().toString().trim();
                str_phone = et_phone.getText().toString().trim();
                str_pin = et_pin1.getText().toString() + et_pin2.getText().toString() + et_pin3.getText().toString() + et_pin4.getText().toString();
                str_repin = et_repin1.getText().toString() + et_repin2.getText().toString() + et_repin3.getText().toString() + et_repin4.getText().toString();
                if (!(str_name.isEmpty())) {
                    if (!(str_phone.isEmpty() || str_phone.length() < 9 || str_phone.length() > 10)) {
                        if (!(str_pin == str_repin)) {

                            if (!Data_Service.isNetworkAvailable(AdminStaffCreate.this)) {
                                new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Oops!")
                                        .setContentText("No network Available!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.setCancelable(false);
                                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                                sweetAlertDialog.dismiss();
                                            }
                                        })
                                        .show();
                            } else {
                                new staffCreat_Task().execute();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Pin not matech", Toast.LENGTH_LONG).show();
                            et_repin1.requestFocus();
                        }
                    } else {
                        et_phone.setError("Enter Valid Phone Number");
                        et_phone.requestFocus();
                    }
                } else {
                    et_name.setError("Name should not be empty");
                    et_name.requestFocus();

                }

            }
        });

    }

    class staffCreat_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#FFE64A19"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", str_name);
                jsonObject.accumulate("phone", str_phone);
                jsonObject.accumulate("password", str_pin);
                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();
                return jsonStr = PostService.makeRequest(str_address, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("tag", "<-----rerseres---->" + s);
            super.onPostExecute(s);
            sweetDialog.dismiss();
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.d("tag", "<-----Status----->" + status);


                if (status.equals("success")) {
                    Log.d("tag", "<-----msg----->" + msg);
                    new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(msg)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent goDash = new Intent(getApplicationContext(), AdminDashboard.class);
                                    startActivity(goDash);
                                    AdminStaffCreate.this.finish();
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Failed")
                            .setContentText(msg)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    et_phone.requestFocus();
                                }
                            })
                            .show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
