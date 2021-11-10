package com.game.killersudoku;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.killersudoku.R;
import com.game.views.IconButton;
import com.game.views.NumberButton;
import com.game.views.Square;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.IntStream;

//TODO java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Integer Square.getAnswer()' on a null object reference
//https://developer.android.com/reference/androidx/appcompat/app/AppCompatDelegate#setDefaultNightMode(int)
public class MainActivity extends AppCompatActivity {

    private final Square[] squares = new Square[81];
    private Square selectedSquare;
    private final Row[] rows = new Row[9];
    private final Col[] cols = new Col[9];
    private final Box[] boxes = new Box[9];

    private final NumberButton[] numberButtons = new NumberButton[9];
    private final IconButton[] iconButtons = new IconButton[5];
    private final Context mainActivityContext = this;

    private GameState gs;

    private boolean noteMode = false;

    private final View.OnClickListener squareOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v instanceof Square){
                //Select square
                for(Square sq: squares){
                    if(sq.getSquareIndex()==((Square)v).getSquareIndex()){
                        sq.toggleSelected();
                        selectedSquare = sq;
                    }else{
                        sq.unselect();
                    }
                }
                //Show selection background on row, col and box
                for(Square sq: squares){
                    if(sq.relatedTo(selectedSquare)){
                        sq.showSelectionBackground();
                    }else{
                        sq.hideSelectionBackground();
                    }
                }
            }
        }
    };

    private final View.OnClickListener numberButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v instanceof NumberButton){
                if(selectedSquare!=null){
                    if(noteMode){
                        selectedSquare.setEditNumber(((NumberButton)v).getButtonNumber());
                    }else{
                        selectedSquare.setMainNumber(((NumberButton)v).getButtonNumber());
                    }
                }
            }
        }
    };

    private final View.OnClickListener iconButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v instanceof IconButton){
                //TODO code what happens in button methods
                String action = ((IconButton)v).getAction();
                Resources res = getResources();
                if(action.equals(res.getString(R.string.button_action_delete))){
                    if(selectedSquare!=null){
                        selectedSquare.erase();
                    }
                }
                if(action.equals(res.getString(R.string.button_action_new))){
                    openLevelChooser();
                }
                if(action.equals(res.getString(R.string.button_action_edit))){
                    //Toggle note mode
                    noteMode=!noteMode;
                }
            }
        }
    };

    private final void openLevelChooser(){
        //Open new page for choosing new game level
        Intent intent = new Intent(mainActivityContext, NewGame.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ultra_sudoku_layout);

        setUpInfrastructure();

        // Get any Intent that may have started this activity and extract the string
        Intent intent = getIntent();
        String game_level = intent.getStringExtra(NewGame.GAME_LEVEL);
        if(game_level != null){
            gs = new GameState();
            gs.setLevel(game_level);
            this.createGame();
            populateGameState();
        }else{
            //Check if there is a solution in storage. If there is, then use it. Otherwise, open choose level page.
            gs = Utils.retrieveGameState(getResources(), this.getFilesDir());
            if(gs!=null){
                populateGameState();
            }else{
                openLevelChooser();
            }
        }
    }

    private void populateGameState(){
        //Put solution and reveal shown values in squares
        for(Square sq: squares){
            sq.setAnswer(gs.getSolution()[sq.getSquareIndex()]);
            if(IntStream.of(gs.getShownSquares()).anyMatch(x -> x == sq.getSquareIndex())){
                sq.showAnswer();
            }
        }
    }

    private void createGame(){
        for(Square sq: squares){
            sq.reset();
        }
        //Create new sudoku solution
        this.createSolution();
        GameMaker gm = new GameMaker(squares, rows, cols, boxes);
        gm.makeEasy();

        //Get all the answers and shown values in an int[]
        int[] solution = new int[81];//81 answers expected.
        HashSet<Integer> shown = new HashSet<>();
        for(Square sq: squares){
            solution[sq.getSquareIndex()] = sq.getAnswer();
            if(sq.shown()){
                shown.add(sq.getSquareIndex());
            }
        }
        gs.setSolution(solution);
        gs.setShownSquares(shown.stream().mapToInt(Number::intValue).toArray());
        //Save solution to persistent storage
        Utils.persistGameState(gs, getResources(), this.getFilesDir());
    }

    private void setUpInfrastructure(){
        Resources res = getResources();
        //Populate number buttons array
        int buttonPanelId = res.getIdentifier("buttonPanel","id",getApplicationContext().getPackageName());//buttonPanel
        for(int buttonIndex = 1; buttonIndex<10; buttonIndex++){
            String buttonName = "NumberButton" + (buttonIndex);
            int buttonId = res.getIdentifier(buttonName,"id",getApplicationContext().getPackageName());
            NumberButton foundButton = (NumberButton)findViewById(buttonPanelId).findViewById(buttonId);
            //Set click listener
            foundButton.setOnClickListener(numberButtonOnClickListener);
            numberButtons[buttonIndex-1] = foundButton;
        }
        //Populate icon buttons array
        for(int buttonIndex = 1; buttonIndex<6; buttonIndex++){
            String buttonName = "IconButton" + (buttonIndex);
            int buttonId = res.getIdentifier(buttonName,"id",getApplicationContext().getPackageName());
            IconButton foundButton = (IconButton)findViewById(buttonPanelId).findViewById(buttonId);
            //Set click listener
            foundButton.setOnClickListener(iconButtonOnClickListener);
            iconButtons[buttonIndex-1] = foundButton;
        }
        //Populate squares array
        int countSquares = 0;
        int boardId = res.getIdentifier("board","id",getApplicationContext().getPackageName());
        for(int rowIndex=1; rowIndex<10; rowIndex++){
            int rowId = res.getIdentifier("Row" +  rowIndex,"id",getApplicationContext().getPackageName());
            for(int squareIndex=1; squareIndex<10; squareIndex++){
                int squareId = res.getIdentifier("square" +  squareIndex,"id",getApplicationContext().getPackageName());
                Square foundSquare = (Square)findViewById(boardId).findViewById(rowId).findViewById(squareId);
                //Set click listener
                foundSquare.setOnClickListener(squareOnClickListener);
                squares[countSquares] = foundSquare;
                countSquares++;
            }
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
            square.setCollections(row, col, box, squareIndex);
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
                backtrack(sq, countBacktracks);
                countBacktracks++;
            }
        }
    }

    public boolean processSquare(Square sq){
        //Iterate the squares one row at a time.
        HashSet<Integer> possibles = sq.possibleAnswers();
        Integer possibleAnswer = Utils.getRandomItemFromSet(possibles);
        while(possibleAnswer != null && !tryAnswer(sq, possibleAnswer)){
            //Answer was no good
            //Remove it from possibles
            possibles.remove(possibleAnswer);
            possibleAnswer = Utils.getRandomItemFromSet(possibles);
        }
        return possibleAnswer != null;
    }

    public void backtrack(Square sq, int numberBackTracks){
        int startIndex = sq.getSquareIndex() - numberBackTracks;
        //Back track squares should be exclusive of the original square
        List<Square> backTrackSquares = Arrays.asList(squares).subList(startIndex, sq.getSquareIndex());
        //Set possible answers to null except for the first square
        //Set all backtrack square to null answers
        for(Square square: backTrackSquares){
            square.setAnswer(null);
        }
        //Now process them again
        for(Square square: backTrackSquares){
            processSquare(square);
        }
    }

    public boolean tryAnswer(Square square, Integer possibleAnswer){
        //Try an answer
        square.setAnswer(possibleAnswer);
        //For rest of squares, make sure all still have a possible answer
        if(passRestOfSquareCheck(square)){
            if(passBoxCheck(square)) {
                if(passColCheck(square)) {
                    if(passRowCheck(square)) {
                        if(passPossibleValuesCheck(square)) {
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
     * Helper method for creating a sudoku solution
     * For rest of squares, make sure all still have a possible answer
     * @param square The square to be checked
     * @return Returns true if there is still a possible answer for this square, false if not
     */
    public boolean passRestOfSquareCheck (Square square){
        for(int index = square.getSquareIndex() + 1; index<squares.length; index++){
            Square sq = squares[index];
            if(sq.possibleAnswers().size()==0){
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
        return squaresWithOneAnswer.size() == possibles.size();
    }

    public boolean passPossibleValuesCheck(Square sq){
        //If, looking at answer values and possible value, there are 9 values, then pass
        HashSet<Integer> allValues = new HashSet<>();
        allValues.addAll(sq.getRow().getPossibleAnswers());
        allValues.addAll(sq.getRow().getValues());
        return allValues.size()==9;
    }

}