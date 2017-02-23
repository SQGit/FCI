package net.fciapp.fciscanner;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
public class AdminStaffCreate1 extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
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
    EditText mPinHiddenEditText,mPinHiddenEditText1;
public int id_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
     //   setContentView(R.layout.admin_createstaff1);
        setContentView(new MainLayout(this, null));

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

        mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);
        mPinHiddenEditText1 = (EditText) findViewById(R.id.pin_hidden_edittext1);


        mPinHiddenEditText.addTextChangedListener(this);
        mPinHiddenEditText1.addTextChangedListener(this);

        et_pin1.setOnFocusChangeListener(this);
        et_pin2.setOnFocusChangeListener(this);
        et_pin3.setOnFocusChangeListener(this);
        et_pin4.setOnFocusChangeListener(this);
        et_pin1.setOnKeyListener(this);
        et_pin2.setOnKeyListener(this);
        et_pin3.setOnKeyListener(this);
        et_pin4.setOnKeyListener(this);

        et_repin1.setOnFocusChangeListener(this);
        et_repin2.setOnFocusChangeListener(this);
        et_repin3.setOnFocusChangeListener(this);
        et_repin4.setOnFocusChangeListener(this);
        et_repin1.setOnKeyListener(this);
        et_repin2.setOnKeyListener(this);
        et_repin3.setOnKeyListener(this);
        et_repin4.setOnKeyListener(this);


        sss = get_intent.getIntExtra("sts", 0);
        Log.d("tag", "" + status);
        if (sss == 0) {
            et_phone.setEnabled(true);
            Log.d("tag", "fromdash");
            str_address = Data_Service.SERVICE_URL_NEW + "staff/add";
            tv_header.setText("Create Staff");
        } else if (sss == 1) {
            String name = get_intent.getStringExtra("name");
            String phone = get_intent.getStringExtra("phone");
            String pass = get_intent.getStringExtra("pass");
            str_address = Data_Service.SERVICE_URL_NEW + "staff/update";
            Log.e("tag", "fromUpdate" + pass);
            et_name.setText(name);
            et_phone.setText(phone);
            et_phone.setEnabled(false);
            et_name.requestFocus();

            et_name.setImeOptions(EditorInfo.IME_ACTION_DONE);

            frm_edit = name;

            et_pin1.setText(String.valueOf(pass.charAt(0)));
            et_pin2.setText(String.valueOf(pass.charAt(1)));
            et_pin3.setText(String.valueOf(pass.charAt(2)));
            et_pin4.setText(String.valueOf(pass.charAt(3)));

            et_pin1.setEnabled(false);
            setDefaultPinBackground(et_pin1);
            et_pin2.setEnabled(false);
            et_pin3.setEnabled(false);
            et_pin4.setEnabled(false);

            et_repin1.setEnabled(false);
            et_repin2.setEnabled(false);
            et_repin3.setEnabled(false);
            et_repin4.setEnabled(false);



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
                    AdminStaffCreate1.this.finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), AdminStaffEdit.class);
                    startActivity(i);
                    AdminStaffCreate1.this.finish();
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

                }
                return false;
            }
        });


        lt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to Logout?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                AdminStaffCreate1.this.finish();
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


        if (Util.Operations.isOnline(AdminStaffCreate1.this)) {
            new staffFetch_Task().execute();
        } else {
            new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(i);
                            AdminStaffCreate1.this.finish();
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


                    if (Util.Operations.isOnline(AdminStaffCreate1.this)) {
                        if (!(str_name.isEmpty())) {
                            if (!(str_phone.isEmpty())) {
                                if (!(str_phone.length() < 9 || str_phone.length() > 10)) {

                                    if (!(names.contains(str_name))) {
                                        if (!(phones.contains(str_phone))) {

                                            Log.e("tag", "" + str_pin + str_repin);
                                            if (!(str_pin.isEmpty() || str_pin.length() < 4)) {
                                                if (!(str_repin.isEmpty() || str_repin.length() < 4)) {
                                                    if (str_pin.equals(str_repin)) {

                                                        if (!Data_Service.isNetworkAvailable(AdminStaffCreate1.this)) {
                                                            new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
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

                        new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("WARNING MESSAGE!!!")
                                .setContentText("No Internet Connectivity.\nPlease Try Again Later.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                        startActivity(i);
                                        AdminStaffCreate1.this.finish();
                                    }
                                })
                                .show();

                    }


                } else {


                    if (Util.Operations.isOnline(AdminStaffCreate1.this)) {
                        if (!(str_name.isEmpty())) {
                            if (!(names.contains(str_name))) {
                                if (!(str_pin.isEmpty() || str_pin.length() < 4)) {
                                    if (!(str_repin.isEmpty() || str_repin.length() < 4)) {
                                        if (str_pin.equals(str_repin)) {

                                            if (!Data_Service.isNetworkAvailable(AdminStaffCreate1.this)) {
                                                new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText("WARNING MESSAGE!!!")
                                                        .setContentText("No Internet Connectivity.\nPlease Try Again Later.")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismiss();
                                                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                                                startActivity(i);
                                                                AdminStaffCreate1.this.finish();
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

                        new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("WARNING MESSAGE!!!")
                                .setContentText("No Internet Connectivity.\nPlease Try Again Later.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                        startActivity(i);
                                        AdminStaffCreate1.this.finish();
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
                    hideSoftKeyboard(AdminStaffCreate1.this);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(id_data == R.id.et_pin1 || id_data == R.id.et_pin2 || id_data == R.id.et_pin3 || id_data == R.id.et_pin4){

            Log.e("tag","pin ");

            setDefaultPinBackground(et_pin1);
            setDefaultPinBackground(et_pin2);
            setDefaultPinBackground(et_pin3);
            setDefaultPinBackground(et_pin4);

            if (s.length() == 0) {
                setFocusedPinBackground(et_pin1);
                et_pin1.setText("");
            } else if (s.length() == 1) {
                setFocusedPinBackground(et_pin2);
                et_pin1.setText(s.charAt(0) + "");
                et_pin2.setText("");
                et_pin3.setText("");
                et_pin4.setText("");
            } else if (s.length() == 2) {
                setFocusedPinBackground(et_pin3);
                et_pin2.setText(s.charAt(1) + "");
                et_pin3.setText("");
                et_pin4.setText("");
            } else if (s.length() == 3) {
                setFocusedPinBackground(et_pin4);
                et_pin3.setText(s.charAt(2) + "");
                et_pin4.setText("");
            }
            else if (s.length() == 4) {
                et_pin4.setText(s.charAt(3) + "");
                hideSoftKeyboard(AdminStaffCreate1.this);
            }

        }
        else{
            Log.e("tag","reenterpin ");


            setDefaultPinBackground(et_repin1);
            setDefaultPinBackground(et_repin2);
            setDefaultPinBackground(et_repin3);
            setDefaultPinBackground(et_repin4);

            if (s.length() == 0) {
                setFocusedPinBackground(et_repin1);
                et_repin1.setText("");
            } else if (s.length() == 1) {
                setFocusedPinBackground(et_repin2);
                et_repin1.setText(s.charAt(0) + "");
                et_repin2.setText("");
                et_repin3.setText("");
                et_repin4.setText("");
            } else if (s.length() == 2) {
                setFocusedPinBackground(et_repin3);
                et_repin2.setText(s.charAt(1) + "");
                et_repin3.setText("");
                et_repin4.setText("");
            } else if (s.length() == 3) {
                setFocusedPinBackground(et_repin4);
                et_repin3.setText(s.charAt(2) + "");
                et_repin4.setText("");
            }
            else if (s.length() == 4) {
                et_repin4.setText(s.charAt(3) + "");
                hideSoftKeyboard(AdminStaffCreate1.this);
            }



        }






    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        Log.e("tagqq",""+v.getId()+hasFocus);
        id_data = v.getId();


        final int id = v.getId();

        switch (id) {
            case R.id.et_pin1:
                if (hasFocus) {

                    if(et_pin1.getText().length()>0) {


                        setFocus(mPinHiddenEditText);
                        setFocusedPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText);
                    }
                    else{
                        setFocus(mPinHiddenEditText);
                        setFocusedPinBackground(et_pin1);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText);


                    }
                }
                break;

            case R.id.et_pin2:
                if (hasFocus) {


                    if(mPinHiddenEditText.getText().length()>0) {
                        setFocus(mPinHiddenEditText);

                        setFocusedPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);

                        showSoftKeyboard(mPinHiddenEditText);
                    }
                    else{
                        setFocus(mPinHiddenEditText);
                        setFocusedPinBackground(et_pin1);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText);



                    }
                }
                break;

            case R.id.et_pin3:
                if (hasFocus) {

                    if(mPinHiddenEditText.getText().length()>0) {


                        setFocus(mPinHiddenEditText);
                        setFocusedPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText);
                    }
                    else{


                        setFocus(mPinHiddenEditText);
                        setFocusedPinBackground(et_pin1);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText);

                    }
                }
                break;

            case R.id.et_pin4:
                if (hasFocus) {

                    if(mPinHiddenEditText.getText().length()>0) {
                        setFocus(mPinHiddenEditText);
                        setFocusedPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText);
                    }
                    else{

                        setFocus(mPinHiddenEditText);
                        setFocusedPinBackground(et_pin1);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText);


                    }
                }
                break;

            case R.id.et_repin1:
                if (hasFocus) {


                    if(mPinHiddenEditText1.getText().length()>0) {

                        setFocus(mPinHiddenEditText1);
                        setFocusedPinBackground(et_repin4);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin1);
                        showSoftKeyboard(mPinHiddenEditText1);
                    }

                    else{

                        setFocus(mPinHiddenEditText1);
                        setFocusedPinBackground(et_repin1);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText1);

                    }
                }
                break;

            case R.id.et_repin2:
                if (hasFocus) {

                    if(mPinHiddenEditText1.getText().length()>0) {

                        setFocus(mPinHiddenEditText1);
                        setFocusedPinBackground(et_repin4);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin2);
                        showSoftKeyboard(mPinHiddenEditText1);
                    }
                    else{


                        setFocus(mPinHiddenEditText1);
                        setFocusedPinBackground(et_repin1);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText1);

                    }
                }
                break;
            case R.id.et_repin3:
                if (hasFocus) {


                    if(mPinHiddenEditText1.getText().length()>0) {

                        setFocus(mPinHiddenEditText1);
                        setFocusedPinBackground(et_repin4);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin1);
                        setDefaultPinBackground(et_repin3);
                        showSoftKeyboard(mPinHiddenEditText1);
                    }
                    else{

                        setFocus(mPinHiddenEditText1);
                        setFocusedPinBackground(et_repin1);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText1);
                    }
                }
                break;
            case R.id.et_repin4:
                if (hasFocus) {

                    if(mPinHiddenEditText1.getText().length()>0) {

                        setFocus(mPinHiddenEditText1);
                        setFocusedPinBackground(et_repin4);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin1);
                        showSoftKeyboard(mPinHiddenEditText1);

                    }
                    else{


                        setFocus(mPinHiddenEditText1);
                        setFocusedPinBackground(et_repin1);
                        setDefaultPinBackground(et_pin2);
                        setDefaultPinBackground(et_pin3);
                        setDefaultPinBackground(et_pin4);
                        setDefaultPinBackground(et_pin1);
                        setDefaultPinBackground(et_repin2);
                        setDefaultPinBackground(et_repin3);
                        setDefaultPinBackground(et_repin4);
                        showSoftKeyboard(mPinHiddenEditText1);
                    }
                }
                break;


            default:
                break;
        }


    }



    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        Log.e("tagkk",""+event.getAction()+keyCode);


        if(id_data == R.id.et_pin1 || id_data == R.id.et_pin2 || id_data == R.id.et_pin3 || id_data == R.id.et_pin4) {

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                final int id = v.getId();
                switch (id) {
                    case R.id.pin_hidden_edittext:
                        if (keyCode == KeyEvent.KEYCODE_DEL) {
                            if (mPinHiddenEditText.getText().length() == 4)
                                et_pin4.setText("");
                            else if (mPinHiddenEditText.getText().length() == 3)
                                et_pin3.setText("");
                            else if (mPinHiddenEditText.getText().length() == 2)
                                et_pin2.setText("");
                            else if (mPinHiddenEditText.getText().length() == 1)
                                et_pin1.setText("");

                            if (mPinHiddenEditText.length() > 0)
                                mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));

                            return true;
                        }

                        break;

                    default:
                        return false;
                }
            }
        }
        else{


            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                final int id = v.getId();
                switch (id) {
                    case R.id.pin_hidden_edittext1:
                        if (keyCode == KeyEvent.KEYCODE_DEL) {
                            if (mPinHiddenEditText1.getText().length() == 4)
                                et_repin4.setText("");
                            else if (mPinHiddenEditText1.getText().length() == 3)
                                et_repin3.setText("");
                            else if (mPinHiddenEditText1.getText().length() == 2)
                                et_repin2.setText("");
                            else if (mPinHiddenEditText1.getText().length() == 1)
                                et_repin1.setText("");

                            if (mPinHiddenEditText1.length() > 0)
                                mPinHiddenEditText1.setText(mPinHiddenEditText1.getText().subSequence(0, mPinHiddenEditText1.length() - 1));

                            return true;
                        }

                        break;

                    default:
                        return false;
                }
            }

        }




        return false;
    }



    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }



    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.admin_createstaff1, this);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);

            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                if (mPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(et_pin1);
                else
                    setDefaultPinBackground(et_pin1);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    private void setFocusedPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.pin_square_bg1));
    }

    private void setDefaultPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.pin_square_bg));
    }


    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }




    class staffFetch_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.PROGRESS_TYPE);
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

                    new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Not Connected!!!")
                            .setContentText("Please Try Again Later.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                    startActivity(i);
                                    AdminStaffCreate1.this.finish();
                                }
                            })
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

                new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(i);
                                AdminStaffCreate1.this.finish();
                            }
                        })
                        .show();


            }
        }

    }

    class staffCreat_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.PROGRESS_TYPE);
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
                    new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(msg)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent goDash = new Intent(getApplicationContext(), AdminDashboard.class);
                                    startActivity(goDash);
                                    AdminStaffCreate1.this.finish();
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
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

                new SweetAlertDialog(AdminStaffCreate1.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(i);
                                AdminStaffCreate1.this.finish();
                            }
                        })
                        .show();

            }

        }

    }


}
