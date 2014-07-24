package com.lang.social.photoguess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

public class PlayAreaView extends View {

	private static final String DEBUG_TAG = "Graphic";
	
	private Matrix translate;
    private Bitmap droid;

	private GestureDetector gestures;
    
	public PlayAreaView(Context context, int imageRes) {
		super(context);
	    translate = new Matrix();
	    gestures = new GestureDetector(context, new GestureListener(this));
	    droid = BitmapFactory.decodeResource(getResources(), imageRes);
	}

    @SuppressWarnings("deprecation")
	protected void onDraw(Canvas canvas) {

        int viewWidth = getWidth();
        int viewHeight = getHeight();
        canvas.translate(viewWidth/2 - 70 , viewHeight/2);
        
        canvas.drawBitmap(droid, translate, null);
        Matrix m = canvas.getMatrix();
        Log.d(DEBUG_TAG, "Matrix: "+translate.toShortString());
        Log.d(DEBUG_TAG, "Canvas: "+m.toShortString());
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestures.onTouchEvent(event);
    }
    
    public void onMove(float dx, float dy) {
        translate.postTranslate(dx, dy);
        invalidate();
    }
    
	public void onResetLocation() {
		 translate.reset();
		 invalidate();
	}

	
	private Matrix animateStart;
	private Interpolator animateInterpolator;
	private long startTime;
	private long endTime;
	private float totalAnimDx;
	private float totalAnimDy;
	
	public void onAnimateMove(float totalDx, float totalDy, long duration) {
		animateStart = new Matrix(translate);
	    animateInterpolator = new OvershootInterpolator();
	    startTime = System.currentTimeMillis();
	    endTime = startTime + duration;
	    totalAnimDx = totalDx;
	    totalAnimDy = totalDy;
	    post(new Runnable() {
	        @Override
	        public void run() {
	            onAnimateStep();
	        }
	    });
		
	}
	
	private void onAnimateStep() {
	    long curTime = System.currentTimeMillis();
	    float percentTime = (float) (curTime - startTime)
	            / (float) (endTime - startTime);
	    float percentDistance = animateInterpolator.getInterpolation(percentTime);
	    float curDx = percentDistance * totalAnimDx;
	    float curDy = percentDistance * totalAnimDy;
	    translate.set(animateStart);
	    onMove(curDx, curDy);

	    Log.v(DEBUG_TAG, "We're " + percentDistance + " of the way there!");
	    if (percentTime < 1.0f) {
	        post(new Runnable() {
	            @Override
	            public void run() {
	                onAnimateStep();
	            }
	        });
	    }
	}

    private class GestureListener implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
    	
        private static final String DEBUG_TAG = "Graphic";
		PlayAreaView view;

        public GestureListener(PlayAreaView view) {
            this.view = view;
        }

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			Log.v(DEBUG_TAG, "onDoubleTap");
		    view.onResetLocation();
		    return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			Log.v(DEBUG_TAG, "onDown");
		    return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			
			   Log.v(DEBUG_TAG, "onScroll");
			   view.onMove(-distanceX, -distanceY);
			   return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

	        Log.v(DEBUG_TAG, "onFling");
	        final float distanceTimeFactor = 0.4f;
	        final float totalDx = (distanceTimeFactor * velocityX/2);
	        final float totalDy = (distanceTimeFactor * velocityY/2);

	        view.onAnimateMove(totalDx, totalDy,
	                (long) (1000 * distanceTimeFactor));
	        
	        return true;
		}
    }




}
