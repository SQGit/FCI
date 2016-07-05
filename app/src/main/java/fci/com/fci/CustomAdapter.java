package fci.com.fci;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ramya on 16-02-2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layout;
  List<String> data;

    public CustomAdapter(Context context, int layout , List<String> source) {
        super(context,R.layout.list,source);
        this.context = context;
        this.data = source;
        this.layout = layout;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = super.getView(position, convertView, parent);


        TextView suggestion = (TextView) view.findViewById(R.id.text1);
        suggestion.setTextSize(30);


        return view;
    }
}