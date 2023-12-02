package com.example.vrpanoramaengine.panorama.model;

import org.rajawali3d.Object3D;

public class Sphere3D extends Object3D {

    private final float mRadius;
    private final int mSegmentsW;
    private final int mSegmentsH;
    private final boolean mCreateTextureCoords;
    private final boolean mCreateVertexColorBuffer;
    private final boolean mMirrorTextureCoords;

    public Sphere3D(float radius, int segmentsW, int segmentsH) {
        this(radius, segmentsW, segmentsH, true, false, true);
    }

    public Sphere3D(float radius, int segmentsW, int segmentsH, boolean mirrorTextureCoords) {
        this(radius, segmentsW, segmentsH, true, false, true, mirrorTextureCoords);
    }

    public Sphere3D(float radius, int segmentsW, int segmentsH, boolean createTextureCoordinates,
                    boolean createVertexColorBuffer, boolean createVBOs) {
        this(radius, segmentsW, segmentsH, createTextureCoordinates, createVertexColorBuffer, createVBOs, false);
    }

    public Sphere3D(float radius, int segmentsW, int segmentsH, boolean createTextureCoordinates,
                    boolean createVertexColorBuffer, boolean createVBOs, boolean mirrorTextureCoords) {
        super();
        mRadius = radius;
        mSegmentsW = segmentsW;
        mSegmentsH = segmentsH;
        mCreateTextureCoords = createTextureCoordinates;
        mCreateVertexColorBuffer = createVertexColorBuffer;
        mMirrorTextureCoords = mirrorTextureCoords;
        init(createVBOs);
    }

    protected void init(boolean createVBOs) {
        int numVertices = (mSegmentsW + 1) * (mSegmentsH + 1);
        int numIndices = 2 * mSegmentsW * (mSegmentsH - 1) * 3;

        float[] vertices = new float[numVertices * 3];
        float[] normals = new float[numVertices * 3];
        int[] indices = new int[numIndices];

        int i, j;
        int vertIndex = 0, index = 0;
        final float normLen = 1.0f / mRadius;

        for (j = 0; j <= mSegmentsH; ++j) {
            float horAngle = (float) (Math.PI * j / mSegmentsH);
            float z = mRadius * (float) Math.cos(horAngle);
            float ringRadius = mRadius * (float) Math.sin(horAngle);

            for (i = 0; i <= mSegmentsW; ++i) {
                float verAngle = (float) (2.0f * Math.PI * i / mSegmentsW);
                float x = ringRadius * (float) Math.cos(verAngle);
                float y = ringRadius * (float) Math.sin(verAngle) * -1;

                normals[vertIndex] = x * normLen;
                vertices[vertIndex++] = x;
                normals[vertIndex] = z * normLen;
                vertices[vertIndex++] = z;
                normals[vertIndex] = y * normLen;
                vertices[vertIndex++] = y;

                if (i > 0 && j > 0) {
                    int a = (mSegmentsW + 1) * j + i;
                    int b = (mSegmentsW + 1) * j + i - 1;
                    int c = (mSegmentsW + 1) * (j - 1) + i - 1;
                    int d = (mSegmentsW + 1) * (j - 1) + i;

                    if (j == mSegmentsH) {
                        indices[index++] = a;
                        indices[index++] = c;
                        indices[index++] = d;
                    } else if (j == 1) {
                        indices[index++] = a;
                        indices[index++] = b;
                        indices[index++] = c;
                    } else {
                        indices[index++] = a;
                        indices[index++] = b;
                        indices[index++] = c;
                        indices[index++] = a;
                        indices[index++] = c;
                        indices[index++] = d;
                    }
                }
            }
        }

        float[] textureCoords = null;
        if (mCreateTextureCoords) {
            int numUvs = (mSegmentsH + 1) * (mSegmentsW + 1) * 2;
            textureCoords = new float[numUvs];

            numUvs = 0;
            for (j = 0; j <= mSegmentsH; ++j) {
                for (i = mSegmentsW; i >= 0; --i) {
                    float u = (float) i / mSegmentsW;
                    textureCoords[numUvs++] = mMirrorTextureCoords ? 1.0f - u : u;
                    textureCoords[numUvs++] = (float) j / mSegmentsH;
                }
            }
        }

        float[] colors = null;

        if (mCreateVertexColorBuffer)
        {
            int numColors = numVertices * 4;
            colors = new float[numColors];
            for (j = 0; j < numColors; j += 4)
            {
                colors[j] = 1.0f;
                colors[j + 1] = 0;
                colors[j + 2] = 0;
                colors[j + 3] = 1.0f;
            }
        }

        setData(vertices, normals, textureCoords, colors, indices, createVBOs);
    }
}


