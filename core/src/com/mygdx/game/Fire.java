package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

public class Fire {
    float x, y;
    int width, height;
    int direction;//Только для огня
    long timeGetFire;
    int timeLiveFire = 500;
    boolean isLive = true;

    Fire(float x, float y, int direction,long timeGetFire) {
        this.timeGetFire = timeGetFire;
        this.direction = direction;
        if (direction == MyGdxGame.UP) {
            width = 50;
            height = 75;
            this.x = x;
            this.y = y;
        }
        if (direction == MyGdxGame.DOWN) {
            width = 50;
            height = 75;
            this.x = x;
            this.y = y - 25;
        }
        if (direction == MyGdxGame.LEFT) {
            width = 75;
            height = 50;
            this.x = x - 25;
            this.y = y;
        }
        if (direction == MyGdxGame.RIGHT) {
            width = 75;
            height = 50;
            this.x = x;
            this.y = y;
        }
        if (direction == MyGdxGame.NONE) {
            width = 50;
            height = 50;
            this.x = x;
            this.y = y;
        }

    }
    void tick(){
        if (TimeUtils.millis() > timeGetFire + timeLiveFire) {
            isLive = false;
        }
    }

}
