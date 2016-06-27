package fci.com.fci;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class AdapterAddEntry extends BaseAdapter {

    // public TextView tv_vinno, tv_make, tv_startgug, tv_endgug;
    public String dddd;
    public int post;
    public HashMap<Integer, String> vin_make = new HashMap<>();
    Context context;
    Activity activity;
    ArrayList<UserData> arrayList;
    LinearLayout lt_bg;
    ArrayList<String> ar_name = new ArrayList<>();
    ArrayList<String> ar_phone = new ArrayList<>();
    ArrayList<String> ar_pass = new ArrayList<>();
    Typeface tf;
    SweetAlertDialog sweetDialog;
    Holder holder;
    ArrayList<Integer> v_pos = new ArrayList<>();
    ArrayList<String> v_mm = new ArrayList<>();
    ArrayList<String> final_datas = new ArrayList<>();
    ArrayList<String> final_datas1 = new ArrayList<>();
    int sizz;
    DbHelper dbclass;
    ArrayList<String> getdata = new ArrayList<>();
    ArrayList<String> getdata1 = new ArrayList<>();
    ArrayList<String> getdata2 = new ArrayList<>();
    private ZXingScannerView mScannerView;


    AdapterAddEntry(Context c1, HashMap<Integer, String> v_mk, ArrayList<Integer> v_p, ArrayList<String> aa, int k) {
        this.context = c1;
        if (!(dddd == null)) {
            Log.e("tag", "outside" + dddd);
        }
        this.vin_make = v_mk;
        this.v_pos = v_p;
        this.v_mm = aa;
        this.sizz = k;
    }


    public void getData() {

        //new getVin_Make(dddd).execute();

        /*if (!(dddd.isEmpty())) {
            Log.e("tag", "" + dddd + "pos--" + post);
            //tv_vinno.setText(dddd);


        }*/


    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
       /* if (!(dddd.isEmpty())) {
            Log.d("tag", "notify" + dddd );
        }*/


    }

    @Override
    public int getCount() {
        return sizz;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflat = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflat.inflate(R.layout.adapter_addentry, null);
        holder = (Holder) convertView.getTag();
        holder = new Holder(convertView);
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");
        // holder.tv_vinno = (TextView) convertView.findViewById(R.id.tv_vinno);
        holder.tv_make = (TextView) convertView.findViewById(R.id.tv_make);
        holder.tv_startgug = (TextView) convertView.findViewById(R.id.tv_startgug);
        holder.tv_endgug = (TextView) convertView.findViewById(R.id.tv_endgug);
        holder.tv_vinno.setTypeface(tf);
        holder.tv_make.setTypeface(tf);
        holder.tv_startgug.setTypeface(tf);
        holder.tv_endgug.setTypeface(tf);

        dbclass = new DbHelper(context);

        getFromDb();

        putData(position);










     /*   Log.e("tag", "adapterTag" + dddd);

        if (!(dddd == null)) {
            if (!(dddd.isEmpty())) {
                Log.e("tag", "outside" + dddd);
            }
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!(sharedPreferences.getString("data", "").isEmpty())) {

            String vinno = sharedPreferences.getString("data", "");
            String asd = sharedPreferences.getString("pos", "");
            String make = sharedPreferences.getString("make", "");
            int ss = Integer.valueOf(asd);

        }

        if (!(sharedPreferences.getString("vv_vin" + position, "").isEmpty())) {

            for (int i = 0; i < 2; i++) {
                final_datas.add(sharedPreferences.getString("vv_vin" + position, ""));
                final_datas1.add(sharedPreferences.getString("vv_make" + position, ""));
            }

        }

        if (!(final_datas.isEmpty())) {
            for (int i = 0; i < final_datas.size(); i++) {

                holder.tv_vinno.setText(final_datas.get(i));
                holder.tv_make.setText(final_datas1.get(i));
            }
        }


        Log.e("tag", "" + v_pos.size());
        Log.e("tag", "" + v_mm.size());*/

      /*  if (!(v_pos.isEmpty())) {
            for (int i = 0; i < v_pos.size(); i++) {
                if (v_pos.get(i).equals(position)) {

                    holder.tv_vinno.setText(v_mm.get(i));
                    holder.tv_make.setText(make);
                }
            }

        }*/


      /*  if (ss == position) {

            if (!(vinno.isEmpty())) {
                holder.tv_vinno.setText(vinno);
                holder.tv_make.setText(make);


            }
        }
*/




       /* if(!(vin_make.isEmpty())){
            for (int i = 0; i< vin_make.size(); i++){
                if(vin_make.get(position).equals(position)){
                    holder.tv_vinno.setText(vin_make.get(position));

                }
            }
        }*/


        holder.tv_vinno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "vinno_clicked");

                Intent goScan = new Intent(context, BarScan.class);
                goScan.putExtra("pos", position);
                context.startActivity(goScan);


                for (int i = 0; i < sizz; i++) {

                    if (!(holder.tv_vinno.getText().toString().contains("Scan Vin"))) {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("vv_vin" + position, holder.tv_vinno.getText().toString());
                        editor.putString("vv_make" + position, holder.tv_make.getText().toString());
                        editor.commit();
                    }
                }

                post = position;

                if (!(dddd == null)) {
                    if (!(dddd.isEmpty())) {
                        Log.e("tag", "" + dddd);
                    }
                }


            }
        });


        return convertView;
    }

    private void getFromDb() {

        Log.e("tag", "getfromdb");

        Cursor cursor = dbclass.getFromDb();


        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    String id = cursor.getString(cursor.getColumnIndex("pos"));
                    String vin_no = cursor.getString(cursor.getColumnIndex("vinno"));
                    String make = cursor.getString(cursor.getColumnIndex("make"));

                    getdata.add(id);
                    getdata1.add(vin_no);
                    getdata2.add(make);

                } while (cursor.moveToNext());
            }
        }

        Log.e("tag","getfromdb"+getdata.size()+getdata1.size()+getdata2.size());


    }

    private void putData(int position) {
        Log.e("tag", "putdata");

        for (int k = 0; k < getdata.size(); k++) {

            int da = Integer.parseInt(getdata.get(k));

            if (da == position) {

                holder.tv_vinno.setText(getdata1.get(k));
                holder.tv_make.setText(getdata2.get(k));

            }

        }

    }


    class getVin_Make extends AsyncTask<String, Void, String> {

        String vin_no;
        int pos;

        //https://api.edmunds.com/api/vehicle/v2/vins/2G1FC3D33C9165616?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9

        String web_p1 = "https://api.edmunds.com/api/vehicle/v2/vins/";

        String web_p2 = "?fmt=json&api_key=zucnv9yrgtcgqdnxk7f5xzx9";

        public getVin_Make(String vinno) {

            this.vin_no = vinno;
            // this.pos = pos;

        }

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {

            String json = "", jsonStr = "";


            try {
                vin_no = "2G1FC3D33C9165616";
                String virtual_url = web_p1 + vin_no + web_p2;

                JSONObject jsonobject = PostService.getVin(virtual_url);

                Log.d("tag", "" + jsonobject.toString());

                if (jsonobject.toString() == "sam") {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops!")
                            .setContentText("Try Check your Network")
                            .setConfirmText("OK")
                            .show();
                }

                json = jsonobject.toString();

                return json;
            } catch (Exception e) {
                Log.e("InputStream", "" + e.getLocalizedMessage());
                jsonStr = "";
            }
            return jsonStr;

        }

        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----rerseres---->" + jsonStr);
            super.onPostExecute(jsonStr);


            try {

                JSONObject jo = new JSONObject(jsonStr);

                // String status = jo.getString("status");
                // String msg = jo.getString("make");

                if (!(jo.getString("make").isEmpty())) {
                    String msg = jo.getString("make");
                    Log.e("tag", "<>" + msg);

                    JSONObject data = new JSONObject(msg);

                    String name = data.getString("name");
                    Log.e("tag", "<>" + name);


                    //notifyDataSetChanged();

//                    holder.tv_make.setText(name);

                    Intent asdf = new Intent(context, StaffAddEntry.class);
                    context.startActivity(asdf);


                } else if (!(jo.getString("make").isEmpty())) {
                    String status = jo.getString("status");
                    Log.e("tag", "<>" + status);
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }


    final class Holder {
        public TextView mTVItem;
        TextView tv_vinno;
        TextView tv_make;
        TextView tv_startgug;
        TextView tv_endgug;


        public Holder(View base) {
            tv_vinno = (TextView) base.findViewById(R.id.tv_vinno);

        }


    }
}
