package com.example.vrpanoramaengine.panorama.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vrpanoramaengine.R;

import org.rajawali3d.renderer.ISurfaceRenderer;
import org.rajawali3d.view.IDisplay;
import org.rajawali3d.view.ISurface;

public abstract class BaseFragment extends Fragment implements IDisplay {
    protected FrameLayout mLayout;
    protected ISurface mRenderSurface;
    protected ISurfaceRenderer mRenderer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayout = (FrameLayout) inflater.inflate(getLayoutId(), container, false);

        mRenderSurface = mLayout.findViewById(R.id.rajwali_surface);
        mRenderer = createRenderer();

        applyRenderer();
        return mLayout;
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
    @LayoutRes
    protected int getLayoutId() {
        return R.layout.rajawali_textureview_fragment;
    }
}
