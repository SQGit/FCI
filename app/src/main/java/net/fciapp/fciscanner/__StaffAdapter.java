package net.fciapp.fciscanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SMK on 8/6/2016.
 */
public class __StaffAdapter extends BaseAdapter {

    public int count;
    ArrayList myList = new ArrayList();
    LayoutInflater inflater;
    Context context;
    DbHelper dbclass;
    int limit;
    Typeface tf;
    ArrayList<String> asd;
    ArrayList<String> ar_mva;
    int comp_sts;
    LinearLayout lt_mva;


    public __StaffAdapter(Context context, ArrayList
            array_list, int count, int limit,int comp_sts) {

        this.context = context;
        this.myList = array_list;
        this.count = count;
        inflater = LayoutInflater.from(this.context);
        this.limit = limit;
        this.comp_sts  = comp_sts;
        dbclass = new DbHelper(context);

    }

    @Override
    public int getCount() {
        return count;
    }

    public void goFocus(int count) {

    }


    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MyViewHolder mViewHolder;
        final TextView tv_vin_no, tv_vin_make, tv_add;
        final Spinner spin_start, spin_end, spin_mva;

        tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");
        //font


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.staff_adapter, parent, false);
            mViewHolder = new MyViewHolder(convertView);

            tv_vin_no = (TextView) convertView.findViewById(R.id.textview_vin_no);
            tv_vin_make = (TextView) convertView.findViewById(R.id.textview_vin_make);
            spin_start = (Spinner) convertView.findViewById(R.id.spinner_start);
            spin_end = (Spinner) convertView.findViewById(R.id.spinner_end);

            lt_mva = (LinearLayout) convertView.findViewById(R.id.mva_layout);



            tv_vin_no.setTypeface(tf);
            tv_vin_make.setTypeface(tf);

            asd = new ArrayList<>();
            ar_mva = new ArrayList<>();


            asd.add("1/8 Tank");
            asd.add("1/4 Tank");
            asd.add("1/2 Tank");
            asd.add("3/4 Tank");
            asd.add("Empty Tank");
            asd.add(("Full Tank"));

            ar_mva.add("S");
            ar_mva.add("SH");


            final CustomAdapter arrayAdapter = new CustomAdapter(context, R.layout.list, asd) {


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);

                    TextView staff = (TextView) view.findViewById(R.id.text1);
                    staff.setTypeface(tf, 1);
                    staff.setTextSize(15);

                    return view;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);

                    TextView staff_dropdown = (TextView) view.findViewById(R.id.text1);
                    staff_dropdown.setTypeface(tf);
                    staff_dropdown.setTextSize(14);

                    return view;
                }
            };

            final CustomAdapter arrayMVA = new CustomAdapter(context, R.layout.list_1, ar_mva) {


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);

                    TextView staff = (TextView) view.findViewById(R.id.text1);
                    staff.setTypeface(tf, 1);
                    staff.setTextSize(15);

                    return view;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);

                    TextView staff_dropdown = (TextView) view.findViewById(R.id.text1);
                    staff_dropdown.setTypeface(tf);
                    staff_dropdown.setTextSize(14);

                    return view;
                }
            };


            spin_start.setAdapter(arrayAdapter);
            spin_end.setAdapter(arrayAdapter);

            spin_mva = (Spinner) convertView.findViewById(R.id.spinner_mva);
            spin_mva.setAdapter(arrayMVA);


            if(comp_sts == 1){

                lt_mva.setVisibility(View.VISIBLE);
            }
            else{
                lt_mva.setVisibility(View.GONE);
            }




            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();

            tv_vin_no = mViewHolder.tv_vin_no;
            tv_vin_make = mViewHolder.tv_vin_make;
            spin_start = mViewHolder.spin_start;
            spin_end = mViewHolder.spin_end;
            spin_mva = mViewHolder.spin_mva;

        }


        if (position < limit) {
            __StaffData currentListData = (__StaffData) getItem(position);
            tv_vin_no.setText(currentListData.getVin_no());
            tv_vin_make.setText(currentListData.getVin_make());
            spin_start.setSelection(currentListData.getVin_start());
            spin_end.setSelection(currentListData.getVin_end());
            if(lt_mva.getVisibility()==View.VISIBLE)
            spin_mva.setSelection(currentListData.getVin_mva());
        } else {
            tv_vin_no.setText("Scan VinNo");
            tv_vin_make.setText("");
            spin_start.setSelection(5);
            spin_end.setSelection(1);
            if(lt_mva.getVisibility()==View.VISIBLE)
                spin_mva.setSelection(0);
        }


        tv_vin_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "click " + position);


                Intent goScan = new Intent(context, BarcodeCaptureActivity.class);
                goScan.putExtra("pos", position);
                goScan.putExtra("start", spin_start.getSelectedItemPosition());
                goScan.putExtra("end", spin_end.getSelectedItemPosition());
                if(lt_mva.getVisibility() == View.VISIBLE)
                goScan.putExtra("mva", spin_mva.getSelectedItemPosition());
                else
                goScan.putExtra("mva",0);
                goScan.putExtra("make", tv_vin_make.getText().toString());
                goScan.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                goScan.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                goScan.putExtra(BarcodeCaptureActivity.UseFlash, false);
                context.startActivity(goScan);




                /*dialog1 = new Dialog(context);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setCancelable(true);
                dialog1.setContentView(R.layout.choos);
                final RelativeLayout button_barcode = (RelativeLayout) dialog1.findViewById(R.id.layout_barcode);
                final RelativeLayout button_vinnumber = (RelativeLayout) dialog1.findViewById(R.id.layout_vinnumber);
                TextView txtscan = (TextView) dialog1.findViewById(R.id.tv_scan);
                TextView txtvin = (TextView) dialog1.findViewById(R.id.tv_vin);
                button_barcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent goScan = new Intent(context, BarcodeCaptureActivity.class);
                        goScan.putExtra("pos", position);
                        goScan.putExtra("start", spin_start.getSelectedItemPosition());
                        goScan.putExtra("end", spin_end.getSelectedItemPosition());
                        goScan.putExtra("make", tv_vin_make.getText().toString());
                        goScan.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        goScan.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                        goScan.putExtra(BarcodeCaptureActivity.UseFlash, false);
                        context.startActivity(goScan);

                        dialog1.dismiss();

                    }
                });

                button_vinnumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent goScan = new Intent(context, OcrCaptureActivity.class);
                        goScan.putExtra("pos", position);
                        goScan.putExtra("start", spin_start.getSelectedItemPosition());
                        goScan.putExtra("end", spin_end.getSelectedItemPosition());
                        goScan.putExtra("make", tv_vin_make.getText().toString());
                        goScan.putExtra(OcrCaptureActivity.AutoFocus, true);
                        goScan.putExtra(OcrCaptureActivity.UseFlash, false);
                        goScan.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(goScan);

                        dialog1.dismiss();
                    }
                });

                txtscan.setTypeface(tf);
                txtvin.setTypeface(tf);

                dialog1.show();*/


            }
        });


        spin_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                spin_start.setSelection(pos);
                Log.e("tag_spin", "" + pos);

                if (position < limit) {
                    dbclass.insertIntoDB2(position, pos);
                    __StaffData currentListData = (__StaffData) getItem(position);
                    currentListData.setVin_start(pos);
                    notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        spin_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String start = spin_start.getSelectedItem().toString();
                String end = spin_end.getSelectedItem().toString();
                int a = spin_start.getSelectedItemPosition();
                int b = spin_end.getSelectedItemPosition();

                spin_end.setSelection(pos);

                if (position < limit) {
                    dbclass.insertIntoDB3(position, pos);
                    __StaffData currentListData = (__StaffData) getItem(position);
                    currentListData.setVin_end(pos);
                    notifyDataSetChanged();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spin_mva.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spin_mva.setSelection(pos);
                if (position < limit) {
                    dbclass.insertIntoDB4(position, pos);
                    __StaffData currentListData = (__StaffData) getItem(position);
                    currentListData.setVin_mva(pos);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        return convertView;
    }


    private class MyViewHolder {
        TextView tv_vin_no, tv_vin_make;
        Spinner spin_start, spin_end, spin_mva;

        public MyViewHolder(View item) {
            tv_vin_no = (TextView) item.findViewById(R.id.textview_vin_no);
            tv_vin_make = (TextView) item.findViewById(R.id.textview_vin_make);
            spin_start = (Spinner) item.findViewById(R.id.spinner_start);
            spin_end = (Spinner) item.findViewById(R.id.spinner_end);
            spin_mva = (Spinner) item.findViewById(R.id.spinner_mva);
        }
    }


}
