package com.fang.touhou_danmaku.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fang.touhou_danmaku.R;
import com.fang.touhou_danmaku.entity.Center;
import com.fang.touhou_danmaku.util.BitmapUtil;
import com.fang.touhou_danmaku.entity.Boss;
import com.fang.touhou_danmaku.entity.Character;
import com.fang.touhou_danmaku.entity.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameView extends View implements Runnable {
    private Paint paint;
    private Boss boss;
    private Character character;
    private Center center;
    private List<Bitmap> bitmapList = new ArrayList<>();
    private List<Sprite> bullet = new ArrayList<>();
    private List<Sprite> bossBullet = new ArrayList<>();
    private List<Sprite> smallBossBullet = new ArrayList<>();

    private long frame = 0;
    private float touchX = -1;
    private float touchY = -1;
    private float characterX = 0;
    private float characterY = 0;

    public SoundPool soundPool;
    public Map<Integer, Integer> soundMap = new HashMap<>();

    public GameView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundMap.put(0, soundPool.load(context, R.raw.shot, 1));
        soundMap.put(1, soundPool.load(context, R.raw.damage00, 1));
        soundMap.put(2, soundPool.load(context, R.raw.damage01, 1));
        soundMap.put(3, soundPool.load(context, R.raw.bossdesstroy, 1));
        soundMap.put(4, soundPool.load(context, R.raw.bossshot00, 1));
        soundMap.put(5, soundPool.load(context, R.raw.kira, 1));
        soundMap.put(6, soundPool.load(context, R.raw.bossshot01, 1));
        soundMap.put(7, soundPool.load(context, R.raw.chardead, 1));

        bitmapList.add(BitmapFactory.decodeResource(getResources(), R.mipmap.charcter));
        bitmapList.add(BitmapFactory.decodeResource(getResources(), R.mipmap.bullet));
        bitmapList.add(BitmapFactory.decodeResource(getResources(), R.mipmap.boss));
        bitmapList.add(BitmapFactory.decodeResource(getResources(), R.mipmap.bigstar));
        bitmapList.add(BitmapFactory.decodeResource(getResources(), R.mipmap.smallstar));
        bitmapList.add(BitmapFactory.decodeResource(getResources(), R.mipmap.center));
        for (int i = 0; i < bitmapList.size(); i++) {
            bitmapList.set(i, BitmapUtil.scale(bitmapList.get(i), 2));
        }
        bitmapList.set(2, BitmapUtil.scale(bitmapList.get(2), 1.5f));
        character = new Character(bitmapList.get(0));
        boss = new Boss(bitmapList.get(2));
        center = new Center(bitmapList.get(5));
        setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (frame == 0) {
            float centerX = canvas.getWidth() / 2;
            float centerY = canvas.getHeight() - character.getHeight() / 2;
            character.centerTo(centerX, centerY);
            center.centerTo(centerX, centerY);
            boss.centerTo(centerX, 300);
        }
        frame++;

        boss.draw(canvas, paint, this);
        //遍历sprites，绘制敌机、子弹、奖励、爆炸效果
        Iterator<Sprite> iteratorBullet = bullet.iterator();
        while (iteratorBullet.hasNext()) {
            Sprite s = iteratorBullet.next();

            if (!s.isDestroyed()) {
                //在Sprite的draw方法内有可能会调用destroy方法
                s.draw(canvas, paint, this);
            }

            //我们此处要判断Sprite在执行了draw方法后是否被destroy掉了
            if (s.isDestroyed()) {
                //如果Sprite被销毁了，那么从Sprites中将其移除
                iteratorBullet.remove();
            }
        }
        character.draw(canvas, paint, this);
        center.draw(canvas,paint,this);

        Iterator<Sprite> iteratorBossBullet = bossBullet.iterator();
        while (iteratorBossBullet.hasNext()) {
            Sprite s = iteratorBossBullet.next();

            if (!s.isDestroyed()) {
                //在Sprite的draw方法内有可能会调用destroy方法
                s.draw(canvas, paint, this);
            }

            //我们此处要判断Sprite在执行了draw方法后是否被destroy掉了
            if (s.isDestroyed()) {
                //如果Sprite被销毁了，那么从Sprites中将其移除
                iteratorBossBullet.remove();
            }
        }
        Iterator<Sprite> iteratorSmallBossBullet = smallBossBullet.iterator();
        while (iteratorSmallBossBullet.hasNext()) {
            Sprite s = iteratorSmallBossBullet.next();

            if (!s.isDestroyed()) {
                //在Sprite的draw方法内有可能会调用destroy方法
                s.draw(canvas, paint, this);
            }

            //我们此处要判断Sprite在执行了draw方法后是否被destroy掉了
            if (s.isDestroyed()) {
                //如果Sprite被销毁了，那么从Sprites中将其移除
                iteratorSmallBossBullet.remove();
            }
        }


        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = event.getX();
            touchY = event.getY();
            characterX = character.getX();
            characterY = character.getY();
        }
        float x = characterX + event.getX() - touchX;
        float y = characterY + event.getY() - touchY;
        character.setX(x);
        character.setY(y);
        Log.d("GameView", "onTouchEvent: " + character.getX() + "," + character.getY());
        return true;
    }

    @Override
    public void run() {
        while (true) {
            postInvalidate();
        }
    }

    public void addBullet(Sprite sprite) {
        bullet.add(sprite);
    }

    public void addBossBullet(Sprite sprite) {
        bossBullet.add(sprite);
    }

    public void addSmallBossBullet(Sprite sprite) {
        smallBossBullet.add(sprite);
    }

    public List<Sprite> getBullet() {
        return bullet;
    }

    public List<Sprite> getBossBullet() {
        return bossBullet;
    }

    public List<Sprite> getSmallBossBullet() {
        return smallBossBullet;
    }

    public Character getCharacter() {
        return character;
    }

    public Bitmap getBulletBitmap() {
        return bitmapList.get(1);
    }

    public Bitmap getBigStarBitmap() {
        return bitmapList.get(3);
    }

    public Bitmap getSmallStarBitmap() {
        return bitmapList.get(4);
    }
}
