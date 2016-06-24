package fci.com.fci;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Salman on 6/23/2016.
 */
public class DialogSignature extends Dialog {

    Activity activity;

    public DialogSignature(Activity act) {
        super(act);
        this.activity = act;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign);
    }


    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }
}
