package fci.com.fci;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Salman on 6/22/2016.
 */
public class Dialog_ChooseCompany extends Dialog {

    Spinner spn_compname;
    Typeface tf;
    Activity activity;
    ImageView submit;
    ArrayList<CompanyFetchList> baL;
    ArrayList<String> boardlist;
    public String URL = Data_Service.SERVICE_URL_NEW + "company/fetch";
    String managername, managerphone;
    DbHelper dbclass;
    Context context = this.activity;

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
        new staffFetch_Task().execute();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dbclass.deletedata();
                String company_name = spn_compname.getSelectedItem().toString();
                Log.e("tag", "name1" + company_name);
                Log.e("tag", "managername" + managername);
                SharedPreferences s_pref = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor edit = s_pref.edit();
                edit.putString("companyname", company_name);
                edit.putString("managername", managername);
                edit.putString("managerphone", managerphone);
                edit.commit();

                Intent goEntry = new Intent(activity, StaffAddEntry.class);
                activity.startActivity(goEntry);
                dismiss();

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }


    class staffFetch_Task extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
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
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("tag", "<-----result---->" + s);
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
                        baL.add(bl);
                        boardlist.add(jsonobject.optString("company_name"));
                        Log.d("tag", "<----worldlist----->" + boardlist);
                    }

                    final CustomAdapter arrayAdapter = new CustomAdapter(activity, R.layout.custom_spinner, boardlist) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {

                                return false;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);

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

                                Log.e("tag", "password" + managername);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
