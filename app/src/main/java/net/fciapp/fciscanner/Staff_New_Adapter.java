package net.fciapp.fciscanner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Salman on 8/6/2016.
 */
public class Staff_New_Adapter extends ArrayAdapter {


    TextView tv_vinno,tv_make,btn_add;
    Spinner spn_start,spn_end;
    ArrayAdapter<String> start_adapter,end_adapter;
    String tanks[] = {"1/2 tank", "1/4 tank","1/8 tank","3/4 tank"};
    Context context;
    DbHelper dbclass;




    public Staff_New_Adapter(Context context, int resource) {
        super(context, resource);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            Log.e("tag","inside_convertview");
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.adapter_addentry, parent, false);

            tv_vinno = (TextView) convertView.findViewById(R.id.tv_vinno);
            tv_make = (TextView) convertView.findViewById(R.id.tv_make);
            spn_start = (Spinner) convertView.findViewById(R.id.tv_startgug);
            spn_end = (Spinner) convertView.findViewById(R.id.tv_endgug);

                context = getContext();

            start_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tanks);
            end_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tanks);

            start_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            end_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spn_start.setAdapter(start_adapter);
            spn_end.setAdapter(end_adapter);


            dbclass = new DbHelper(context);


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

           // count = Integer.valueOf(sharedPreferences.getString("size", ""));
            notifyDataSetChanged();
        }



        return super.getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return super.getCount();
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
