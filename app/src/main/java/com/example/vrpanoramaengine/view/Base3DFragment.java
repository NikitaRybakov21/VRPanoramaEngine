package com.example.vrpanoramaengine.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vrpanoramaengine.R;

import org.rajawali3d.renderer.ISurfaceRenderer;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.IDisplay;
import org.rajawali3d.view.ISurface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class Base3DFragment extends Fragment implements IDisplay {

    protected ProgressBar mProgressBarLoader;
    protected FrameLayout mLayout;
    protected ISurface mRenderSurface;
    protected ISurfaceRenderer mRenderer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the view
        mLayout = (FrameLayout) inflater.inflate(getLayoutId(), container, false);
        mLayout.findViewById(R.id.relative_layout_loader_container).bringToFront();

        // Find the TextureView
        mRenderSurface = mLayout.findViewById(R.id.rajwali_surface);

        // Create the loader
        mProgressBarLoader = mLayout.findViewById(R.id.progress_bar_loader);
        mProgressBarLoader.setVisibility(View.GONE);

        // Create the renderer
        mRenderer = createRenderer();

        onBeforeApplyRenderer();
        applyRenderer();
        return mLayout;
    }

    protected void onBeforeApplyRenderer() {
    }

    @CallSuper
    protected void applyRenderer() {
        mRenderSurface.setSurfaceRenderer(mRenderer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mLayout != null) {
            mLayout.removeView((View) mRenderSurface);
        }
    }

    @CallSuper
    protected void hideLoader() {
        mProgressBarLoader.post(() -> mProgressBarLoader.setVisibility(View.GONE));
    }

    @CallSuper
    protected void showLoader() {
        mProgressBarLoader.post(() -> mProgressBarLoader.setVisibility(View.VISIBLE));
    }

    @LayoutRes
    protected int getLayoutId() {
        return R.layout.rajawali_textureview_fragment;
    }

    public static abstract class BaseRenderer extends Renderer {

        final Base3DFragment baseFragment;

        public BaseRenderer(Context context, @Nullable Base3DFragment fragment) {
            super(context);
            baseFragment = fragment;
        }

        @Override
        public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
            if (baseFragment != null) baseFragment.showLoader();
            super.onRenderSurfaceCreated(config, gl, width, height);
            if (baseFragment != null) baseFragment.hideLoader();
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
        }

    }
}
