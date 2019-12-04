package com.example.shareapp.ui;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

public class ShareApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize Fresco library
        Fresco.initialize(this);
    }
}
