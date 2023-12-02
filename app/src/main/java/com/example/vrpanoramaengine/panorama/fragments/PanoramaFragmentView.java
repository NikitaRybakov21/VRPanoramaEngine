package com.example.vrpanoramaengine.panorama.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.example.vrpanoramaengine.R;
import com.example.vrpanoramaengine.panorama.render.PanoramaRender;
import com.example.vrpanoramaengine.panorama.render.TouchSceneListener;
import com.example.vrpanoramaengine.panorama.model.Scene;

import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;

import java.util.ArrayList;


public class PanoramaFragmentView extends BaseFragment implements View.OnTouchListener , PanoramaRender.FrameCallback {
	private PanoramaRender renderClass;
	private TouchSceneListener touchMap;
	private ArrayList<Scene> scenes = new ArrayList<>();
	private String currentNameScene;
	private CallbackPanorama callbackPanorama;
	private int currentSceneIndex = 0;
	private boolean isFullScreenMode;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		inflater.inflate(R.layout.object_picking_overlay, mLayout, true);
		return mLayout;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initTouchListener();
		initListenerButtons(view);
		initView(view);
	}

	@Override
	public Renderer createRenderer() {
		renderClass = new PanoramaRender(getActivity(),scenes,currentNameScene,this);
		touchMap = new TouchSceneListener(renderClass);
		return renderClass;
	}

	private void initTouchListener() {
		((View) mRenderSurface).setOnTouchListener(this);
	}


	private void initView(View view) {
		ImageView splitButtonImage = view.findViewById(R.id.imageSplitButton);
		if(isFullScreenMode) {
			splitButtonImage.setImageResource(R.drawable.baseline_map_24);
		} else {
			splitButtonImage.setImageResource(R.drawable.baseline_open_in_full_24);
		}
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
			nextPanorama();
		});

		view.findViewById(R.id.left).setOnClickListener(v -> {
			lastPanorama();
		});
		/////////////////////////////////////////////////////////////////////////////////////////////

		view.findViewById(R.id.zoomMinus).setOnClickListener(v -> {
			renderClass.reduceZoom();
		});

		view.findViewById(R.id.zoomPlus).setOnClickListener(v -> {
			renderClass.increaseZoom();
		});

		SeekBar seekBar = view.findViewById(R.id.seekBarRotateHotspot);
		TextView rotateText = view.findViewById(R.id.angleRotateText);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				rotateText.setText("Angle: " + i + "°");
				renderClass.rotateArrow(i);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});


		CardView cardViewDebug = view.findViewById(R.id.cardDebug);
		CardView rotateDebugButton = view.findViewById(R.id.debugButton);
		cardViewDebug.setVisibility(View.GONE);

		rotateDebugButton.setOnClickListener(view1 ->{
			if (cardViewDebug.getVisibility() == View.GONE) {
				cardViewDebug.setVisibility(View.VISIBLE);
			} else {
				cardViewDebug.setVisibility(View.GONE);
			}
		});

		view.findViewById(R.id.splitButton).setOnClickListener(view1 -> {
			callbackPanorama.changeSplit();
		});
	}

	public void nextPanorama() {
		if(currentSceneIndex < scenes.size() - 1) {
			currentSceneIndex++;
			currentNameScene = scenes.get(currentSceneIndex).name;
			callbackPanorama.onCurrentName(currentNameScene);
			renderClass.setPanoramaOfName(currentNameScene);
		}
	}

	public void lastPanorama() {
		if(currentSceneIndex > 0) {
			currentSceneIndex--;
			currentNameScene = scenes.get(currentSceneIndex).name;
			callbackPanorama.onCurrentName(currentNameScene);
			renderClass.setPanoramaOfName(currentNameScene);
		}
	}

	public void setIndexFirstPanorama() {
		for (int i = 0; i < scenes.size(); i++) {
			Scene scene = scenes.get(i);
			if (scene.name.equals(currentNameScene)) {
				currentSceneIndex = i;
			}
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

	@Override
	public void onPostFrame() {
		callbackPanorama.postFrame();
	}

	interface CallbackPanorama {
		void onCurrentName(String name);
		void changeSplit();
		void postFrame();
	}

	public static PanoramaFragmentView getInstance(ArrayList<Scene> _scenes, String firstNameScene, CallbackPanorama currentNameSceneCallback,boolean isFullScreenMode) {
		PanoramaFragmentView fragment = new PanoramaFragmentView();
		fragment.setScenes(_scenes,firstNameScene,currentNameSceneCallback,isFullScreenMode);
		return fragment;
	}

	private void setScenes(ArrayList<Scene> _scenes, String firstNameScene, CallbackPanorama currentNameSceneCallback, boolean isFullScreenMode) {
		this.scenes = _scenes;
		this.currentNameScene = firstNameScene;
		this.callbackPanorama = currentNameSceneCallback;
		this.isFullScreenMode = isFullScreenMode;
		setIndexFirstPanorama();
	}
}