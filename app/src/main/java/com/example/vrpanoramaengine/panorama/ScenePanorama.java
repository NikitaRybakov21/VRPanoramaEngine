package com.example.vrpanoramaengine.panorama;

import java.util.ArrayList;

public class ScenePanorama {
    private final SpherePanoramaModel sphere;
    private final ArrayList<Arrow3d> arrow3dList;
    public ScenePanorama(SpherePanoramaModel sphere, ArrayList<Arrow3d> arrow3dList) {
        this.sphere = sphere;
        this.arrow3dList = arrow3dList;
    }

    public SpherePanoramaModel getSphere() {
        return sphere;
    }

    public ArrayList<Arrow3d> getArrow3dList() {
        return arrow3dList;
    }

    public static class Arrow3d {
        public Object3DPrism object3DPrism;
        public double angle;

        public Arrow3d(Object3DPrism object3DPrism,double angle) {
            this.object3DPrism = object3DPrism;
            this.angle = angle;
        }
    }
}
