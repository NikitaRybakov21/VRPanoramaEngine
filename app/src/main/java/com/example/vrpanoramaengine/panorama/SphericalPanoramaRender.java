package com.example.vrpanoramaengine.panorama;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;

import androidx.annotation.Nullable;

import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.ColorAnimation3D;
import org.rajawali3d.animation.IAnimationListener;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;

import java.util.ArrayList;
import java.util.Stack;

public class SphericalPanoramaRender extends Base3DFragment.BaseRenderer {
    private ScenePanorama currentPanoramaScene;
    private final int radiusSphere = 10;
    private final ArrayList<ScenePanorama> scenePanoramaList = new ArrayList<>();
    private int currentScenePanoramaIndex = 0;

    public SphericalPanoramaRender(Context context, @Nullable Base3DFragment fragment) {
        super(context, fragment);
    }

    @Override
    protected void initScene() {
        createBackGround();
        initCamera();

        setZoomPanorama(-60);
    }

    private void initCamera() {
        getCurrentCamera().setPosition(0, 0, 0);
        getCurrentCamera().setLookAt(0, 0, -1);
    }

    private void createBackGround() {
        getCurrentScene().setBackgroundColor(Color.rgb(210, 235, 255));
       // drawLine();
    }

    private void drawLine() {
        Stack<Vector3> points2 = new Stack<>();

        points2.add(new Vector3(-10,0,0));
        points2.add(new Vector3(10,0,0));

        points2.add(new Vector3(0,-10,0));
        points2.add(new Vector3(0,10,0));

        points2.add(new Vector3(0,0,-10));
        points2.add(new Vector3(0,0,10));

        Material material3 = new Material();
        Line3D line2 = new Line3D(points2, 3, Color.WHITE);
        line2.setMaterial(material3);
        getCurrentScene().addChild(line2);
    }

    public void addPanorama(Bitmap bitmapPanorama,String name) {
        Material sphereMaterial = new Material();
        sphereMaterial.setColor(Color.TRANSPARENT);
        try {
            sphereMaterial.addTexture(new Texture(name,bitmapPanorama));
        } catch (ATexture.TextureException e) {
            throw new RuntimeException(e);
        }
        int qualitySphere = 110;
        SpherePanoramaModel sphere = new SpherePanoramaModel(radiusSphere, qualitySphere, qualitySphere);
        sphere.setMaterial(sphereMaterial);


        ArrayList<ScenePanorama.Arrow3d> listArrow = new ArrayList<>();

        Object3DPrism object3DPrism = createObject3DArrow(Color.RED);
        object3DPrism.setRotation(Vector3.Axis.Y, 0);
        listArrow.add(new ScenePanorama.Arrow3d(object3DPrism, 0));

        Object3DPrism object3DPrism1 = createObject3DArrow(Color.WHITE);
        object3DPrism1.setRotation(Vector3.Axis.Y, 90);
        listArrow.add(new ScenePanorama.Arrow3d(object3DPrism1, 90));

        Object3DPrism object3DPrism2 = createObject3DArrow(Color.WHITE);
        object3DPrism2.setRotation(Vector3.Axis.Y, -90);
        listArrow.add(new ScenePanorama.Arrow3d(object3DPrism2, -90));

        Object3DPrism object3DPrism3 = createObject3DArrow(Color.WHITE);
        object3DPrism3.setRotation(Vector3.Axis.Y, 180);
        listArrow.add(new ScenePanorama.Arrow3d(object3DPrism3, 180));

        scenePanoramaList.add(new ScenePanorama(sphere,listArrow));
    }

    private Object3DPrism createObject3DArrow(int color) {
        Object3DPrism arrow3d = new Object3DPrism();

        ArrayList<PointF> pointsPolygonArrow = new ArrayList<>();

        float weightArrow = 0.3f;
        float heightArrow = 1f;

        pointsPolygonArrow.add(new PointF(-weightArrow, -heightArrow));
        pointsPolygonArrow.add(new PointF(-weightArrow,-heightArrow*3));

        pointsPolygonArrow.add(new PointF(-weightArrow*2,-heightArrow*3));
        pointsPolygonArrow.add(new PointF(0,-heightArrow*4));

        pointsPolygonArrow.add(new PointF(weightArrow*2,-heightArrow*3));
        pointsPolygonArrow.add(new PointF(weightArrow,-heightArrow*3));
        pointsPolygonArrow.add(new PointF(weightArrow, -heightArrow));

        ColorARGB colorARGB = new ColorARGB(0.5f, 1, 1, 1);

        if(color == Color.RED) {
            colorARGB = new ColorARGB(0.5f, 1, 0, 0);
        }

        arrow3d.drawObject3DPrism(colorARGB, pointsPolygonArrow, -3, 0.2f, true);

        return arrow3d;
    }

