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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
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
    static int sizz;
    DbHelper dbclass;
    ArrayList<String> getdata = new ArrayList<>();
    ArrayList<String> getdata1 = new ArrayList<>();
    ArrayList<String> getdata2 = new ArrayList<>();
    ArrayList<String> getStart = new ArrayList<>();
    ArrayList<String> getEnd = new ArrayList<>();


    StaffAddEntry staffaddEntry;
    private ZXingScannerView mScannerView;
    ArrayList<String> s = new ArrayList<>();

    String tanks[] = {"1/2 tank", "1/4 tank", "3/4 tank"};
    String spn;

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

    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

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
        staffaddEntry = new StaffAddEntry();
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");
        // holder.tv_vinno = (TextView) convertView.findViewById(R.id.tv_vinno);
        holder.tv_make = (TextView) convertView.findViewById(R.id.tv_make);
        holder.tv_startgug = (Spinner) convertView.findViewById(R.id.tv_startgug);
        holder.tv_endgug = (Spinner) convertView.findViewById(R.id.tv_endgug);
        holder.tv_vinno.setTypeface(tf);
        holder.tv_make.setTypeface(tf);
        dbclass = new DbHelper(context);
        getFromDb();
        putData(position);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tanks);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.tv_startgug.setAdapter(spinnerArrayAdapter);
        holder.tv_endgug.setAdapter(spinnerArrayAdapter);
        holder.tv_startgug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spn = holder.tv_startgug.getSelectedItem().toString();
                dbclass.insertIntoDB2(position, spn);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        holder.tv_endgug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spn = holder.tv_endgug.getSelectedItem().toString();
                dbclass.insertIntoDB3(position, spn);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        staffaddEntry.name.put(position, holder.tv_make.getText().toString());


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
                    String start = cursor.getString(cursor.getColumnIndex("st_g"));
                    String end = cursor.getString(cursor.getColumnIndex("ed_g"));
                    getdata.add(id);
                    getdata1.add(vin_no);
                    getdata2.add(make);
                    getStart.add(start);
                    getEnd.add(end);
                } while (cursor.moveToNext());

            }
        }

        Log.e("tag", "getfromdb" + getdata.size() + getdata1.size() + getdata2.size());
        Log.e("tag", "starttt" + getStart.size() + getStart);


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

    public static String getAllValues() {

        for (int i = 0; i < sizz; i++)
        {
            ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> row1 = new HashMap<String, String>();
            data.add(row1);

        }

        return "";
    }




    final class Holder {
        public TextView mTVItem;
        TextView tv_vinno;
        TextView tv_make;
        Spinner tv_startgug;
        Spinner tv_endgug;


        public Holder(View base) {
            tv_vinno = (TextView) base.findViewById(R.id.tv_vinno);

        }


    }


}
