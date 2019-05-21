package com.example.opencvtest;

import android.app.Activity;
import android.graphics.Camera;
import android.os.Bundle;

public class Main extends Activity {
    CameraPreview cp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        cp = (CameraPreview) findViewById(R.id.cp);
    }
}
