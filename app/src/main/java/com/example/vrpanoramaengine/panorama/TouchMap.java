package com.example.vrpanoramaengine.panorama;

import android.view.MotionEvent;

public class TouchMap {

    private final SphericalPanoramaRender map3D;

    public TouchMap(SphericalPanoramaRender map3D) {
        this.map3D = map3D;
    }

    float tempX = 0;
    float tempY = 0;

    public boolean processingTouch(MotionEvent motionEvent) {

        if(motionEvent.getAction() == 0) {
            tempX = motionEvent.getX();
            tempY = motionEvent.getY();
        }

        if(motionEvent.getAction() == 2) {

            float deltaX = (motionEvent.getX() - tempX) / 25f;
            float deltaY = - (motionEvent.getY() - tempY) / 25f;

            tempX = motionEvent.getX();
            tempY = motionEvent.getY();

            map3D.rotateSpherePanoramaAxisX(deltaY);
            map3D.rotateSpherePanoramaAxisY(deltaX);
        }
        return true;
    }
}
