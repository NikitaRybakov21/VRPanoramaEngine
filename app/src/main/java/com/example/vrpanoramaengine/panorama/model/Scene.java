package com.example.vrpanoramaengine.panorama.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Scene {
    public final Bitmap panoramaBitmap;
    public final ArrayList<HotSpot> hotSpots;
    public int correctAngle;

    public String name;

    public Scene(Bitmap panoramaBitmap, ArrayList<HotSpot> hotSpots,int correctAngle, String name) {
        this.panoramaBitmap = panoramaBitmap;
        this.hotSpots = hotSpots;

        this.correctAngle = correctAngle;
        this.name = name;
    }

    public static class HotSpot {
        public final double angle;
        public final ColorARGB color;
        public final String name;

        public HotSpot(ColorARGB color, int angle, String name) {
            this.angle = angle;
            this.color = color;
            this.name = name;
        }
    }
}
