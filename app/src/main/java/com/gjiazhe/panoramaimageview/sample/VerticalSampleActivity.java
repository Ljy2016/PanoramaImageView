package com.gjiazhe.panoramaimageview.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gjiazhe.panoramaimageview.sample.observe_impl.GyroscopeControllerImpl;
import com.gjiazhe.panoramaimageview.sample.observe_impl.GyroscopeObservedImpl;
import com.gjiazhe.panoramaimageview.sample.observe_impl.PanoramaImageView;

public class VerticalSampleActivity extends AppCompatActivity {
    private GyroscopeControllerImpl controller;
    private GyroscopeObservedImpl gyroscopeObserved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_sample);
        controller = new GyroscopeControllerImpl();
        gyroscopeObserved = new GyroscopeObservedImpl(controller);
        PanoramaImageView panoramaImageView = (PanoramaImageView) findViewById(R.id.panorama_image_view);
        controller.attach(panoramaImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroscopeObserved.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscopeObserved.unregister();
    }
}
