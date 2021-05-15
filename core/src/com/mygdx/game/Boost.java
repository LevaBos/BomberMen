package com.mygdx.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Boost {
    float x,y;
    int width,height;
    int type;
    boolean isLive = true;
    long timePutBoost;
    int timeFireDefBoost = 1000;
    Boost(float x,float y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
        width = height = 50;
        timePutBoost = TimeUtils.millis();
    }
    void collisionFire(Array<Fire> fires) {
            for (Fire f : fires) {
                if (isLive && timePutBoost+timeFireDefBoost<TimeUtils.millis()) {
                    if (x + width > f.x + (f.width / 3) && y + height > f.y + (f.height / 3) && x < f.x + (f.width / 3) * 2 && y < f.y + (f.height / 3) * 2)
                        isLive = false;
                }
            }
    }
}
