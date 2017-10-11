package com.example.duxiji.ratingstar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.duxiji.ratingstar.R;


/**
 * Created by duxiji on 2017/9/21
 * 星星控件，评分的单位长度为1颗星星
 */

public class Ratingbar extends View {
    public static final int DEFAULT_STAR_NUM = 5;//默认星星的数目
    public static final int DEFAULT_STAR_INIT = 3;//默认点亮的星星数目
    public static final int DEFAULT_STAR_PADDING = 6;//默认星星之间的padding
    public static final int DEFAULT_STAR_SIZE = 24;//默认星星的大小，按照正方形绘制
    public static final int DEFAULT_STAR_MININUM = 1;//默认最小的星星数量

    private int starNum;
    private int starInitNum;
    private int starPadding;
    private int starSize;
    private int starEmptyPicRes;
    private int starFullPicRes;
    private Paint mPaint;
    private Bitmap starEmptyBitmap;
    private Bitmap starFullBitmap;
    private Boolean isFirstDraw = true;
    private Boolean isCanTouch = true;

    public interface OnStarChangeListener {
        void OnStarChange(int currentStarNum);
    }

    private OnStarChangeListener listener;

    public void setOnStarChangeListener(OnStarChangeListener listener) {
        this.listener = listener;
    }

    public Ratingbar(Context context) {
        this(context, null);
    }

    public Ratingbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ratingbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Ratingbar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.Ratingbar_starNum:
                    starNum = a.getInt(attr, DEFAULT_STAR_NUM);
                    break;
                case R.styleable.Ratingbar_starInitNum:
                    starInitNum = a.getInt(attr, DEFAULT_STAR_INIT);
                    break;
                case R.styleable.Ratingbar_starSize:
                    starSize = a.getDimensionPixelOffset(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STAR_SIZE, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Ratingbar_starPadding:
                    starPadding = a.getDimensionPixelOffset(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STAR_PADDING, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Ratingbar_starEmptyPic:
                    starEmptyPicRes = a.getResourceId(attr, -1);
                    if (starEmptyPicRes != -1) {
                        starEmptyBitmap = BitmapFactory.decodeResource(getResources(), starEmptyPicRes);
                        starEmptyBitmap = Bitmap.createScaledBitmap(starEmptyBitmap, starSize, starSize, true);//将bitmap拉伸
                    }
                    break;
                case R.styleable.Ratingbar_starFullPic:
                    starFullPicRes = a.getResourceId(attr, -1);
                    if (starFullPicRes != -1) {
                        starFullBitmap = BitmapFactory.decodeResource(getResources(), starFullPicRes);
                        starFullBitmap = Bitmap.createScaledBitmap(starFullBitmap, starSize, starSize, true);//将bitmap拉伸
                    }
                    break;
            }
        }
        a.recycle();
//        setClickable(true);
        unitLength = starSize + starPadding;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measuredHeight(heightMeasureSpec));
    }

    private int measuredHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            //padding + 星星高度
            result = getPaddingTop() + getPaddingBottom() + starSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            //padding + 星星间距*（num-1）+星星大小*num
            result = getPaddingLeft() + getPaddingRight() + starPadding * (starNum - 1) + starSize * starNum;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int left = 0;
    private int top = getPaddingTop();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, getMeasuredHeight() / 2 - starSize / 2);
        //先画出灰色的星星
        for (int i = 1; i <= starNum; i++) {
            left = getPaddingLeft() + (i - 1) * starPadding + (i - 1) * starSize;//算出左坐标
            canvas.drawBitmap(starEmptyBitmap, left, top, mPaint);
        }
        //判断是否第一次初始化，是就画出默认点亮的星星
        if (isFirstDraw) {
            for (int i = 1; i <= starInitNum; i++) {
                left = getPaddingLeft() + (i - 1) * starPadding + (i - 1) * starSize;//算出左坐标
                canvas.drawBitmap(starFullBitmap, left, top, mPaint);
            }
        }
        //画出点亮的星星
        for (int i = 1; i <= starFullPicNum; i++) {
            left = getPaddingLeft() + (i - 1) * starPadding + (i - 1) * starSize;//算出左坐标
            canvas.drawBitmap(starFullBitmap, left, top, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isFirstDraw = false;
        if (isCanTouch) {//可以滑动触摸
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    caculateStarFullPicNum(event.getX());
                    break;
                case MotionEvent.ACTION_MOVE:
                    caculateStarFullPicNum(event.getX());
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
        return false;
    }

    private int starFullPicNum = 1;
    private int unitLength;//一个星星大小加上一个间距

    /**
     * 根据X轴坐标计算当前的星星num
     *
     * @param startX
     */
    private void caculateStarFullPicNum(float startX) {
        if (startX < getPaddingLeft()) {//触摸位置在星星左侧
            starFullPicNum = DEFAULT_STAR_MININUM;
        } else if (startX > (getMeasuredWidth() - getPaddingRight() - starSize)) {//触摸位置在右侧星星边界加上倒数第一个星星的距离
            starFullPicNum = starNum;
        } else {//触摸在第一个星星到第(n-1)个星星之间，用坐标轴的思想考虑
            starFullPicNum = (int) ((startX - getPaddingLeft()) / unitLength) + 1;
        }
        if (listener != null) {
            listener.OnStarChange(starFullPicNum);
        }
        invalidate();
    }

    /**
     * 设置画的星星num，并重画
     */
    public void setCurrentStarFullNum(int num) {
        this.starFullPicNum = num;
        invalidate();
    }

    /**
     * 设置能否滑动
     */
    public void setIsCanTouch(Boolean isCanTouch) {
        this.isCanTouch = isCanTouch;
    }
}
