package com.example.vrpanoramaengine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vrpanoramaengine.panorama.Map3DFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, new Map3DFragment()).commit();
    }
}


/*
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
 */