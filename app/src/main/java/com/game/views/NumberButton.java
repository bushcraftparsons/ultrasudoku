package com.game.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import com.example.killersudoku.R;

/**
 * A button which uses an icon or single character
 */
public class NumberButton extends Button {
    private String iconButtonText=""; // TODO: use a default from R.string...
    private int textColour = Color.RED; // TODO: use a default from R.color...

    private TextPaint mTextPaint;
    private float mTextHeight;

    public NumberButton(Context context) {
        super(context);
        init(null, 0);
    }

    public NumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public NumberButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.NumberButton, defStyle, 0);

        iconButtonText = a.getString(
                R.styleable.NumberButton_iconButtonText);
        textColour = a.getColor(
                R.styleable.NumberButton_iconButtonTextColor,
                textColour);

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        // TODO: use a default from R.dimen...
        float iconButtonTextSize = 100;
        mTextPaint.setTextSize(iconButtonTextSize);
        mTextPaint.setColor(textColour);

        Rect bounds = new Rect();
        mTextPaint.getTextBounds("8", 0, "8".length(), bounds);
        mTextHeight = bounds.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the text.
        canvas.drawText(iconButtonText,
                getWidth()/2.0f,
                (getBottom()/2.0f) + (mTextHeight/2),
                mTextPaint);

    }

    @Override
    protected void onMeasure (int widthMeasureSpec,
                              int heightMeasureSpec){
        setMeasuredDimension(Math.max(widthMeasureSpec, 100), Math.max(widthMeasureSpec, 100));
    }

    @Override
    protected void onSizeChanged (int w,
                                  int h,
                                  int oldw,
                                  int oldh){
//        int targetWidth = w - 24;
//        float hi = 100;
//        float lo = 2;
//        final float threshold = 0.5f; // How close we have to be
//        while((hi - lo) > threshold) {
//            float size = (hi+lo)/2;
//            mTextPaint.setTextSize(size);
//            if(mTextPaint.measureText("3") >= targetWidth)
//                hi = size; // too big
//            else
//                lo = size; // too small
//        }
//        // Use lo so that we undershoot rather than overshoot
//        iconButtonTextSize=lo;
//
////        iconButtonTextSize = (float)((amountGreaterThanMin/50) * 50) + 100;//e.g. 103.8
//        if(iconButtonTextSize<100){
//            iconButtonTextSize = 100;//Minimum 60
//        }
//        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the button number.
     *
     * @return The button number.
     */
    public int getButtonNumber() {
        return Integer.parseInt(iconButtonText);
    }
}