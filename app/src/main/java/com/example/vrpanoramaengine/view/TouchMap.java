package com.example.vrpanoramaengine.view;

import android.util.Log;
import android.view.MotionEvent;

import com.example.vrpanoramaengine.view.Map3DRenderer;

import org.rajawali3d.math.vector.Vector3;

public class TouchMap {

    private final Map3DRenderer map3D;

    public TouchMap(Map3DRenderer map3D) {
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

            float deltaX = - (motionEvent.getX() - tempX) / 40f;
            float deltaY = - (motionEvent.getY() - tempY) / 40f;

            tempX = motionEvent.getX();
            tempY = motionEvent.getY();

         //   Vector3 vector3Pos = map3D.getCurrentCamera().getPosition();
          //  Vector3 vector3look = map3D.getCurrentCamera().getLookAt();

         //   Vector3 newVector3Pos = new Vector3(vector3Pos.x + deltaX,vector3Pos.y  ,vector3Pos.z + deltaY);
        //    map3D.getCurrentCamera().setPosition(newVector3Pos);

          //  Vector3 newVector3look = new Vector3(vector3look.x + deltaX,vector3look.y,vector3look.z + deltaY);

         //   Vector3 newVector3look2 = new Vector3(0,0,0);

         //   map3D.getCurrentCamera().setLookAt(newVector3look);

            map3D.getCurrentCamera().rotate(Vector3.Axis.Y,deltaX * 1);

            double look = map3D.getCurrentCamera().getRotY();
            Log.d("", "VVV rote y" + look);
          //  Vector3 vector3 = new Vector3(look.x,look.y,0);
          //  map3D.getCurrentCamera().rotate(vector3,-deltaY * 1);
        }
        return true;
    }

}
