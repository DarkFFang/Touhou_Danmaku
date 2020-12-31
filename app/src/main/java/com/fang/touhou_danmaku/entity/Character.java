package com.fang.touhou_danmaku.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.fang.touhou_danmaku.view.GameView;

import java.util.List;

public class Character extends Sprite {

    private int sequence = 8;
    private int level = 0;
    private int frequency = 5;

    public Character(Bitmap bitmap) {
        super(bitmap);
    }

    @Override
    public RectF getCollideRectF() {
        RectF rectF = getRectF();
        rectF.left += 53;
        rectF.right -= 53;
        rectF.top += 80;
        rectF.bottom -= 80;
        return rectF;
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint, GameView gameView) {
        if (!isDestroyed()) {
            //确保战斗机完全位于Canvas范围内
            validatePosition(canvas);
            Log.d("charCollideRect", getCollideRectF().toString());
            Log.d("charCollideRect", getCollideRectF().centerX()+","+getCollideRectF().centerY());
            //每隔4帧发射子弹
            if (getFrame() % 5 == 0) {
                shot(gameView, 3);
            }
        }
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
            if (getFrame() % frequency == 0) {
                level++;
                if (level > 7) {
                    level = 0;
                }
            }
            //绘制完成后要检查自身是否被子弹打中
            if (!isDestroyed()) {
                //敌机在绘制完成后要判断是否被子弹打中

                List<Sprite> bossBullet = gameView.getBossBullet();
                for (Sprite sprite : bossBullet) {
                    //判断敌机是否与子弹相交
                    Point p = getCollidePointWithOther(sprite);
                    if (p != null) {
                        //如果有交点，说明子弹打到了飞机上
                        sprite.destroy();
                        destroy();
                        gameView.soundPool.play(gameView.soundMap.get(7), 3f, 3f, 0, 0, 1);
                        return;
                    }
                }
                List<Sprite> smallBossBullet = gameView.getSmallBossBullet();
                for (Sprite sprite : smallBossBullet) {
                    //判断敌机是否与子弹相交
                    Point p = getCollidePointWithOther(sprite);
                    if (p != null) {
                        //如果有交点，说明子弹打到了飞机上
                        sprite.destroy();
                        destroy();
                        gameView.soundPool.play(gameView.soundMap.get(7), 3f, 3f, 0, 0, 1);
                        return;
                    }
                }
            }
        }
    }


    //确保战斗机完全位于Canvas范围内
    private void validatePosition(Canvas canvas) {
        if (getX() < 0) {
            setX(0);
        }
        if (getY() < 0) {
            setY(0);
        }
        RectF rectF = getRectF();
        int canvasWidth = canvas.getWidth();
        if (rectF.right > canvasWidth) {
            setX(canvasWidth - getWidth());
        }
        int canvasHeight = canvas.getHeight();
        if (rectF.bottom > canvasHeight) {
            setY(canvasHeight - getHeight());
        }
    }

    public void shot(GameView gameView) {
        if (isDestroyed()) {
            return;
        }
        float x = getX() + getWidth() / 2;
        float y = getY() - 5;
        Bullet bullet = new Bullet(gameView.getBulletBitmap());
        bullet.centerTo(x, y);
        gameView.addBullet(bullet);
    }

    public void shot(GameView gameView, int power) {
        if (isDestroyed()) {
            return;
        }
        gameView.soundPool.play(gameView.soundMap.get(0), 0.7f, 0.7f, 0, 0, 1);
        float angle = 3 * power;
        float offset = getWidth() / (power - 1);
        for (int i = 0; i < power; i++) {
            float x = getX() + i * offset;
            float y = getY() + 100;
            float bulletAngle = i * (angle / (power - 1)) - (angle / 2);
            Bullet bullet = new Bullet(gameView.getBulletBitmap(), -bulletAngle);
            bullet.centerTo(x, y);
            gameView.addBullet(bullet);
        }
    }
}
