package com.touchmenotapps.radialdemo;

import java.util.ArrayList;

import com.touchmenotapps.widget.radialmenu.menu.v2.RadialMenuItem;
import com.touchmenotapps.widget.radialmenu.menu.v2.RadialMenuRenderer;
import com.touchmenotapps.widget.radialmenu.menu.v2.RadialMenuRenderer.OnRadailMenuClick;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 
 * @author Arindam Nath
 *
 */
public class RadialMenuActivity extends FragmentActivity {

	//Variable declarations
	private RadialMenuRenderer mRenderer;
	private FrameLayout mHolderLayout;
	public RadialMenuItem menuContactItem, menuMainItem, menuAboutItem;
	private ArrayList<RadialMenuItem> mMenuItems = new ArrayList<RadialMenuItem>(0);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Check the OS and set the app bar likewise
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR2) {
			setTheme(android.R.style.Theme_Holo_Light);
			setContentView(R.layout.layout_holder);
			getActionBar().setDisplayShowHomeEnabled(true);
	    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
			setContentView(R.layout.layout_holder);
			getWindow().setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
			getActionBar().setDisplayShowHomeEnabled(true);
	    } else {
	    	setTheme(R.style.RadialMenuLegacyTitleBar);
	    	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    	setContentView(R.layout.layout_holder);
	    	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_appbar);
	    	TextView barHeader = (TextView) findViewById(R.id.appbar_title_text);
	    	barHeader.setText(R.string.app_name);
	    }
		
		//Init the frame layout
		mHolderLayout = (FrameLayout) findViewById(R.id.fragment_container);
		// Init the Radial Menu and menu items
		mRenderer = new RadialMenuRenderer(mHolderLayout, true, 30, 60);		
		menuContactItem = new RadialMenuItem(getResources().getString(R.string.contact),getResources().getString(R.string.contact));
		menuMainItem = new RadialMenuItem(getResources().getString(R.string.main_menu), getResources().getString(R.string.main_menu));
		menuAboutItem = new RadialMenuItem(getResources().getString(R.string.about), getResources().getString(R.string.about));
		//Add the menu Items
		mMenuItems.add(menuMainItem);
		mMenuItems.add(menuAboutItem);
		mMenuItems.add(menuContactItem);
		mRenderer.setRadialMenuContent(mMenuItems);
		mHolderLayout.addView(mRenderer.renderView());
		//Handle the menu item interactions
		menuContactItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
			@Override
			public void onRadailMenuClickedListener(String id) {
				//Can edit based on preference. Also can add animations here.
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), new RadialMenuContactFragment()).commit();
			}
		});
		
		menuMainItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
			@Override
			public void onRadailMenuClickedListener(String id) {
				//Can edit based on preference. Also can add animations here.
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), new RadialMenuMainFragment()).commit();
			}
		});
		
		menuAboutItem.setOnRadialMenuClickListener(new OnRadailMenuClick() {
			@Override
			public void onRadailMenuClickedListener(String id) {
				//Can edit based on preference. Also can add animations here.
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), new RadialMenuAboutFragment()).commit();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//Init with home fragment
		getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		getSupportFragmentManager().beginTransaction().replace(mHolderLayout.getId(), new RadialMenuMainFragment()).commit();
	}
}
