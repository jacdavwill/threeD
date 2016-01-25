package com.Jgames.threeD;

import android.hardware.Camera;
import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import java.io.IOException;
import android.util.Log;
import android.hardware.Camera.PreviewCallback;
import android.util.*;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback 
{
    private static final String TAG = "CAMERAPREVIEW";

	private final SurfaceHolder holder;
    private Camera cam;
	private Camera.CameraInfo info;
	private boolean isSurfaceCreated;
	
	//PreviewReadyCallback mPreviewReadyCallback = null;
	
    public CameraPreview(Context context) {
        super(context);
		
		isSurfaceCreated = false;
        holder = getHolder();
        holder.addCallback(this);

        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
	
	public void setCam(Camera camera, Camera.CameraInfo information)
	{
		if (cam != null)
		{
			try 
			{
				cam.stopPreview();
			}
			catch(Exception e)
			{
				Log.e(TAG,"Cant stop camera preview",e);
			}
		}
		
		cam = camera;
		info = information;
		
		if (!isSurfaceCreated)
		{
			return;
		}
		
		try
		{
			cam.setPreviewDisplay(holder);
			configureCam();
			camera.startPreview();
		}
		catch(Exception e)
		{
			Log.e(TAG,"Cant start preview",e);
		}
	}

	@Override
    public void surfaceCreated(SurfaceHolder holder) 
	{
        isSurfaceCreated = true;
		
        if (cam != null)
		{
			setCam(cam, info);
		}
    }

    public void surfaceDestroyed(SurfaceHolder holder) 
	{
        if (cam == null || holder.getSurface() == null)
		{
			return;
		}
		
		try
		{
			cam.stopPreview();
		}
		catch(Exception e)
		{
			Log.e(TAG,"Cant stop preview",e);
		}
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) 
	{
        

        if (holder.getSurface() == null)
		{
			return;
        }

        try {
            cam.stopPreview();
        } catch (Exception e){}

        try {
            cam.setPreviewDisplay(holder);
            cam.startPreview();

        } catch (Exception e){
            //Log.w("CameraPreview: Surface changed: ", "Error starting camera preview: " + e.getMessage());
        }
	}

	@Override
	protected void onMeasure(int width, int height)
	{
		super.onMeasure(width, height);
		
		width = resolveSize(getSuggestedMinimumWidth(),width);
		height = resolveSize(getSuggestedMinimumHeight(),height);
		setMeasuredDimension(width,height);
	}
	
	private void configureCam()
	{
		Camera.Parameters params = cam.getParameters();
		
		Camera.Size targetPreviewSize = getClosestSize(getWidth(),getHeight(),params.getSupportedPreviewSizes());
		params.setPreviewSize(targetPreviewSize.width,targetPreviewSize.height);
		
		Camera.Size targetImageSize = getClosestSize(1024,1280,params.getSupportedPictureSizes());
		params.setPictureSize(targetImageSize.width,targetImageSize.height);
		
		cam.setDisplayOrientation(90);
		cam.setParameters(params);
	}
	
	private Camera.Size getClosestSize(int width,int height,List<Camera.Size> supportedSizes)
	{
		final double ASPECT_TOLERANCE = .1;
		double targetRatio = (double) (width / height);
		
		Camera.Size targetSize = null;
		double minDiff = Double.MAX_VALUE;
		
		for(Camera.Size size : supportedSizes)
		{
			double ratio = (double) (size.width / size.height);
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
			{
				continue;
			}
			
			int heightDiff = Math.abs(size.height -height);
			if (heightDiff < minDiff)
			{
				targetSize = size;
				minDiff = heightDiff;
			}
		}
		
		if (targetSize == null)
		{
			minDiff = Double.MAX_VALUE;
			for(Camera.Size size : supportedSizes)
			{	
				int heightDiff = Math.abs(size.height -height);
				if (heightDiff < minDiff)
				{
					targetSize = size;
					minDiff = heightDiff;
				}
			}
			
		}
		
		return targetSize;
	}
	
}
