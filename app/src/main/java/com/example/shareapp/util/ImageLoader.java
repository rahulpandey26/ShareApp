package com.example.shareapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.shareapp.R;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class ImageLoader {

    private OnImageDownloadedListener mListener;

    public ImageLoader(OnImageDownloadedListener onImageDownloadedListener){
        mListener = onImageDownloadedListener;
    }

    public void getImageBitmap(Context context, String url) {

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(url);

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRequestPriority(Priority.HIGH)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();

        DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchDecodedImage(imageRequest, context);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {

                if (dataSource.isFinished() && bitmap != null) {
                    mListener.OnImageBitmapDownloaded(Bitmap.createBitmap(bitmap));
                    dataSource.close();
                } else {
                    Toast.makeText(context, R.string.image_bitmap_not_available,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                mListener.OnImageBitmapDownloadingError();
            }
        }, CallerThreadExecutor.getInstance());

    }

    public interface OnImageDownloadedListener {
        void OnImageBitmapDownloaded(Bitmap bitmap);
        void OnImageBitmapDownloadingError();
    }
}
