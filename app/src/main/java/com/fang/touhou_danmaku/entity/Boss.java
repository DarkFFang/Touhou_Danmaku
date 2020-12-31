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
import java.util.Random;

public class Boss extends Sprite {

    private int hp = 1000;

    private int sequence = 8;
    private int level = 0;
    private int frequency = 20;
    private boolean sequenceFlag = false;

    private float radius;

    private float speed = 4;
    private float destinationX;
    private float destinationY;
    private float distance;

    public Boss(Bitmap bitmap) {
        super(bitmap);
        radius = getWidth() / 1.5f;
    }

    @Override
    public Point getCollidePointWithOther(Sprite s) {
        Point p = null;
        float squareX = (s.getCenterX() - getCenterX()) * (s.getCenterX() - getCenterX());
        float squareY = (s.getCenterY() - getCenterY()) * (s.getCenterY() - getCenterY());
        float distance = (float) Math.sqrt(squareX + squareY);
        RectF rectF = new RectF();
        boolean isIntersect = false;
        if (distance <= radius) {
            isIntersect = true;
        }
        if (isIntersect) {
            p = new Point(Math.round(rectF.centerX()), Math.round(rectF.centerY()));
        }
        return p;
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint, GameView gameView) {
        if (!isDestroyed()) {
            //确保战斗机完全位于Canvas范围内
            validatePosition(canvas);
            Random random = new Random(System.currentTimeMillis());

            if (getFrame() % 30 == 0) {
                shot(gameView, 20);
                if (getFrame() % 60 == 0) {
                    shot(gameView, 3, 60, 10);
                    if (getFrame() % 120 == 0) {
                        shot(gameView, 5, 150, 4);
                        if (getFrame() % 600 == 0) {
                            destinationX = random.nextFloat() * canvas.getWidth();
                            destinationY = random.nextFloat() * (canvas.getHeight() / 3f) + 100;
                            Log.d("BossDestination", "BossDestination:" + destinationX + "," + destinationY);
                        }
                    }
                }
            }
            if (destinationX != 0 && destinationY != 0) {
                float deltaX = (destinationX - getCenterX());
                float deltaY = (destinationY - getCenterY());
                Log.d("BossLocation", "BossLocation:" + getCenterX() + "," + getCenterY());
                float squareX = deltaX * deltaX;
                float squareY = deltaY * deltaY;
                distance = (float) Math.sqrt(squareX + squareY);
                move(deltaX / distance * speed, deltaY / distance * speed);
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
                if (level > 4) {
                    sequenceFlag = true;
                } else if (level == 0) {
                    sequenceFlag = false;
                }
                if (!sequenceFlag) {
                    level++;
                } else {
                    level--;
                }

            }
        }
        //绘制完成后要检查自身是否被子弹打中
        if (!isDestroyed()) {
            //敌机在绘制完成后要判断是否被子弹打中

            List<Sprite> spriteList = gameView.getBullet();
            for (Sprite sprite : spriteList) {
                //判断敌机是否与子弹相交
                Point p = getCollidePointWithOther(sprite);
                if (p != null) {
                    //如果有交点，说明子弹打到了飞机上
                    sprite.destroy();
                    if (hp > 300) {
                        gameView.soundPool.play(gameView.soundMap.get(1), 0.7f, 0.7f, 0, 0, 1);
                    } else {
                        gameView.soundPool.play(gameView.soundMap.get(2), 1, 1, 0, 0, 1);
                    }
                    hp--;
                    Log.d("Boss", "hp:" + hp);
                    if (hp <= 0) {
                        //敌机已经没有能量了，执行爆炸效果
                        destroy();
                        gameView.soundPool.play(gameView.soundMap.get(3), 3f, 3f, 0, 0, 1);
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

    public void shot(GameView gameView, int power, int explodeTime, float speed) {
        if (isDestroyed()) {
            return;
        }
        gameView.soundPool.play(gameView.soundMap.get(4), 0.7f, 0.7f, 0, 0, 1);
        float angle = 22.5f * power;
        for (int i = 0; i < power; i++) {
            float x = getCenterX();
            float y = getY() + 200;
            float bulletAngle = i * (angle / (power - 1)) - (angle / 2);
            BossBullet bossBullet = new BossBullet(gameView.getBigStarBitmap(), -bulletAngle + 180, explodeTime, speed);
            bossBullet.centerTo(x, y);
            gameView.addBossBullet(bossBullet);
        }
    }

    public void shot(GameView gameView, int power) {
        if (isDestroyed()) {
            return;
        }
        gameView.soundPool.play(gameView.soundMap.get(6), 0.5f, 0.5f, 0, 1, 1);
        Random random = new Random(System.currentTimeMillis());
        float offset = random.nextFloat() * 360/power;
        float angle = 360;
        for (int i = 0; i < power; i++) {
            float x = getCenterX();
            float y = getCenterY();
            float bulletAngle = i * (angle / (power - 1));
            SmallBossBullet smallBossBullet = new SmallBossBullet(gameView.getSmallStarBitmap(), bulletAngle + offset);
            smallBossBullet.centerTo(x, y);
            gameView.addSmallBossBullet(smallBossBullet);
        }
    }


}
