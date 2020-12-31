package com.fang.touhou_danmaku.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.fang.touhou_danmaku.view.GameView;

public class Sprite {
    private float x = 0;
    private float y = 0;
    private Bitmap bitmap = null;
    private float collideOffset = 0;
    private boolean destroyed = false;
    private long frame = 0;

    public Sprite(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getWidth() {
        return bitmap.getWidth();
    }

    public float getHeight() {
        return bitmap.getHeight();
    }

    public void move(float offsetX, float offsetY) {
        x += offsetX;
        y += offsetY;
    }

    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void centerTo(float centerX, float centerY) {
        float w = getWidth();
        float h = getHeight();
        x = centerX - w / 2;
        y = centerY - h / 2;
    }

    public RectF getRectF() {
        float left = x;
        float top = y;
        float right = left + getWidth();
        float bottom = top + getHeight();
        RectF rectF = new RectF(left, top, right, bottom);
        return rectF;
    }

    public Rect getBitmapSrcRec() {
        Rect rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = (int) getWidth();
        rect.bottom = (int) getHeight();
        return rect;
    }

    public RectF getCollideRectF() {
        RectF rectF = getRectF();
        rectF.left -= collideOffset;
        rectF.right += collideOffset;
        rectF.top -= collideOffset;
        rectF.bottom += collideOffset;
        return rectF;
    }

    public Point getCollidePointWithOther(Sprite s) {
        Point p = null;
        RectF rectF1 = getCollideRectF();
        RectF rectF2 = s.getCollideRectF();
        RectF rectF = new RectF();
        boolean isIntersect = rectF.setIntersect(rectF1, rectF2);
        if (isIntersect) {
            p = new Point(Math.round(rectF.centerX()), Math.round(rectF.centerY()));
        }
        return p;
    }

    public void draw(Canvas canvas, Paint paint, GameView gameView) {
        frame++;
        beforeDraw(canvas, paint, gameView);
        onDraw(canvas, paint, gameView);
        afterDraw(canvas, paint, gameView);
    }

    protected void beforeDraw(Canvas canvas, Paint paint, GameView gameView) {
    }

    public void onDraw(Canvas canvas, Paint paint, GameView gameView) {
        if (!destroyed && this.bitmap != null) {
            Rect srcRef = getBitmapSrcRec();
            RectF dstRecF = getRectF();
            canvas.drawBitmap(bitmap, srcRef, dstRecF, paint);
        }
    }

    protected void afterDraw(Canvas canvas, Paint paint, GameView gameView) {
    }

    public void destroy() {
        this.setDestroyed(true);
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getCenterX() {
        float w = getWidth();
        return x + w / 2;
    }


    public float getCenterY() {
        float h = getHeight();
        return y + h / 2;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isDestroyed() {
        return destroyed;
    }


    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public long getFrame() {
        return frame;
    }

    public float getCollideOffset() {
        return collideOffset;
    }

    public void setCollideOffset(float collideOffset) {
        this.collideOffset = collideOffset;
    }
}
