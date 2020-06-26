package com.reconciliationhouse.android.loverekindle.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;


public class BlurBuilder {

    private static final float BITMAP_SCALE = 0.5f;
    private static final float BLUR_RADIUS = 25.0f;

    public static Bitmap blur(Context context, Bitmap bitmap) {
        int width = Math.round(bitmap.getWidth() * BITMAP_SCALE);
        int height = Math.round(bitmap.getHeight() * BITMAP_SCALE);

        Bitmap outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript renderScript = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        Allocation tmpin = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation tmpout = Allocation.createFromBitmap(renderScript, outBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpin);
        theIntrinsic.forEach(tmpout);
        tmpout.copyTo(outBitmap);
        bitmap.recycle();
        renderScript.destroy();
        return outBitmap;
    }
}
