package fci.com.fci;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Salman on 6/16/2016.
 */
public class AdapterAddEntry extends BaseAdapter implements ZXingScannerView.ResultHandler {

    Context context;
    Activity activity;
    ArrayList<UserData> arrayList;
    public TextView tv_vinno, tv_make, tv_startgug, tv_endgug;
    LinearLayout lt_bg;
    ArrayList<String> ar_name = new ArrayList<>();
    ArrayList<String> ar_phone = new ArrayList<>();
    ArrayList<String> ar_pass = new ArrayList<>();
    Typeface tf;
    private ZXingScannerView mScannerView;
    public String dddd;

    AdapterAddEntry(Context c1,Activity act,String asdf) {
        this.context = c1;
        this.activity = act;
       /* this.ar_name = name;
        this.ar_phone = phone;
        this.ar_pass = pass;*/
        notifyDataSetChanged();
        this.dddd = asdf;

        if(!(dddd == null)) {
                Log.d("tag", "outside" + dddd);
        }
    }


    @Override
    public int getCount() {
        return 3;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflat = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflat.inflate(R.layout.adapter_addentry, null);

        notifyDataSetChanged();
        notifyDataSetInvalidated();

        Log.d("tag","adapterTag");

        if(!(dddd == null)) {
            if (!(dddd.isEmpty())) {
                Log.d("tag", "outside" + dddd);
            }
        }



        tf = Typeface.createFromAsset(context.getAssets(), "fonts/asin.TTF");

        tv_vinno = (TextView) convertView.findViewById(R.id.tv_vinno);
        tv_make = (TextView) convertView.findViewById(R.id.tv_make);
        tv_startgug = (TextView) convertView.findViewById(R.id.tv_startgug);
        tv_endgug = (TextView) convertView.findViewById(R.id.tv_endgug);


        tv_vinno.setTypeface(tf);
        tv_make.setTypeface(tf);
        tv_startgug.setTypeface(tf);
        tv_endgug.setTypeface(tf);


        tv_vinno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag","vinno clicked");

                Intent goScan = new Intent(context,BarScan.class);
                context.startActivity(goScan);

              /*  BarScan cdd = new BarScan(context);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();*/


             /*   mScannerView = new ZXingScannerView(context);
                mScannerView.startCamera();*/


                if(!(dddd == null)) {
                    if (!(dddd.isEmpty())) {
                        Log.d("tag", "" + dddd);
                    }
                }


            }
        });





        return convertView;
    }

    @Override
    public void handleResult(Result result) {

        Log.e("tag", result.getText()); // Prints scan results
        Log.e("tag", result.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
    }
}
