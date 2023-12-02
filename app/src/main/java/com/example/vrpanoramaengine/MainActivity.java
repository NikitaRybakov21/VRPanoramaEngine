package com.example.vrpanoramaengine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vrpanoramaengine.panorama.fragments.PanoramaFragmentContainer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContainer, new PanoramaFragmentContainer())
                .commit();
    }
}