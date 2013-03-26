package com.touchmenotapps.radialdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainMenu extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Check the OS and set the app bar likewise
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR2) {
			setTheme(android.R.style.Theme_Holo_Light);
			getActionBar().setDisplayShowHomeEnabled(true);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
			getWindow().setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
			getActionBar().setDisplayShowHomeEnabled(true);
		} else {
			setTheme(R.style.RadialMenuLegacyTitleBar);
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_appbar);
			TextView barHeader = (TextView) findViewById(R.id.appbar_title_text);
			barHeader.setText(R.string.app_name);
		}

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.main_menu)));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case 0:
					startActivity(new Intent(MainMenu.this,
							AltRadialMenuActivity.class));
					break;
				case 1:
					startActivity(new Intent(MainMenu.this,
							RadialMenuActivity.class));
					break;
				case 2:
					startActivity(new Intent(MainMenu.this, RadialProgressActivity.class));
					break;
				default:
					break;
				}
			}
		});

	}
}
