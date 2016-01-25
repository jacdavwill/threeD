package com.Jgames.threeD;


import android.app.*;
import android.os.*;
import android.view.View;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.content.Intent;
import android.widget.Toast;
import java.lang.Math;

public class MainActivity extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Toast.makeText(this, "Toasts work", 3).show();
    }
	
	@Override
	public void render ()
	{
		

	}
	
	@Override
	public void dispose ()
	{
		
	}
	
	@Override
	public void resize ()
	{

	}
	
	@Override
	public void pause ()
	{

	}
	
	@Override
	public void resume ()
	{

	}
	
	public void startPreview(View view)
	{
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}
	
}
