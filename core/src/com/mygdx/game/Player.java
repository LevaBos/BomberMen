package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Player {
    float x;
    float y;
    float dx, dy;
    int bomb;
    int width, height;
    int imageWidth, imageHeight;
    int color;
    int direction;
    int frame;
    int fire;
    long timeLastFrame;
    int timeFrame = 1000 / 8;
    boolean isLive = true;
    boolean deathSound = false;
    boolean armor= false,roller = false,walkThroughBombs = false,walkThroughWalls = false;
    //,BOOST_WALK_THROUGH_BOMBS=4,BOOST_WALK_THROUGH_WALLS=5,BOOST_BONUS_POINTS=6,BOOST_EXTRA_LIFE=7,BOOST_RANDOM=8,BOOST_NULL = 9;

    Player(float x, float y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
        imageHeight = imageWidth = 70;
        width = 30;
        height = 30;
        direction = MyGdxGame.DOWN;
        frame = 0;
        bomb = 1;
        fire = 1;
    }

    void move(Object map[][],Array<Bomb> bombs) {
        boolean collision = false;
        if (isLive) {
            // Проверка на столкновения с обьектами

                for (Object[] line : map) {
                    for (Object o : line) {
                        if (o != null) {
                            if (o.type == MyGdxGame.BRICK)if (!walkThroughWalls) {
                                if (x + dx + width > o.x && y + dy + height > o.y && x + dx < o.x + o.width && y + dy < o.y + o.height)
                                    collision = true;
                            }
                            if (o.type != MyGdxGame.BRICK) if (x + dx + width > o.x && y + dy + height > o.y && x + dx < o.x + o.width && y + dy < o.y + o.height)
                                collision = true;

                        }
                    }
                }

                if (!walkThroughBombs) {
                    for (Bomb o : bombs) {
                        if (o != null) {
                            if (x + width > o.x && y + height > o.y && x < o.x + o.width && y < o.y + o.height);
                            else if (x + dx + width > o.x && y + dy + height > o.y && x + dx < o.x + o.width && y + dy < o.y + o.height)
                                collision = true;

                        }
                    }
                }

            // Проверка, можно ли двигаться
            if (collision || (x + width + dx > MyGdxGame.SCREEN_WIDHT - MyGdxGame.SIDE_PADDING || x + dx < MyGdxGame.SIDE_PADDING || y + dy < 0 || y + height + dy > MyGdxGame.SCREEN_HEIGHT - MyGdxGame.TOP_PADDING)) {
                frame = 0;
            } else {
                if (roller){
                    x += dx*1.5f;
                    y += dy*1.5f;
                }
                else {
                    x += dx;
                    y += dy;
                }

                if (TimeUtils.millis() > timeLastFrame + timeFrame) {
                    if (frame < 3) frame++;
                    else frame = 0;
                    timeLastFrame = TimeUtils.millis();
                }
            }
        }
    }

    void collisionFire(Array<Fire> fires) {
        if (!armor) {
            for (Fire f : fires) {
                if (isLive) {
                    if (x + width > f.x + (f.width / 3) && y + height > f.y + (f.height / 3) && x < f.x + (f.width / 3) * 2 && y < f.y + (f.height / 3) * 2)
                        isLive = false;

                }
            }
        }
    }
    void collisionEnemy(Array<Enemy> enemies) {
            for (Enemy e : enemies) {
                if (isLive) {
                    if (x + width > e.x && y  + height > e.y && x  < e.x + e.width && y  < e.y + e.height)
                        isLive = false;
                }
            }
    }
    void collisionBoost(Array<Boost> boosts) {
        for (Boost b : boosts) {
            if (isLive) {
                if (x  + width > b.x && y + height > b.y && x < b.x + (b.width / 3) * 2 && y < b.y + (b.height / 3) * 2) {
                    if (b.type != MyGdxGame.BOOST_DOOR) {
                        MyGdxGame.score += 200;
                        MyGdxGame.sndGetBoost.play();
                    }else {
                        if (MyGdxGame.doorOpen) {
                            MyGdxGame.levelWin = true;
                            isLive = false;
                        }
                    }
                    if (b.type == MyGdxGame.BOOST_BOMB) {
                        bomb++;
                        boosts.removeValue(b, true);
                    }
                    if (b.type == MyGdxGame.BOOST_FIRE) {
                        fire++;
                        boosts.removeValue(b, true);
                    }
                    if (b.type == MyGdxGame.BOOST_ARMOR){
                        armor = true;
                        boosts.removeValue(b, true);
                    }
                    if (b.type == MyGdxGame.BOOST_ROLLER){
                        roller = true;
                        boosts.removeValue(b, true);
                    }
                    if (b.type == MyGdxGame.BOOST_WALK_THROUGH_BOMBS){
                        walkThroughBombs = true;
                        boosts.removeValue(b, true);
                    }
                    if (b.type == MyGdxGame.BOOST_WALK_THROUGH_WALLS){
                        walkThroughWalls= true;
                        boosts.removeValue(b, true);
                    }
                    if (b.type == MyGdxGame.BOOST_BONUS_POINTS){
                        MyGdxGame.score += 5000;
                        boosts.removeValue(b, true);
                    }
                    if (b.type == MyGdxGame.BOOST_EXTRA_LIFE){
                        MyGdxGame.life ++;
                        boosts.removeValue(b, true);
                    }
                    if (b.type == MyGdxGame.BOOST_RANDOM){
                        b.type = MathUtils.random(8);
                    }
                }
            }
        }
    }
}

