package net.fciapp.fciscanner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Salman on 6/22/2016.
 */
public class Dialog_ChooseCompany extends Dialog {

    public String URL = Data_Service.SERVICE_URL_NEW + "company/fetch";
    Spinner spn_compname;
    Typeface tf;
    Activity activity;
    ImageView submit;
    ArrayList<CompanyFetchList> baL;
    ArrayList<String> boardlist;
    String managername, managerphone, alt_mgr, alt_ph;
    DbHelper dbclass;
    Context context = this.activity;
    SweetAlertDialog sweetDialog;
    View progress;
    TextView header;
    ArrayList<String> alt_managerName = new ArrayList<>();
    ArrayList<String> alt_phone = new ArrayList<>();
    int comp_sts;


    public Dialog_ChooseCompany(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_choosecompany);
        dbclass = new DbHelper(context);
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/asin.TTF");
        spn_compname = (Spinner) findViewById(R.id.spn_comp_name);
        submit = (ImageView) findViewById(R.id.submit);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        header = (TextView) findViewById(R.id.header);

        header.setTypeface(tf);


        if (Util.Operations.isOnline(activity)) {
            new companyFetch_Task().execute();
        } else {
            new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING MESSAGE!!!")
                    .setContentText("No Internet Connectivity")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent i = new Intent(activity.getApplicationContext(), StaffDashboard.class);
                            context.startActivity(i);
                            activity.finish();
                        }
                    })
                    .show();
        }





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dbclass.deletedata();

                String company_name = spn_compname.getSelectedItem().toString();
                if(!(company_name.equals(null))){


                Log.e("tag", "name1" + company_name);
                Log.e("tag", "managername" + managername);
                SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor edit = s_pref.edit();
                edit.putString("companyname", company_name);
                edit.putString("managername", managername);
                edit.putString("managerphone", managerphone);
                    edit.putString("alt_managername", alt_mgr);
                    edit.putString("alt_managerphone", alt_ph);
                edit.commit();

                    if(company_name.contains("avis") || company_name.contains("AVIS") || company_name.contains("Avis")){
                        comp_sts= 1;
                        Log.e("tag",comp_sts+" av: "+company_name);
                    }
                    else{
                        comp_sts =0;
                        Log.e("tag",comp_sts+" anfv: "+company_name);
                    }


                Intent goEntry = new Intent(activity, __Staff.class);
                    goEntry.putExtra("comp_sts",comp_sts);
                activity.startActivity(goEntry);
                    dismiss();
                }
                else{
                    Toast.makeText(activity,"Please Wait",Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }


    class companyFetch_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            progress.setVisibility(View.VISIBLE);
            submit.setEnabled(false);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            baL = new ArrayList<CompanyFetchList>();
            boardlist = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = PostService.makeRequest(URL, json);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());

            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("tag", "<-----result---->" + s);
           // sweetDialog.dismiss();
            progress.setVisibility(View.GONE);
            submit.setEnabled(true);
            super.onPostExecute(s);
            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");
                Log.d("tag", "<-----Status----->" + status);
                if (status.equals("success")) {
                    JSONArray jsonarray = jo.getJSONArray("company");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        CompanyFetchList bl = new CompanyFetchList();
                        bl.setCompanyname(jsonobject.optString("company_name"));
                        bl.setManagername(jsonobject.optString("mgr_name"));
                        bl.setManagerphone(jsonobject.optString("mgr_phone"));

                        alt_managerName.add(i, jsonobject.getString("alt_mgr_name"));
                        alt_phone.add(i, jsonobject.getString("alt_mgr_phone"));


                        baL.add(bl);
                        boardlist.add(jsonobject.optString("company_name"));
                        Log.d("tag", "<----worldlist----->" + boardlist);
                    }
                    context = activity.getApplicationContext();

                    final CustomAdapter arrayAdapter = new CustomAdapter(activity, R.layout.list, boardlist) {
                        @Override
                        public boolean isEnabled(int position) {

                            return true;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            TextView txt = (TextView) view.findViewById(R.id.text1);
                            txt.setTypeface(tf, 1);
                            txt.setTextSize(28);

                            txt.setTextColor(context.getResources().getColor(R.color.sweet_dialog_bg_color));
                            return view;
                        }

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            // view.setBackgroundColor(activity.getResources().getColor(R.color.darkblk));
                            TextView txt = (TextView) view.findViewById(R.id.text1);
                            txt.setTypeface(tf);
                            txt.setTextSize(25);
                            txt.setTextColor(activity.getResources().getColor(R.color.whit));
                            view.setBackground(activity.getResources().getDrawable(R.drawable.spinnerbb));

                            return view;
                        }
                    };

                    spn_compname.setAdapter(arrayAdapter);


                    spn_compname.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position >= 0) {
                                managername = baL.get(position).getManagername();
                                managerphone = baL.get(position).getManagerphone();
                                alt_mgr = alt_managerName.get(position);
                                alt_ph = alt_phone.get(position);
                                Log.e("tag", "password" + managername + alt_ph + alt_mgr);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
                progress.setVisibility(View.GONE);


                new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Network Error.")
                        .setContentText("Try Again Later.")
                        .setConfirmText("OK")
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
