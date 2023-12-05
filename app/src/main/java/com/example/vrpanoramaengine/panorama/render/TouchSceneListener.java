package com.example.vrpanoramaengine.panorama.render;

import android.view.MotionEvent;
import android.view.View;

public class TouchSceneListener {

    private final PanoramaRender scene3D;

    public TouchSceneListener(PanoramaRender scene3D) {
        this.scene3D = scene3D;
    }

    float tempX = 0;
    float tempY = 0;

    public boolean processingTouch(MotionEvent motionEvent) {

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            tempX = motionEvent.getX();
            tempY = motionEvent.getY();

            scene3D.touchDown(motionEvent.getX(),motionEvent.getY());
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

            float deltaX = (motionEvent.getX() - tempX) / 20f;
            float deltaY = - (motionEvent.getY() - tempY) / 20f;

            tempX = motionEvent.getX();
            tempY = motionEvent.getY();

            scene3D.rotateScenePanoramaAxisX(deltaY);
            scene3D.rotateScenePanoramaAxisY(deltaX);
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            scene3D.touchUp(motionEvent.getX(),motionEvent.getY());
        }

        return true;
    }
}
