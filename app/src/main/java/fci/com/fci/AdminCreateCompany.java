package fci.com.fci;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Ramya on 11-06-2016.
 */
public class AdminCreateCompany extends AppCompatActivity {
    ImageView submit;
    TextView tv_header, tv_add_manag;
    EditText et_comp_name, et_comp_loc, et_manag_name, et_manag_phone, et_comp_email;
    Typeface tf;
    LinearLayout lt_add;
    SweetAlertDialog sweetDialog;
    String companyname, companylocation, companyemail, managername, managerphone, altermanagername, altermanagerphone, str_address;
    int sss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_create_company_1);
        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_add_manag = (TextView) findViewById(R.id.tv_add_manag);
        et_comp_name = (EditText) findViewById(R.id.et_comp_name);
        et_comp_loc = (EditText) findViewById(R.id.et_comp_location);
        et_manag_name = (EditText) findViewById(R.id.et_manager_name);
        et_manag_phone = (EditText) findViewById(R.id.et_manager_phone);
        et_comp_email = (EditText) findViewById(R.id.et_comp_email);

        submit = (ImageView) findViewById(R.id.submit_iv);
        lt_add = (LinearLayout) findViewById(R.id.layout_add);
        tv_header.setTypeface(tf);
        tv_add_manag.setTypeface(tf);
        et_comp_name.setTypeface(tf);
        et_comp_loc.setTypeface(tf);
        et_manag_name.setTypeface(tf);
        et_manag_phone.setTypeface(tf);
        et_comp_email.setTypeface(tf);
        str_address = Data_Service.SERVICE_URL_NEW + "company/add";
        Intent get_intent = getIntent();
        sss = get_intent.getIntExtra("sts", 0);
        if (sss == 0) {
            et_comp_loc.setEnabled(true);
            et_manag_name.setEnabled(true);
            et_comp_email.setEnabled(true);
            et_manag_phone.setEnabled(true);
            Log.d("tag", "fromdash");
            str_address = Data_Service.SERVICE_URL_NEW + "company/add";
            tv_header.setText("Create Company");
        } else if (sss == 1) {
            companyname = get_intent.getStringExtra("name");
            String location = get_intent.getStringExtra("location");
            String phone = get_intent.getStringExtra("phone");
            String email = get_intent.getStringExtra("email");
            String managerName = get_intent.getStringExtra("managerName");
            str_address = Data_Service.SERVICE_URL_NEW + "company/update";
            Log.d("tag", "fromUpdate");
            et_comp_name.setText(companyname);
            et_comp_loc.setText(location);
            et_comp_email.setText(email);
            et_manag_name.setText(managerName);
            et_manag_phone.setText(phone);
            et_comp_name.setEnabled(false);
            et_comp_loc.requestFocus();
            tv_header.setText("Update Company");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AdminCreateCompany.this);
                String code = sharedPreferences.getString("tag", "");
                if ((code == "")) {
                    managername = et_manag_name.getText().toString();
                    managerphone = et_manag_phone.getText().toString();
                } else {
                    altermanagername = et_manag_name.getText().toString();
                    altermanagerphone = et_manag_phone.getText().toString();
                }

                if (!(et_comp_name.getText().toString().trim().isEmpty())) {
                    if (!(et_comp_loc.getText().toString().trim().isEmpty())) {
                        if (!(et_comp_email.getText().toString().isEmpty())) {
                            if (android.util.Patterns.EMAIL_ADDRESS.matcher(et_comp_email.getText().toString()).matches()) {
                                if (!(et_manag_name.getText().toString().isEmpty())) {
                                    if (!(et_manag_phone.getText().toString().isEmpty())) {
                                        if(!(et_manag_phone.getText().length()<9)){
                                        companyname = et_comp_name.getText().toString();
                                        companylocation = et_comp_loc.getText().toString();
                                        companyemail = et_comp_email.getText().toString();
                                        new createCompany_Task().execute();
                                    }
                                        else{
                                            et_manag_phone.setError("Enter Valid Phone Number");
                                            et_manag_phone.requestFocus();

                                        }
                                    }
                                        else {
                                        et_manag_phone.setError("Enter Phone Number");
                                        et_manag_phone.requestFocus();
                                    }
                                } else {
                                    et_manag_name.setError("Enter Manager Name");
                                    et_manag_name.requestFocus();
                                }
                            } else {
                                et_comp_email.setError("Enter Valid Mail");
                                et_comp_email.requestFocus();
                            }
                        } else {
                            et_comp_email.setError("Enter Email");
                            et_comp_email.requestFocus();
                        }
                    } else {
                        et_comp_loc.setError("Enter Company Location");
                        et_comp_loc.requestFocus();
                    }
                } else {
                    et_comp_name.setError("Enter Company Name");
                    et_comp_name.requestFocus();
                }
            }
        });

        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                startActivity(i);
                AdminCreateCompany.this.finish();
            }
        });

        lt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = s_pref.edit();
                edit.putString("tag", "1234");
                edit.commit();
                managername = et_manag_name.getText().toString();
                managerphone = et_manag_phone.getText().toString();
                et_manag_name.setText("");
                et_manag_phone.setText("");
                et_manag_name.requestFocus();

            }
        });


    }


    class createCompany_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            sweetDialog = new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.PROGRESS_TYPE);
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
                jsonObject.accumulate("company_name", companyname);
                jsonObject.accumulate("company_location", companylocation);
                jsonObject.accumulate("company_email", companyemail);
                jsonObject.accumulate("mgr_name", managername);
                jsonObject.accumulate("mgr_phone", managerphone);
                jsonObject.accumulate("alt_mgr_name", altermanagername);
                jsonObject.accumulate("alt_mgr_phone", altermanagerphone);
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
                    new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(msg)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent goDash = new Intent(getApplicationContext(), AdminDashboard.class);
                                    startActivity(goDash);
                                    AdminCreateCompany.this.finish();
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(AdminCreateCompany.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Failed")
                            .setContentText(msg)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
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
