package com.touchmenotapps.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RadialExample extends Activity {
    /** Called when the activity is first created. */
    Radial menu;
    TextView tv;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        RelativeLayout layout = new RelativeLayout(this);
        menu = new Radial(this);
        menu.alt=true;
        menu.setMenu("up","right","down","left");//simple options
        menu.setVisibility(View.GONE);//makes it gone
        
        
        tv = new TextView(this);
        
        //listener for the menu to be triggered
        OnTouchListener otl = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return menu.gestureHandler(event, true);
			}
		};
        
        layout.addView(tv);
        layout.addView(menu);
        layout.setOnTouchListener(otl);
        
        setContentView(layout);
    }
    
    //rewrite event and pre-event to suit your needs
	public class Radial extends View {
		String[] list;
		boolean alt = false;
		float mWidth = -1;//center of screen, will change to touch location
		float mHeight = -1;
		float mThick = 30;//thickness of the radial
		float mRadius = 60;//radias of the radial menu
		int selected = -1;
		int lastE = -1;//last event, used to prevent excessive redrawing
		float[] endTouch;

		//paint of background
		Paint paint;
		Paint tpaint;

		Paint spaint;

		//paint of boarders
		Paint bpaint;

		public Radial(Context context) {
			super(context);
			//paint of background
			paint = new Paint();
			paint.setColor(Color.parseColor("#ee444444"));
			paint.setStrokeWidth(mThick);
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.STROKE);

			spaint = new Paint();
			spaint.setColor(Color.parseColor("#ee999999"));
			spaint.setStrokeWidth(mThick);
			spaint.setAntiAlias(true);
			spaint.setStyle(Paint.Style.STROKE);

			//paint of boarders
			bpaint = new Paint();
			bpaint.setColor(Color.parseColor("#ee777777"));
			bpaint.setStrokeWidth(mThick);
			bpaint.setAntiAlias(true);
			bpaint.setStyle(Paint.Style.STROKE);

			tpaint = new Paint();
			tpaint.setColor(Color.WHITE);
			tpaint.setTextSize((float) (mThick / 2));
			tpaint.setAntiAlias(true);
		}

		public void setMenu(String... data) {
			list = data;
		}

		public void setAlt(boolean newAlt) {
			alt = newAlt;
		}

		//prevents offscreen drawing and calcs
		public void setLoc(float x, float y) {
			if (x < mRadius + mThick / 2)
				x = mRadius + mThick / 2;
			if (y < mRadius + mThick / 2)
				y = mRadius + mThick / 2;

			if (y > this.getHeight() - (mRadius + mThick / 2))
				y = this.getHeight() - (mRadius + mThick / 2);
			if (x > this.getWidth() - (mRadius + mThick / 2))
				x = this.getWidth() - (mRadius + mThick / 2);

			mWidth = x;
			mHeight = y;
		}

		@Override
		public void onDraw(Canvas canvas) {
			setLoc(mWidth, mHeight);//fixes drawing off screen
			final RectF rect = new RectF();
			//Example values
			rect.set(mWidth - mRadius, mHeight - mRadius, mWidth + mRadius, mHeight + mRadius);

			float tot = list.length;
			int i = 0;

			bpaint.setStrokeWidth(mThick);
			//draws back of radial first
			for (i = 0; i < tot; i++) {
				if (!(list[i].equals("NONE") || list[i].equals("HOLLOW")))
					if (alt) {
						canvas.drawArc(rect, (float) (360 / tot * i - 90 - 360 / tot / 2), (float) (360 / tot), false, (selected == i ? spaint : paint));
					} else {
						canvas.drawArc(rect, (float) (360 / tot * i - 90), (float) (360 / tot), false, (selected == i ? spaint : paint));
					}
			}

			//draws text
			for (i = 0; i < tot; i++) {
				if (!(list[i].equals("NONE") || list[i].equals("HOLLOW"))) {
					Path arc = new Path();
					if (alt) {
						arc.addArc(rect, (float) (360 / tot * i - 90 - 360 / tot / 2) + 10, (float) (360 / tot) - 10);
						canvas.drawTextOnPath(list[i], arc, 0, +mThick / 8, tpaint);
					} else {
						arc.addArc(rect, (float) (360 / tot * i - 90) + 10, (float) (360 / tot) - 10);
						canvas.drawTextOnPath(list[i], arc, 0, -mThick / 8, tpaint);
					}
				}
			}

			//draws seperaters between each option
			if (tot > 1)
				for (i = 0; i < tot; i++) {
					if (!list[i].equals("NONE"))
						if (alt) {
							canvas.drawArc(rect, (float) (360 / tot * i - 91 - 360 / tot / 2), 2, false, bpaint);
							canvas.drawArc(rect, (float) (360 / tot * (i + 1) - 91 - 360 / tot / 2), 2, false, bpaint);
						} else {
							canvas.drawArc(rect, (float) (360 / tot * i - 91), 2, false, bpaint);
							canvas.drawArc(rect, (float) (360 / tot * (i + 1) - 91), 2, false, bpaint);
						}
				}

			//draws outer and inner boarders
			bpaint.setStrokeWidth(2);

			rect.set(mWidth - mRadius - mThick / 2, mHeight - mRadius - mThick / 2, mWidth + mRadius + mThick / 2, mHeight + mRadius + mThick / 2);

			for (i = 0; i < tot; i++) {
				if (!list[i].equals("NONE"))
					if (alt) {
						canvas.drawArc(rect, (float) (360 / tot * i - 91 - 360 / tot / 2), (float) (360 / tot) + 2, false, bpaint);
					} else {
						canvas.drawArc(rect, (float) (360 / tot * i - 91), (float) (360 / tot) + 2, false, bpaint);
					}
			}

			rect.set(mWidth - mRadius + mThick / 2, mHeight - mRadius + mThick / 2, mWidth + mRadius - mThick / 2, mHeight + mRadius - mThick / 2);

			for (i = 0; i < tot; i++) {
				if (!list[i].equals("NONE"))
					if (alt) {
						canvas.drawArc(rect, (float) (360 / tot * i - 91 - 360 / tot / 2), (float) (360 / tot) + 1, false, bpaint);
					} else {
						canvas.drawArc(rect, (float) (360 / tot * i - 91), (float) (360 / tot) + 1, false, bpaint);
					}
			}
			//still have to add in icons, will come later
		}

		//handles resulting event from onTouch up
		public boolean handleEvent(int e) {
			if (e == list.length)
				e = 0;
			else if (e == -1) {
				selected = -1;
				return false;
			}
			String event = list[e];
			if (event.equals("NONE")) {
				//viewA.setText("");
				selected = -1;
				invalidate();
				return false;
			}//more if statements for each event
			tv.setText(list[e]);
			selected = -1;
			invalidate();
			return true;
		}

		//handles moving guestures that havnt finished yet
		public void preEvent(int e) {
			if (e == list.length)
				e = 0;
			else if (lastE == e)
				return;
			lastE = e;
			if (e == -1) {
				selected = -1;
				tv.setText("");
				invalidate();
				return;
			}
			String event = list[e];
			if (event.equals("NONE") || event.equals("HOLLOW")) {
				tv.setText("");
				selected = -1;
				invalidate();
				return;
			}//more if statements for each event
			selected = e;
			tv.setText(list[e]);
			invalidate();
			return;
		}

		//basic distance formula
		public float distance(float x2, float y2) {
			double dx = mWidth - x2; //horizontal difference 
			double dy = mHeight - y2; //vertical difference 
			float dist = (float) Math.sqrt(dx * dx + dy * dy); //distance using Pythagoras theorem
			return dist;
		}

		//returns index of menu item given center of radial and end touch
		public float angle(float x2, float y2) {
			double dx = x2 - mWidth; //horizontal difference 
			double dy = y2 - mHeight; //vertical difference 
			float angle = (float) (Math.atan2(dy, dx) * 180 / Math.PI) + 90 + (alt ? (360 / list.length) / 2 : 0);
			if (angle < 0)
				return (angle + 360) / (360 / list.length);
			return angle / (360 / list.length);
		}

		//handler function that fits into onTouchHandler{onTouch()};
		public boolean gestureHandler(MotionEvent event, boolean eat) {
			if (event.getAction() == 1) {//end
				endTouch = new float[] { event.getX(), event.getY() };
				if (distance(endTouch[0], endTouch[1]) > mRadius - mThick / 2) {
					this.setVisibility(View.GONE);
					return handleEvent((int) angle(endTouch[0], endTouch[1]));
				} else {
					this.setVisibility(View.GONE);
					return handleEvent(-1);
				}
			} else if (event.getAction() == 0) {//start

				mWidth = event.getX();
				mHeight = event.getY();
				this.setVisibility(View.VISIBLE);
				invalidate();
			} else if (event.getAction() == 2) {//drag
				endTouch = new float[] { event.getX(), event.getY() };
				if (distance(endTouch[0], endTouch[1]) > mRadius - mThick / 2) {
					preEvent((int) angle(endTouch[0], endTouch[1]));
				} else {
					preEvent(-1);
				}
			}
			return eat;//eats touch if needed, fixes scrollable elements from interfering
		}
	}
}