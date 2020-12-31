package com.fang.touhou_danmaku.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.fang.touhou_danmaku.view.GameView;

public class SmallBossBullet extends BossBullet {

    public SmallBossBullet(Bitmap bitmap) {
        super(bitmap);
        setSpeed(-10);
        setSequence(16);
    }

    public SmallBossBullet(Bitmap bitmap, float angle) {
        super(bitmap, angle,0,0);
        setSpeed(-10);
        setSequence(16);
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

            if (getFrame() % getFrequency() == 0) {
                setLevel(getLevel() + 1);
                if (getLevel() > 15) {
                    setLevel(0);
                }
            }
        }
    }
}
