package com.example.android.tictactoe;

/*
*  Board is initialized with -1
*  For PLAYERX, board[][] = 1
*    For PLAYER0, board[][] = 0
* */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

@SuppressWarnings("RedundantCast")
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    boolean PLAYER_X_TURN = true;

    static final int BOARD_SIZE = 3;
    int EASY_MODE = 1;
    int MEDIUM_MODE = 2;
    int IMPOSSIBLE_MODE = 3;
    int TWO_PLAYER_MODE = 4;
    int GAME_MODE = EASY_MODE;
    int iIndex = 0;
    int jIndex = 1;
    int iIndexForSecondToLastMove;
    int jIndexForSecondToLastMove;
    int iIndexForLastMove = 1;
    int jIndexForLastMove = 1;
    Random randomNumberForBoardIndex = new Random();

    private int numberOfMoves = 0;
    private int playerXScore = 0;
    private int playerOScore = 0;

    private Button row0col0;
    private Button row0col1;
    private Button row0col2;

    private Button row1col0;
    private Button row1col1;
    private Button row1col2;

    private Button row2col0;
    private Button row2col1;
    private Button row2col2;

    private TextView playerXScoreboard;
    private TextView playerOScoreboard;
    private TextView playerToMoveTextView;

    private LinearLayout playerXToMoveButton;
    private LinearLayout playerOToMoveButton;

    Button resetButton;

    int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        row0col0 = (Button) findViewById(R.id.row0_col0);
        row0col1 = (Button) findViewById(R.id.row0_col1);
        row0col2 = (Button) findViewById(R.id.row0_col2);

        row1col0 = (Button) findViewById(R.id.row1_col0);
        row1col1 = (Button) findViewById(R.id.row1_col1);
        row1col2 = (Button) findViewById(R.id.row1_col2);

        row2col0 = (Button) findViewById(R.id.row2_col0);
        row2col1 = (Button) findViewById(R.id.row2_col1);
        row2col2 = (Button) findViewById(R.id.row2_col2);

        resetButton = (Button) findViewById(R.id.reset);
        playerXScoreboard = (TextView) findViewById(R.id.player_x_scoreboard);
        playerOScoreboard = (TextView) findViewById(R.id.player_o_scoreboard);
        playerToMoveTextView = (TextView) findViewById(R.id.player_to_move_textview);

        playerXToMoveButton = (LinearLayout) findViewById(R.id.player_x_to_move);
        playerXToMoveButton.isSelected();
        playerOToMoveButton = (LinearLayout) findViewById(R.id.player_o_to_move);

        initGame(GAME_MODE);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.level_or_player_type_array, android.R.layout.simple_spinner_item);
        // Layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void boardButtons(View view) {
        playerXToMoveButton.setEnabled(false);
        playerOToMoveButton.setEnabled(false);
        switch (view.getId()) {
            case R.id.row0_col0:
                setMoveByPlayerAt(0, 0);
                /*if (PLAYER_X_TURN_TURN) {
                    row0col0.setText("X");
                    board[0][0] = 1;
                } else {
                    row0col0.setText("O");
                    board[0][0] = 0;
                }
                row0col0.setEnabled(false);*/
                break;
            case R.id.row0_col1:
                setMoveByPlayerAt(0, 1);
                break;
            case R.id.row0_col2:
                setMoveByPlayerAt(0, 2);
                break;

            case R.id.row1_col0:
                setMoveByPlayerAt(1, 0);
                break;

            case R.id.row1_col1:
                setMoveByPlayerAt(1, 1);
                break;

            case R.id.row1_col2:
                setMoveByPlayerAt(1, 2);
                break;

            case R.id.row2_col0:
                setMoveByPlayerAt(2, 0);
                break;

            case R.id.row2_col1:
                setMoveByPlayerAt(2, 1);
                break;
            case R.id.row2_col2:
                setMoveByPlayerAt(2, 2);
                break;
        }
        numberOfMoves++;
        if (PLAYER_X_TURN) {
            playerToMoveTextView.setText("X Move");
        } else {
            playerToMoveTextView.setText("O Move");
        }
        PLAYER_X_TURN = !PLAYER_X_TURN;

        if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
            if (isThereAWinner()) {
                isThereAWinner();
                return;
            } else {
                playerToMoveTextView.setText("Game Draw");
                Toast.makeText(this, "Game Draw", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (GAME_MODE == EASY_MODE || GAME_MODE == MEDIUM_MODE || GAME_MODE == IMPOSSIBLE_MODE) {
            computerPlay();
        }
    }

    private boolean isThereAWinner() {
        // Horizontal --- rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                if (board[i][0] == 1) {
                    enableAllBoxes(false);
                    playerToMoveTextView.setText("Game Over");
                    Toast.makeText(this, "Player X Wins!\n" + (i + 1) + " row", Toast.LENGTH_SHORT).show();
                    playerXScore++;
                    playerXScoreboard.setText(String.valueOf(playerXScore));
                    break;
                } else if (board[i][0] == 0) {
                    enableAllBoxes(false);
                    playerToMoveTextView.setText("Game Over");
                    Toast.makeText(this, "Player O Wins!\n" + (i + 1) + " row", Toast.LENGTH_SHORT).show();
                    playerOScore++;
                    playerOScoreboard.setText(String.valueOf(playerOScore));
                    break;
                }
                return true;
            }
        }

        //Vertical --- columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j] == board[1][j] && board[0][j] == board[2][j]) {
                if (board[0][j] == 1) {
                    enableAllBoxes(false);
                    playerToMoveTextView.setText("Game Over");
                    Toast.makeText(this, "Player X Wins!\n" + (j + 1) + " column", Toast.LENGTH_SHORT).show();
                    playerXScore++;
                    playerXScoreboard.setText(String.valueOf(playerXScore));
                    break;
                } else if (board[0][j] == 0) {
                    enableAllBoxes(false);
                    playerToMoveTextView.setText("Game Over");
                    Toast.makeText(this, "Player O Wins!\n" + (j + 1) + " column", Toast.LENGTH_SHORT).show();
                    playerOScore++;
                    playerOScoreboard.setText(String.valueOf(playerOScore));
                    break;
                }
                return true;
            }
        }

        //First diagonal
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            if (board[0][0] == 1) {
                enableAllBoxes(false);
                playerToMoveTextView.setText("Game Over");
                Toast.makeText(this, "Player X Wins!\nFirst Diagonal", Toast.LENGTH_SHORT).show();
                playerXScore++;
                playerXScoreboard.setText(String.valueOf(playerXScore));
            } else if (board[0][0] == 0) {
                enableAllBoxes(false);
                playerToMoveTextView.setText("Game Over");
                Toast.makeText(this, "Player O Wins!\nFirst Diagonal", Toast.LENGTH_SHORT).show();
                playerOScore++;
                playerOScoreboard.setText(String.valueOf(playerOScore));
            }
            return true;
        }

        //Second diagonal
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            if (board[0][2] == 1) {
                enableAllBoxes(false);
                Toast.makeText(this, "Player X Wins!\nSecond Diagonal", Toast.LENGTH_SHORT).show();
                playerXScore++;
                playerXScoreboard.setText(String.valueOf(playerXScore));
            } else if (board[0][2] == 0) {
                enableAllBoxes(false);
                Toast.makeText(this, "Player O Wins!\nSecond Diagonal", Toast.LENGTH_SHORT).show();
                playerOScore++;
                playerOScoreboard.setText(String.valueOf(playerOScore));
            }
        }
        return true;
    }

    public void resetButton(View view) {
        initGame(GAME_MODE);
    }

    public void playerXToMoveButton(View view) {
        playerXToMoveButton.setEnabled(false);
        playerOToMoveButton.setEnabled(false);
        PLAYER_X_TURN = true;
        playerToMoveTextView.setText("X Move");
        if (GAME_MODE == EASY_MODE
                || GAME_MODE == MEDIUM_MODE
                || GAME_MODE == IMPOSSIBLE_MODE) {
            computerPlay();
        }
    }

    public void playerOToMoveButton(View view) {
        playerXToMoveButton.setEnabled(false);
        playerOToMoveButton.setEnabled(false);
        PLAYER_X_TURN = false;
        playerToMoveTextView.setText("O Move");
        if (GAME_MODE == EASY_MODE
                || GAME_MODE == MEDIUM_MODE
                || GAME_MODE == IMPOSSIBLE_MODE) {
            computerPlay();
        }
    }

    public void setMoveByPlayerAt(int i, int j) {
        if (i == 0 && j == 0) {
            if (PLAYER_X_TURN) {
                row0col0.setText("X");
                board[i][j] = 1;
            } else {
                row0col0.setText("O");
                board[i][j] = 0;
            }
            row0col0.setEnabled(false);
        }
        if (i == 0 && j == 1) {
            if (PLAYER_X_TURN) {
                row0col1.setText("X");
                board[i][j] = 1;
            } else {
                row0col1.setText("O");
                board[i][j] = 0;
            }
            row0col1.setEnabled(false);
        }
        if (i == 0 && j == 2) {
            if (PLAYER_X_TURN) {
                row0col2.setText("X");
                board[i][j] = 1;
            } else {
                row0col2.setText("O");
                board[i][j] = 0;
            }
            row0col2.setEnabled(false);
        }
        if (i == 1 && j == 0) {
            if (PLAYER_X_TURN) {
                row1col0.setText("X");
                board[i][j] = 1;
            } else {
                row1col0.setText("O");
                board[i][j] = 0;
            }
            row1col0.setEnabled(false);
        }
        if (i == 1 && j == 1) {
            if (PLAYER_X_TURN) {
                row1col1.setText("X");
                board[i][j] = 1;
            } else {
                row1col1.setText("O");
                board[i][j] = 0;
            }
            row1col1.setEnabled(false);
        }
        if (i == 1 && j == 2) {
            if (PLAYER_X_TURN) {
                row1col2.setText("X");
                board[i][j] = 1;
            } else {
                row1col2.setText("O");
                board[i][j] = 0;
            }
            row1col2.setEnabled(false);
        }
        if (i == 2 && j == 0) {
            if (PLAYER_X_TURN) {
                row2col0.setText("X");
                board[i][j] = 1;
            } else {
                row2col0.setText("O");
                board[i][j] = 0;
            }
            row2col0.setEnabled(false);
        }
        if (i == 2 && j == 1) {
            if (PLAYER_X_TURN) {
                row2col1.setText("X");
                board[i][j] = 1;
            } else {
                row2col1.setText("O");
                board[i][j] = 0;
            }
            row2col1.setEnabled(false);
        }
        if (i == 2 && j == 2) {
            if (PLAYER_X_TURN) {
                row2col2.setText("X");
                board[i][j] = 1;
            } else {
                row2col2.setText("O");
                board[i][j] = 0;
            }
            row2col2.setEnabled(false);
        }
    }

    private void resetBoard() {
        Log.d(TAG, "Inside resetBoard");
        row0col0.setText("");
        row0col1.setText("");
        row0col2.setText("");

        row1col0.setText("");
        row1col1.setText("");
        row1col2.setText("");

        row2col0.setText("");
        row2col1.setText("");
        row2col2.setText("");

        enableAllBoxes(true);
        numberOfMoves = 0;

        //setInfo("Start Again!!!");

        //Toast.makeText(this, "Board Reset", Toast.LENGTH_SHORT).show();
    }

    private void enableAllBoxes(boolean value) {
        Log.d(TAG, "Inside enableAllBoxes");
        row0col0.setEnabled(value);
        row0col1.setEnabled(value);
        row0col2.setEnabled(value);

        row1col0.setEnabled(value);
        row1col1.setEnabled(value);
        row1col2.setEnabled(value);

        row2col0.setEnabled(value);
        row2col1.setEnabled(value);
        row2col2.setEnabled(value);
    }

    private void initGame(int gameMode) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = -1;
            }
        }
        PLAYER_X_TURN = true;
        playerXToMoveButton.isSelected();
        playerToMoveTextView.setText("Start game or select player");
        playerXToMoveButton.setEnabled(true);
        playerOToMoveButton.setEnabled(true);
        GAME_MODE = gameMode;
        resetBoard();
    }

    private void computerPlay() {
        if (GAME_MODE == EASY_MODE) {
            playRandom();
        }
        numberOfMoves++;
        isThereAWinner();
        if (numberOfMoves == BOARD_SIZE * BOARD_SIZE && !(isThereAWinner())) {
            playerToMoveTextView.setText("Game Draw");
            Toast.makeText(this, "Game Draw", Toast.LENGTH_SHORT).show();
            return;
        }
        if (PLAYER_X_TURN) {
            playerToMoveTextView.setText("X Move");
        } else {
            playerToMoveTextView.setText("O Move");
        }
        PLAYER_X_TURN = !PLAYER_X_TURN;
        if (isThereAWinner()) {
            //return;
        }
    }

    private void playRandom() {
        while (!(play(iIndex, jIndex))) {
            // Keep trying until a successful move is played
            iIndex = randomNumberForBoardIndex.nextInt(3);
            jIndex = randomNumberForBoardIndex.nextInt(3);
        }

            /*switch (numberOfMoves) {
                case 0:
                    play(0, 0);// Some certain steps are used.
                    break;
                case 1:
                    if (!(play(1, 1)))
                        play(0, 0);
                    break;
                case 2:
                    if (!(play(2, 2)))
                        play(0, 2);
                    break;
                default:
                    while (!(play(iIndex, jIndex))) {
                        // Keep trying until a successful move is made
                        iIndex = randomNumberForBoardIndex.nextInt(3);
                        jIndex = randomNumberForBoardIndex.nextInt(3);
                    }
                    break;
            }*/


        /*
        if (GAME_MODE == EASY_MODE) {
            while ((board[iIndex][jIndex] == -1)) {
                iIndex = randomNumberForBoardIndex.nextInt(3);
                jIndex = randomNumberForBoardIndex.nextInt(3);
            }
            setMoveByPlayerAt(iIndex, jIndex);
        */    /*switch (numberOfMoves) {
                case 0:
                    // Computer to make first move
                    // Play any random square with index generated by computer
                    iIndex = randomNumberForBoardIndex.nextInt(3);
                    jIndex = randomNumberForBoardIndex.nextInt(3);
                    board[iIndex][jIndex] = 0;
                    setMoveByPlayerAt(iIndex, jIndex);
                    break;
                default:
                    while (!(board[iIndex][jIndex] == -1)) {
                        iIndex = randomNumberForBoardIndex.nextInt(3);
                        jIndex = randomNumberForBoardIndex.nextInt(3);
                    }
                    setMoveByPlayerAt(iIndex, jIndex);
                    break;
            }*/
            /*switch(numberOfMoves){
                case 0:
                    // Computer to make first move
                    // Play any random square with index generated by computer
                    int iIndex = randomNumberForBoardIndex.nextInt(3);
                    int jIndex = randomNumberForBoardIndex.nextInt(3);
                    board[iIndex][jIndex] = 0;
                    break;
                case 1:
                    if (board[0][2] == -1) // If square at this index hasn't been played
                        board[0][2] = 0;
                    else if (board[1][0] == -1) // If square at this index hasn't been played
                        board[1][0] = 0;
                    break;
                case 2:
                    if (board[1][1] == -1) // If square at this index hasn't been played
                        board[1][1] = 0;
                    else if (board[2][2] == -1)
                        board[2][2] = 0;
                    break;
            }*/
    }

    /*private void resetScoreBoard() {
        playerXScoreboard.setText("-");
        playerOScoreboard.setText("-");
    }*/

    /*
    * Method to handle spinner selection
    */
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                // Easy is clicked
                GAME_MODE = EASY_MODE;
                initGame(GAME_MODE);
                //Toast.makeText(this, "Now Playing in Easy Mode", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                // Medium is clicked
                GAME_MODE = MEDIUM_MODE;
                initGame(GAME_MODE);
                Toast.makeText(this, "Now Playing in Medium Mode", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                // Impossible is clicked
                GAME_MODE = IMPOSSIBLE_MODE;
                initGame(GAME_MODE);
                Toast.makeText(this, "Now Playing in Impossible Mode", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                // Two Players is clicked
                GAME_MODE = TWO_PLAYER_MODE;
                initGame(GAME_MODE);
                Toast.makeText(this, "Now Playing in Two Players Mode", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    boolean play(int row, int column) {
        // If square hasn't been played yet
        if (board[row][column] == -1) {
            iIndexForSecondToLastMove = iIndexForLastMove;
            jIndexForSecondToLastMove = jIndexForLastMove;
            iIndexForLastMove = row;
            jIndexForLastMove = column;  // Store coordinates for 2nd last and the last move.
            setMoveByPlayerAt(row, column);
            return true;
        } else
            return false;  
    }
}
