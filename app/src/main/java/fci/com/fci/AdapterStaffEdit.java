package fci.com.fci;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Salman on 6/16/2016.
 */
public class AdapterStaffEdit extends BaseAdapter {

    Context context;
    ArrayList<UserData> arrayList;
    TextView tv_name, tv_phone, tv_password, tv_editview;
    LinearLayout lt_bg;


    AdapterStaffEdit(Context c1) {
        this.context = c1;
      //  arrayList = list;

    }


    @Override
    public int getCount() {
        return 5;
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
        convertView = inflat.inflate(R.layout.adapter_staffedit, null);

        tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
        tv_password = (TextView) convertView.findViewById(R.id.tv_password);
        tv_editview = (TextView) convertView.findViewById(R.id.tv_editview);
        lt_bg = (LinearLayout) convertView.findViewById(R.id.layout_bg);



        if (position%2 == 0) {
            lt_bg.setBackgroundColor(context.getResources().getColor(R.color.bg1));
        } else {
            lt_bg.setBackgroundColor(context.getResources().getColor(R.color.bg2));
        }






        return convertView;
    }
}
