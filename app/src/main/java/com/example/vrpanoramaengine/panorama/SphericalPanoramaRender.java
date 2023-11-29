package com.example.vrpanoramaengine.panorama;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.vrpanoramaengine.R;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;

public class SphericalPanoramaRender extends Base3DFragment.BaseRenderer {
    private SpherePanoramaModel currentSphere;
    private final int radiusSphere = 10;

    private final ArrayList<SpherePanoramaModel> sphereList = new ArrayList<>();
    private final ArrayList<Bitmap> listBitmap;

    public SphericalPanoramaRender(Context context, @Nullable Base3DFragment fragment, ArrayList<Bitmap> listBitmap) {
        super(context, fragment);
        this.listBitmap = listBitmap;
    }

    @Override
    protected void initScene() {
        createBackGround();
        initCamera();
        createPanoramaSphere();

        setZoomPanorama(-60);
    }

    private void createBackGround() {
        getCurrentScene().setBackgroundColor(Color.rgb(210, 235, 255));
    }

    private void createPanoramaSphere() {
        for (int i = 0; i < listBitmap.size(); i++) {
            Material sphereMaterial = new Material();
            sphereMaterial.setColorInfluence(0);

            try {
                sphereMaterial.addTexture(new Texture("panorama"+i,listBitmap.get(i)));
            } catch (ATexture.TextureException e) {
                throw new RuntimeException(e);
            }

            int qualitySphere = 110;
            SpherePanoramaModel sphere = new SpherePanoramaModel(radiusSphere, qualitySphere, qualitySphere);
            sphere.setMaterial(sphereMaterial);

            sphereList.add(sphere);
        }
    }

    public void createNewSphere(Bitmap bitmap) {
        Material sphereMaterial = new Material();
        sphereMaterial.setColorInfluence(0);

        try {
            sphereMaterial.addTexture(new Texture("panorama45",bitmap));
        } catch (ATexture.TextureException e) {
            throw new RuntimeException(e);
        }

        int qualitySphere = 110;
        SpherePanoramaModel sphere = new SpherePanoramaModel(radiusSphere, qualitySphere, qualitySphere);
        sphere.setMaterial(sphereMaterial);

        if(currentSphere != null) {
            getCurrentScene().removeChild(currentSphere);
        }
        currentSphere = sphere;
        sphereList.add(sphere);
    }

    public void setPanorama(int index) {
        if(index >= 0 && index < sphereList.size()) {
            if(currentSphere != null) {
                getCurrentScene().removeChild(currentSphere);
            }
            currentSphere = sphereList.get(index);
            getCurrentScene().addChild(currentSphere);
        }
    }

    public void rotateSpherePanoramaAxisY(double radian) {
        double rotate = currentSphere.getRotX();
        double z = - cos(rotate + PI/2);
        double y = - sin(rotate + PI/2);

        currentSphere.rotate(new Vector3(0,y,z),-radian);
    }

    public void rotateSpherePanoramaAxisX(double radian) {
        double camRotX = currentSphere.getRotX();
        if((camRotX - radian >= -PI / 2.1) && (camRotX - radian <= PI / 2.1) ) {
            currentSphere.rotate(Vector3.Axis.X, -radian);
        } else {
            if(camRotX - radian < 0) {
                currentSphere.rotate(Vector3.Axis.X, -(PI / 2.1) - camRotX);
            } else {
                currentSphere.rotate(Vector3.Axis.X, (PI / 2.1) - camRotX);
            }
        }
    }

    public void setZoomPanorama(int zoom) {
        double cameraRadius = (radiusSphere * zoom) / 100.0;

        if(cameraRadius > radiusSphere - 2) {
            cameraRadius = radiusSphere - 2;
        }
        if(cameraRadius < -radiusSphere * 2) {
            cameraRadius = -radiusSphere * 2;
        }
        getCurrentCamera().setPosition(0, 0, -cameraRadius);
    }

    private void initCamera() {
        getCurrentCamera().setPosition(0, 0, 0);
        getCurrentCamera().setLookAt(0, 0, -1);
    }
}
