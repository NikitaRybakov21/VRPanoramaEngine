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

	private int currentIndex = -1;

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
			currentIndex++;
			renderClass.setPanorama(currentIndex);
		});

		view.findViewById(R.id.left).setOnClickListener(v -> {
			currentIndex--;
			renderClass.setPanorama(currentIndex);
		});
	}

	@Override
    public Renderer createRenderer() {

		ArrayList<Bitmap> listBitmap = new ArrayList<>();

		if(renderClass == null) {
			renderClass = new SphericalPanoramaRender(getActivity(), this,listBitmap);
		}
		touchMap = new TouchMap(renderClass);


		Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.panorama1);
		listBitmap.add(bitmap);

		Bitmap bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.panorama2);
		listBitmap.add(bitmap2);

		Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.panorama3);
		listBitmap.add(bitmap3);


		Glide.with(this)
				.asBitmap()
				.load("https://pilothub.ru/datas/folio/10000_5000_auto/6725-vologda--ul-burmaginyx.jpg")
				.into(new CustomTarget<Bitmap>() {

					@Override
					public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
						renderClass.createNewSphere(resource);
					}

					@Override
					public void onLoadCleared(@Nullable Drawable placeholder) {

					}
				});

		return renderClass;
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

