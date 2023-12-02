package com.example.vrpanoramaengine.panorama.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.vrpanoramaengine.R;
import com.example.vrpanoramaengine.panorama.model.ColorARGB;
import com.example.vrpanoramaengine.panorama.model.Scene;

import java.util.ArrayList;

public class PanoramaFragmentContainer extends Fragment implements PanoramaFragmentView.CallbackPanorama {
    private final ArrayList<Bitmap> listBitmapPanorama = new ArrayList<>();
    private final ArrayList<Scene> scenes = new ArrayList<>();
    private String stateCurrentNameScene = "";
    private boolean isFullScreen = true;
    private FrameLayout loadingScreen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.panorama_containers,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingScreen = view.findViewById(R.id.loadingScreen);
        loadingScreen.setVisibility(View.VISIBLE);

        loadPanorama();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(listBitmapPanorama.size() > 0) {
            setPanoramaFragment();
        }
    }

    private void loadPanorama() {
        ArrayList<String> urlPanorama = new ArrayList<>();
        urlPanorama.add("https://pilothub.ru/datas/folio/10000_5000_auto/6725-vologda--ul-burmaginyx.jpg");
        urlPanorama.add("https://pilothub.ru/datas/folio/10000_5000_auto/3412-moskva-leto-nagatinskaya-ul.jpg");
        urlPanorama.add("https://upload.wikimedia.org/wikipedia/commons/b/b4/IMG_0265_Panorama_out.jpg");

        for (int i = 0; i < urlPanorama.size(); i++) {
            String url = urlPanorama.get(i);
            Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    listBitmapPanorama.add(resource);

                    if(listBitmapPanorama.size() == urlPanorama.size()) {
                        loadSuccess();
                    }
                }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {}
            });
        }
    }

    private void loadSuccess() {
        Toast.makeText(getContext(), "Load panorama success", Toast.LENGTH_SHORT).show();
        createScene();
        setPanoramaFragment();
    }

    private void createScene() {
        ColorARGB colorWhite = new ColorARGB(0.5f, 1, 1,1);
        ColorARGB colorRed = new ColorARGB(0.5f, 1, 1f, 1f);

        ArrayList<Scene.HotSpot> hotSpots = new ArrayList<>();
        hotSpots.add(new Scene.HotSpot(colorRed,0,"p11"));
        hotSpots.add(new Scene.HotSpot(colorWhite,90,"p12"));
        hotSpots.add(new Scene.HotSpot(colorWhite,-90,"p13"));
        hotSpots.add(new Scene.HotSpot(colorWhite,180,"p14"));
        Scene scene1 = new Scene(listBitmapPanorama.get(0),hotSpots,0,"panorama1");

        ArrayList<Scene.HotSpot> hotSpots2 = new ArrayList<>();
        hotSpots2.add(new Scene.HotSpot(colorRed,0,"p21"));
        hotSpots2.add(new Scene.HotSpot(colorWhite,30,"p22"));
        hotSpots2.add(new Scene.HotSpot(colorWhite,60,"p23"));
        hotSpots2.add(new Scene.HotSpot(colorWhite,90,"p24"));
        Scene scene2 = new Scene(listBitmapPanorama.get(1),hotSpots2,0,"panorama2");

        ArrayList<Scene.HotSpot> hotSpots3 = new ArrayList<>();
        hotSpots3.add(new Scene.HotSpot(colorRed,0,"p31"));
        hotSpots3.add(new Scene.HotSpot(colorWhite,30,"p32"));
        hotSpots3.add(new Scene.HotSpot(colorWhite,-30,"p33"));
        hotSpots3.add(new Scene.HotSpot(colorWhite,180,"p34"));
        Scene scene3 = new Scene(listBitmapPanorama.get(2),hotSpots3,0,"panorama3");

        scenes.add(scene1);
        scenes.add(scene2);
        scenes.add(scene3);

        stateCurrentNameScene = scene2.name;
    }

    private void setPanoramaFragment() {
        FragmentManager manager = getChildFragmentManager();
        if(isFullScreen) {
            manager.beginTransaction().replace(R.id.splitScreenContainer, new Fragment()).commit();
            manager.beginTransaction()
                    .replace(R.id.fullscreenContainer, PanoramaFragmentView.getInstance(scenes,stateCurrentNameScene,this,isFullScreen))
                    .commit();
        } else {
            manager.beginTransaction().replace(R.id.fullscreenContainer, new Fragment()).commit();
            manager.beginTransaction()
                    .replace(R.id.splitScreenContainer, PanoramaFragmentView.getInstance(scenes,stateCurrentNameScene,this,isFullScreen))
                    .commit();
        }
    }

    @Override
    public void postFrame() {
        loadingScreen.setVisibility(View.GONE);
    }

    @Override
    public void onCurrentName(String name) {
        stateCurrentNameScene = name;
    }

    @Override
    public void changeSplit() {
        if(listBitmapPanorama.size() > 0) {
            loadingScreen.setVisibility(View.VISIBLE);
            isFullScreen = !isFullScreen;
            setPanoramaFragment();
        }
    }
}
