package com.game.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.game.killersudoku.Box;
import com.game.killersudoku.Col;
import com.example.killersudoku.R;
import com.game.killersudoku.Row;
import com.game.killersudoku.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

/**
 * Represents one small square of the 81 on a sudoku board.
 * Keeps track of all square methods and styles
 */
public class Square extends View implements Comparable<Square> {
    /**
     * User added main number
     */
    private Integer mainNumber;
    /**
     * Actual answer for the square
     */
    private Integer answer = null;
    /**
     * Square is selected
     */
    private boolean selected = false;
    private boolean selectionBackground = false;
    /**
     * true if shown at the start of the game
     */
    private boolean shown = false;
    /**
     * true if square is calculable from the show solutions
     */
    private boolean calculable = false;
    /**
     * Solutions which are still possible for this square given the game board.
     * Used when calculating which squares to show.
     */
    HashSet<Integer> possibleSolutions = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

    private boolean show1 = false;
    private boolean show2 = false;
    private boolean show3 = false;
    private boolean show4 = false;
    private boolean show5 = false;
    private boolean show6 = false;
    private boolean show7 = false;
    private boolean show8 = false;
    private boolean show9 = false;

    private int mainNumberErrorColor;
    private int mainNumberColor;
    private int mainNumberShownColor;
    private int editNumberColor;
    private int squareBackgroundColor;
    private int showSelectionBackgroundColor;
    private int squareBorderColor;
    private int selectedSquareBorderColor;

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

    private Box box;
    private Row row;
    private Col col;
    /**Index of the square**/
    private int squareIndex;

    public Square(Context context) {
        super(context);
        setClickable(true);
        init(null, 0);
    }

    public Square(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        init(attrs, 0);
    }

