package com.Jgames.threeD;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.content.Context;
import android.widget.FrameLayout;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.view.WindowManager;
import android.util.Log;
import android.widget.Toast;

public class CameraActivity extends Activity {

    private static final String CAMERA_ID = "Camera_Id";
	private static final String TAG = "CAMERAACTIVITY";
	
	private Camera cam;
	private int cameraId;
    private CameraPreview preview;
	private Camera.CameraInfo info;
	private FrameLayout fLayout;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_preview);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		if (savedInstanceState != null)
		{
			cameraId = savedInstanceState.getInt(CAMERA_ID);
		}
		else
		{
			cameraId = 0;
		}
		
		preview = new CameraPreview(this);
		
		FrameLayout fLayout = (FrameLayout) findViewById(R.id.camera_preview);
		//fLayout.addView(fLayout, 0);
    }

	@Override
	protected void onResume()
	{
		super.onResume();
		setCamera();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		
		if (cam != null)
		{
			preview.setCam(null, null);
			cam.release();
			cam = null;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(CAMERA_ID,cameraId);
	}

	public void setCamera()
	{
		boolean found = false;
		
		if (cam != null)
		{
			preview.setCam(null, null);
			cam.release();
			cam = null;
		}
		
		for (int i = 0; i <= cam.getNumberOfCameras(); i++)
		{
			try 
			{
				cam = Camera.open(i);
			}
			catch (Exception e)
			{
				Log.e(TAG, "Cant open camera" + i, e);
				//Toast.makeText(this, "Camera" + i + "couldnt be set", 3);
				return;
			}
			
			cam.getCameraInfo(i,info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
			{
				found = true;
				break;
			}
			else
			{
				cam.release();
			}
		}
		
		if (found)
		{
			Toast.makeText(this, "Camera found", 3).show();
		}
		else
		{
			cam = null;
			Toast.makeText(this, "Sorry, you must have a back camera", 5).show();
		}
	}
	
	public void returnMain (View view)
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
