package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
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



public class AdapterAddEntry extends BaseAdapter {
    static int sizz;
    public String dddd;
    public int post;
    public HashMap<Integer, String> vin_make = new HashMap<>();
    public int p;
    Context context;

    LinearLayout lt_bg;
    ArrayList<String> ar_name = new ArrayList<>();
    ArrayList<String> ar_phone = new ArrayList<>();
    ArrayList<String> ar_pass = new ArrayList<>();
    Typeface tf;
    Holder holder;
    ArrayList<Integer> v_pos = new ArrayList<>();
    ArrayList<String> v_mm = new ArrayList<>();
    ArrayList<String> final_datas = new ArrayList<>();
    ArrayList<String> final_datas1 = new ArrayList<>();
    DbHelper dbclass;
    ArrayList<String> vin_positions = new ArrayList<>();
    ArrayList<String> vin_no = new ArrayList<>();
    ArrayList<String> vin_makemodel = new ArrayList<>();
    ArrayList<String> vin_start_guage = new ArrayList<>();
    ArrayList<String> vin_end_guage = new ArrayList<>();
    TextView tv_add_another,tv_save;
    Activity activity;
    StaffAddEntry staffaddEntry;
    ArrayList<String> s = new ArrayList<>();
    String tanks[] = {"1/2 tank", "1/4 tank","1/8 tank","3/4 tank"};
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
        this.sizz = k;

       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!(sharedPreferences.getString("size","").equals(""))) {
            Log.e("tag","adapter_size: "+Integer.valueOf(sharedPreferences.getString("size","")));

            if (Integer.valueOf(sharedPreferences.getString("size", "")) > 3) {
                sizz = Integer.valueOf(sharedPreferences.getString("size", ""));
            }
        }
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
        return vin_positions.get(position);
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


          //  tv_add_another = (TextView) activity.findViewById(R.id.tv_add_another);

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

            // putData(position);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tanks);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.tv_startgug.setAdapter(spinnerArrayAdapter);
            holder.tv_endgug.setAdapter(spinnerArrayAdapter);


        } else {

        }







        p = pos_view;
        Log.e("tag", "positions " + pos_view);
        getFromDb();

         notifyDataSetChanged();
           if(vin_positions.size()>0){

                notifyDataSetChanged();
                Log.e("tag","adatpter_settext");

                for (int i =0; i<vin_positions.size();i++){
                    Log.e("tag__","<0>"+i);
                    Log.e("tag__","<1>"+pos_view +"--|--"+Integer.valueOf(vin_positions.get(i)));
                    if(pos_view == i){
                        Log.e("tag__","<2>"+pos_view +"--|--"+Integer.valueOf(vin_positions.get(i)));
                        Log.e("tag__","data"+i+"\t"+pos_view+"\t"+vin_no.get(i)+"\t"+vin_makemodel.get(i)+"\t"+vin_start_guage.get(i)+"\t"+vin_start_guage.get(i));

                        holder.tv_vinno.setText(vin_no.get(i));
                        holder.tv_make.setText(vin_makemodel.get(i));
                        holder.tv_startgug.setSelection(Integer.parseInt(vin_start_guage.get(i)));
                        holder.tv_endgug.setSelection(Integer.parseInt(vin_end_guage.get(i)));
                      //  Log.e("tag","data"+i+"\t"+pos_view+"\t"+vin_no.get(i)+"\t"+vin_makemodel.get(i)+"\t"+vin_start_guage.get(i)+"\t"+vin_start_guage.get(i));
                    }

               }
            }


           holder.tv_startgug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dbclass.insertIntoDB2(p, position);
                    //Log.e("tag","click_start "+ pos_view+position);
                    holder.tv_startgug.setSelection(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder.tv_endgug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dbclass.insertIntoDB3(p, position);
                   // Log.e("tag","click_end "+ pos_view+position);
                    holder.tv_endgug.setSelection(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            holder.tv_vinno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("tag", "vinno_clicked"+pos_view);

                }
            });








        return convertView;
    }

    private void getFromDb() {

        vin_positions.clear();
        vin_no.clear();
        vin_makemodel.clear();
        vin_start_guage.clear();
        vin_end_guage.clear();


        Log.e("tag", "adapter_getfromdb");
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

        Log.e("tag", "adapter_length: " + vin_positions.size());

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
