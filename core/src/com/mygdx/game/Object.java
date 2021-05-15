package com.mygdx.game;

public class Object {
    float x,y;
    int width,height;
    int type;
    int boost = MyGdxGame.BOOST_NULL;
    Object(float x,float y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
        width = height = 50;
    }

}
