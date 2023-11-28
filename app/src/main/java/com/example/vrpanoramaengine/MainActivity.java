package com.example.vrpanoramaengine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vrpanoramaengine.view.Map3DFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, new Map3DFragment()).commit();
    }
}