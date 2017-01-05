package com.gjiazhe.panoramaimageview.sample.observe_impl;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.gjiazhe.panoramaimageview.sample.observe_interface.ImgvObserve;

/**
 * Created by gjz on 19/12/2016.
 */

public class PanoramaImageView extends ImageView implements ImgvObserve {

    // Image's scroll orientation
    public final static byte ORIENTATION_NONE = -1;
    public final static byte ORIENTATION_HORIZONTAL = 2;
    public final static byte ORIENTATION_VERTICAL = 1;
    private byte orientation = ORIENTATION_NONE;
    // Enable panorama effect or not
    private boolean mEnablePanoramaMode;
    // If true, the image scroll left(top) when the device clockwise rotate along y-axis(x-axis).
    private boolean mInvertScrollDirection;
    // Image's width and height
    private int drawableWidth;
    private int drawableHeight;
    // View's width and height
    private int pictureShowWidth;
    private int pictureShowHeight;
    // Image's offset from initial state(center in the view).
    private float maxOffset;
    // The scroll progress.
    private float progress;
    // Show scroll bar or not
    private boolean mEnableScrollbar;
    // The paint to draw scrollbar
    private Paint mScrollbarPaint;
    // Observe scroll state
    private OnPanoramaScrollListener mOnPanoramaScrollListener;

    private static double maxRotateRadian = Math.PI / 6;

    public PanoramaImageView(Context context) {
        this(context, null);
    }

    public PanoramaImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanoramaImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //恩  很是无耻^-^
        super.setScaleType(ScaleType.CENTER_CROP);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.gjiazhe.panoramaimageview.R.styleable.PanoramaImageView);
        mEnablePanoramaMode = typedArray.getBoolean(com.gjiazhe.panoramaimageview.R.styleable.PanoramaImageView_piv_enablePanoramaMode, true);
        mInvertScrollDirection = typedArray.getBoolean(com.gjiazhe.panoramaimageview.R.styleable.PanoramaImageView_piv_invertScrollDirection, false);
        mEnableScrollbar = typedArray.getBoolean(com.gjiazhe.panoramaimageview.R.styleable.PanoramaImageView_piv_show_scrollbar, true);
        typedArray.recycle();
        if (mEnableScrollbar) {
            initScrollbarPaint();
        }
    }

    private void initScrollbarPaint() {
        mScrollbarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScrollbarPaint.setColor(Color.WHITE);
        mScrollbarPaint.setStrokeWidth(dp2px(1.5f));
    }

    //    //设置观察者
