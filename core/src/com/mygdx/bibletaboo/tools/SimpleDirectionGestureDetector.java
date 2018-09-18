package com.mygdx.bibletaboo.tools;

import com.badlogic.gdx.input.GestureDetector;

//https://stackoverflow.com/questions/15185799/libgdx-get-swipe-up-or-swipe-right-etc

public class SimpleDirectionGestureDetector extends GestureDetector {
    public interface DirectionListener {
        float onLeft(float velocityX);

        float onRight(float velocityX );

        float onUp(float velocityY);

        float onDown(float velocityY);
    }

    public SimpleDirectionGestureDetector(DirectionListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    private static class DirectionGestureListener extends GestureAdapter{
        DirectionListener directionListener;

        public DirectionGestureListener(DirectionListener directionListener){
            this.directionListener = directionListener;
        }

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return super.touchDown(x, y, pointer, button);
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if(Math.abs(velocityX)>Math.abs(velocityY)){
                if(velocityX>0){
                    directionListener.onRight(velocityX);
                }else{
                    directionListener.onLeft(velocityX);
                }
            }else{
                if(velocityY>0){
                    directionListener.onDown(velocityY);
                }else{
                    directionListener.onUp(velocityY);
                }
            }
            return super.fling(velocityX, velocityY, button);
        }

    }

}