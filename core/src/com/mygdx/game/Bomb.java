package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Bomb{
    float x,y;
    int width,height;
    int color;
    int frame;
    long timeGetBomb;
    int timeLiveBomb = 3000;
    long timeLastFrame;
    int timeFrame = 1000/6;
    boolean isLive = true;
    Bomb(float x, float y, int color,long timeGetBomb) {
        this.x = x;
        this.y = y;
        this.timeGetBomb = timeGetBomb;
        width = height = 50;
        this.color = color;
        this.frame=0;
    }

    void tick() {
        if (TimeUtils.millis() > timeGetBomb + timeLiveBomb) {
            isLive = false;
        } else {
            if (TimeUtils.millis() > timeLastFrame + timeFrame) {
                if (frame < 8) frame++;
                else frame = 0;
                timeLastFrame = TimeUtils.millis();
            }
        }
    }
    void collisionFire(Array<Fire> fires) {
        for (Fire f : fires) {
            if (x  + width > f.x + f.width/3 && y  + height > f.y + f.height/3&& x  < f.x + (f.width/3)*2 && y  < f.y + (f.height/3)*2)
                isLive = false;
        }
    }
}
