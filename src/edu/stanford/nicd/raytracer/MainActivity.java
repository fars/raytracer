package edu.stanford.nicd.raytracer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class MainActivity extends Activity {
	
	private Bitmap mImage;
	private LinearLayout mLinearLayout;
	private RaytraceTask raytraceThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		mLinearLayout = (LinearLayout) findViewById(R.id.background);
	}
	
	@Override
    public void onResume(){
        super.onResume();
        raytraceThread = new RaytraceTask();
		raytraceThread.execute(0);
    }
	
	@Override
    public void onPause(){
        super.onPause();
        raytraceThread.terminateThread=true;
    }

	class RaytraceTask extends AsyncTask<Integer, Void, Bitmap> {
	    private boolean terminateThread = false;
		private long lastMeterTime = 0;
		private int lastMeterFrame = 0;

	    public RaytraceTask() {
	        lastMeterTime = System.currentTimeMillis();
	    }

	    // Do raytracing in background
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
			if(mImage == null) {
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				mImage = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
				mImage.setDensity(Bitmap.DENSITY_NONE);
				mImage.setHasAlpha(false);
			}
			int frame = 0;
			while(!terminateThread) {
				RayTrace(mImage, frame++);
				publishProgress();
			}
	        return mImage;
	    }

	    @SuppressWarnings("deprecation")
		@Override
	    protected void onProgressUpdate(Void... v) {
	        if(mImage != null) {
	            Drawable d = new BitmapDrawable(getResources(), mImage);
	            mLinearLayout.setBackgroundDrawable(d);
	        }
	        lastMeterFrame++;
    		if(System.currentTimeMillis() - lastMeterTime >= 1000) {
    			float FramesPerSecond = lastMeterFrame / ((System.currentTimeMillis() - lastMeterTime) / 1000.0f);
    			((TextView) findViewById(R.id.FPS)).setText("FPS: " + String.format("%.2f", FramesPerSecond));
    			lastMeterTime = System.currentTimeMillis();
    			lastMeterFrame = 0;
    		}
	    }
	}
	
	/* load our native library */
	static {
		System.loadLibrary("plasma");
	}

	private static native void RayTrace(Bitmap output, int frame);
}
