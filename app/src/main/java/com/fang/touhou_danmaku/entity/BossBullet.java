package com.fang.touhou_danmaku.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.fang.touhou_danmaku.view.GameView;

public class BossBullet extends Bullet {

    private int sequence = 8;
    private int level = 0;
    private int frequency = 8;

    private int explodeTime = 90;

    public BossBullet(Bitmap bitmap) {
        super(bitmap);
        setSpeed(-4);
        setCollideOffset(-5);
    }

    public BossBullet(Bitmap bitmap, float angle, int explodeTime,float speed) {
        super(bitmap, angle);
        setSpeed(-speed);
        this.explodeTime = explodeTime;
    }

    @Override
    public float getWidth() {
        Bitmap bitmap = getBitmap();
        return bitmap.getWidth() / sequence;
    }

    @Override
    public Rect getBitmapSrcRec() {
        Rect rect = super.getBitmapSrcRec();
        int left = (int) (level * getWidth());
        rect.offsetTo(left, 0);
        return rect;
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

            if (getFrame() % frequency == 0) {
                level++;
                if (level > 7) {
                    level = 0;
                }
            }
            if (getFrame() > explodeTime) {
                explode(gameView, 9);
                destroy();
            }
        }
    }

    public void explode(GameView gameView, int power) {
        if (isDestroyed()) {
            return;
        }
        gameView.soundPool.play(gameView.soundMap.get(5), 0.25f, 0.25f, 0, 1, 1);
        for (int i = 0; i < power; i++) {
            float x = getCenterX();
            float y = getCenterY();
            float bulletAngle = i * (getAngle() / (power - 1)) - (getAngle() / 2);
            SmallBossBullet smallBossBullet = new SmallBossBullet(gameView.getSmallStarBitmap(), bulletAngle + getAngle());
            smallBossBullet.centerTo(x, y);
            gameView.addSmallBossBullet(smallBossBullet);
        }
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
