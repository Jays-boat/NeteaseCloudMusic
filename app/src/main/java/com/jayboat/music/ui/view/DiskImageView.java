package com.jayboat.music.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.animation.LinearInterpolator;

import com.jayboat.music.App;
import com.jayboat.music.R;

public class DiskImageView extends android.support.v7.widget.AppCompatImageView {

    private Drawable upPicture;
    private Bitmap disk;
    private Paint mPaint = new Paint();
    private ObjectAnimator objectAnimator;

    public static final int STATE_PLAYING =1;//正在播放
    public static final int STATE_PAUSE =2;//暂停
    public static final int STATE_STOP =3;//停止
    public int state;

    public DiskImageView(Context context) {
        this(context, null);
    }

    public DiskImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiskImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(attrs);
        initAnimator();
        mPaint.setAntiAlias(true);
        disk = initDisc();
    }


    private void initAttribute(AttributeSet attributeSet) {
        TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.DiskImageView);
        int resId = array.getResourceId(R.styleable.DiskImageView_div_src, -1);
        if (resId == -1) {
            upPicture = getResources().getDrawable(R.drawable.bg_default_main_drawer_header);
        } else {
            upPicture = getResources().getDrawable(resId);
        }
        array.recycle();

    }

    private Bitmap initDisc() {
        Bitmap cropBitmap, tempBitmap;
        Bitmap up = ((BitmapDrawable) upPicture).getBitmap();
        int bmpHeight = up.getHeight();
        int bmpWidth = up.getWidth();
        int cropLength, x, y;
        int diameter = (int) SizeUtil.getPicSize();
        int strokeWidth = (int) ((SizeUtil.getDiscSize() - SizeUtil.getPicSize()) / 2);

        if (bmpHeight > bmpWidth) {
            cropLength = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            tempBitmap = Bitmap.createBitmap(up, x, y, cropLength, cropLength);
        } else if (bmpHeight < bmpWidth) {
            cropLength = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            tempBitmap = Bitmap.createBitmap(up, x, y, cropLength, cropLength);
        } else {
            tempBitmap = up;
        }

        if (tempBitmap.getWidth() != diameter || tempBitmap.getHeight() != diameter) {
            cropBitmap = Bitmap.createScaledBitmap(tempBitmap, diameter, diameter, true);

        } else {
            cropBitmap = tempBitmap;
        }

        cropLength = cropBitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(cropLength + strokeWidth * 2, cropLength + strokeWidth * 2
                , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, cropLength, cropLength);

        canvas.drawBitmap(cropBitmap , strokeWidth + (cropLength / 2) - rect.width() / 2, strokeWidth + (cropLength / 2) - rect.height() / 2, paint);

        Paint mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setColor(Color.parseColor("#000000"));
        mBackgroundPaint.setStrokeWidth(strokeWidth);

        canvas.drawCircle(strokeWidth+cropLength/2,strokeWidth+cropLength/2,(cropLength+strokeWidth)/2,mBackgroundPaint);
        return output;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == heightMode && widthMode == MeasureSpec.AT_MOST) {
            int length = Math.min(heightSize, widthSize);
            length = (int) Math.min(length, SizeUtil.getDiscSize());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        } else {
            int length = Math.min(heightSize, widthSize);
            length = (int) Math.min(length,SizeUtil.getDiscSize());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int left = getWidth()-SizeUtil.getDiscSize() <= 0 ? 0 : (int) ((getWidth() - SizeUtil.getDiscSize()) / 2);
        canvas.drawBitmap(disk,left,0,mPaint);

    }

    public void setUpPicture(Drawable drawable) {
        upPicture = drawable;
        disk = initDisc();
        invalidate();
    }

    private void initAnimator(){
        state = STATE_STOP;
        objectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);//添加旋转动画，旋转中心默认为控件中点
        objectAnimator.setDuration(10000);//设置动画时间
        objectAnimator.setInterpolator(new LinearInterpolator());//动画时间线性渐变
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
    }

    public void playDisk(){
        if(state == STATE_STOP){
            objectAnimator.start();//动画开始
            state = STATE_PLAYING;
        }else if(state == STATE_PAUSE){
            objectAnimator.resume();//动画重新开始
            state = STATE_PLAYING;
        }else if(state == STATE_PLAYING){
            objectAnimator.pause();//动画暂停
            state = STATE_PAUSE;
        }
    }

    public void stopDisk(){
        objectAnimator.end();//动画结束
        state = STATE_STOP;
    }

    static class SizeUtil {
        private static int getScreenWidth() {
            DisplayMetrics dm = App.getAppContext().getResources().getDisplayMetrics();
            return dm.widthPixels;
        }

        private static int getScreenHeight(){
            DisplayMetrics dm = App.getAppContext().getResources().getDisplayMetrics();
            return dm.heightPixels;
        }

        static float getPicSize() {
            return (float) (533.0 / 1080.0 * getScreenWidth());
        }

        static float getDiscSize() {
            return (float) (813.0 / 1080 * getScreenWidth());
        }

        static float getMarginTop() { return (float) (190.0 / 1080.0 * getScreenHeight());}
    }


}


