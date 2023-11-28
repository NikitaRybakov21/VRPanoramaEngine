package com.example.vrpanoramaengine.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vrpanoramaengine.R;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;



public class Map3DFragment extends Base3DFragment implements View.OnTouchListener {

	private Map3DRenderer renderClass;
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
			Vector3 newVector3Pos = new Vector3(vector3Pos.x ,vector3Pos.y - 2 ,vector3Pos.z);
			renderClass.getCurrentCamera().setPosition(newVector3Pos);
		});

		view.findViewById(R.id.minuse123).setOnClickListener(v -> {
			Vector3 vector3Pos = renderClass.getCurrentCamera().getPosition();
			Vector3 newVector3Pos = new Vector3(vector3Pos.x ,vector3Pos.y + 2 ,vector3Pos.z);
			renderClass.getCurrentCamera().setPosition(newVector3Pos);
		});
	}

	@Override
    public Renderer createRenderer() {
		if(renderClass == null) {
			renderClass = new Map3DRenderer(getActivity(), this);
		}
		touchMap = new TouchMap(renderClass);
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

