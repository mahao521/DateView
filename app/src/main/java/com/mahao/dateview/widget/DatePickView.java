package com.mahao.dateview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.mahao.dateview.R;

/**
 * Created by Penghy on 2017/8/31.
 */


public class DatePickView extends View {

    private float mDensity;
    private int mTouchSlop;
    private int showCount;
    private float mDateheight;
    private int mDateBgColor;
    private int mDateTxtColor;
    private Paint mPaint;
    private int mLineColor;
    private String[] mWeekStr;
    private String[] mDayStr;
    private Paint mBgPaint;
    private int mCurrentNum = -1;
    private int mBgSelectColor;
    int downX = 0;
    int downY = 0;
    private int startIndex = 6;
    private DateClickListener mClickListener;
    private int mTxtSelectColor;

    public DatePickView(Context context) {
        this(context,null);
    }

    public DatePickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DatePickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context,attrs);
    }

    /**
     *  设置开始显示的位置
     * @param index
     */
    public void setStartIndex(int index){

        this.startIndex = index;
        invalidate();
    }

    private void initData(Context context, AttributeSet attrs) {

        showCount = 7;
        mDensity = getResources().getDisplayMetrics().density;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray typedArray = context.getResources().obtainAttributes(attrs, R.styleable.DatePickView);
        mDateheight = typedArray.getDimension(R.styleable.DatePickView_dateHeight, 200);
        mDateBgColor = typedArray.getInt(R.styleable.DatePickView_txtBgColor, Color.RED);
        mDateTxtColor = typedArray.getInt(R.styleable.DatePickView_txtColor, Color.BLUE);
        mLineColor = typedArray.getInt(R.styleable.DatePickView_lineColor, Color.BLUE);
        mBgSelectColor = typedArray.getInt(R.styleable.DatePickView_txtBgSecColor, Color.BLUE);
        mTxtSelectColor = typedArray.getColor(R.styleable.DatePickView_txtSelectColor, Color.WHITE);
        typedArray.recycle();

        //画字
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mLineColor);
        mPaint.setTextSize(15*mDensity);

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mBgPaint.setColor(mDateBgColor);
        mBgPaint.setStyle(Paint.Style.FILL);
        mWeekStr = new String[]{"周一","周二","周三","周四","周五","周六","周日"};
        mDayStr = new String[31+startIndex];
        for(int i = 0; i < startIndex; i++){
            mDayStr[i] = "";
        }
        for(int i = startIndex; i < mDayStr.length; i++){
            mDayStr[i] = ""+(i-startIndex +1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int realWidth = withSize,realHeigh;
        if(withMode == MeasureSpec.AT_MOST){
            realWidth = (int) (300*mDensity);
        }
        realHeigh = (int) (showCount * mDateheight);
        setMeasuredDimension(realWidth,realHeigh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        int width = getWidth();
        Path path;
        float startX = 0;
        float endX = width;
        mPaint.setColor(mLineColor);
        canvas.drawRect(0,0,width,mDateheight,mPaint);
        for(int i = 0; i < 2; i++){

            path = new Path();
            float startY = i*mDateheight;
            path.moveTo(startX,startY);
            path.lineTo(endX,startY);
            canvas.drawPath(path,mPaint);
        }
        mPaint.setColor(mDateTxtColor);
        int widthColum = width/showCount;
        Paint.FontMetricsInt font = mPaint.getFontMetricsInt();
        for(int i = 0; i < mWeekStr.length; i++){

            String showWeek = mWeekStr[i].toString();
            canvas.drawText(showWeek,widthColum*i + widthColum/2- mPaint.measureText(showWeek)/2,widthColum/2 - font.bottom/2 - font.top/2,mPaint);
        }
        mBgPaint.setColor(mDateBgColor);
        for(int j = 0; j < mDayStr.length; j++){

            String showDay = mDayStr[j];
            if (!TextUtils.isEmpty(showDay)) {
                /*canvas.drawCircle(widthColum*(j%7) + widthColum/2,
                        (j/showCount+1)*mDateheight + mDateheight/2,
                        Math.min(widthColum,mDateheight)/3,mBgPaint);*/
                canvas.drawText(showDay, widthColum*(j%7) + widthColum/2- mPaint.measureText(showDay)/2,
                        (j/showCount+1)*mDateheight + mDateheight/2 - font.bottom/2 - font.top/2,
                        mPaint);
            }
        }
        mBgPaint.setColor(mBgSelectColor);
        mPaint.setColor(mTxtSelectColor);
        if(mCurrentNum != -1){
            String showDay = mDayStr[mCurrentNum];
            canvas.drawCircle(widthColum*(mCurrentNum%7) + widthColum/2,
                    (mCurrentNum/showCount+1)*mDateheight + mDateheight/2,
                    Math.min(widthColum,mDateheight)/3,mBgPaint);
            canvas.drawText(showDay, widthColum*(mCurrentNum%7) + widthColum/2- mPaint.measureText(showDay)/2,
                    (mCurrentNum/showCount+1)*mDateheight + mDateheight/2 - font.bottom/2 - font.top/2,
                    mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
                    performClick();
                    doClickAction((upX + downX)/2,(upY + downY)/2);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void doClickAction(int x, int y) {

        int row = (int) (y / mDateheight );
        int column = (int) (x / (getWidth()/ showCount));
        Log.i("mahao",row+"....." + column);
        if(row == 0){
            mCurrentNum = column;
        }else{
            mCurrentNum = (row -1) * showCount + column;
        }
        if(mCurrentNum >= startIndex  && mCurrentNum < startIndex + 31){
            invalidate();
            if(mClickListener != null){
                mClickListener.onItemClick(mCurrentNum+"");
            }
        }
    }

    public void setOnItemClickListener(DateClickListener listener){

        this.mClickListener = listener;
    }

    public interface DateClickListener{

        void onItemClick(String str);
    }

}




















