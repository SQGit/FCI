package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Salman on 6/16/2016.
 */
public class AdminStaffEdit extends Activity {

    ListView lv_staffsList;
    SweetAlertDialog sweetDialog;
    ArrayList<String> ar_staff_name = new ArrayList<>();
    ArrayList<String> ar_staff_phone = new ArrayList<>();
    ArrayList<String> ar_staff_pass = new ArrayList<>();
    Typeface tf;
    LinearLayout lt_logout;
    TextView tv_header, tv_name_txt, tv_phone_txt, tv_pass_txt, tv_edit_txt, tv_admin, tv_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.admin_staffedit);



        tf = Typeface.createFromAsset(getAssets(), "fonts/asin.TTF");
        lv_staffsList = (ListView) findViewById(R.id.listview);

        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_name_txt = (TextView) findViewById(R.id.tv_name);
        tv_phone_txt = (TextView) findViewById(R.id.tv_phone);
        tv_pass_txt = (TextView) findViewById(R.id.tv_password);
        tv_edit_txt = (TextView) findViewById(R.id.tv_edit);

        tv_admin = (TextView) findViewById(R.id.tv_admin_txt);
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        lt_logout = (LinearLayout) findViewById(R.id.layout_logout);

        tv_header.setTypeface(tf);
        tv_name_txt.setTypeface(tf);
        tv_phone_txt.setTypeface(tf);
        tv_pass_txt.setTypeface(tf);
        tv_edit_txt.setTypeface(tf);

        tv_admin.setTypeface(tf);
        tv_logout.setTypeface(tf);


        if (Util.Operations.isOnline(AdminStaffEdit.this)) {
            new staffEdit_Task().execute();
        } else {
            new SweetAlertDialog(AdminStaffEdit.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(i);
                            AdminStaffEdit.this.finish();
                        }
                    })
                    .show();
        }




        tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AdminDashboard.class);
                startActivity(i);
                AdminStaffEdit.this.finish();
            }
        });


        lt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(AdminStaffEdit.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to Logout?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);
                                AdminStaffEdit.this.finish();
                                sDialog.dismiss();
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







    }


    class staffEdit_Task extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            sweetDialog = new SweetAlertDialog(AdminStaffEdit.this, SweetAlertDialog.PROGRESS_TYPE);
            sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#FFE64A19"));
            sweetDialog.setTitleText("Loading");
            sweetDialog.setCancelable(false);
            sweetDialog.show();
        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";


            try {
                String virtual_url = Data_Service.SERVICE_URL_NEW + "staff/fetch";
                JSONObject jsonobject = PostService.getStaffs(virtual_url);
                Log.e("tag_", "0" + jsonobject.toString());

                if (jsonobject.toString() == "sam") {
                    new SweetAlertDialog(AdminStaffEdit.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops!")
                            .setContentText("Try Check your Network")
                            .setConfirmText("OK")
                            .show();
                    Log.e("tag_", "1" + jsonobject.toString());
                }

                json = jsonobject.toString();
                return json;
            } catch (Exception e) {
                Log.e("InputStream", "2" + e.toString());
                jsonStr = "";
                sweetDialog.dismiss();
            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag_", "<-----rerseres---->" + jsonStr);
            super.onPostExecute(jsonStr);
            sweetDialog.dismiss();


            try {

                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                String count = jo.getString("count");


                if (status.equals("success")) {

                    if(Integer.valueOf(count)>0){


                        JSONArray staff_datas = jo.getJSONArray("staff");
                        Log.e("tag_", "<-----staff_datas----->" + "" + staff_datas);

                        for (int i = 0; i < staff_datas.length(); i++) {

                            JSONObject datas = staff_datas.getJSONObject(i);


                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("staff_name", datas.getString("name"));
                            map.put("staff_phone", datas.getString("phone"));
                            map.put("staff_password", datas.getString("password"));

                            Log.d("tag", "" + map.get("staff_name"));
                            Log.d("tag", "" + map.get("staff_phone"));
                            Log.d("tag", "" + map.get("staff_password"));

                            ar_staff_name.add(i, datas.getString("name"));
                            ar_staff_phone.add(i, datas.getString("phone"));
                            ar_staff_pass.add(i, datas.getString("password"));

                        }

                        AdapterStaffEdit staff_adapter = new AdapterStaffEdit(AdminStaffEdit.this, ar_staff_name, ar_staff_phone, ar_staff_pass);
                        lv_staffsList.setAdapter(staff_adapter);
                    }

                    else{


                        new SweetAlertDialog(AdminStaffEdit.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("No Data Found")
                                .setContentText("Staff not created. Please Add Staff")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                        startActivity(i);
                                        AdminStaffEdit.this.finish();
                                    }
                                })
                                .show();

                    }



                }
                else {
                    Log.e("tag_", "error");
                    new SweetAlertDialog(AdminStaffEdit.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("No Network!!!")
                            .setContentText("Please Try Again Later.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();

                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                Log.e("tag_", "3" + e.getLocalizedMessage());

                new SweetAlertDialog(AdminStaffEdit.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("No Network!!!")
                        .setContentText("Please Try Again Later.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(i);
                                AdminStaffEdit.this.finish();
                            }
                        })
                        .show();
            }

        }

    }


}
