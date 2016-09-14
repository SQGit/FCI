package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 11-06-2016.
 */
public class AdminStaffCreate extends AppCompatActivity {
    public String URL = Data_Service.SERVICE_URL_NEW + "staff/fetch";
    ImageView submit;
    EditText et_name, et_phone, et_pin1, et_pin2, et_pin3, et_pin4, et_repin1, et_repin2, et_repin3, et_repin4;
    String str_pin, str_repin, str_name, str_phone, str_address;
    LinearLayout lt_logout;
    SweetAlertDialog sweetDialog;
    Typeface tf;
    TextView tv_header, tv_pass_txt, tv_repass_txt, tv_logout_txt, tv_logout;
    int sss;
    String status;
    ArrayList<String> names;
    ArrayList<String> phones;
    String frm_edit;

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
        setContentView(R.layout.admin_createstaff);

        Intent get_intent = getIntent();

        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");

        names = new ArrayList<>();
        phones = new ArrayList<>();


        submit = (ImageView) findViewById(R.id.submit_iv);

        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_logout_txt = (TextView) findViewById(R.id.tv_admin);
        tv_logout = (TextView) findViewById(R.id.tv_logout);
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

        sss = get_intent.getIntExtra("sts", 0);
        Log.d("tag", "" + status);
        if (sss == 0) {
            et_phone.setEnabled(true);
            Log.d("tag", "fromdash");
            str_address = Data_Service.SERVICE_URL + "staff/add";
            tv_header.setText("Create Staff");
        } else if (sss == 1) {
            String name = get_intent.getStringExtra("name");
            String phone = get_intent.getStringExtra("phone");
            String pass = get_intent.getStringExtra("pass");
            str_address = Data_Service.SERVICE_URL + "staff/update";
            Log.e("tag", "fromUpdate" + pass);
            et_name.setText(name);
            et_phone.setText(phone);
            et_phone.setEnabled(false);
            et_repin1.requestFocus();

            et_name.setImeOptions(EditorInfo.IME_ACTION_DONE);

            frm_edit = name;

            et_pin1.setText(String.valueOf(pass.charAt(0)));
            et_pin2.setText(String.valueOf(pass.charAt(1)));
            et_pin3.setText(String.valueOf(pass.charAt(2)));
            et_pin4.setText(String.valueOf(pass.charAt(3)));

            et_repin1.setText(String.valueOf(pass.charAt(0)));
            et_repin2.setText(String.valueOf(pass.charAt(1)));
            et_repin3.setText(String.valueOf(pass.charAt(2)));
            et_repin4.setText(String.valueOf(pass.charAt(3)));

            tv_header.setText("Update Staff");
        }

        tv_header.setTypeface(tf);
        et_name.setTypeface(tf);
        et_phone.setTypeface(tf);
        tv_pass_txt.setTypeface(tf);
        tv_repass_txt.setTypeface(tf);

