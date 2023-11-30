package com.example.vrpanoramaengine.panorama;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.vrpanoramaengine.R;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;

import java.util.ArrayList;


public class Map3DFragment extends Base3DFragment implements View.OnTouchListener {

	private SphericalPanoramaRender renderClass;
	private TouchMap touchMap;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		inflater.inflate(R.layout.object_picking_overlay, mLayout, true);
		return mLayout;
	}

	@SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		((View) mRenderSurface).setOnTouchListener(this);
		initListenerButtons(view);
	}
	private void initListenerButtons(View view) {

		view.findViewById(R.id.pluse123).setOnClickListener(v -> {
			Vector3 vector3Pos = renderClass.getCurrentCamera().getPosition();
			Vector3 newVector3Pos = new Vector3(vector3Pos.x ,vector3Pos.y  ,vector3Pos.z - 1);
			renderClass.getCurrentCamera().setPosition(newVector3Pos);
		});

		view.findViewById(R.id.minuse123).setOnClickListener(v -> {
			Vector3 vector3Pos = renderClass.getCurrentCamera().getPosition();
			Vector3 newVector3Pos = new Vector3(vector3Pos.x ,vector3Pos.y ,vector3Pos.z + 1);
			renderClass.getCurrentCamera().setPosition(newVector3Pos);
		});


		view.findViewById(R.id.right).setOnClickListener(v -> {
			renderClass.nextPanorama();
		});

		view.findViewById(R.id.left).setOnClickListener(v -> {
			renderClass.lastPanorama();
		});
	}

	@Override
    public Renderer createRenderer() {
		if(renderClass == null) {
			renderClass = new SphericalPanoramaRender(getActivity(), this);
		}
		touchMap = new TouchMap(renderClass);
		loadPanorama();

		return renderClass;
	}

	private void loadPanorama() {

		ArrayList<String> urlPanorama = new ArrayList<>();
		urlPanorama.add("https://pilothub.ru/datas/folio/10000_5000_auto/6725-vologda--ul-burmaginyx.jpg");
		urlPanorama.add("https://pilothub.ru/datas/folio/10000_5000_auto/10188-fok-zvezdnyj-bashkortostan-leto-2019-panorama-360.jpg");
		urlPanorama.add("https://pilothub.ru/datas/folio/10000_5000_auto/3412-moskva-leto-nagatinskaya-ul.jpg");
		urlPanorama.add("https://pilothub.ru/datas/folio/10000_5000_auto/2529-skolkovo-business-school.jpg");
		urlPanorama.add("https://upload.wikimedia.org/wikipedia/commons/b/b4/IMG_0265_Panorama_out.jpg");

		ArrayList<Bitmap> bitmaps = new ArrayList<>();
		for (int i = 0; i < urlPanorama.size(); i++) {
			String url = urlPanorama.get(i);
			Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
				@Override
				public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
					bitmaps.add(resource);
					if(bitmaps.size() == urlPanorama.size()) {

						for (int j = 0; j < bitmaps.size(); j++) {
							renderClass.addPanorama(bitmaps.get(j),"panorama" + j);
						}
						renderClass.setPanoramaOfIndex(0);
						Toast.makeText(getContext(), "Load panorama success", Toast.LENGTH_SHORT).show();
					}
				}
				@Override
				public void onLoadCleared(@Nullable Drawable placeholder) {}
			});
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if (touchMap != null) {
			return touchMap.processingTouch(motionEvent);
		} else {
			return false;
		}
	}
}

