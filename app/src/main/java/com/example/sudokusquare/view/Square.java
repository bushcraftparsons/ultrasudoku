package com.example.sudokusquare.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.killersudoku.R;

import java.util.Locale;

/**
 * TODO: document your custom view class.
 */
public class Square extends View {
    private Integer mainNumber; //TODO remove default
    private Integer answer = 7; //TODO remove default
    private boolean selected = false;
    private boolean show1 = false;
    private boolean show2 = false;
    private boolean show3 = false;
    private boolean show4 = false;
    private boolean show5 = false;
    private boolean show6 = false;
    private boolean show7 = false;
    private boolean show8 = false;
    private boolean show9 = false;

    private int mainNumberErrorColor = Color.RED; //TODO: use theme
    private int mainNumberColor = Color.BLUE; // TODO: use theme
    private int editNumberColor = Color.WHITE; //TODO: use theme
    private int squareBackgroundColor = Color.BLACK; //TODO use theme
    private int squareBorderColor = Color.BLUE;// TODO use theme
    private int selectedSquareBorderColor = Color.RED;//TODO use theme

    private float mainTextDimension = 150; //Minimum 100 Up at square size 200 to 150
    private float editTextDimension = 50; //Minimum 30 Up at square size 200 to 50
    private float borderWidth = 6;//Maximum 12
    private float squareSize = 200;//Minimum 120 Max 250

