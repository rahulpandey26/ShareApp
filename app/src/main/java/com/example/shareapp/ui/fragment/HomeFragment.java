package com.example.shareapp.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shareapp.R;
import com.example.shareapp.util.Constants;
import com.example.shareapp.util.ImageLoader;
import com.example.shareapp.util.NetworkUtil;

import java.util.Objects;

public class HomeFragment extends BaseFragment implements ImageLoader.OnImageDownloadedListener {

    private Bitmap mImageBitmap;
    private Uri mBitmapUri;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setLayout(R.layout.fragment_home);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        showToolbar();
    }

    private void initializeViews(View view) {
        view.findViewById(R.id.share_btn).setOnClickListener(view1 -> {
            if (null != mImageBitmap && null != mBitmapUri) {
                shareTextAndImage(mBitmapUri);
            } else {
                loadImageBitmap();
            }
        });
    }

    private void loadImageBitmap() {
        if (!NetworkUtil.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            return;
        }
        setProgressBarVisible();
        new ImageLoader(this).getImageBitmap(getContext(), Constants.SHARE_IMAGE_URL);
    }

    @Override
    public void OnImageBitmapDownloaded(Bitmap bitmap) {
        hideProgressBarLyt();
        mImageBitmap = bitmap;
        getBitmapUri(bitmap);
        shareTextAndImage(mBitmapUri);
    }

    @Override
    public void OnImageBitmapDownloadingError() {
        hideProgressBarLyt();
        Toast.makeText(getContext(), R.string.image_bitmap_not_available, Toast.LENGTH_LONG).show();
    }

    private void shareTextAndImage(Uri bitmapUri ) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text_msg));
        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }

    private void getBitmapUri(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(Objects.requireNonNull(getContext()).
                getContentResolver(), bitmap, getString(R.string.share_image), null);
        mBitmapUri = Uri.parse(path);
    }

    private void hideProgressBarLyt() {
        Objects.requireNonNull(getActivity()).runOnUiThread(this::hideProgressBar);
    }
}
