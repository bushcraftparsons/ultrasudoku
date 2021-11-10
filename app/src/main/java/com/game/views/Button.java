package com.game.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.killersudoku.R;

/**
 * A button with an oval background which flash animates when clicked
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
    }

    @Override
    protected void onMeasure (int widthMeasureSpec,
                              int heightMeasureSpec){
        setMeasuredDimension(Math.max(widthMeasureSpec, 100), Math.max(widthMeasureSpec, 100));
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
            assert mainListener != null;
            mainListener.onClick(v);
        };
        super.setOnClickListener(includeAnimation);
    }
}