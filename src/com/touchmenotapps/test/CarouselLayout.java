package com.touchmenotapps.test;

//~--- non-JDK imports --------------------------------------------------------

import android.content.Context;

import android.graphics.Matrix;

import android.os.SystemClock;

import android.util.AttributeSet;
import android.util.Log;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;

import android.widget.BaseAdapter;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

//~--- classes ----------------------------------------------------------------

public class CarouselLayout extends ViewGroup {
    //~--- static fields ------------------------------------------------------

    /* Scale ratio for each "layer" of children */
    private final float SCALE_RATIO = 0.9f;
    /* Gesture sensibility */
    private final int MAJOR_MOVE      = 120;
    /* Animation time */
    private int DURATION        = 200;

    //~--- fields -------------------------------------------------------------

    /* Number of pixel between the top of two Views */
    private int mSpaceBetweenViews = 20;
    /* Rotation between two Views*/
    private int mRotation;
    /* Status of rotation */
    private boolean mRotationEnabled = false;
    /* Tanslation between two Views*/
    private int mTranslate;
    /* Status of translatation */
    private boolean mTranslatateEnbabled = false;

    /* Number of internal Views */
    private int mHowManyViews    = 99;
    /* Size of internal Views */
    private float mChildSizeRatio = 0.6f;
    /* Adapter */
    private BaseAdapter mAdapter = null;
    /* Item index of center view */
    private int mCurrentItem = 0;
    /* Index of center view in the ViewGroup */
    private int mCenterView  = 0;
    /* Width of all children */
    private int mChildrenWidth;
    /* Width / 2 */
    private int mChildrenWidthMiddle;
    /* Height of all children */
    private int mChildrenHeight;
    /* Height / 2 */
    private int mChildrenHeightMiddle;
    /* Height center of the ViewGroup */
    private int mHeightCenter;
    /* Width center of the ViewGroup */
    private int mWidthCenter;
    /* Number of view below/above center view */
    private int mMaxChildUnderCenter;
    /* Collect crap views */
    private Collector mCollector   = new Collector();
    /* Avoid multiple allocation */
    private Matrix mMatrix = new Matrix();
    
    /* Gap between fixed position (for animation) */
    private float mGap;
    /* is animating */
    private boolean mIsAnimating = false;
    /* Avoid multiple allocation */
    private long mCurTime;
    /* Animation start time */
    private long mStartTime;
    /* Final item to reach (for animation from mCurrentItem to mItemToReach) */
    private int mItemtoReach       = 0;
    /* Animation Task */
    private Runnable animationTask = new Runnable() {
        public void run() {
            mCurTime = SystemClock.uptimeMillis();

            long totalTime = mCurTime - mStartTime;

            // Animation end
            if (totalTime > DURATION) {

                // Add new views
                if (mItemtoReach > mCurrentItem) {
                    fillBottom();
                } else {
                    fillTop();
                }
                
                // Register value to stop animation
                mCurrentItem = mItemtoReach;
                mGap         = 0;
                mIsAnimating  = false;

                // Calculate the new center view in the ViewGroup
                mCenterView = mCurrentItem;
                if (mCurrentItem >= mMaxChildUnderCenter) {
                    mCenterView = mMaxChildUnderCenter;
                }

                removeCallbacks(animationTask);
            // Animate    
            } else {
                float perCent = ((float) totalTime) / DURATION;

                mGap = (mCurrentItem - mItemtoReach) * perCent;
                post(this);
            }
            //Layout children
            childrenLayout(mGap);
            invalidate();
        }
    };
    
    /* Detect user gesture */
    private GestureDetector mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        	// Intercept gestion only if not animating views
            if (!mIsAnimating && mAdapter!=null) {
                int dy = (int) (e2.getY() - e1.getY());
                if ((Math.abs(dy) > MAJOR_MOVE) && (Math.abs(velocityX) < Math.abs(velocityY))) {
                    if (velocityY > 0) {
                        // Top-bottom movement
                        if (mCurrentItem > 0) {
                            mItemtoReach = mCurrentItem - 1;
                            mStartTime   = SystemClock.uptimeMillis();
                            mIsAnimating = true;
                            post(animationTask);

                            return true;
                        }
                    } else {
                        // Bottom-Top movement
                        if (mCurrentItem < (mAdapter.getCount() - 1)) {
                            mItemtoReach = mCurrentItem + 1;
                            mStartTime   = SystemClock.uptimeMillis();
                            mIsAnimating = true;
                            post(animationTask);   

                            return true;
                        }
                    }
                }
            }

