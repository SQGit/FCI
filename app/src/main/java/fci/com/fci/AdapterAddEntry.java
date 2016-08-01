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
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class AdapterAddEntry extends BaseAdapter {
    public String dddd;
    public int post;
    public HashMap<Integer, String> vin_make = new HashMap<>();
    Context context;
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
    ArrayList<String> vin_positions = new ArrayList<>();
    ArrayList<String> vin_no = new ArrayList<>();
    ArrayList<String> vin_makemodel = new ArrayList<>();
    ArrayList<String> vin_start_guage = new ArrayList<>();
    ArrayList<String> vin_end_guage = new ArrayList<>();
    TextView tv_add_another,tv_save;
    Activity activity;
    public int p;

    StaffAddEntry staffaddEntry;
    private ZXingScannerView mScannerView;
    ArrayList<String> s = new ArrayList<>();
    String tanks[] = {"1/2 tank", "1/4 tank", "3/4 tank"};
    String spn;

    AdapterAddEntry(Activity act,Context c1, HashMap<Integer, String> v_mk, ArrayList<Integer> v_p, ArrayList<String> aa, int k) {

        this.context = c1;
        this.activity = act;
        if (!(dddd == null)) {
            Log.e("tag", "outside" + dddd);
        }
        this.vin_make = v_mk;
        this.v_pos = v_p;
        this.v_mm = aa;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!(sharedPreferences.getString("size","").equals(""))) {
            Log.e("tag","size: "+Integer.valueOf(sharedPreferences.getString("size","")));

            if (Integer.valueOf(sharedPreferences.getString("size", "")) > 3) {
                sizz = Integer.valueOf(sharedPreferences.getString("size", ""));
            } else {
                sizz = 3;
            }
        }
        else{
            sizz = 3;
        }

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
    public View getView(final int pos_view, View convertView, ViewGroup parent) {

        LayoutInflater inflat = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = inflat.inflate(R.layout.adapter_addentry, null);
            tv_add_another = (TextView) activity.findViewById(R.id.tv_add_another);
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
            p = pos_view;
            Log.e("tag", "position_view " + p);
            getFromDb();
            // putData(position);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tanks);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.tv_startgug.setAdapter(spinnerArrayAdapter);
            holder.tv_endgug.setAdapter(spinnerArrayAdapter);


            if(vin_positions.size()>0){

                Log.e("tag","settext");

                for (int i =0; i<vin_positions.size();i++){

                    if(pos_view == i){
                        holder.tv_vinno.setText(vin_no.get(i));
                        holder.tv_make.setText(vin_makemodel.get(i));
                        holder.tv_startgug.setSelection(Integer.parseInt(vin_start_guage.get(i)));
                        holder.tv_endgug.setSelection(Integer.parseInt(vin_end_guage.get(i)));
                        Log.e("tag","data"+i+"\t"+pos_view+"\t"+vin_no.get(i)+"\t"+vin_makemodel.get(i)+"\t"+vin_start_guage.get(i)+"\t"+vin_start_guage.get(i));



                    }
                }
            }









            holder.tv_startgug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dbclass.insertIntoDB2(pos_view, position);
                    Log.e("tag","click_start "+ pos_view+position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder.tv_endgug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dbclass.insertIntoDB3(pos_view, position);
                    Log.e("tag","click_end "+ pos_view+position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });




           /* holder.tv_endgug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // spn = String.valueOf(holder.tv_startgug.getSelectedItemPosition());
                    //  Log.d("tag", "spn_position" + spn + "POSSSSSSSSSSSSS" + String.valueOf(pos_view));
                    int pio = holder.tv_endgug.getSelectedItemPosition();
                    dbclass.insertIntoDB2(p, pio);
                    Log.e("tag","click_end "+ p+pio);
                }
            });*/

            tv_add_another.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("tag","insideclidk "+sizz);
                    sizz = sizz+1;
                    notifyDataSetChanged();
                    Log.e("tag","insideclidk1 "+sizz);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("size",String.valueOf(sizz));
                    // editor.putString("vv_make" + pos_view, holder.tv_make.getText().toString());
                    editor.commit();

                    Log.e("tag","size1: "+Integer.valueOf(sharedPreferences.getString("size","")));
                }
            });


            /*holder.tv_endgug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spn = String.valueOf(holder.tv_endgug.getSelectedItemPosition());

                    dbclass.insertIntoDB3(p, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/


            staffaddEntry.name.put(pos_view, holder.tv_make.getText().toString());


            holder.tv_vinno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("tag", "vinno_clicked"+pos_view);
                    Intent goScan = new Intent(context, BarScan.class);
                    goScan.putExtra("pos", pos_view);
                    context.startActivity(goScan);

                 /*   for (int i = 0; i < sizz; i++) {
                        if (!(holder.tv_vinno.getText().toString().contains("Scan Vin"))) {
                            // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            //  SharedPreferences.Editor editor = sharedPreferences.edit();
                            // editor.putString("vv_vin" + pos_view, holder.tv_vinno.getText().toString());
                            // editor.putString("vv_make" + pos_view, holder.tv_make.getText().toString());
                            //  editor.commit();


                        }
                    }
                    //post = pos_view;
                    if (!(dddd == null)) {
                        if (!(dddd.isEmpty())) {
                            Log.e("tag", "" + dddd);
                        }
                    }*/


                }
            });
        } else {

        }







        return convertView;
    }

    private void getFromDb() {

        vin_positions.clear();
        vin_no.clear();
        vin_makemodel.clear();
        vin_start_guage.clear();
        vin_end_guage.clear();


        Log.e("tag", "getfromdb");
        Cursor cursor = dbclass.getFromDb();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex("pos"));
                    String vin_nos = cursor.getString(cursor.getColumnIndex("vinno"));
                    String make = cursor.getString(cursor.getColumnIndex("make"));
                    String start = cursor.getString(cursor.getColumnIndex("st_g"));
                    String end = cursor.getString(cursor.getColumnIndex("ed_g"));
                    vin_positions.add(id);
                    vin_no.add(vin_nos);
                    vin_makemodel.add(make);
                    vin_start_guage.add(start);
                    vin_end_guage.add(end);

                } while (cursor.moveToNext());

            }
        }

        Log.e("tag", "length: " + vin_positions.size());

        //   Log.e("tag", "getfromdb" + vin_positions.size() + vin_no.size() + vin_makemodel.size());
        // Log.e("tag", "starttt" + getStart.size() + getStart);
        // putData(p);
    }


    private void putData(int position) {
        Log.e("tag", "putdata");
        for (int k = 0; k < vin_positions.size(); k++) {


            Log.e("tag", "position: " + position);

            for (int l = 0; l < sizz; l++) {

                if (l == k) {

                    holder.tv_vinno.setText(vin_no.get(k));
                    holder.tv_make.setText(vin_makemodel.get(k));
                    holder.tv_startgug.setSelection(Integer.parseInt(vin_start_guage.get(k)));
                    holder.tv_endgug.setSelection(Integer.parseInt(vin_end_guage.get(k)));

                    Log.e("tag", "" + vin_no.get(k));
                    Log.e("tag", "" + vin_makemodel.get(k));
                    Log.e("tag", "" + vin_start_guage.get(k));
                    Log.e("tag", "" + vin_end_guage.get(k));
                }

            }


        }

    }

    public static String getAllValues() {

        for (int i = 0; i < sizz; i++) {
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
