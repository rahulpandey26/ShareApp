package com.example.shareapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.shareapp.R;


public class BaseFragment extends Fragment {

    private int mLayoutId;
    private Toolbar mToolbar;
    private FrameLayout mProgressBarLyt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        FrameLayout fragmentLayoutContainer = view.findViewById(R.id.layout_container);
        inflater.inflate(mLayoutId, fragmentLayoutContainer);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
    }

    private void initializeViews(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        mProgressBarLyt = view.findViewById(R.id.progress_bar_container);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            appCompatActivity.setSupportActionBar(mToolbar);
            if (appCompatActivity.getSupportActionBar() != null) {
                appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                appCompatActivity.getSupportActionBar().setHomeButtonEnabled(false);
                appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }

    void setLayout(int layoutId) {
        mLayoutId = layoutId;
    }

    void showToolbar() {
        if (mToolbar != null) {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    public void hideToolbar() {
        if (mToolbar != null) {
            mToolbar.setVisibility(View.GONE);
        }
    }

    public void setToolbarTitle(String toolbarTitle) {
        ((TextView) mToolbar.findViewById(R.id.toolbar_title)).setText(toolbarTitle);
    }

    void setProgressBarVisible() {
        mProgressBarLyt.setVisibility(View.VISIBLE);
    }

    void hideProgressBar() {
        mProgressBarLyt.setVisibility(View.GONE);
    }
}
