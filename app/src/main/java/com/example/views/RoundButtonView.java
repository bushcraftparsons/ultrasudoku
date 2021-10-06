package com.example.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.killersudoku.R;

/**
 * TODO: document your custom view class.
 */
public class RoundButtonView extends View {
    private String buttonText = "8";
    private int buttonColor = Color.RED;
    private int textColor = Color.YELLOW;
    private float buttonRadius = 30;

    private float buttonTextDimension = 50;

    private TextPaint buttonPaint;
    private TextPaint textPaint;

    private int buttonTextHeight;

    public RoundButtonView(Context context) {
        super(context);
        init(null, 0);
    }

    public RoundButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RoundButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RoundButtonView, defStyle, 0);

        buttonColor = a.getColor(
                R.styleable.RoundButtonView_buttonColor,
                buttonColor);
        textColor = a.getColor(
                R.styleable.RoundButtonView_textColor,
                textColor);
        buttonText = a.getString(
                R.styleable.RoundButtonView_buttonText);
        if(buttonText==null){
            buttonText = "5";
        }
        buttonRadius = a.getDimension(
                R.styleable.RoundButtonView_buttonRadius,
                buttonRadius
        );

        a.recycle();

        // Set up a default TextPaint object
        buttonPaint = new TextPaint();
        buttonPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        buttonPaint.setTextAlign(Paint.Align.LEFT);

        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign((Paint.Align.CENTER));

    }

    private void invalidateTextPaintAndMeasurements() {
        buttonPaint.setColor(buttonColor);
        textPaint.setColor(textColor);
        textPaint.setTextSize(buttonTextDimension);

        Rect bounds2 = new Rect();
        textPaint.getTextBounds("8", 0, "8".length(), bounds2);
        buttonTextHeight = bounds2.height();
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
        //Min square is 120, jump 200. Difference 80
        //Main number min 100 up to 150. Difference 50
        //Edit number min 30 up to 50. Difference 20
        //Don't go smaller than square size 100
        buttonRadius = Math.max(w/2, 50);
        int amountGreaterThanMin = (int)(buttonRadius*2) - 100;//e.g. 10
        buttonTextDimension = (float)((amountGreaterThanMin/50) * 50) + 100;//e.g. 103.8
        if(buttonTextDimension<100){
            buttonTextDimension = 100;//Minimum 100
        }
        invalidateTextPaintAndMeasurements();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the button.
        canvas.drawCircle(buttonRadius,buttonRadius,buttonRadius,buttonPaint);
        canvas.drawText(buttonText,
                buttonRadius,
                buttonRadius + (buttonTextHeight/2),
                textPaint);
    }

    /**
     * Gets the button number.
     *
     * @return The button number.
     */
    public int getButtonNumber() {
        return Integer.parseInt(buttonText);
    }

    /**
     * Sets the view"s example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setButtonText(String exampleString) {
        buttonText = exampleString;
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getButtonColor() {
        return buttonColor;
    }

    /**
     * Sets the view"s example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setButtonColor(int exampleColor) {
        buttonColor = exampleColor;
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getButtonTextDimension() {
        return buttonTextDimension;
    }

    /**
     * Sets the view"s example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setButtonTextDimension(float exampleDimension) {
        buttonTextDimension = exampleDimension;
    }
}