package com.example.vrpanoramaengine.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import androidx.annotation.Nullable;

import com.example.vrpanoramaengine.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;
import org.rajawali3d.primitives.Sphere;

import java.util.Stack;

public class Map3DRenderer extends Base3DFragment.BaseRenderer {
    private int[] viewPort;
    private final Context context;
    private final Base3DFragment fragment;
    private final Handler mHandler;

    private Object3D mSphere;

    public Map3DRenderer(Context context, @Nullable Base3DFragment fragment) {
        super(context, fragment);

        this.context = context;
        this.fragment = fragment;

        mHandler = new Handler(context.getMainLooper());
    }

    @Override
    protected void initScene() {
        initBackGround();
        drawSphere();

        initCamera();
    }

    private void initBackGround() {
        viewPort = new int[] {0, 0, getViewportWidth(), getViewportHeight()};
        getCurrentScene().setBackgroundColor(Color.rgb(210, 235, 255));
        drawLine();
    }

    private void drawLine() {
        Stack<Vector3> points2 = new Stack<>();

        points2.add(new Vector3(0,0, 0));
        points2.add(new Vector3(0,80, 0));

        points2.add(new Vector3(-80,0, 0));
        points2.add(new Vector3(80,0, 0));

        points2.add(new Vector3(0,0, -80));
        points2.add(new Vector3(0,0, 80));

        Material material3 = new Material();
        Line3D line2 = new Line3D(points2,4f , Color.WHITE);
        line2.setMaterial(material3);
        getCurrentScene().addChild(line2);
    }

    private void drawSphere() {
        Material sphereMaterial = new Material();
        sphereMaterial.setColorInfluence(0);

        try {
            sphereMaterial.addTexture(new Texture("earthColors", R.drawable.panorama_vr_big));
        } catch (ATexture.TextureException e) {
            throw new RuntimeException(e);
        }

        mSphere = new SpherePanorama(50, 110, 110);
        mSphere.setMaterial(sphereMaterial);
        getCurrentScene().addChild(mSphere);
    }

    private void initCamera() {
     //   getCurrentCamera().setPosition(0, 0, 30);
     //   getCurrentCamera().setLookAt(0, 0, 0);

        getCurrentCamera().setPosition(0, 0, 0);
        getCurrentCamera().setLookAt(0, 0, -1);
    }
}
