package fci.com.fci;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;

/**
 * Created by Salman on 6/23/2016.
 */
public class DialogSignature extends Dialog {

    Activity activity;
    TextView tv_sign_hint, tv_clear;
    ImageView iv_back;
    SignaturePad mSignaturePad;

    public DialogSignature(Activity act) {
        super(act);
        this.activity = act;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign);


        tv_sign_hint = (TextView) findViewById(R.id.tv_sign_hint);

        tv_clear = (TextView) findViewById(R.id.tv_sign_clear);

        iv_back = (ImageView) findViewById(R.id.back_imag);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                tv_sign_hint.setText("");
            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {

                tv_sign_hint.setText("Manager Sign");
            }
        });


        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                tv_sign_hint.setText("Manager Sign");
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }
}
