package com.fang.touhou_danmaku.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

public class BitmapUtil {
    public static Bitmap rotate(Bitmap b, int degrees) {
        if (b == null) {
            return null;
        }
        Matrix m = new Matrix();
        m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
        Bitmap bitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, false);

        Log.w("BitmapUtil", "rotate: "+bitmap+","+b );
        if (!b.isRecycled()) {
            b = null;
        }

        return bitmap;
    }

    public static Bitmap scale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }


}
