package com.touchmenotapps.radialdemo;

import java.util.ArrayList;
import java.util.List;

import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuWidget;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AltRadialMenuActivity extends FragmentActivity {

	private RadialMenuWidget pieMenu;

	private FrameLayout mFragmentContainer;
	public RadialMenuItem menuItem, menuCloseItem, menuExpandItem;
	public RadialMenuItem firstChildItem, secondChildItem, thirdChildItem;
	private List<RadialMenuItem> children = new ArrayList<RadialMenuItem>();

	@SuppressWarnings("serial")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Check the OS and set the app bar likewise
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
				&& Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR2) {
			setTheme(android.R.style.Theme_Holo_Light);
			setContentView(R.layout.activity_radial);
			getActionBar().setDisplayShowHomeEnabled(true);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
			setContentView(R.layout.activity_radial);
			getWindow().setUiOptions(
					ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
			getActionBar().setDisplayShowHomeEnabled(true);
		} else {
			setTheme(R.style.RadialMenuLegacyTitleBar);
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.activity_radial);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.layout_appbar);
			TextView barHeader = (TextView) findViewById(R.id.appbar_title_text);
			barHeader.setText(R.string.app_name);
		}
		
		mFragmentContainer = (FrameLayout) findViewById(R.id.alt_fragment_container);

		pieMenu = new RadialMenuWidget(this);
		menuCloseItem = new RadialMenuItem(getString(R.string.close), null);
		menuCloseItem
				.setDisplayIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menuItem = new RadialMenuItem(getString(R.string.normal),
				getString(R.string.normal));
		menuItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
				pieMenu.dismiss();
			}
		});

		firstChildItem = new RadialMenuItem(getString(R.string.main_menu),
				getString(R.string.main_menu));
		firstChildItem
				.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
					@Override
					public void execute() {
						// Can edit based on preference. Also can add animations
						// here.
						getSupportFragmentManager().popBackStack(null,
								FragmentManager.POP_BACK_STACK_INCLUSIVE);
						getSupportFragmentManager()
								.beginTransaction()
								.replace(mFragmentContainer.getId(),
										new RadialMenuMainFragment()).commit();
						pieMenu.dismiss();
					}
				});

		secondChildItem = new RadialMenuItem(getString(R.string.contact),
				getString(R.string.contact));
		secondChildItem.setDisplayIcon(R.drawable.ic_launcher);
		secondChildItem
				.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
					@Override
					public void execute() {
						// Can edit based on preference. Also can add animations
						// here.
						getSupportFragmentManager().popBackStack(null,
								FragmentManager.POP_BACK_STACK_INCLUSIVE);
						getSupportFragmentManager()
								.beginTransaction()
								.replace(mFragmentContainer.getId(),
										new RadialMenuContactFragment())
								.commit();
						pieMenu.dismiss();
					}
				});

		thirdChildItem = new RadialMenuItem(getString(R.string.about),
				getString(R.string.about));
		thirdChildItem.setDisplayIcon(R.drawable.ic_launcher);
		thirdChildItem
				.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
					@Override
					public void execute() {
						// Can edit based on preference. Also can add animations
						// here.
						getSupportFragmentManager().popBackStack(null,
								FragmentManager.POP_BACK_STACK_INCLUSIVE);
						getSupportFragmentManager()
								.beginTransaction()
								.replace(mFragmentContainer.getId(),
										new RadialMenuAboutFragment()).commit();
						pieMenu.dismiss();
					}
				});

		menuExpandItem = new RadialMenuItem(getString(R.string.expandable),
				getString(R.string.expandable));
		children.add(firstChildItem);
		children.add(secondChildItem);
		children.add(thirdChildItem);
		menuExpandItem.setMenuChildren(children);

		menuCloseItem
				.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
					@Override
					public void execute() {
						// menuLayout.removeAllViews();
						pieMenu.dismiss();
					}
				});

		// pieMenu.setDismissOnOutsideClick(true, menuLayout);
		pieMenu.setAnimationSpeed(0L);
		pieMenu.setSourceLocation(200, 200);
		pieMenu.setIconSize(15, 30);
		pieMenu.setTextSize(13);
		pieMenu.setOutlineColor(Color.BLACK, 225);
		pieMenu.setInnerRingColor(0xAA66CC, 180);
		pieMenu.setOuterRingColor(0x0099CC, 180);
		//pieMenu.setHeader("Test Menu", 20);
		pieMenu.setCenterCircle(menuCloseItem);

		pieMenu.addMenuEntry(new ArrayList<RadialMenuItem>() {
			{
				add(menuItem);
				add(menuExpandItem);
			}
		});

		// pieMenu.addMenuEntry(menuItem);
		// pieMenu.addMenuEntry(menuExpandItem);

		Button testButton = (Button) this.findViewById(R.id.radial_menu_btn);
		testButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pieMenu.show(v);
			}
		});
		
	}
		@Override
	protected void onResume() {
		super.onResume();
		// Init with home fragment
		getSupportFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(mFragmentContainer.getId(),
						new RadialMenuMainFragment()).commit();
		
		/*findViewById(R.id.alt_fragment_container).post(new Runnable() {
			public void run() {
				pieMenu.show(findViewById(R.id.alt_fragment_container));
			}
		});*/
	}
}
