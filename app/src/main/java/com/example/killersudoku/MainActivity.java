package com.example.killersudoku;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.views.RoundButtonView;
import com.example.views.Square;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

//TODO java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Integer com.example.views.Square.getAnswer()' on a null object reference
//https://developer.android.com/reference/androidx/appcompat/app/AppCompatDelegate#setDefaultNightMode(int)
public class MainActivity extends AppCompatActivity {

    private Square[] squares = new Square[81];
    private Square selectedSquare;
    private Row[] rows = new Row[9];
    private Col[] cols = new Col[9];
    private Box[] boxes = new Box[9];

    private RoundButtonView[] numberButtons = new RoundButtonView[9];

    private View.OnClickListener squareOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v instanceof Square){
                for(Square sq: squares){
                    if(sq.getId()==v.getId()){
                        sq.toggleSelected();
                        selectedSquare = sq;
                    }else{
                        sq.unselect();
                    }
                }
            }
        }
    };

    private View.OnClickListener numberButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v instanceof RoundButtonView){
                RoundButtonView button = (RoundButtonView) v;
                selectedSquare.setMainNumber(button.getButtonNumber());
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ultra_sudoku_layout);

        Resources res = getResources();
        //Populate number buttons array
        for(int buttonIndex = 0; buttonIndex<9; buttonIndex++){
            String buttonName = "roundButtonView" + (buttonIndex + 2);
            int buttonId = res.getIdentifier(buttonName,"id",getApplicationContext().getPackageName());
            RoundButtonView foundButton = (RoundButtonView)findViewById(buttonId);
            //Set click listener
            foundButton.setOnClickListener(numberButtonOnClickListener);
            numberButtons[buttonIndex] = (RoundButtonView)findViewById(buttonId);
        }
        //Populate squares array
        for(int index=0; index<81; index++){
            String squareName = "square" + (index + 11);
            int squareId = res.getIdentifier(squareName,"id",getApplicationContext().getPackageName());
            Square foundSquare = (Square)findViewById(squareId);
            //Set click listener
            foundSquare.setOnClickListener(squareOnClickListener);
            squares[index] = (Square)findViewById(squareId);
        }

        //Create Rows, Cols and Boxes
        for(int index = 0; index<9; index++){
            rows[index] = new Row(index);
            cols[index] = new Col(index);
            boxes[index] = new Box(index);
        }

        //Add squares to collections and collections to squares
        int squareIndex = 0;
        int rowIndex = 0;
        int colStartIndex = 0;
        int colIndex = 0;
        int boxStartIndex = 0;
        int boxIndex = 0;
        for(int count = 1; count<82; count++){
            //Get square
            Square square = this.squares[squareIndex];
            //Add row, col and box to the square
            Row row = this.rows[rowIndex];
            Col col = this.cols[colIndex];
            Box box = this.boxes[boxIndex];
            square.setCollections(row, col, box, squareIndex, boxes, rows, cols);
            //Every square
            //col index increments
            colIndex++;
            //square index increments
            squareIndex++;

            //Every 3 squares
            if(count%3 == 0){
                //Box index increments
                boxIndex++;
            }

            //Every 9 squares
            if(count%9 == 0){
                //col index goes back to start
                colIndex = colStartIndex;
                //row index increments
                rowIndex++;
                //Box index goes back to the start
                boxIndex = boxStartIndex;
            }

            //Every 27 squares
            if(count%27==0){
                //Box start index increments by 3
                boxStartIndex = boxStartIndex + 3;
                boxIndex = boxStartIndex;
            }
        }

        //Create new sudoku game
        this.createSolution();

        //TODO remove after debug
        showAnswers();
    }

    public void showAnswers(){
        for(Square square: squares){
            square.showAnswer();
        }
    }

    /**
     * Iterate the squares one row at a time
     * Try an answer
     * For rest of squares, make sure all still have a possible answer
     * If answer works, use it, otherwise remove from possibles and try again.
     */
    public void createSolution (){
        for(Square sq: squares){
            int countBacktracks = 1;
            while(!processSquare(sq)){//If successful, then carrying on
                //There was no value for this square, need to backtrack
                sq.setAnswer(null);
                System.out.println("No values left for square, backtracking " + countBacktracks);
                backtrack(sq, countBacktracks);
                countBacktracks++;
            }
        }
    }

    public boolean processSquare(Square sq){
        //Iterate the squares one row at a time.
        HashSet<Integer> possibles = sq.possibleAnswers();
        Integer possibleAnswer = Utils.getRandomItemFromSet(possibles);
        System.out.println(sq.toString() + " trying answer " + possibleAnswer);
        while(possibleAnswer != null && !tryAnswer(sq, possibleAnswer, possibles)){
            //Answer was no good
            //Remove it from possibles
            possibles.remove(possibleAnswer);
            possibleAnswer = Utils.getRandomItemFromSet(possibles);
            System.out.println(sq.toString() + " trying answer " + possibleAnswer);
        }
        if(possibleAnswer==null){
            return false;
        }
        return true;
    }

    public void backtrack(Square sq, int numberBackTracks){
        int startIndex = sq.getSquareIndex() - numberBackTracks;
        //Back track squares should be exclusive of the original square
        List<Square> backTrackSquares = Arrays.asList(squares).subList(startIndex, sq.getSquareIndex());
        //Set possible answers to null except for the first square
        //Set all backtrack square to null answers
        int count = 0;
        for(Square square: backTrackSquares){
            square.setAnswer(null);
            count++;
        }
        //Now process them again
        for(Square square: backTrackSquares){
            processSquare(square);
        }
    }

    public boolean tryAnswer(Square square, Integer possibleAnswer, HashSet<Integer> possibles){
        //Try an answer
        square.setAnswer(possibleAnswer);
        //For rest of squares, make sure all still have a possible answer
        if(passRestOfSquareCheck(square)){
            if(passBoxCheck(square)) {
                if(passColCheck(square)) {
                    if(passRowCheck(square)) {
                        if(passPossibleValuesCheck(square)) {
                            System.out.println("ANSWER " + possibleAnswer);
                            //Answer is fine, continue
                            return true;
                        }
                    }
                }
            }
        }
        //Answer was no good.
        square.setAnswer(null);
        return false;
    }

    /**
     * For rest of squares, make sure all still have a possible answer
     * @param square
     * @return
     */
    public boolean passRestOfSquareCheck (Square square){
        for(int index = square.getSquareIndex() + 1; index<squares.length; index++){
            Square sq = squares[index];
            if(sq.possibleAnswers().size()==0){
                System.out.println("Using this answer would leave no possible answers for square " + sq.toString());
                return false;
            }
        }
        return true;
    }

    public boolean passBoxCheck (Square square){
        int rowNumber = square.getRow().getRowNumber();
        int boxNumber = square.getBox().getBoxNumber();
        int endBox = 2;
        if(boxNumber>2){
            endBox = 5;
        }
        if(boxNumber>5){
            endBox = 8;
        }
        boxNumber = boxNumber + 1;
        while(boxNumber<=endBox){
            int numberPossibleAnswersForRow = boxes[boxNumber].getPossibleAnswersForRow(rowNumber).size();
            if(numberPossibleAnswersForRow<boxes[boxNumber].getSquaresInRowWithNoAnswer(rowNumber).size()){
                System.out.println("Not enough answers left for box " + boxNumber + " row " + rowNumber);
                return false;
            }
            boxNumber++;
        }
        return true;
    }

    public boolean passColCheck(Square square){
        //Iterate rest of columns
        for(int colIndex = square.getCol().getColNumber() + 1; colIndex<cols.length; colIndex++){
            Col checkCol = cols[colIndex];
            TreeSet<Square> squaresWithNoAnswer = checkCol.getSquaresWithNoAnswer();
            HashSet<Integer> possibles = new HashSet<>();
            for(Square sq: squaresWithNoAnswer){
                possibles.addAll(sq.possibleAnswers());
            }
            //Same number of possible answers as there are squares
            if(possibles.size()==squaresWithNoAnswer.size()){
                for(int possAnswer: possibles){
                    if(!square.getRow().containsAnswer(possAnswer)){
                        return true;//At least one answer not already contained in row
                    }
                }
                System.out.println("The same row can't contain all the answers required in column " + colIndex);
                return false;
            }
        }
        return true;
    }

    public boolean passRowCheck(Square sq){
        //If there are two squares in the row with only one possible answer which match, then fail
        TreeSet<Square> squaresWithOneAnswer = sq.getRow().getSquaresWithOneAnswer();
        HashSet<Integer> possibles = new HashSet<>();
        for(Square square: squaresWithOneAnswer){
            possibles.addAll(square.possibleAnswers());
        }
        boolean pass = squaresWithOneAnswer.size() == possibles.size();
        if(!pass){
            System.out.println("There are two squares in the row with only one matching possible answer");
        }
        return pass;
    }

    public boolean passPossibleValuesCheck(Square sq){
        //If, looking at answer values and possible value, there are 9 values, then pass
        HashSet<Integer> allValues = new HashSet();
        allValues.addAll(sq.getRow().getPossibleAnswers());
        allValues.addAll(sq.getRow().getValues());
        boolean pass = allValues.size()==9;
        if(!pass){
            System.out.println("There are not enough values left for the row. values " + Utils.printHashSet(allValues));
        }
        return pass;
    }

}