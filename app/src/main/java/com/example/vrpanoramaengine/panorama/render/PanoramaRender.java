package com.example.vrpanoramaengine.panorama.render;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.example.vrpanoramaengine.panorama.model.Arrow3D;
import com.example.vrpanoramaengine.panorama.model.Scene;
import com.example.vrpanoramaengine.panorama.model.Sphere3D;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.ColorAnimation3D;
import org.rajawali3d.animation.IAnimationListener;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Etc1Texture;
import org.rajawali3d.materials.textures.Etc2Texture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.scene.ASceneFrameCallback;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.util.OnObjectPickedListener;

import java.util.ArrayList;

public class PanoramaRender extends Renderer implements OnObjectPickedListener {
    private ScenePanorama currentPanoramaScene;
    private final int radiusSphere = 10;
    private final ArrayList<ScenePanorama> scenePanoramaList = new ArrayList<>();
    private int currentScenePanoramaIndex = 0;
    private int currentZoom = 0;
    private ObjectColorPicker mPicker;
    private Arrow3D currentSelectedArrow;
    private Sphere3D bugFixSphere;
    private final ArrayList<Scene> scenes;
    private final String firstNamePanorama;
    private Handler mHandler;
    private final FrameCallback frameCallback;

    public PanoramaRender(Context context, ArrayList<Scene> scenes, String firstNamePanorama,FrameCallback frameCallback) {
        super(context);
        this.scenes = scenes;
        this.firstNamePanorama = firstNamePanorama;
        this.frameCallback = frameCallback;
    }

    @Override
    protected void initScene() {
        initObjectPicker();
        initBackground();
        initPanoramasScene();
        initCamera();
        setPanoramaOfName(firstNamePanorama);
        setZoomPanorama(-60);

        mHandler = new Handler(Looper.getMainLooper());
        getCurrentScene().registerFrameCallback(new ElapsedTimeFrameCallback());
    }

    private void initObjectPicker() {
        mPicker = new ObjectColorPicker(this);
        mPicker.setOnObjectPickedListener(this);

        bugFixSphere = new Sphere3D(0.5f, 12, 12);
        bugFixSphere.setMaterial(new Material());
        bugFixSphere.setZ(-40);
    }

    private void initCamera() {
        getCurrentCamera().setPosition(0, 0, 0);
        getCurrentCamera().setLookAt(0, 0, -1);
    }

    private void initBackground() {
        getCurrentScene().setBackgroundColor(Color.rgb(210, 235, 255));
    }

    private void initPanoramasScene() {
        for (int i = 0; i < scenes.size(); i++) {
            Scene scene = scenes.get(i);
            ArrayList<Arrow3D> listArrow = new ArrayList<>();

            for (int j = 0; j < scene.hotSpots.size(); j++) {
                Scene.HotSpot hotSpot = scene.hotSpots.get(j);
                listArrow.add(new Arrow3D(hotSpot.color, (int) hotSpot.angle,hotSpot.name));
            }
            createPanoramaScene(scene.panoramaBitmap.copy(Bitmap.Config.RGB_565,false),scene.name,listArrow,scene.correctAngle);
        }
    }

    public void createPanoramaScene(Bitmap bitmapPanorama,String name,ArrayList<Arrow3D> listArrow,double correctAngle) {
        Material sphereMaterial = new Material();
        sphereMaterial.setColor(Color.TRANSPARENT);
        try {
            sphereMaterial.addTexture(new Texture(name + "texture",bitmapPanorama));
        } catch (ATexture.TextureException e) {
            throw new RuntimeException(e);
        }
        int qualitySphere = 110;
        Sphere3D sphere = new Sphere3D(radiusSphere, qualitySphere, qualitySphere);
        sphere.setMaterial(sphereMaterial);

        ScenePanorama scenePanorama = new ScenePanorama(sphere,listArrow,name);
        scenePanorama.correctAngleDegree(correctAngle);

        scenePanoramaList.add(scenePanorama);
    }

    public void setPanoramaOfIndex(int index) {
        if(index >= 0 && index < scenePanoramaList.size()) {
            currentScenePanoramaIndex = index;

            if(currentPanoramaScene != null) {
                replacePanorama(index);
            } else {
                currentPanoramaScene = scenePanoramaList.get(index);
                addPanorama(currentPanoramaScene);
            }
        }
    }

    public void setPanoramaOfName(String name) {
        for (int i = 0; i < scenePanoramaList.size(); i++) {
            ScenePanorama scene = scenePanoramaList.get(i);
            if (scene.getName().equals(name)) {
                setPanoramaOfIndex(i);
            }
        }
    }

    private void addPanorama(ScenePanorama panorama) {
        fadingAnimation(true,500, panorama.getSphere(),() -> {});
        addScenePanorama(panorama);
    }

