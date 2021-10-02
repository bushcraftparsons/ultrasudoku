package com.example.sudokusquare.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.killersudoku.R;

/**
 * TODO: document your custom view class.
 */
public class Square extends View {
    private Integer mainNumber = 5; //TODO remove default
    private boolean show1 = true;
    private boolean show2 = true;
    private boolean show3 = true;
    private boolean show4 = true;
    private boolean show5 = true;
    private boolean show6 = true;
    private boolean show7 = true;
    private boolean show8 = true;
    private boolean show9 = true;

    private int mainNumberColor = Color.RED; // TODO: use theme
    private int editNumberColor = Color.WHITE; //TODO: use theme
    private int squareBackgroundColor = Color.BLACK; //TODO use theme
    private int squareBorderColor = Color.BLUE;// TODO use theme

    private float mainTextDimension = 100; // TODO: use a default from R.dimen...
    private float editTextDimension = 30; //TODO use a default from R.dimen...
    private float borderWidth = 12;

    private TextPaint mTextPaint;
    private TextPaint editTextPaint;
    private Paint squarePaint;
    private Paint squareBorderPaint;
    private float editTextWidth;
    private float editTextHeight;

    public Square(Context context) {
        super(context);
        init(null, 0);
    }

    public Square(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Square(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.Square, defStyle, 0);

        mainNumberColor = a.getColor(
                R.styleable.Square_exampleColor,
                mainNumberColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mainTextDimension = a.getDimension(
                R.styleable.Square_exampleDimension,
                mainTextDimension);

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        editTextPaint = new TextPaint();
        editTextPaint.setFlags((Paint.ANTI_ALIAS_FLAG));
        editTextPaint.setTextAlign(Paint.Align.CENTER);

        squarePaint = new Paint();
        squarePaint.setColor(squareBackgroundColor);
        squarePaint.setStyle(Paint.Style.FILL);

        squareBorderPaint = new Paint();
        squareBorderPaint.setColor(squareBorderColor);
        squareBorderPaint.setStyle(Paint.Style.STROKE);
        squareBorderPaint.setStrokeWidth(borderWidth);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private String getMainNumberText() {
        if(mainNumber == null){
            return "";
        }else{
            return String.format("%d", mainNumber);
        }
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mainTextDimension);
        mTextPaint.setColor(mainNumberColor);

        editTextPaint.setTextSize((editTextDimension));
        editTextPaint.setColor(editNumberColor);
        editTextWidth = editTextPaint.measureText("9");

        Paint.FontMetrics fontMetrics = editTextPaint.getFontMetrics();
        editTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();// These methods are from the parent View class
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;


        //Draw the background
        RectF square = new RectF(0,0,150,150);
        //Fill
        canvas.drawRoundRect(square, (float) 0.5, (float) 0.5, squarePaint);
        //Border
        canvas.drawRoundRect(square, (float) 0.5, (float) 0.5, squareBorderPaint);

        // Draw the text.
        canvas.drawText(getMainNumberText(),
                75,
                116,
                mTextPaint);

        //Draw the edit numbers
        if(show1){
            canvas.drawText(
                    "1",
                    32,//strokeWidth 12 + 20 space
                    43,//strokeWidth 12 + 31 = not sure what 31 is Size of text?
                    editTextPaint);
        }

        if(show2){
            canvas.drawText(
                    "2",
                    70,
                    43,
                    editTextPaint);
        }

        if(show3){
            canvas.drawText(
                    "3",
                    108,
                    43,
                    editTextPaint);
        }

        if(show4){
            canvas.drawText(
                    "4",
                    32,
                    86,
                    editTextPaint);
        }

        if(show5){
            canvas.drawText(
                    "5",
                    70,
                    86,
                    editTextPaint);
        }

        if(show6){
            canvas.drawText(
                    "6",
                    108,
                    86,
                    editTextPaint);
        }

        if(show7){
            canvas.drawText(
                    "7",
                    32,
                    129,
                    editTextPaint);
        }

        if(show8){
            canvas.drawText(
                    "8",
                    70,
                    129,
                    editTextPaint);
        }

        if(show9){
            canvas.drawText(
                    "9",
                    108,
                    129,
                    editTextPaint);
        }

//        // Draw the example drawable on top of the text.
//        if (mExampleDrawable != null) {
//            mExampleDrawable.setBounds(paddingLeft, paddingTop,
//                    paddingLeft + contentWidth, paddingTop + contentHeight);
//            mExampleDrawable.draw(canvas);
//        }
    }

    /**
     * Gets the main number attribute value.
     *
     * @return The main number attribute value. Could be null
     */
    public Integer getMainNumber() {
        return mainNumber;
    }

    /**
     * Sets the sudoku square main number
     *
     * @param newMainNumber The main number attribute value to use.
     */
    public void setMainNumber(int newMainNumber) {
        if(mainNumber == newMainNumber){
            mainNumber = null;
        }else{
            mainNumber = newMainNumber;
        }
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the main number color attribute value.
     *
     * @return The main number color attribute value.
     */
    public int getMainNumberColor() {
        return mainNumberColor;
    }

    /**
     * Sets the view"s main number color attribute value. This is the colour of the main number
     *
     * @param numberColor The main number color attribute value to use.
     */
    public void setMainNumberColor(int numberColor) {
        mainNumberColor = numberColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the main text dimension attribute value.
     *
     * @return The main text dimension attribute value.
     */
    public float getMainTextDimension() {
        return mainTextDimension;
    }

    /**
     * Sets the view"s main text dimension attribute value. This dimension
     * is the main number font size.
     *
     * @param mainDimension The example dimension attribute value to use.
     */
    public void setMainTextDimension(float mainDimension) {
        mainTextDimension = mainDimension;
        invalidateTextPaintAndMeasurements();
    }

    public void setEditNumber(int editNumber) {
        switch(editNumber)
    }
}