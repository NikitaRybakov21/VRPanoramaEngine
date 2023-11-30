package com.example.vrpanoramaengine.panorama;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import android.graphics.PointF;
import android.opengl.GLES20;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;

import java.util.ArrayList;
import java.util.List;

import earcut4j.Earcut;

public class Object3DPrism extends Object3D {
    private ColorARGB colorARGB;
    private float[] colors;
    private float[] normals;

    private boolean enableLight = true;

    public void drawObject3DPrism(ColorARGB colorARGB, ArrayList<PointF> pathPoints, float base, float height, boolean enableTransparency) {
        this.colorARGB = colorARGB;

        draw3DObject(pathPoints,base,height,enableTransparency);
    }

    private void draw3DObject(ArrayList<PointF> pathPoints, float base, float height, boolean enableTransparency) {

        int NUM_VERTICES_2D_POLYGON = pathPoints.size();

        colors = new float[NUM_VERTICES_2D_POLYGON * 4 * 2 * 3];
        normals = new float[NUM_VERTICES_2D_POLYGON * 4 * 2 * 3];

        float[] vertices = getVertices(NUM_VERTICES_2D_POLYGON,pathPoints,base,height);
        int[] indices = getIndices(NUM_VERTICES_2D_POLYGON,pathPoints);

        drawObject3D(vertices,indices,colors,enableTransparency,normals);
    }

    private float[] getVertices(int NUM_VERTICES_2D_POLYGON, ArrayList<PointF> pathPoints, float base, float height) {

        float[] vertices = new float[NUM_VERTICES_2D_POLYGON * 3 * 2 * 3];

        //--Down Polygon--//
        for (int i = 0; i < pathPoints.size(); i++) {
            PointF pointF = pathPoints.get(i);

            int index = i * 3;

            vertices[index] = pointF.x;
            vertices[index + 1] = base;
            vertices[index + 2] = pointF.y;

            createBaseColors(i,false);
            createNormals(i,false);
        }

        //--Up Polygon--//
        for (int i = 0; i < pathPoints.size(); i++) {
            PointF pointF = pathPoints.get(i);

            int index = i * 3 + NUM_VERTICES_2D_POLYGON * 3;

            vertices[index] = pointF.x;
            vertices[index + 1] = base + height;
            vertices[index + 2] = pointF.y;

            createBaseColors(i + NUM_VERTICES_2D_POLYGON, true);
            createNormals(i,false);
        }

        //--Walls--//
        for (int i = 0; i < pathPoints.size(); i++) {
            PointF pointF = pathPoints.get(i);
            PointF pointF2;

            if(i + 1 < pathPoints.size()) {
                pointF2 = pathPoints.get(i + 1);
            } else  {
                pointF2 = pathPoints.get(0);
            }

            int index = i * 3 * 4 + NUM_VERTICES_2D_POLYGON * 3 * 2;

            vertices[index] = pointF.x;
            vertices[index + 1] = base;
            vertices[index + 2] = pointF.y;
            createWallsColors(i*4 + NUM_VERTICES_2D_POLYGON * 2,pointF,pointF2);

            vertices[index + 3] = pointF2.x;
            vertices[index + 4] = base;
            vertices[index + 5] = pointF2.y;
            createWallsColors(i*4 + 1 + NUM_VERTICES_2D_POLYGON * 2,pointF,pointF2);

            vertices[index + 6] = pointF.x;
            vertices[index + 7] = base + height;
            vertices[index + 8] = pointF.y;
            createWallsColors(i*4 + 2 + NUM_VERTICES_2D_POLYGON * 2,pointF,pointF2);

            vertices[index + 9] = pointF2.x;
            vertices[index + 10] = base + height;
            vertices[index + 11] = pointF2.y;
            createWallsColors(i*4 + 3 + NUM_VERTICES_2D_POLYGON * 2,pointF,pointF2);
        }

        return vertices;
    }

