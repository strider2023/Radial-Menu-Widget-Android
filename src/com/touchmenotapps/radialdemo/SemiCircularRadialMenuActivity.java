package com.touchmenotapps.radialdemo;

import com.touchmenotapps.widget.radialmenu.semicircularmenu.SemiCircularRadialMenu;
import com.touchmenotapps.widget.radialmenu.semicircularmenu.SemiCircularRadialMenuItem;
import com.touchmenotapps.widget.radialmenu.semicircularmenu.SemiCircularRadialMenuItem.OnSemiCircularRadialMenuPressed;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class SemiCircularRadialMenuActivity extends Activity {
	
	private SemiCircularRadialMenu mMenu;
	private SemiCircularRadialMenuItem mCamera, mDislike, mInfo, mRefresh, mSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semi_circular_radial_menu);
		
		mCamera = new SemiCircularRadialMenuItem("camera", getResources().getDrawable(R.drawable.ic_action_camera), "Camera");
		mDislike = new SemiCircularRadialMenuItem("dislike", getResources().getDrawable(R.drawable.ic_action_dislike), "Dislike");
		mInfo = new SemiCircularRadialMenuItem("info", getResources().getDrawable(R.drawable.ic_action_info), "Info");
		mRefresh = new SemiCircularRadialMenuItem("refresh", getResources().getDrawable(R.drawable.ic_action_refresh), "Refresh");
		mSearch = new SemiCircularRadialMenuItem("search", getResources().getDrawable(R.drawable.ic_action_search), "Search");
				
		mMenu = (SemiCircularRadialMenu) findViewById(R.id.radial_menu);
		mMenu.addMenuItem(mCamera.getMenuID(), mCamera);
		mMenu.addMenuItem(mDislike.getMenuID(), mDislike);
		mMenu.addMenuItem(mInfo.getMenuID(), mInfo);
		mMenu.addMenuItem(mRefresh.getMenuID(), mRefresh);
		mMenu.addMenuItem(mSearch.getMenuID(), mSearch);
				
		mCamera.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed() {
				Toast.makeText(SemiCircularRadialMenuActivity.this, mCamera.getText(), Toast.LENGTH_LONG).show();
			}
		});
		
		mDislike.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed() {
				Toast.makeText(SemiCircularRadialMenuActivity.this, mDislike.getText(), Toast.LENGTH_LONG).show();
			}
		});
		
		mInfo.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed() {
				Toast.makeText(SemiCircularRadialMenuActivity.this, mInfo.getText(), Toast.LENGTH_LONG).show();
			}
		});
		
		mRefresh.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed() {
				Toast.makeText(SemiCircularRadialMenuActivity.this, mRefresh.getText(), Toast.LENGTH_LONG).show();
			}
		});
		
		mSearch.setOnSemiCircularRadialMenuPressed(new OnSemiCircularRadialMenuPressed() {
			@Override
			public void onMenuItemPressed() {
				Toast.makeText(SemiCircularRadialMenuActivity.this, mSearch.getText(), Toast.LENGTH_LONG).show();
				mMenu.dismissMenu();
			}
		});
	}
}
