package com.touchmenotapps.radialdemo;

import com.touchmenotapps.widget.radialmenu.progress.widget.RadialProgressWidget;
import com.touchmenotapps.widget.radialmenu.progress.widget.RadialProgressWidget.OnRadialViewValueChanged;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class RadialProgressActivity extends Activity {

	private RadialProgressWidget mView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radial_progress);
		mView = (RadialProgressWidget) findViewById(R.id.radial_view);
		mView.setSecondaryText("Brightness");
		//Use this to switch between static progress view and an interactive one
		//mView.setTouchEnabled(false); 
		mView.setOnRadialViewValueChanged(new OnRadialViewValueChanged() {
			@Override
			public void onValueChanged(int value) {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.screenBrightness = value / 100.0f;
				//getWindow().setAttributes(lp);
			}
		});
		
		if((int) (getWindow().getAttributes().screenBrightness * 100) < 0)
			mView.setCurrentValue(50);
		else
			mView.setCurrentValue((int) (getWindow().getAttributes().screenBrightness * 100));
	}
}
