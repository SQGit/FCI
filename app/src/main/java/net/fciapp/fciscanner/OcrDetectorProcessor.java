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

import android.util.SparseArray;


import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import net.fciapp.fciscanner.ocr_camera.GraphicOverlay1;

public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay1<OcrGraphic> mGraphicOverlay;

    OcrDetectorProcessor(GraphicOverlay1<OcrGraphic> ocrGraphicOverlay) {
        mGraphicOverlay = ocrGraphicOverlay;
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if(item.getValue().replaceAll("\\s+","").trim().length() ==17) {
                OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
                mGraphicOverlay.add(graphic);
            }
        }
    }

    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}
