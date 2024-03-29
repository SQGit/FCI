/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.fciapp.fciscanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.scandit.base.camera.camera2.TorchLogic;

import net.fciapp.fciscanner.camera.GraphicOverlay;


public class BarcodeGraphic extends GraphicOverlay.Graphic {


    private int mId;

    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN
    };

    private static int mCurrentColorIndex = 0;

    private Paint mRectPaint;
    private Paint mTextPaint;
    private volatile Barcode mBarcode;

    BarcodeGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];

        mRectPaint = new Paint();
        mRectPaint.setColor(selectedColor);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(4.0f);


        Log.e("tag","color");

        mTextPaint = new Paint();
        mTextPaint.setColor(selectedColor);
        mTextPaint.setTextSize(36.0f);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Barcode getBarcode() {

        Log.e("tag","11bar"+mBarcode.displayValue);

        return mBarcode;

    }

    void updateItem(Barcode barcode) {
        mBarcode = barcode;
      //  Log.e("tag","33bar"+mBarcode.displayValue);
        postInvalidate();
    }

    /**
     * Draws the barcode annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Barcode barcode = mBarcode;
        if (barcode == null && barcode.displayValue.trim().length() != 17) {
            return;
        }

        barcode.displayValue.replaceAll("[\\w\\s\\-\\_\\<.*?>]", "");

        if(barcode.displayValue.trim().length()  ==17) {
            // Draws the bounding box around the barcode.
            RectF rect = new RectF(barcode.getBoundingBox());
            rect.left = translateX(rect.left);
            rect.top = translateY(rect.top);
            rect.right = translateX(rect.right);
            rect.bottom = translateY(rect.bottom);
            canvas.drawRect(rect, mRectPaint);

            Log.e("tag", "55afbar" + barcode.displayValue);
            // Toast.makeText(BarcodeCaptureActivity.this,"message"+barcode.displayValue,Toast.LENGTH_LONG).show();

            // Draws a label at the bottom of the barcode indicate the barcode value that was detected.



            canvas.drawText(barcode.rawValue, rect.left, rect.bottom, mTextPaint);

        }

    }
}