    private TextPaint mTextPaint;
    private TextPaint editTextPaint;
    private Paint squarePaint;
    private Paint squareBorderPaint;
    RectF square;
    private float editTextHeight;
    private float mainNumberTextHeight;

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
                R.styleable.Square_mainNumberColor,
                mainNumberColor);
        mainNumberErrorColor = a.getColor(
                R.styleable.Square_mainNumberErrorColor,
                mainNumberErrorColor);
        editNumberColor = a.getColor(
                R.styleable.Square_editNumberColor,
                editNumberColor);
        squareBackgroundColor = a.getColor(
                R.styleable.Square_squareBackgroundColor,
                squareBackgroundColor);
        squareBorderColor = a.getColor(
                R.styleable.Square_squareBorderColor,
                squareBorderColor);
        selectedSquareBorderColor = a.getColor(
                R.styleable.Square_selectedSquareBorderColor,
                selectedSquareBorderColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mainTextDimension = a.getDimension(
                R.styleable.Square_mainTextDimension,
                mainTextDimension);
        editTextDimension = a.getDimension(
                R.styleable.Square_editTextDimension,
                editTextDimension);
        borderWidth = a.getDimension(
                R.styleable.Square_borderWidth,
                borderWidth);
        squareSize = a.getDimension(
                R.styleable.Square_squareSize,
                squareSize);

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
        squareBorderPaint.setStyle(Paint.Style.STROKE);
        squareBorderPaint.setStrokeWidth(borderWidth);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private String getMainNumberText() {
        if(mainNumber == null){
            return "";
        }else{
            show1 = false;
            show2 = false;
            show3 = false;
            show4 = false;
            show5 = false;
            show6 = false;
            show7 = false;
            show8 = false;
            show9 = false;
            return String.format(Locale.ENGLISH, "%d", mainNumber);
        }
    }

    private void invalidateTextPaintAndMeasurements() {
        square = new RectF(0,0,squareSize,squareSize);

        mTextPaint.setTextSize(mainTextDimension);
        if(mainNumber==answer){
            mTextPaint.setColor(mainNumberColor);
        }else{
            mTextPaint.setColor(mainNumberErrorColor);
        }


        editTextPaint.setTextSize(editTextDimension);
        editTextPaint.setColor(editNumberColor);

        squareBorderPaint.setColor(selected?selectedSquareBorderColor:squareBorderColor);

        Rect bounds = new Rect();
        editTextPaint.getTextBounds("8", 0, "8".length(), bounds);
        editTextHeight = bounds.height();

        Rect bounds2 = new Rect();
        mTextPaint.getTextBounds(getMainNumberText(), 0, getMainNumberText().length(), bounds2);
        mainNumberTextHeight = bounds2.height();
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
        squareSize = Math.max(w, 100);
        int amountGreaterThanMin = (int)squareSize - 100;//e.g. 10
        mainTextDimension = (float)((amountGreaterThanMin/80) * 50) + 100;//e.g. 103.8
        if(mainTextDimension<100){
            mainTextDimension = 100;//Minimum 100
        }
        editTextDimension = (float)((amountGreaterThanMin/80) * 20) + 30;//31.5
        if(editTextDimension<30){
            editTextDimension = 30;//Minimum 30
        }
        invalidateTextPaintAndMeasurements();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();// These methods are from the parent View class
        int paddingTop = getPaddingTop();

        //Fill
        canvas.drawRoundRect(square, (float) paddingLeft, (float) paddingTop, squarePaint);
        //Border
        canvas.drawRoundRect(square, (float) paddingLeft, (float) paddingTop, squareBorderPaint);

        // Draw the text.
        float halfSquare = (squareSize)/2;
        float quarterSquare = (squareSize/4);
        float threeQuarterSquare = (squareSize * 3/4);
        canvas.drawText(getMainNumberText(),
                halfSquare,//Position of center of number?!
                halfSquare + (mainNumberTextHeight/2),//Position of bottom of number
                mTextPaint);

        //Draw the edit numbers
        if(show1){
            canvas.drawText(
                    "1",
                    quarterSquare,
                    quarterSquare + (editTextHeight),
                    editTextPaint);
        }

        if(show2){
            canvas.drawText(
                    "2",
                    halfSquare,
                    quarterSquare + (editTextHeight),
                    editTextPaint);
        }

        if(show3){
            canvas.drawText(
                    "3",
                    threeQuarterSquare,
                    quarterSquare + (editTextHeight),
                    editTextPaint);
        }

        if(show4){
            canvas.drawText(
                    "4",
                    quarterSquare,
                    halfSquare + (editTextHeight),
                    editTextPaint);
        }

        if(show5){
            canvas.drawText(
                    "5",
                    halfSquare,
                    halfSquare + (editTextHeight),
                    editTextPaint);
        }

        if(show6){
            canvas.drawText(
                    "6",
                    threeQuarterSquare,
                    halfSquare + (editTextHeight),
                    editTextPaint);
        }

        if(show7){
            canvas.drawText(
                    "7",
                    quarterSquare,
                    threeQuarterSquare + (editTextHeight),
                    editTextPaint);
        }

        if(show8){
            canvas.drawText(
                    "8",
                    halfSquare,
                    threeQuarterSquare + (editTextHeight),
                    editTextPaint);
        }

        if(show9){
            canvas.drawText(
                    "9",
                    threeQuarterSquare,
                    threeQuarterSquare + (editTextHeight),
                    editTextPaint);
        }
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

    /**
     * Set the correct answer for this square
     * @param newAnswer
     */
    public void setAnswer(int newAnswer) {
        answer = newAnswer;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Toggle the selected state of the square
     */
    @Override
    protected void onFocusChanged (boolean gainFocus,
                                   int direction,
                                   Rect previouslyFocusedRect){
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if(gainFocus){
            selected = !selected;
            invalidateTextPaintAndMeasurements();
        }
    }

    public void setEditNumber(int editNumber) {
        if(mainNumber!=null){
            return;
        }
        switch(editNumber){
            case 1: show1 = !show1;
                return;
            case 2: show2 = !show2;
                return;
            case 3: show3 = !show3;
                return;
            case 4: show4 = !show4;
                return;
            case 5: show5 = !show5;
                return;
            case 6: show6 = !show6;
                return;
            case 7: show7 = !show7;
                return;
            case 8: show8 = !show8;
                return;
            case 9: show9 = !show9;
        }
    }
}