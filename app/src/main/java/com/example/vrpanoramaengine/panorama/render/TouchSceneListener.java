package com.example.vrpanoramaengine.panorama.render;

import android.view.MotionEvent;
import android.view.View;

public class TouchSceneListener {

    private final PanoramaRender map3D;

    public TouchSceneListener(PanoramaRender map3D) {
        this.map3D = map3D;
    }

    float tempX = 0;
    float tempY = 0;

    public boolean processingTouch(MotionEvent motionEvent) {

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            tempX = motionEvent.getX();
            tempY = motionEvent.getY();

            map3D.touchDown(motionEvent.getX(),motionEvent.getY());
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

            float deltaX = (motionEvent.getX() - tempX) / 25f;
            float deltaY = - (motionEvent.getY() - tempY) / 25f;

            tempX = motionEvent.getX();
            tempY = motionEvent.getY();

            map3D.rotateScenePanoramaAxisX(deltaY);
            map3D.rotateScenePanoramaAxisY(deltaX);
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            map3D.touchUp(motionEvent.getX(),motionEvent.getY());
        }

        return true;
    }
}
