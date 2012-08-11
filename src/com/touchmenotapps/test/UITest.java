package com.touchmenotapps.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class UITest extends Activity {
	
	private LayoutInflater mInflater;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carousel);

        mInflater = LayoutInflater.from(this);
        final CarouselLayout layout = (CarouselLayout) findViewById(R.id.carouselLayout);
        final CarouselAdapter adapter = new CarouselAdapter();

        layout.setAdapter(adapter);
        
        //Variant 2
        //layout.setTranslate(50);
        //layout.setSpaceBetweenViews(100);
        
        //Variant 3
        /*layout.setTranslatation(50);
        layout.setSpaceBetweenViews(100);
        layout.setRotation(-10);*/
        
        //Variant 4
        /*layout.setTranslatation(-50);
        layout.setSpaceBetweenViews(100);
        layout.setRotation(-10);*/
    }

    //~--- inner classes ------------------------------------------------------

    private class CarouselAdapter extends BaseAdapter {
        private int[] mImagesID = {
            R.drawable.android1, 
            R.drawable.android2, 
            R.drawable.android3, 
            R.drawable.android4, 
            R.drawable.android5,
            R.drawable.android6, 
            R.drawable.android7, 
            R.drawable.android8, 
            R.drawable.android9
        };

        @Override
        public int getCount() {
            return mImagesID.length;
        }

        @Override
        public Object getItem(int position) {
            return mImagesID[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
        	ViewHolder holder = new ViewHolder();;
        	
            if ((position < 0) && (position > (getCount() - 1))) {
                Log.e("UITEST", "Unexistent position requested");
                return null;
            }
            
            if(convertView ==  null) {
            	convertView = mInflater.inflate(R.layout. item_layout, null);
    			holder.mButton = (ImageButton) convertView.findViewById(R.id.item_image_btn);
    			convertView.setTag(holder);
            } else {
            	holder = (ViewHolder) convertView.getTag();
            }
            
           holder.mButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(v.getContext(), "Test pos " + String.valueOf(position), 5000).show();
				}
			});
            
           return convertView;
        }
    }
    
    static class ViewHolder {
		ImageButton mButton;
	}
}