        tv_logout_txt.setTypeface(tf);
        tv_logout.setTypeface(tf);


        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sss == 0) {
                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                    startActivity(i);
                    AdminStaffCreate.this.finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), AdminStaffEdit.class);
                    startActivity(i);
                    AdminStaffCreate.this.finish();
                }


            }
        });


        et_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {


                    str_repin = et_repin1.getText().toString() + et_repin2.getText().toString() + et_repin3.getText().toString() + et_repin4.getText().toString();
                    if (str_repin.length() == 0) {
                        et_repin1.requestFocus();
                    }

                   /* if (sss == 0) {
                        Log.e("tag","0");
                        et_phone.requestFocus();
                    } else {
                        Log.e("tag","1");
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    }*/
                }
                return false;
            }
        });


        lt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to Logout?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                AdminStaffCreate.this.finish();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                            }
                        })
                        .show();
            }
        });


        if (Util.Operations.isOnline(AdminStaffCreate.this)) {
            new staffFetch_Task().execute();
        } else {
            new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(i);
                            AdminStaffCreate.this.finish();
                        }
                    })
                    .show();


        }


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

                str_pin = str_pin.trim();
                str_repin = str_repin.trim();


                if (sss == 0) {


                    if (Util.Operations.isOnline(AdminStaffCreate.this)) {
                        if (!(str_name.isEmpty())) {
                            if (!(str_phone.isEmpty())) {
                                if (!(str_phone.length() < 9 || str_phone.length() > 10)) {

                                    if (!(names.contains(str_name))) {
                                        if (!(phones.contains(str_phone))) {

                                            Log.e("tag", "" + str_pin + str_repin);
                                            if (!(str_pin.isEmpty() || str_pin.length() < 4)) {
                                                if (!(str_repin.isEmpty() || str_repin.length() < 4)) {
                                                    if (str_pin.equals(str_repin)) {

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
                                                        Toast.makeText(getApplicationContext(), "Pin not matech", Toast.LENGTH_SHORT).show();
                                                        et_repin1.requestFocus();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Re Enter Pin", Toast.LENGTH_SHORT).show();
                                                    if (et_repin1.getText().toString().trim().length() == 0) {
                                                        et_repin1.requestFocus();
                                                    } else if (et_repin2.getText().toString().trim().length() == 0) {
                                                        et_repin2.requestFocus();
                                                    } else if (et_repin3.getText().toString().trim().length() == 0) {
                                                        et_repin3.requestFocus();
                                                    } else {
                                                        et_repin4.requestFocus();
                                                    }


                                                }

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Enter Pin", Toast.LENGTH_SHORT).show();
                                                if (et_pin1.getText().toString().trim().length() == 0) {
                                                    et_pin1.requestFocus();
                                                } else if (et_pin2.getText().toString().trim().length() == 0) {
                                                    et_pin2.requestFocus();
                                                } else if (et_pin3.getText().toString().trim().length() == 0) {
                                                    et_pin3.requestFocus();
                                                } else {
                                                    et_pin4.requestFocus();
                                                }

                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Staff Phone Number already Exist", Toast.LENGTH_SHORT).show();
                                            et_phone.requestFocus();
                                        }


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Staff Name already Exist Please Choose Another", Toast.LENGTH_SHORT).show();
                                        et_name.requestFocus();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                                    et_phone.requestFocus();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Phone should not be empty", Toast.LENGTH_SHORT).show();
                                et_name.requestFocus();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Name should not be empty", Toast.LENGTH_SHORT).show();
                            et_name.requestFocus();

                        }
                    } else {

                        new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("WARNING MESSAGE!!!")
                                .setContentText("No Internet Connectivity.\nPlease Try Again Later.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                        startActivity(i);
                                        AdminStaffCreate.this.finish();
                                    }
                                })
                                .show();

                    }


                } else {


                    if (Util.Operations.isOnline(AdminStaffCreate.this)) {
                        if (!(str_name.isEmpty())) {
                            if (!(names.contains(str_name))) {
                                if (!(str_pin.isEmpty() || str_pin.length() < 4)) {
                                    if (!(str_repin.isEmpty() || str_repin.length() < 4)) {
                                        if (str_pin.equals(str_repin)) {

                                            if (!Data_Service.isNetworkAvailable(AdminStaffCreate.this)) {
                                                new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText("WARNING MESSAGE!!!")
                                                        .setContentText("No Internet Connectivity.\nPlease Try Again Later.")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismiss();
                                                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                                                startActivity(i);
                                                                AdminStaffCreate.this.finish();
                                                            }
                                                        })
                                                        .show();

                                            } else {
                                                new staffCreat_Task().execute();
                                            }

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Pin not matech", Toast.LENGTH_SHORT).show();
                                            et_repin1.requestFocus();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Re Enter Pin", Toast.LENGTH_SHORT).show();
                                        if (et_repin1.getText().toString().trim().length() == 0) {
                                            et_repin1.requestFocus();
                                        } else if (et_repin2.getText().toString().trim().length() == 0) {
                                            et_repin2.requestFocus();
                                        } else if (et_repin3.getText().toString().trim().length() == 0) {
                                            et_repin3.requestFocus();
                                        } else {
                                            et_repin4.requestFocus();
                                        }


                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter Pin", Toast.LENGTH_SHORT).show();
                                    if (et_pin1.getText().toString().trim().length() == 0) {
                                        et_pin1.requestFocus();
                                    } else if (et_pin2.getText().toString().trim().length() == 0) {
                                        et_pin2.requestFocus();
                                    } else if (et_pin3.getText().toString().trim().length() == 0) {
                                        et_pin3.requestFocus();
                                    } else {
                                        et_pin4.requestFocus();
                                    }

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Staff Name already Exist Please Choose Another", Toast.LENGTH_SHORT).show();
                                et_name.requestFocus();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Name should not be empty", Toast.LENGTH_SHORT).show();
                            et_name.requestFocus();

                        }
                    } else {

                        new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("WARNING MESSAGE!!!")
                                .setContentText("No Internet Connectivity.\nPlease Try Again Later.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                        startActivity(i);
                                        AdminStaffCreate.this.finish();
                                    }
                                })
                                .show();

                    }


                }


            }
        });


        setupUI(findViewById(R.id.top));

    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(AdminStaffCreate.this);
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

    class staffFetch_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.PROGRESS_TYPE);
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
                json = jsonObject.toString();
                return jsonStr = PostService.makeRequest(URL, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
                sweetDialog.dismiss();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("tag", "<-----result---->" + s);
            sweetDialog.dismiss();
            super.onPostExecute(s);
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.d("tag", "<-----Status----->" + status);
                if (status.equals("success")) {
                    JSONArray jsonarray = jo.getJSONArray("staff");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        names.add(jsonobject.optString("name"));
                        phones.add(jsonobject.optString("phone"));
                        Log.e("tag", "<----worldlist----->" + names + phones);
                    }

                    if (sss == 1) {
                        names.remove(frm_edit);
                    }

                } else {

                    new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Not Connected!!!")
                            .setContentText("Please Try Again Later.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                    startActivity(i);
                                    AdminStaffCreate.this.finish();
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(i);
                                AdminStaffCreate.this.finish();
                            }
                        })
                        .show();


            }
        }

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
                sweetDialog.dismiss();
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

                new SweetAlertDialog(AdminStaffCreate.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(i);
                                AdminStaffCreate.this.finish();
                            }
                        })
                        .show();

            }

        }

    }


}
