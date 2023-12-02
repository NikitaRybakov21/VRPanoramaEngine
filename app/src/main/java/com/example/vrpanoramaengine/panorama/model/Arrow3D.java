package com.example.vrpanoramaengine.panorama.model;

import android.graphics.PointF;

import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;

public class Arrow3D extends Prism3D {

    ArrayList<PointF> pointsPolygonArrow = new ArrayList<>();
    private final double angle;
    private final ColorARGB colorARGB;
    private final float base = -3f;
    private final float height = 0.2f;
    private final float alphaSelected = 0.3f;
    private final float alphaUnSelected = 0.5f;
    public Arrow3D(ColorARGB color, int rotation, String name) {

        initModelArrow();

        colorARGB = new ColorARGB(alphaUnSelected, color.r, color.g, color.b);
        this.angle = rotation;

        drawObject3DPrism(colorARGB, pointsPolygonArrow, base, height, true);
        setRotation(Vector3.Axis.Y,rotation);
        setName(name);
    }

    private void initModelArrow() {
        float weightArrow = 0.3f;
        float heightArrow = 0.9f;

        pointsPolygonArrow.add(new PointF(-weightArrow + 0.1f, -heightArrow + 0.1f));

        pointsPolygonArrow.add(new PointF(-weightArrow, -heightArrow));
        pointsPolygonArrow.add(new PointF(-weightArrow,-heightArrow*3));

        pointsPolygonArrow.add(new PointF(-weightArrow*2,-heightArrow*3 + 0.1f));
        pointsPolygonArrow.add(new PointF(0,-heightArrow*4));
        pointsPolygonArrow.add(new PointF(weightArrow*2,-heightArrow*3 + 0.1f));

        pointsPolygonArrow.add(new PointF(weightArrow,-heightArrow*3));
        pointsPolygonArrow.add(new PointF(weightArrow, -heightArrow));

        pointsPolygonArrow.add(new PointF(weightArrow - 0.1f, -heightArrow + 0.1f));
    }

    public void onSelected() {
        drawObject3DPrism(new ColorARGB(alphaSelected, colorARGB.r, colorARGB.g, colorARGB.b), pointsPolygonArrow, base, height, true);
    }

    public void onUnSelected() {
        drawObject3DPrism(new ColorARGB(alphaUnSelected, colorARGB.r, colorARGB.g, colorARGB.b), pointsPolygonArrow, base, height, true);
    }

    public double getAngle() {
        return angle;
    }
}