    private void replacePanorama(int index) {
        fadingAnimation(false,200,currentPanoramaScene.getSphere(),() -> {

            Sphere3D sphere = currentPanoramaScene.getSphere();

            double rotXSphere = radianToDegree(sphere.getRotX());
            removeScenePanorama();
            currentPanoramaScene = scenePanoramaList.get(index);

            double rotYSphere = radianToDegree(currentPanoramaScene.getSphere().getRotY());
            currentPanoramaScene.getSphere().setRotation(Vector3.Axis.X, rotXSphere);
            currentPanoramaScene.getSphere().setRotY(rotYSphere);

            currentPanoramaScene.getArrow3dList().forEach(arrow3d -> {
                double rotYArrow = radianToDegree(arrow3d.getRotY());

                arrow3d.setRotation(Vector3.Axis.X,rotXSphere);
                arrow3d.setRotY(rotYArrow);
            });

            addPanorama(currentPanoramaScene);
        });
    }

    private void addScenePanorama(ScenePanorama scenePanorama) {
        getCurrentScene().addChild(scenePanorama.getSphere());

        scenePanorama.getArrow3dList().forEach(arrow3d -> {
            mPicker.registerObject(arrow3d);
            getCurrentScene().addChild(arrow3d);
        });
        mPicker.registerObject(bugFixSphere);
        getCurrentScene().addChild(bugFixSphere);
    }

    private void removeScenePanorama() {
        getCurrentScene().clearChildren();
        mPicker.unregisterObject(bugFixSphere);
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

    public void rotateScenePanoramaAxisY(double radian) {
        if(currentPanoramaScene != null) {
            currentPanoramaScene.rotateSceneAxisY(radian);
        }
    }

    public void rotateScenePanoramaAxisX(double radian) {
        if(currentPanoramaScene != null) {
            currentPanoramaScene.rotateSceneAxisX(radian);
        }
    }

    public void rotateScenePanoramaAxisXSpec(double radian) {
        if(currentPanoramaScene != null) {
            currentPanoramaScene.rotateSceneAxisXSpec(radian);
        }
    }

    public void rotateArrow(double angle) {
        currentPanoramaScene.correctAngleDegree(angle);
    }

    public void setZoomPanorama(int zoom) {
        currentZoom = zoom;
        double cameraRadius = (radiusSphere * zoom) / 100.0;

        if(cameraRadius > radiusSphere - 2) {
            cameraRadius = radiusSphere - 2;
        }
        if(cameraRadius < -radiusSphere * 2) {
            cameraRadius = -radiusSphere * 2;
        }
        getCurrentCamera().setPosition(0, 0, -cameraRadius);
    }

    public void reduceZoom() {
        if(currentZoom > -200) {
            currentZoom -= 6;
            setZoomPanorama(currentZoom);
        }
    }

    public void increaseZoom() {
        if(currentZoom < 80) {
            currentZoom += 6;
            setZoomPanorama(currentZoom);
        }
    }

    private double radianToDegree(double radian) {
        return (radian * 360.0) / (2.0 * PI);
    }

    public void touchDown(float x, float y) {
        mPicker.getObjectAt(x, y);
    }

    public void touchUp(float x, float y) {
        if(currentSelectedArrow != null) {
            currentSelectedArrow.onUnSelected();
            onClickArrow(currentSelectedArrow);
        }
    }

    public void onClickArrow(Arrow3D currentSelectedArrow) {

    }

    @Override
    public void onObjectPicked(@NonNull @androidx.annotation.NonNull Object3D object) {
        if(object.getName() != null) {
            currentSelectedArrow = ((Arrow3D) object);
            currentSelectedArrow.onSelected();
        }
    }

    @Override
    public void onNoObjectPicked() {}

    public interface SphereAnimationCall {
        void animationEnd();
    }

    private void fadingAnimation(boolean isFading, long duration, Sphere3D sphere, SphereAnimationCall callback) {
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

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {}
    @Override
    public void onTouchEvent(MotionEvent event) {}

    public interface FrameCallback {
        void onPostFrame();
    }

    private final class ElapsedTimeFrameCallback extends ASceneFrameCallback {
        private boolean flagIsFirsFrame = true;
        @Override
        public void onPreFrame(long sceneTime, double deltaTime) {}
        @Override
        public void onPreDraw(long l, double v) {}
        @Override
        public void onPostFrame(final long sceneTime, double deltaTime) {
            mHandler.post(() -> {
                if(flagIsFirsFrame) {
                    frameCallback.onPostFrame();
                    flagIsFirsFrame = false;
                }
            });
        }
        @Override
        public boolean callPostFrame() {
            return true;
        }
    }
}