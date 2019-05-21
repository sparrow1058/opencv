package com.example.opencvtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private static final boolean DEBUG=true;
    private static final String TAG="Webcam";
    protected Context context;
    private SurfaceHolder holder;
    Thread mainLoop =null;
    private Bitmap bmp=null;
    private boolean cameraExists=false;
    private boolean shouldStop=false;
    private int cameraId=0;
    private int cameraBase=0;

    static final int IMG_WIDTH=640;
    static final int IMG_HEIGHT=480;

    private int winWidth=0;
    private int winHeight=0;
    private Rect rect;
    private int dw,dh;
    private float rate;

    public native int prepareCamera(int videoid);
    public native int prepareCameraWithBase(int videoid,int camerabase);
    public native void processCamera();
    public native void stopCamera();
    public native void pixeltobmp(Bitmap bitmap);
    static{
     //   System.load("native-lib");
       // System.loadLibrary("ndk-camera");
        System.loadLibrary("native-lib");
    }
    public CameraPreview(Context context){
        super(context);
        this.context=context;
        if(DEBUG) Log.d(TAG,"CameraPreview  constructed");
        setFocusable(true);
        holder=getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }
    public CameraPreview(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context=context;
        if(DEBUG) Log.d(TAG,"CameraPreview 2 Constructed");
        setFocusable(true);

        holder=getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }
    @Override
    public void run(){
        while(true && cameraExists){
            if(winWidth==0){
                winWidth=this.getWidth();
                winHeight=this.getHeight();
                if(winWidth*3/4<winHeight){
                    dw=0;
                    dh=(winHeight-winWidth*3/4)/2;
                    rate=((float)winWidth)/IMG_WIDTH;
                    rect=new Rect(dw,dh,dw+winWidth-1,dh+winWidth*3/4-1);
                }else{
                    dw=(winWidth-winHeight*4/3)/2;
                    dh=0;
                    rate=((float)winHeight)/IMG_HEIGHT;
                    rect=new Rect(dw,dh,dw+winHeight*4/3-1,dh+winHeight-1);
                }
            }
            processCamera();
            pixeltobmp(bmp);
            Canvas canvas=getHolder().lockCanvas();
            if(canvas !=null)
            {
                canvas.drawBitmap(bmp,null,rect,null);
                getHolder().unlockCanvasAndPost(canvas);
            }
            if(shouldStop){
                shouldStop=false;
                break;
            }
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        if(DEBUG) Log.d(TAG,"SurfaceCreated");
        if(bmp==null){
            bmp=Bitmap.createBitmap(IMG_WIDTH,IMG_HEIGHT,Bitmap.Config.ARGB_8888);
        }
        int ret=prepareCameraWithBase(cameraId,cameraBase);
        if(ret!=-1) cameraExists=true;
        mainLoop=new Thread(this);
        mainLoop.start();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
        if(DEBUG) Log.d(TAG,"surfaceChanged");
    }
    public void surfaceDestroyed(SurfaceHolder holder){
        if(DEBUG) Log.d(TAG,"surfaceDestroyed");
        if(cameraExists){
            shouldStop=true;
            while(shouldStop){
                try{
                    Thread.sleep(100);
                }catch (Exception e){}
            }
        }
        stopCamera();
    }
}
