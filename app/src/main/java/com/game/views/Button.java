package com.game.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.killersudoku.R;

/**
 * A button which uses an icon or single character
 */
public class Button extends View {


    public Button(Context context) {
        super(context);
        init(null, 0);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.Button, defStyle, 0);

        if (a.hasValue(R.styleable.Button_gradientRing)) {
            Drawable flashyRing = a.getDrawable(
                    R.styleable.Button_gradientRing);
            flashyRing.setCallback(this);
            setBackground(flashyRing);
        }

        a.recycle();

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener mainListener) {
        OnClickListener includeAnimation = v -> {
            if(v instanceof Button){
                Button button = (Button) v;
                AnimationDrawable buttonAnimation = (AnimationDrawable) button.getBackground();
                buttonAnimation.stop();
                buttonAnimation.start();
            }
            mainListener.onClick(v);
        };
        super.setOnClickListener(includeAnimation);
    }
}