    public void setPanoramaOfIndex(int index) {
        if(index >= 0 && index < scenePanoramaList.size()) {
            currentScenePanoramaIndex = index;

            if(currentPanoramaScene != null) {
                fadingAnimation(false,200,currentPanoramaScene.getSphere(),() -> {

                    SpherePanoramaModel sphere = currentPanoramaScene.getSphere();

                    double rotXSphere = radianToDegree(sphere.getRotX());
                    removeScenePanorama(currentPanoramaScene);
                    currentPanoramaScene = scenePanoramaList.get(index);

                    double rotYSphere = radianToDegree(currentPanoramaScene.getSphere().getRotY());
                    currentPanoramaScene.getSphere().setRotation(Vector3.Axis.X, rotXSphere);
                    currentPanoramaScene.getSphere().setRotY(rotYSphere);

                    currentPanoramaScene.getArrow3dList().forEach(arrow3d -> {
                        double rotYArrow = radianToDegree(arrow3d.object3DPrism.getRotY());

                        arrow3d.object3DPrism.setRotation(Vector3.Axis.X,rotXSphere);
                        arrow3d.object3DPrism.setRotY(rotYArrow);
                    });

                    fadingAnimation(true,500,currentPanoramaScene.getSphere(),() -> {});
                    addScenePanorama(currentPanoramaScene);
                });
            } else {
                currentPanoramaScene = scenePanoramaList.get(index);
                fadingAnimation(true,500,currentPanoramaScene.getSphere(),() -> {});
                addScenePanorama(currentPanoramaScene);
            }
        }
    }

    private double radianToDegree(double radian) {
        return (radian * 360.0) / (2.0 * PI);
    }
    private void addScenePanorama(ScenePanorama scenePanorama) {
        getCurrentScene().addChild(scenePanorama.getSphere());

        scenePanorama.getArrow3dList().forEach(arrow3d -> {
            getCurrentScene().addChild(arrow3d.object3DPrism);
        });
    }

    private void removeScenePanorama(ScenePanorama scenePanorama) {
        getCurrentScene().removeChild(scenePanorama.getSphere());

        scenePanorama.getArrow3dList().forEach(arrow3d -> {
            getCurrentScene().removeChild(arrow3d.object3DPrism);
        });
    }
    public void nextPanorama() {
        if(currentScenePanoramaIndex < scenePanoramaList.size() - 1) {
            currentScenePanoramaIndex++;
            setPanoramaOfIndex(currentScenePanoramaIndex);
        }
    }

    public void lastPanorama() {
        if(currentScenePanoramaIndex > 0) {
            currentScenePanoramaIndex--;
            setPanoramaOfIndex(currentScenePanoramaIndex);
        }
    }

    public void rotateSpherePanoramaAxisY(double radian) {
        SpherePanoramaModel sphere = currentPanoramaScene.getSphere();

        double rotate = sphere.getRotX();
        double z = - cos(rotate + PI/2);
        double y = - sin(rotate + PI/2);

        sphere.rotate(new Vector3(0,y,z),-radian);

        currentPanoramaScene.getArrow3dList().forEach(arrow3d -> {
            arrow3d.object3DPrism.rotate(new Vector3(0,y,z),-radian);
        });
    }

    public void rotateSpherePanoramaAxisX(double radian) {
        SpherePanoramaModel sphere = currentPanoramaScene.getSphere();

        double camRotX = sphere.getRotX();
        double boundAngle = PI / 2.1;

        if((camRotX - radian >= -boundAngle) && (camRotX - radian <= boundAngle) ) {
            sphere.rotate(Vector3.Axis.X, -radian);

            currentPanoramaScene.getArrow3dList().forEach(arrow3d -> {
                arrow3d.object3DPrism.rotate(Vector3.Axis.X, -radian);
            });


        } else {
            if(camRotX - radian < 0) {
                sphere.rotate(Vector3.Axis.X, -(boundAngle) - camRotX);

                currentPanoramaScene.getArrow3dList().forEach(arrow3d -> {
                    arrow3d.object3DPrism.rotate(Vector3.Axis.X, -(boundAngle) - camRotX);
                });
            } else {
                sphere.rotate(Vector3.Axis.X, (boundAngle) - camRotX);

                currentPanoramaScene.getArrow3dList().forEach(arrow3d -> {
                    arrow3d.object3DPrism.rotate(Vector3.Axis.X, (boundAngle) - camRotX);
                });
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

    public interface SphereAnimationCall {
        void animationEnd();
    }

    private void fadingAnimation(boolean isFading, long duration, SpherePanoramaModel sphere, SphereAnimationCall callback) {

        int colorStart;
        int colorEnd;
        if(isFading) {
            colorStart = Color.DKGRAY;
            colorEnd = Color.TRANSPARENT;
        } else {
            colorEnd = Color.DKGRAY;
            colorStart = Color.TRANSPARENT;
        }
        Animation3D anim = new ColorAnimation3D(colorStart, colorEnd);
        anim.setTransformable3D(sphere);
        anim.setDurationMilliseconds(duration);
        getCurrentScene().registerAnimation(anim);
        anim.registerListener(new IAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                callback.animationEnd();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationUpdate(Animation animation, double interpolatedTime) {}
        });
        anim.play();
    }
}