    private int[] getIndices(int NUM_VERTICES_2D_POLYGON, ArrayList<PointF> pathPoints) {

        //--Triangulate Polygon--//
        double[] points = new double[NUM_VERTICES_2D_POLYGON * 2];
        for (int i = 0; i < pathPoints.size(); i++) {
            PointF pointF = pathPoints.get(i);

            int index = i * 2;

            points[index] = pointF.x;
            points[index + 1] = pointF.y;
        }

        List<Integer> numVerticesToTriangle = Earcut.earcut(points, null, 2);


        //--UP DOWN Polygon--//
        int size = numVerticesToTriangle.size();
        List<Integer> upNumVerticesToTriangle = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int num = numVerticesToTriangle.get(i);
            upNumVerticesToTriangle.add(num + NUM_VERTICES_2D_POLYGON);
        }
        numVerticesToTriangle.addAll(upNumVerticesToTriangle);


        //--Walls--//
        for (int i = 0; i < NUM_VERTICES_2D_POLYGON; i++) {

            int index = i * 4;

            numVerticesToTriangle.add(index + NUM_VERTICES_2D_POLYGON * 2);
            numVerticesToTriangle.add(index + NUM_VERTICES_2D_POLYGON * 2 + 2);
            if (i + 1 < NUM_VERTICES_2D_POLYGON) {
                numVerticesToTriangle.add(index + NUM_VERTICES_2D_POLYGON * 2 + 1);
            } else  {
                numVerticesToTriangle.add(NUM_VERTICES_2D_POLYGON * 2);
            }

            numVerticesToTriangle.add(index + NUM_VERTICES_2D_POLYGON * 2 + 2);
            if (i + 1 < NUM_VERTICES_2D_POLYGON) {
                numVerticesToTriangle.add(index + NUM_VERTICES_2D_POLYGON * 2 + 1);
                numVerticesToTriangle.add(index + NUM_VERTICES_2D_POLYGON * 2 + 3);
            } else  {
                numVerticesToTriangle.add(NUM_VERTICES_2D_POLYGON * 2);
                numVerticesToTriangle.add(NUM_VERTICES_2D_POLYGON * 2 + 2);
            }
        }


        //--ToArray--//
        int[] indices = new int[numVerticesToTriangle.size()];
        for (int i = 0; i < indices.length; i++) {
            int numVert = numVerticesToTriangle.get(i);
            indices[i] = numVert;
        }

        return indices;
    }

    private void createBaseColors(int i, boolean isUp) {
        int index = i * 4;

        float r1, g1, b1;

        float alpha = colorARGB.a;

        if(!enableLight) {
            isUp = true;
        }

        if (isUp) {r1 = colorARGB.r + 0.1f;g1 = colorARGB.g + 0.1f;b1 = colorARGB.b + 0.1f;
        } else {r1 = colorARGB.r - 0.3f;g1 = colorARGB.g - 0.3f;b1 = colorARGB.b - 0.3f;}

        colors[index] = r1;
        colors[index + 1] = g1;
        colors[index + 2] = b1;
        colors[index + 3] = alpha;
    }

    private void createWallsColors(int i, PointF pointF, PointF pointF2) {
        int index = i * 4;

        float deltaX = abs(pointF2.x - pointF.x);
        float deltaY = abs(pointF2.y - pointF.y);

        float lightK = 0.26f;
        float min = -0.26f;

        float lightPower;

        if (deltaX != 0) {
            float tg = deltaY / deltaX;
            float radian = (float) Math.atan(tg);

            lightPower = (float) ((radian / (PI / 2f)) * lightK + min);
        } else {
                lightPower = lightK * 1 + min;
        }

        if(!enableLight) {
            lightPower = 0;
        }

        colors[index] = colorARGB.r + lightPower;
        colors[index + 1] = colorARGB.g + lightPower;
        colors[index + 2] = colorARGB.b + lightPower;
        colors[index + 3] = colorARGB.a;
    }

    private void createNormals(int i, boolean isUp) {
        int index = i * 3;

        int n = 1; if(!isUp) { n = -1; }

        normals[index] = 0;
        normals[index + 1] = n;
        normals[index + 2] = 0;
    }

    public void setEnabledLight(boolean enableLight) {
        this.enableLight = enableLight;
    }

    private void drawObject3D(float[] vertices, int[] indices, float[] colors, boolean enableTransparency, float[] normals) {
        setData(vertices, normals, null, colors, indices, true);

        setDrawingMode(GLES20.GL_TRIANGLES);
        isContainer(false);
        setDoubleSided(true);
        setTransparent(enableTransparency);

        Material material = new Material();
        material.useVertexColors(true);

        setMaterial(material);
    }
}
