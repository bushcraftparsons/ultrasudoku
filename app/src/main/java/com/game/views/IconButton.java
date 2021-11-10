package com.game.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.example.killersudoku.R;

/**
 * Represents icon buttons for game actions
 */
public class IconButton extends Button {
    private TextPaint mTextPaint;
    private Drawable icon;
    private String action;
    private int labelColour;

    public IconButton(Context context) {
        super(context);
        init(null, 0);
    }

    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public IconButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setWillNotDraw(false);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.IconButton, defStyle, 0);

        if (a.hasValue(R.styleable.IconButton_iconDrawable)) {
            icon = a.getDrawable(
                    R.styleable.IconButton_iconDrawable);
            icon.setCallback(this);
        }

        labelColour = a.getColor(
                R.styleable.IconButton_labelColour,
                labelColour);

        action = a.getString(
                R.styleable.IconButton_iconAction);

        a.recycle();


        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(labelColour);

        setPadding(0,0,0,150);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (icon != null) {
            // Draw the icon
            icon.draw(canvas);
        }
        canvas.drawText(action,
                getWidth()/2.0f,//Position of center of number, because it's centred text
                getHeight()*23.0f/24,//Position of bottom of number
                mTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged (int w,
                                  int h,
                                  int oldw,
                                  int oldh){
        if(icon!=null){
            icon.setBounds(new Rect(w/4,h/8,w*3/4,(h*7/12)));
        }

        invalidateTextPaintAndMeasurements();
    }

    public String getAction() {
        return action;
    }
}