package net.fciapp.fciscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Salman on 8/5/2016.
 */
public class Adapter_Add extends BaseAdapter {

    Activity activity;
    Context context;
    int count,stc,edc;
    DbHelper dbclass;

    ArrayList<String> vin_positions = new ArrayList<>();

    ArrayList<String> vin_no = new ArrayList<>();
    ArrayList<String> vin_makemodel = new ArrayList<>();
    ArrayList<String> vin_start_guage = new ArrayList<>();
    ArrayList<String> vin_end_guage = new ArrayList<>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    Adapter_Add(Activity get_activity, Context get_context,int get_count) {

        this.activity = get_activity;
        this.context = get_context;
        this.count = get_count;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(!(sharedPreferences.getString("size","").equals(""))) {
            if (Integer.valueOf(sharedPreferences.getString("size", "")) > 3) {
                this.count = Integer.valueOf(sharedPreferences.getString("size", ""));
            }
        }


    }


    @Override
    public int getCount() {
        Log.e("tag","count "+count);
        return count;
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

        TextView tv_vinno,tv_make,btn_add;
        final Spinner spn_start,spn_end;
        ArrayAdapter<String> start_adapter,end_adapter;
        String tanks[] = {"1/2 tank", "1/4 tank","1/8 tank","3/4 tank"};
  /*      ArrayList<String> vin_positions ;
        ArrayList<String> vin_no;
        ArrayList<String> vin_makemodel;
        ArrayList<String> vin_start_guage;
        ArrayList<String> vin_end_guage;*/



        if (convertView == null) {
            Log.e("tag","inside_convertview");
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.adapter_addentry, parent, false);

            tv_vinno = (TextView) convertView.findViewById(R.id.tv_vinno);
            tv_make = (TextView) convertView.findViewById(R.id.tv_make);
            spn_start = (Spinner) convertView.findViewById(R.id.tv_startgug);
            spn_end = (Spinner) convertView.findViewById(R.id.tv_endgug);



            start_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tanks);
            end_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tanks);

            start_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            end_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spn_start.setAdapter(start_adapter);
            spn_end.setAdapter(end_adapter);

            Log.e("tag","inside_convertview"+vin_positions.size());
            Log.e("tag","positions"+position);
            dbclass = new DbHelper(context);
           /* vin_positions = new ArrayList<>();
            vin_no = new ArrayList<>();
            vin_makemodel = new ArrayList<>();
            vin_start_guage = new ArrayList<>();
            vin_end_guage = new ArrayList<>();*/

            convertView.setTag(new ViewHolder(tv_vinno,tv_make,spn_start,spn_end));
        }
        else{

            Log.e("tag","inside_convertview_else");
            Log.e("tag","positions"+position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            tv_vinno = viewHolder.tv_vinno;
            tv_make = viewHolder.tv_make;
            spn_start = viewHolder.spn_start;
            spn_end = viewHolder.spn_end;

            count = Integer.valueOf(sharedPreferences.getString("size", ""));
            notifyDataSetChanged();
        }


        stc = 0;
        edc = 0;

        getFromDb();

        if(vin_positions.size()>0){

            notifyDataSetChanged();
            Log.e("tag","adatpter_settext");

            for (int i =0; i<vin_positions.size();i++){
                Log.e("tag__","<0>"+i);
                Log.e("tag__","<1>"+position +"--|--"+Integer.valueOf(vin_positions.get(i)));
                if(position == i){
                    Log.e("tag__","<2>"+position +"--|--"+Integer.valueOf(vin_positions.get(i)));
                    Log.e("tag__","data"+i+"\t"+position+"\t"+vin_no.get(i)+"\t"+vin_makemodel.get(i)+"\t"+vin_start_guage.get(i)+"\t"+vin_start_guage.get(i));

                    tv_vinno.setText(vin_no.get(i));
                    tv_make.setText(vin_makemodel.get(i));
                    spn_start.setSelection(Integer.parseInt(vin_start_guage.get(i)));
                    spn_end.setSelection(Integer.parseInt(vin_end_guage.get(i)));
                    //  Log.e("tag","data"+i+"\t"+pos_view+"\t"+vin_no.get(i)+"\t"+vin_makemodel.get(i)+"\t"+vin_start_guage.get(i)+"\t"+vin_start_guage.get(i));
                }

            }
        }


     //   btn_add = (TextView) activity.findViewById(R.id.tv_add_another);


        Log.e("tag","view"+vin_positions.size());
        Log.e("tag","positions"+position);


       spn_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Log.e("tag","spin0");

               if(stc == 0)
               {
                   stc =1;

                   Log.e("tag","spin00");
               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
               Log.e("tag","spin1");
           }
       });


        spn_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("tag","spin1");

                if(edc ==0)
                {
                    edc = 1;
                    Log.e("tag","spin11");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("tag","spin1");
            }
        });


        tv_vinno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent goScan = new Intent(context, ScanActivity.class);
                goScan.putExtra("pos", position);
                goScan.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(goScan);

                vin_positions.add(String.valueOf(position));*/



            }
        });


      /*  btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = count+1;

                editor = sharedPreferences.edit();
                editor.putString("size",String.valueOf(count));
                editor.commit();



               // notifyDataSetChanged();
            }
        });*/






        return convertView;
    }



    private void getFromDb() {

        vin_positions.clear();
        vin_no.clear();
        vin_makemodel.clear();
        vin_start_guage.clear();
        vin_end_guage.clear();




        Log.e("tag", "getfromdb_class");
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
    }







    private static class ViewHolder {

        public final Spinner spn_start,spn_end;
        public final TextView tv_vinno,tv_make;

        public ViewHolder(TextView text_vinno,TextView text_make,Spinner start,Spinner end) {
            this.tv_vinno = text_vinno;
            this.tv_make = text_make;
            this.spn_start = start;
            this.spn_end = end;
        }
    }

}