    public Square(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setClickable(true);
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
        mainNumberShownColor = a.getColor(
                R.styleable.Square_mainNumberShownColor,
                mainNumberShownColor
        );
        editNumberColor = a.getColor(
                R.styleable.Square_editNumberColor,
                editNumberColor);
        squareBackgroundColor = a.getColor(
                R.styleable.Square_squareBackgroundColor,
                squareBackgroundColor);
        squareBorderColor = a.getColor(
                R.styleable.Square_squareBorderColor,
                squareBorderColor);
        showSelectionBackgroundColor = a.getColor(
                R.styleable.Square_squareBorderColor,
                showSelectionBackgroundColor);
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

    public void showSelectionBackground(){
        selectionBackground=true;
        invalidateTextPaintAndMeasurements();
    }

    public void hideSelectionBackground(){
        selectionBackground = false;
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        square = new RectF(0,0,squareSize,squareSize);

        mTextPaint.setTextSize(mainTextDimension);
        if(shown){
            mTextPaint.setColor(mainNumberShownColor);
        }else if(mainNumber != null && mainNumber.equals(answer)){
            mTextPaint.setColor(mainNumberColor);
        }else{
            mTextPaint.setColor(mainNumberErrorColor);
        }

        squarePaint.setColor(selectionBackground?showSelectionBackgroundColor:squareBackgroundColor);
        squarePaint.setAlpha(selectionBackground?100:255);

        editTextPaint.setTextSize(editTextDimension);
        editTextPaint.setColor(editNumberColor);

        squareBorderPaint.setColor(selected?selectedSquareBorderColor:squareBorderColor);

        Rect bounds = new Rect();
        editTextPaint.getTextBounds("8", 0, "8".length(), bounds);
        editTextHeight = bounds.height();

        Rect bounds2 = new Rect();
        mTextPaint.getTextBounds(getMainNumberText(), 0, getMainNumberText().length(), bounds2);
        mainNumberTextHeight = bounds2.height();
        invalidate ();
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
                halfSquare,//Position of center of number, because it's centred text
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
     * Sets the sudoku square main number
     *
     * @param newMainNumber The main number attribute value to use.
     */
    public void setMainNumber(int newMainNumber) {
        if(shown || mainNumber==answer){
            //Square not editable when it is shown as a starting square, nor
            //after the correct answer has been added
            return;
        }
        if(mainNumber != null && mainNumber == newMainNumber){
            mainNumber = null;
        }else{
            mainNumber = newMainNumber;
        }
        invalidateTextPaintAndMeasurements();
    }

    public void toggleSelected(){
        selected = !selected;
        invalidateTextPaintAndMeasurements();
    }

    public void unselect(){
        if(selected){
            selected = false;
            invalidateTextPaintAndMeasurements();
        }

    }

    /**
     * Set the correct answer for this square
     * @param newAnswer The correct solution for this square
     */
    public void setAnswer(Integer newAnswer) {
        answer = newAnswer;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Get the value of the correct answer
     * @return an Integer or null
     */
    public Integer getAnswer(){
        if(answer != null){
            return answer;
        }else {
            return null;
        }
    }

    /**
     * Set the row, col and box for the square and add the square to the row, col and box
     * @param row The row containing this square
     * @param col The column containing this square
     * @param box The box containing this square
     */
    public void setCollections(Row row, Col col, Box box, int squareIndex){
        this.squareIndex = squareIndex;
        this.row = row;
        this.col = col;
        this.box = box;
        //This square not added to collections yet,
        // so collection size should equal the square's index
        //Add square to the row, col and box
        row.addSquare(this);
        col.addSquare(this);
        box.addSquare(this);
    }

    public int getSquareIndex() {
        return squareIndex;
    }

    public Box getBox() {
        return box;
    }

    public Row getRow() {
        return row;
    }

    public Col getCol() {
        return col;
    }

    /**
     * Called when setting up the game
     */
    public void showAnswer(){
        if(answer != null){
            this.possibleSolutions.clear();
            this.possibleSolutions.add(answer);
            //Remove the answer from possible solutions in other squares in
            //the row, col and box
            for(Square sq: this.getCol().getRestOfSet(this)){
                sq.removePossibleSolution(answer);
            }
            for(Square sq: this.getRow().getRestOfSet(this)){
                sq.removePossibleSolution(answer);
            }
            for(Square sq: this.getBox().getRestOfSet(this)){
                sq.removePossibleSolution(answer);
            }
            this.setMainNumber(answer);
            this.shown = true;
            this.calculable = true;
            invalidateTextPaintAndMeasurements();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "SQUARE " + squareIndex + "\nROW " + row.getRowNumber() + " COL " + col.getColNumber() + " BOX " + box.getBoxNumber();
    }

    public void erase() {
        mainNumber=null;
        show1 = false;
        show2 = false;
        show3 = false;
        show4 = false;
        show5 = false;
        show6 = false;
        show7 = false;
        show8 = false;
        show9 = false;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Resets the square ready for a new game
     */
    public void reset() {
        answer = null;
        erase();
        shown = false;
        calculable = false;
        possibleSolutions = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }

    public void setEditNumber(int editNumber) {
        if(shown || mainNumber==answer){
            //Square not editable when it is shown as a starting square, nor
            //after the correct answer has been added
            return;
        }
        if(mainNumber!=null){
            mainNumber=null;
        }
        switch(editNumber){
            case 1: show1 = !show1;
                break;
            case 2: show2 = !show2;
                break;
            case 3: show3 = !show3;
                break;
            case 4: show4 = !show4;
                break;
            case 5: show5 = !show5;
                break;
            case 6: show6 = !show6;
                break;
            case 7: show7 = !show7;
                break;
            case 8: show8 = !show8;
                break;
            case 9: show9 = !show9;
        }
        invalidateTextPaintAndMeasurements();
    }

    public HashSet<Integer> possibleAnswers() {
        //Create the set
        HashSet<Integer> possibleAnswers = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        //Remove the ones already in the row, col or box
        row.removeDuplicates(possibleAnswers);
        col.removeDuplicates(possibleAnswers);
        box.removeDuplicates(possibleAnswers);
        return possibleAnswers;
    }

    public HashSet<Integer> possibleSolutions() {
        return possibleSolutions;
    }

    public Integer getCalculableAnswer(){
        if(shown | isCalculable()){
            return answer;
        }else{
            return null;
        }
    }
    public boolean shown() {
        return shown;
    }

    public void removePossibleSolution(Integer possibleSolution){
        this.possibleSolutions.remove(possibleSolution);
        if(this.possibleSolutions.size()==1){
            calculable = true;
        }
    }

    public boolean hasHiddenSingle(){
        boolean hasHiddenSingle =  (row.isLastSolution(answer)||col.isLastSolution(answer)||box.isLastSolution(answer));
        if(hasHiddenSingle){
            calculable = true;
        }
        return hasHiddenSingle;
    }

    public boolean isCalculable() {
        return calculable || hasHiddenSingle();
    }

    public void setCalculable(boolean calculable) {
        this.calculable = calculable;
    }

    @Override
    public int compareTo(Square square) {
        return squareIndex-square.getSquareIndex();
    }

    public boolean relatedTo(Square selectedSquare) {
        if(selectedSquare!=null ){
            return selectedSquare.row.getRowNumber()==row.getRowNumber() || selectedSquare.col.getColNumber()==col.getColNumber() || selectedSquare.box.getBoxNumber()==box.getBoxNumber();
        }else{
            return false;
        }

    }
}