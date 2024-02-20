package in.abhishek.playchat.utils;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "ShowCamera";
    Camera camera;
    Context context;
    SurfaceHolder holder;
    int currentZoomLevel = 0;


    public ShowCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.context = context;
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //camera.stopPreview();
        //camera.release();
    }
}
