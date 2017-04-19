package com.example.siyan.imagedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.siyan.imagedemo.custom.ImageSurfaceView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by siYan on 2016/10/29.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            InputStream inputStream = getAssets().open("world.jpg");
            ImageSurfaceView img = (ImageSurfaceView) findViewById(R.id.img);
            img.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
