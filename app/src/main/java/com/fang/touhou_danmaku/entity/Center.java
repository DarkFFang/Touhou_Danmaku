package com.fang.touhou_danmaku.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.fang.touhou_danmaku.view.GameView;

public class Center extends Sprite {
    public Center(Bitmap bitmap) {
        super(bitmap);
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint, GameView gameView) {
        Character character = gameView.getCharacter();
        if (!character.isDestroyed()) {
            centerTo(character.getCenterX(), character.getCenterY());
        } else {
            destroy();
        }
    }
}
