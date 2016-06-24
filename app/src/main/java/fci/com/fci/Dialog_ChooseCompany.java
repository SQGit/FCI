package fci.com.fci;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Salman on 6/22/2016.
 */
public class Dialog_ChooseCompany extends Dialog {

    Spinner spn_compname;
    Typeface tf;
    Activity activity;
    ImageView submit;

    public Dialog_ChooseCompany(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choosecompany);

        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/asin.TTF");

        spn_compname = (Spinner) findViewById(R.id.spn_comp_name);

        submit = (ImageView) findViewById(R.id.submit);


        String[] plants = new String[]{"Choose Company",
                "Avis",
                "BudGet",
                "Foxca",
                "Torado",
                "Janugko"
        };


        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                activity, R.layout.spinner_item1, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view;
                tv.setTypeface(tf, 1);
                tv.setTextColor(activity.getResources().getColor(android.R.color.white));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                tv.setTypeface(tf, 1);

                view.setBackgroundColor(Color.BLACK);

                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.WHITE);
                } else {
                    tv.setTextColor(activity.getResources().getColor(android.R.color.white));
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spn_compname.setAdapter(spinnerArrayAdapter);



        spn_compname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText(activity, "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();

                   /* Intent goEntry = new Intent(activity,StaffAddEntry.class);
                    activity.startActivity(goEntry);*/

                    spn_compname.setSelection(position);

                  // activity.finish();
                   // dismiss();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(spn_compname.getSelectedItemPosition() == 0 )) {
                    String maang = spn_compname.getSelectedItem().toString();
                    Log.d("tag", "" + maang);
                    Intent goEntry = new Intent(activity, StaffAddEntry.class);
                    activity.startActivity(goEntry);

                    //activity.finish();
                    dismiss();

                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }
}
