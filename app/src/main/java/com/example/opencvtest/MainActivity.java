package com.example.opencvtest;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnProc;
    private ImageView imageView;
    private Bitmap bmp;
    // Used to load the 'native-lib' library on application startup.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnProc=(Button)findViewById(R.id.btn_gray_process);
        imageView=(ImageView)findViewById(R.id.image_view);
        bmp =BitmapFactory.decodeResource(getResources(),R.drawable.test);
        imageView.setImageBitmap(bmp);
        btnProc.setOnClickListener(this);
      //  bmp= BitmapFactory.decodeResource(getResources(),R.drawable.test);
        // Example of a call to a native method
     //   TextView tv = findViewById(R.id.sample_text);
    //    tv.setText(stringFromJNI());
    }
    @Override
    public void onClick(View v){
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int [] pixels=new int[w*h];
        bmp.getPixels(pixels,0,w,0,0,w,h);
        int[] resultInt=grayProc(pixels,w,h);
        Bitmap resultImg=Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        resultImg.setPixels(resultInt,0,w,0,0,w,h);
        imageView.setImageBitmap(resultImg);
    }
    public void onResume(){
        super.onResume();;
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI();
    public static native int[] grayProc(int[] pixels,int w,int h);
}
