package com.lang.social.chat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class Transparent extends LinearLayout {
private Paint innerPaint, borderPaint ;
    
	public Transparent(Context context, AttributeSet as) {
		super(context, as);
		init();
	}
	
	public Transparent(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		innerPaint = new Paint();
		innerPaint.setARGB(225, 170, 249, 255); 
		innerPaint.setAntiAlias(true);
		borderPaint = new Paint();
		borderPaint.setARGB(255, 0, 0, 0);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(10);
	}
	
	public void setInnerPaint(Paint innerPaint) {
		this.innerPaint = innerPaint;
	}
	
	public void setBorderPaint(Paint borderPaint) {
		this.borderPaint = borderPaint;
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		RectF drawRect = new RectF();
		drawRect.set(0,0, getMeasuredWidth(), getMeasuredHeight());
		canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);
		super.dispatchDraw(canvas);
        }
}
