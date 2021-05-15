package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy {
    float x;
    float y;
    float dx, dy;
    int width, height;
    int frame;
    int type;
    int imageWidth, imageHeight;
    long timeLastFrame;
    long timeSpawn;
    int timeDefFire = 1000;
    int timeFrame = 1000 / 4;
    boolean isLive = true;
    boolean deathSound = false;

    Enemy(float x, float y,int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        timeSpawn = TimeUtils.millis();
        imageHeight = 50;
        imageWidth = 50;
        width = 50;
        height = 50;
        frame = 0;
        dx =  1;
    }

    void move(Object map[][],Array<Bomb> bombs) {
        boolean collision = false;
        if (isLive) {
            for (Object[] line : map) {
                for (Object o : line) {
                    if (o != null) {
                        if ((x + dx + width > o.x && y + dy + height > o.y && x + dx < o.x + o.width && y + dy < o.y + o.height)||(x + width + dx > MyGdxGame.SCREEN_WIDHT - MyGdxGame.SIDE_PADDING || x + dx < MyGdxGame.SIDE_PADDING || y + dy < 0 || y + height + dy > MyGdxGame.SCREEN_HEIGHT - MyGdxGame.TOP_PADDING))
                            collision = true;

                    }
                }
            }
            for (Bomb o: bombs) {
                if (o != null) {
                    if (x + width > o.x && y + height > o.y && x  < o.x + o.width && y  < o.y + o.height);
                    else if (x + dx + width > o.x && y + dy + height > o.y && x + dx < o.x + o.width && y + dy < o.y + o.height) collision = true;

                }
            }
            if (!collision){
                x += dx;
                y += dy;
            }else {
                int r = MathUtils.random(4);
                if (r == MyGdxGame.UP){
                    dy = 1;
                    dx = 0;
                }
                if (r == MyGdxGame.DOWN){
                    dy = -1;
                    dx = 0;
                }
                if (r == MyGdxGame.LEFT){
                    dx = -1;
                    dy = 0;
                }
                if (r == MyGdxGame.RIGHT){
                    dx = 1;
                    dy = 0;
                }
            }
            if (TimeUtils.millis() > timeLastFrame + timeFrame) {
                if (frame < 4) frame++;
                else frame = 0;
                timeLastFrame = TimeUtils.millis();
            }
        }
    }

    void collisionFire(Array<Fire> fires) {
            for (Fire f : fires) {
                if (isLive && timeSpawn + timeDefFire < TimeUtils.millis()) {
                    if (x + width > f.x + (f.width / 3) && y + height > f.y + (f.height / 3) && x < f.x + (f.width / 3) * 2 && y < f.y + (f.height / 3) * 2)
                        isLive = false;

                }
            }
    }
}

