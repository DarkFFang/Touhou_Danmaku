package com.fang.touhou_danmaku.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.fang.touhou_danmaku.view.GameView;

public class Bullet extends Sprite {

    private float speed = -35;


    private float angle = 0;

    public Bullet(Bitmap bitmap) {
        super(bitmap);
    }

    public Bullet(Bitmap bitmap, float angle) {
        super(bitmap);
        this.angle = angle;
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint, GameView gameView) {
        if (!isDestroyed()) {
            float speedX = (float) Math.sin((angle*Math.PI)/180) * speed;
            float speedY = (float) Math.cos((angle*Math.PI)/180) * speed;
            this.move(speedX,speedY);
        }
    }

    @Override
    protected void afterDraw(Canvas canvas, Paint paint, GameView gameView) {
        if (!isDestroyed()) {
            //检查Sprite是否超出了Canvas的范围，如果超出，则销毁Sprite
            RectF canvasRecF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
            RectF spriteRecF = getRectF();
            if (!RectF.intersects(canvasRecF, spriteRecF)) {
                destroy();
            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

}
