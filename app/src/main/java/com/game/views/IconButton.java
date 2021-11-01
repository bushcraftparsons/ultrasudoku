package com.game.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.killersudoku.R;

/**
 * TODO: document your custom view class.
 */
public class IconButton extends Button {
    private Paint iconPaint;
    private Drawable icon;
    private String action;
//    private enum ACTION{
//        ERASE("delete"),
//        UNDO("undo"),
//        EDIT("edit"),
//        HINT("hint");
//
//        private final String text;
//
//        /**
//         * @param text
//         */
//        ACTION(final String text) {
//            this.text = text;
//        }
//
//        /* (non-Javadoc)
//         * @see java.lang.Enum#toString()
//         */
//        @Override
//        public String toString() {
//            return text;
//        }
//    }

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

        action = a.getString(
                R.styleable.IconButton_iconAction);

        a.recycle();


        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
        if(icon!=null){
            icon.setBounds(new Rect(w/4,h/4,w*3/4,h*3/4));
        }

        invalidateTextPaintAndMeasurements();
    }

    public String getAction() {
        return action;
    }
}