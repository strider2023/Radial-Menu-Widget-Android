package com.touchmenotapps.test;

import java.util.ArrayList;
import java.util.List;

import com.widget.radialmenu.RadialMenuItem;
import com.widget.radialmenu.RadialMenuWidget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RadialMenuActivity extends Activity {

	private RadialMenuWidget pieMenu;
	
	public Activity activity = this;
	public RadialMenuItem menuItem, menuCloseItem, menuExpandItem;
	public RadialMenuItem firstChildItem, secondChildItem, thirdChildItem;
	private List<RadialMenuItem> children = new ArrayList<RadialMenuItem>();

	@SuppressWarnings("serial")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.radial);
		
		pieMenu = new RadialMenuWidget(this);

		menuCloseItem = new RadialMenuItem("Close", null);
		menuCloseItem.setDisplayIcon(android.R.drawable.ic_menu_close_clear_cancel);
		
		menuItem = new RadialMenuItem("Normal","Normal");
		
		firstChildItem = new RadialMenuItem("First","First");
		firstChildItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
				startActivity(new Intent(RadialMenuActivity.this, TestActivity.class));
				pieMenu.dismiss();
			}
		});
		
		secondChildItem = new RadialMenuItem("Second",null);
		secondChildItem.setDisplayIcon(R.drawable.ic_launcher);
		secondChildItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
				Toast.makeText(RadialMenuActivity.this, "Second inner menu selected.", Toast.LENGTH_LONG).show();
			}
		});
		
		thirdChildItem = new RadialMenuItem("Third","Third");
		thirdChildItem.setDisplayIcon(R.drawable.ic_launcher);
		thirdChildItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
				Toast.makeText(RadialMenuActivity.this, "Third inner menu selected.", Toast.LENGTH_LONG).show();
			}
		});
		
		menuExpandItem = new RadialMenuItem("Expandable", "Expandable");
		
		children.add(firstChildItem);
		children.add(secondChildItem);
		children.add(thirdChildItem);
		menuExpandItem.setMenuChildren(children);
		
		menuCloseItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
				//menuLayout.removeAllViews();
				pieMenu.dismiss();
			}
		});
		
		menuItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
				startActivity(new Intent(RadialMenuActivity.this, UITest.class));
				pieMenu.dismiss();
			}
		});
		
		//pieMenu.setDismissOnOutsideClick(true, menuLayout);
		pieMenu.setAnimationSpeed(0L);
		pieMenu.setSourceLocation(200, 200);
		pieMenu.setIconSize(15, 30);
		pieMenu.setTextSize(13);
		pieMenu.setOutlineColor(Color.BLACK, 225);
		pieMenu.setInnerRingColor(0xAA66CC, 180);
		pieMenu.setOuterRingColor(0x0099CC, 180);
		//pieMenu.setHeader("Test Menu", 20);
		//pieMenu.setCenterCircle(menuCloseItem);

		pieMenu.addMenuEntry(new ArrayList<RadialMenuItem>() {{
			add(menuItem); 
			add(menuExpandItem);}});
		
		//pieMenu.addMenuEntry(menuItem);
		//pieMenu.addMenuEntry(menuExpandItem);

		Button testButton = (Button) this.findViewById(R.id.radial_menu_btn);
		testButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//menuLayout.addView(pieMenu);
				pieMenu.show(v);
			}
		});
	}
}
