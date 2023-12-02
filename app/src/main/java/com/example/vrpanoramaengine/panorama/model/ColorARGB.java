package com.example.vrpanoramaengine.panorama.model;

public class ColorARGB {
    public float r, g , b , a ;

    public ColorARGB(float a, float r, float g, float b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static float[] hexToRGB(String hex) {
        if (hex.charAt(0) == '#') {
            hex = hex.substring(1);
        }
        float r = Integer.parseInt(hex.substring(0, 2), 16)/255f;
        float g = Integer.parseInt(hex.substring(2, 4), 16)/255f;
        float b = Integer.parseInt(hex.substring(4, 6), 16)/255f;
        float a = 1f;

        if(hex.length() == 8) {
            a = Integer.parseInt(hex.substring(6, 8), 16)/255f;
        }
        return new float[] {r, g, b, a};
    }
}
