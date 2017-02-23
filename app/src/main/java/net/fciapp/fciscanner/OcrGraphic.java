
package net.fciapp.fciscanner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;


import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import net.fciapp.fciscanner.ocr_camera.GraphicOverlay1;

import java.util.List;

public class OcrGraphic extends GraphicOverlay1.Graphic {

    private int mId;

    private static final int TEXT_COLOR = Color.WHITE;

    private static Paint sRectPaint;
    private static Paint sTextPaint;
    private final TextBlock mText;

    OcrGraphic(GraphicOverlay1 overlay, TextBlock text) {
        super(overlay);

        mText = text;

       // if mtxt==117

        if (sRectPaint == null) {
            sRectPaint = new Paint();
            sRectPaint.setColor(TEXT_COLOR);
            sRectPaint.setStyle(Paint.Style.STROKE);
            sRectPaint.setStrokeWidth(4.0f);
        }

        if (sTextPaint == null) {
            sTextPaint = new Paint();
            sTextPaint.setColor(TEXT_COLOR);
            sTextPaint.setTextSize(54.0f);
        }
        postInvalidate();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public TextBlock getTextBlock() {
        return mText;
    }

    public boolean contains(float x, float y) {
        TextBlock text = mText;
        if (text == null) {
            return false;
        }
        RectF rect = new RectF(text.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y);
    }

    @Override
    public void draw(Canvas canvas) {
        TextBlock text = mText;
        if (text == null && text.getValue().replaceAll("\\s+","").trim().length() != 17) {
            return;
        }


        if(text.getValue().trim().replaceAll("\\s+","").length() == 17) {

            RectF rect = new RectF(text.getBoundingBox());
            rect.left = translateX(rect.left);
            rect.top = translateY(rect.top);
            rect.right = translateX(rect.right);
            rect.bottom = translateY(rect.bottom);
            canvas.drawRect(rect, sRectPaint);
        }

        List<? extends Text> textComponents = text.getComponents();
        for(Text currentText : textComponents) {

            Log.e("tag","sizeof"+currentText.getValue().length());

            if(currentText.getValue().replaceAll("\\s+","").trim().length() == 17) {
                float left = translateX(currentText.getBoundingBox().left);
                float bottom = translateY(currentText.getBoundingBox().bottom);
                canvas.drawText(currentText.getValue(), left, bottom, sTextPaint);
            }
        }
    }
}
