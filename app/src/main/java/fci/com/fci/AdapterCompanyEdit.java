package fci.com.fci;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Salman on 6/16/2016.
 */
public class AdapterCompanyEdit extends BaseAdapter {

    Context context;
    ArrayList<UserData> arrayList;
    TextView tv_name, tv_phone, tv_password, tv_editview;
    LinearLayout lt_bg;
    ArrayList<String> com_name = new ArrayList<>();
    ArrayList<String> com_phone = new ArrayList<>();
    ArrayList<String> com_email = new ArrayList<>();
    ArrayList<String> manager_name = new ArrayList<>();
    ArrayList<String> manager_phone = new ArrayList<>();
    ArrayList<String> com_loc = new ArrayList<>();

    //ArrayList<String> ar_pass = new ArrayList<>();
    Typeface tf;
    SweetAlertDialog sweetDialog;


    AdapterCompanyEdit(Context c1, ArrayList<String> name, ArrayList<String>location, ArrayList<String> email,ArrayList<String> manager_phone) {
        this.context = c1;
        this.com_name = name;
        this.com_email = email;
        this.manager_phone = manager_phone;
        this.com_loc=location;


    }


    @Override
    public int getCount() {
        return com_name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflat = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflat.inflate(R.layout.adapter_staffedit, null);

        tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");

        tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
        tv_password = (TextView) convertView.findViewById(R.id.tv_password);
        tv_editview = (TextView) convertView.findViewById(R.id.tv_editview);
        lt_bg = (LinearLayout) convertView.findViewById(R.id.layout_bg);

        tv_name.setTypeface(tf);
        tv_phone.setTypeface(tf);
        tv_password.setTypeface(tf);
        tv_editview.setTypeface(tf);


        if (position % 2 == 0) {
            lt_bg.setBackgroundColor(context.getResources().getColor(R.color.bg1));


        } else {
            lt_bg.setBackgroundColor(context.getResources().getColor(R.color.bg2));
        }


        tv_name.setText(com_name.get(position));
        tv_phone.setText(com_email.get(position));
        tv_password.setText(com_loc.get(position));


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("tag", "-" + com_name.get(position));


                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you Sure want to Delete?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                new staffDelete_Task(com_name.get(position)).execute();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

                return false;
            }
        });

        tv_editview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d("tag", "-" + ar_phone.get(position) + "-" + ar_name.get(position) + "-" + ar_pass.get(position));

                Intent goupdate = new Intent(context, AdminCreateCompany.class);
                goupdate.putExtra("sts",1);
                goupdate.putExtra("name",com_name.get(position));
                goupdate.putExtra("location",com_loc.get(position) );
                goupdate.putExtra("phone",manager_phone.get(position));
                goupdate.putExtra("email",com_email.get(position));
                context.startActivity(goupdate);
            }
        });


        return convertView;
    }


    class staffDelete_Task extends AsyncTask<String, Void, String> {

        String phone;

        public staffDelete_Task(String ph) {
            this.phone = ph;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            sweetDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
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
                jsonObject.accumulate("company_name", phone);
                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();
                return jsonStr = PostService.makeRequest(Data_Service.SERVICE_URL_NEW + "company/delete", json);
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
                   // notifyDataSetChanged();
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(msg)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                  //  notifyDataSetChanged();
                                    Intent goDash = new Intent(context, AdminCreateCompany.class);
                                    context.startActivity(goDash);

                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
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