//    public void setGyroscopeObserver(GyroscopeObserver observer) {
//        if (observer != null) {
//            observer.addPanoramaImageView(this);
//        }
//    }
    //更新进度
    void updateProgress(float progress) {
        if (mEnablePanoramaMode) {
            progress = mInvertScrollDirection ? -progress : progress;
            invalidate();
            if (mOnPanoramaScrollListener != null) {
                mOnPanoramaScrollListener.onScrolled(this, -progress);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        pictureShowWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        pictureShowHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        if (getDrawable() != null) {
            drawableWidth = getDrawable().getIntrinsicWidth();
            drawableHeight = getDrawable().getIntrinsicHeight();
            Log.e("TAG", "onMeasure: " + pictureShowWidth + "---" + pictureShowHeight + "---" + drawableHeight + "---" + drawableWidth);
            Log.e("TAG", "onMeasure: " + ((float) drawableHeight / pictureShowHeight) + "---" + ((float) drawableWidth / pictureShowWidth));
            if ((float) drawableHeight / pictureShowHeight > (float) drawableWidth / pictureShowWidth) {
                orientation = ORIENTATION_VERTICAL;
                float imgScale = (float) drawableWidth / (float) pictureShowWidth;
                maxOffset = (drawableHeight / imgScale - pictureShowHeight) * 0.5f;
            } else if ((float) drawableHeight / pictureShowHeight < (float) drawableWidth / pictureShowWidth) {
                orientation = ORIENTATION_HORIZONTAL;
                float imgScale = (float) drawableHeight / (float) pictureShowHeight;
                maxOffset = (drawableWidth / imgScale - pictureShowWidth) * 0.5f;
            }
            Log.e("TAG", "onDraw:执行了方向 " + orientation);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mEnablePanoramaMode || getDrawable() == null || isInEditMode()) {
            super.onDraw(canvas);
            Log.e("TAG", "onDraw:执行了 ");
            return;
        }
        if (orientation == ORIENTATION_HORIZONTAL) {
            float currentOffsetX = maxOffset * progress;
            canvas.save();
            canvas.translate(currentOffsetX, 0);
            super.onDraw(canvas);
            canvas.restore();
        } else if (orientation == ORIENTATION_VERTICAL) {
            float currentOffsetY = maxOffset * progress;
            canvas.save();
            canvas.translate(0, currentOffsetY);
            super.onDraw(canvas);
            canvas.restore();
        }
        // Draw scrollbar
//        if (mEnableScrollbar) {
//            switch (mOrientation) {
//                case ORIENTATION_HORIZONTAL: {
//                    float barBgWidth = mWidth * 0.9f;
//                    float barWidth = barBgWidth * mWidth / mDrawableWidth;
//
//                    float barBgStartX = (mWidth - barBgWidth) / 2;
//                    float barBgEndX = barBgStartX + barBgWidth;
//                    float barStartX = barBgStartX + (barBgWidth - barWidth) / 2 * (1 - mProgress);
//                    float barEndX = barStartX + barWidth;
//                    float barY = mHeight * 0.95f;
//
//                    mScrollbarPaint.setAlpha(100);
//                    canvas.drawLine(barBgStartX, barY, barBgEndX, barY, mScrollbarPaint);
//                    mScrollbarPaint.setAlpha(255);
//                    canvas.drawLine(barStartX, barY, barEndX, barY, mScrollbarPaint);
//                    break;
//                }
//                case ORIENTATION_VERTICAL: {
//                    float barBgHeight = mHeight * 0.9f;
//                    float barHeight = barBgHeight * mHeight / mDrawableHeight;
//
//                    float barBgStartY = (mHeight - barBgHeight) / 2;
//                    float barBgEndY = barBgStartY + barBgHeight;
//                    float barStartY = barBgStartY + (barBgHeight - barHeight) / 2 * (1 - mProgress);
//                    float barEndY = barStartY + barHeight;
//                    float barX = mWidth * 0.95f;
//
//                    mScrollbarPaint.setAlpha(100);
//                    canvas.drawLine(barX, barBgStartY, barX, barBgEndY, mScrollbarPaint);
//                    mScrollbarPaint.setAlpha(255);
//                    canvas.drawLine(barX, barStartY, barX, barEndY, mScrollbarPaint);
//                    break;
//                }
//            }
//        }
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public void setEnablePanoramaMode(boolean enable) {
        mEnablePanoramaMode = enable;
    }

    public boolean isPanoramaModeEnabled() {
        return mEnablePanoramaMode;
    }

    public void setInvertScrollDirection(boolean invert) {
        if (mInvertScrollDirection != invert) {
            mInvertScrollDirection = invert;
        }
    }

    public boolean isInvertScrollDirection() {
        return mInvertScrollDirection;
    }

    public void setEnableScrollbar(boolean enable) {
        if (mEnableScrollbar != enable) {
            mEnableScrollbar = enable;
            if (mEnableScrollbar) {
                initScrollbarPaint();
            } else {
                mScrollbarPaint = null;
            }
        }
    }

    public boolean isScrollbarEnabled() {
        return mEnableScrollbar;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        /**
         * Do nothing because PanoramaImageView only
         * supports {@link scaleType.CENTER_CROP}
         */
    }

    @Override
    public void updata(double rotateRadian, int type) {
        //如果type与当前图片的type一致
        if (orientation == type) {
            //限制旋转弧度最大为maxRotateRadian
            if (rotateRadian >= maxRotateRadian) {
                rotateRadian = maxRotateRadian;
            } else if (rotateRadian <= -maxRotateRadian) {
                rotateRadian = -maxRotateRadian;
            }
            //计算出比例值
            progress = (float) (rotateRadian / maxRotateRadian);
            invalidate();
        }
    }

    /**
     * Interface definition for a callback to be invoked when the image is scrolling
     */
    public interface OnPanoramaScrollListener {
        /**
         * Call when the image is scrolling
         *
         * @param view           the panoramaImageView shows the image
         * @param offsetProgress value between (-1, 1) indicating the offset progress.
         *                       -1 means the image scrolls to show its left(top) bound,
         *                       1 means the image scrolls to show its right(bottom) bound.
         */
        void onScrolled(PanoramaImageView view, float offsetProgress);
    }

    public void setOnPanoramaScrollListener(OnPanoramaScrollListener listener) {
        mOnPanoramaScrollListener = listener;
    }
}