            return false;
        }
    });

    //~--- constructors -------------------------------------------------------

    public CarouselLayout(Context context) {
        super(context);
        initSlidingAnimation();
    }

    public CarouselLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSlidingAnimation();
    }

    public CarouselLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSlidingAnimation();
    }

    //~--- set methods --------------------------------------------------------

    public void setAnimationTime(int millis) {
    	this.DURATION = millis;
    }
    
    /* Define height space in pixel between 2 views*/
    public void setSpaceBetweenViews(int spaceInPixel) {
        mSpaceBetweenViews = spaceInPixel;
    }

    /* Define rotation between 2 views */
    public void setRotation(int rotation) {
        mRotationEnabled = true;
        mRotation        = rotation;
    }
    
    public void disableRotation(){
    	mRotationEnabled = false;
    }
    
    /* Define translate between 2 views */
    public void setTranslate(int translate) {
    	mTranslatateEnbabled = true;
    	mTranslate = translate;
    }
    
    public void disableTranslate(){
    	mTranslatateEnbabled = false;
    }

    /* Specify number of child to display (only odd number for this version) */
    public boolean setHowManyViews(int howMany) {
        if (howMany % 2 != 0) {
            return false;
        }
        mHowManyViews = howMany;

        return true;
    }

    /* Specify size ratio of all children */
    public boolean setChildSizeRation(float parentPerCent) {
        if ((parentPerCent > 1f) && (parentPerCent < 1f)) {
            return false;
        }
        mChildSizeRatio = parentPerCent;

        return true;
    }

    /* Set adapter */
    public void setAdapter(BaseAdapter adapter) {
        if (adapter != null) {
        	mAdapter = adapter;
        	
            mCenterView = mCurrentItem = 0;
            
            // even
            if ((mHowManyViews % 2) == 0) {
            	//TODO : Fix it (for the moment work only with odd mHowManyViews)
                mMaxChildUnderCenter = (mHowManyViews / 2);
            // odd
            } else {
                mMaxChildUnderCenter = (mHowManyViews / 2);
            }

            // Populate the ViewGroup
            for (int i = 0; i <= mMaxChildUnderCenter; i++) {
                if (i > (mAdapter.getCount() - 1)) {
                    break;
                }
                final View v = mAdapter.getView(i, null, this);
                addView(v);
            }
            childrenLayout(0);
            invalidate();
        }
    }

    //~--- methods ------------------------------------------------------------

    /* fillTop if required and garbage old views out of screen */
    private void fillTop() {
        // Local (below center): too many children
        if (mCenterView < mMaxChildUnderCenter) {
            if (getChildCount() > mMaxChildUnderCenter + 1) {
                View old = getChildAt(getChildCount() - 1);

                detachViewFromParent(old);
                mCollector.collect(old);
            }
        }

        // Global : too many children
        if (getChildCount() >= mHowManyViews) {
            View old = getChildAt(mHowManyViews - 1);

            detachViewFromParent(old);
            mCollector.collect(old);
        }

        final int indexToRequest = mCurrentItem - (mMaxChildUnderCenter + 1);

        // retrieve if required
        if (indexToRequest >= 0) {
            Log.v("UITEST", "Fill top with " + indexToRequest);

            View recycled = mCollector.retrieve();
            View v        = mAdapter.getView(indexToRequest, recycled, this);

            if (recycled != null) {
                attachViewToParent(v, 0, generateDefaultLayoutParams());
                v.measure(mChildrenWidth, mChildrenHeight);
            } else {
                addView(v, 0);
            }
        }
    }

    /* fillBottom if required and garbage old views out of screen */
    private void fillBottom() {
        // Local (above center): too many children
        if (mCenterView >= mMaxChildUnderCenter) {
            View old = getChildAt(0);

            detachViewFromParent(old);
            mCollector.collect(old);
        }

        // Global : too many children
        if (getChildCount() >= mHowManyViews) {
            View old = getChildAt(0);

            detachViewFromParent(old);
            mCollector.collect(old);
        }

        final int indexToRequest = mCurrentItem + (mMaxChildUnderCenter + 1);

        if (indexToRequest < mAdapter.getCount()) {
            Log.v("UITEST", "Fill bottom with " + indexToRequest);

            View recycled = mCollector.retrieve();
            View v        = mAdapter.getView(indexToRequest, recycled, this);

            if (recycled != null) {
                Log.v("UITEST", "view attached");
                attachViewToParent(v, -1, generateDefaultLayoutParams());
                v.measure(mChildrenWidth, mChildrenHeight);
            } else {
                Log.v("UITEST", "view added");
                addView(v, -1);
            }
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    private void initSlidingAnimation() {
        setChildrenDrawingOrderEnabled(true);
        setStaticTransformationsEnabled(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);

                return true;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count          = getChildCount();

        final int specWidthSize  = MeasureSpec.getSize(widthMeasureSpec);
        final int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);

        mWidthCenter    = specWidthSize / 2;
        mHeightCenter   = specHeightSize / 2;

        mChildrenWidth  = (int) (specWidthSize * mChildSizeRatio);
        mChildrenHeight = (int) (specHeightSize * mChildSizeRatio);
        
        mChildrenWidthMiddle = mChildrenWidth/2;
        mChildrenHeightMiddle = mChildrenHeight/2;

        // Measure all children
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            measureChild(child, mChildrenWidth, mChildrenHeight);
        }

        setMeasuredDimension(specWidthSize, specHeightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        childrenLayout(0);
    }

    /* Fix position of all children */
    private void childrenLayout(float gap) {

        final int leftCenterView = mWidthCenter - (mChildrenWidth / 2);
        final int topCenterView  = mHeightCenter - (mChildrenHeight / 2);

        final int count          = getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            final float offset     = mCenterView - i - gap;
            final int top          = (int) (topCenterView - (mSpaceBetweenViews * offset));

            child.layout(leftCenterView, top, leftCenterView + mChildrenWidth, top + mChildrenHeight);
        }
    }

    //~--- get methods --------------------------------------------------------

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int centerView = mCenterView;

        if (mGap > 0.5f) {
            centerView--;
        } else if (mGap < -0.5f) {
            centerView++;
        }

        // before center view
        if (i < centerView) {
            return i;
        // after center view
        } else if (i > centerView) {
            return centerView + (childCount - 1) - i;
        // center view
        } else {
            return childCount - 1;
        }
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
    	final int topCenterView = mHeightCenter - mChildrenHeightMiddle;

        final float offset            = (-child.getTop() + topCenterView) / (float) mSpaceBetweenViews;
        
        if (offset != 0) {
        	final float absOffset = Math.abs(offset);
            float scale = (float) Math.pow(SCALE_RATIO, absOffset);

            t.clear();
            t.setTransformationType(Transformation.TYPE_MATRIX);
            // We can play with transparency here -> t.setAlpha()
            
            final Matrix m = t.getMatrix();
            m.setScale(scale, scale);
            
            if (mTranslatateEnbabled){
            	m.setTranslate(mTranslate * absOffset, 0);
            }
            
            // scale from top
            if (offset > 0) {
                m.preTranslate(-mChildrenWidthMiddle, 0);
                m.postTranslate(mChildrenWidthMiddle, 0);
            // scale from bottom
            } else {
                m.preTranslate(-mChildrenWidthMiddle, -mChildrenHeight);
                m.postTranslate(mChildrenWidthMiddle, mChildrenHeight);
            }
            
            mMatrix.reset(); 
            if (mRotationEnabled) {
            	mMatrix.setRotate(mRotation * offset);
            }
            mMatrix.preTranslate(-mChildrenWidthMiddle, -mChildrenHeightMiddle);
            mMatrix.postTranslate(mChildrenWidthMiddle, mChildrenHeightMiddle);
            
            m.setConcat(m, mMatrix);
        }

        return true;
    }

    //~--- inner classes ------------------------------------------------------

    /* Class used to recycle views */
    private class Collector {
        ArrayList<View> mOldViews = new ArrayList<View>();

        public void collect(View v) {
            mOldViews.add(v);
        }

        public View retrieve() {
            if (mOldViews.size() == 0) {
                return null;
            } else {
                return mOldViews.remove(0);
            }
        }
    }
}
