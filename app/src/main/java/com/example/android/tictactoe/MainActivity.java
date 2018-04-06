package com.example.android.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * TicTacToe coordinates for each square
 * -----------------
 * (0,0) (0,1) (0,2)
 * (1,0) (1,1) (1,2)
 * (2,0) (2,1) (2,2)
 * ------------------
 */

@SuppressWarnings("RedundantCast")
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Keys to identify the data saved
    static final String STATE_BOARD = "BOARD";
    static final String PLAYER_X_SCOREBOARD_KEY = "PLAYER_X_SCOREBOARD";
    static final String PLAYER_O_SCOREBOARD_KEY = "PLAYER_O_SCOREBOARD";
    static final String PLAYER_TO_MOVE_TEXTVIEW_KEY = "PLAYER_TO_MOVE_TEXTVIEW";
    static final String STATE_GAME_MODE = "GAME_MODE";
    static final String STATE_PLAYER_X_TURN = "PLAYER_X_TURN";
    static final String STATE_NUMBER_OF_MOVES = "NUMBER_OF_MOVES";
    static final String STATE_PLAYER_X_SCORE = "PLAYER_X_SCORE";
    static final String STATE_PLAYER_O_SCORE = "PLAYER_O_SCORE";

    static final int BOARD_SIZE = 3;
    int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    private static final int EASY_MODE = 1;
    private static final int MEDIUM_MODE = 2;
    private static final int IMPOSSIBLE_MODE = 3;
    private static final int TWO_PLAYER_MODE = 4;
    int GAME_MODE = EASY_MODE;
    boolean PLAYER_X_TURN = true;
    private static final int PLAYER_X = 1;
    private static final int PLAYER_O = 4;
    Random randomNumberForBoardIndex = new Random();

    private int numberOfMoves = 0;
    private int playerXScore = 0;
    private int playerOScore = 0;

    int[] num = new int[BOARD_SIZE * BOARD_SIZE];
    int count = 0;
    int count1 = 0;

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

    private boolean userIsInteracting;

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

        playerXScoreboard = (TextView) findViewById(R.id.player_x_scoreboard);
        playerOScoreboard = (TextView) findViewById(R.id.player_o_scoreboard);
        playerToMoveTextView = (TextView) findViewById(R.id.player_to_move_textview);

        playerXToMoveButton = (LinearLayout) findViewById(R.id.player_x_to_move);
        playerXToMoveButton.isSelected();
        playerOToMoveButton = (LinearLayout) findViewById(R.id.player_o_to_move);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            num = savedInstanceState.getIntArray(STATE_BOARD);
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (num != null) {
                        board[i][j] = num[count];
                        count++;
                    }
                }
            }

            playerXScoreboard.setText(savedInstanceState.getString(PLAYER_X_SCOREBOARD_KEY));
            playerOScoreboard.setText(savedInstanceState.getString(PLAYER_O_SCOREBOARD_KEY));
            playerToMoveTextView.setText(savedInstanceState.getString(PLAYER_TO_MOVE_TEXTVIEW_KEY));
            GAME_MODE = savedInstanceState.getInt(STATE_GAME_MODE);
            PLAYER_X_TURN = savedInstanceState.getBoolean(STATE_PLAYER_X_TURN);
            numberOfMoves = savedInstanceState.getInt(STATE_NUMBER_OF_MOVES);
            playerXScore = savedInstanceState.getInt(STATE_PLAYER_X_SCORE);
            playerOScore = savedInstanceState.getInt(STATE_PLAYER_O_SCORE);
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner spinner_dropdown_item
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.level_or_player_type_array, android.R.layout.simple_spinner_item);
        // Layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save the current values
        savedInstanceState.putIntArray(STATE_BOARD, num);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (num != null) {
                    num[count1] = board[i][j];
                    count1++;
                }
            }
        }

        savedInstanceState.putCharSequence(PLAYER_X_SCOREBOARD_KEY, playerXScoreboard.getText());
        savedInstanceState.putCharSequence(PLAYER_O_SCOREBOARD_KEY, playerOScoreboard.getText());
        savedInstanceState.putCharSequence(PLAYER_TO_MOVE_TEXTVIEW_KEY, playerToMoveTextView.getText());
        savedInstanceState.putInt(STATE_GAME_MODE, GAME_MODE);
        savedInstanceState.putInt(STATE_NUMBER_OF_MOVES, numberOfMoves);
        savedInstanceState.putBoolean(STATE_PLAYER_X_TURN, PLAYER_X_TURN);
        savedInstanceState.putInt(STATE_PLAYER_X_SCORE, playerXScore);
        savedInstanceState.putInt(STATE_PLAYER_O_SCORE, playerOScore);

        // Call to superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void boardButtons(View view) {
        playerXToMoveButton.setEnabled(false);
        playerOToMoveButton.setEnabled(false);
        switch (view.getId()) {
            case R.id.row0_col0:
                setMoveByPlayerAt(0, 0);
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
        boolean thereIsAWinner = isThereAWinner();
        if (thereIsAWinner) {
            setWinner();
            return;
        }
        if (PLAYER_X_TURN) {
            playerToMoveTextView.setText("O Move");
        } else {
            playerToMoveTextView.setText("X Move");
        }
        PLAYER_X_TURN = !PLAYER_X_TURN; // Switch the turn to next player

        if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
            if ((isThereAWinner())) {
                playerToMoveTextView.setText("Game Over");
            } else {
                playerToMoveTextView.setText("Game Draw");
                Toast.makeText(this, "Game Draw", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (GAME_MODE == EASY_MODE || GAME_MODE == MEDIUM_MODE || GAME_MODE == IMPOSSIBLE_MODE) {
            if (PLAYER_X_TURN) {
                computerPlay(PLAYER_X);
            } else {
                computerPlay(PLAYER_O);
            }
        }
    }

    private boolean isThereAWinner() {
        // Horizontal --- rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] != 0) {
                return true;
            }
        }

        // Vertical --- columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j] == board[1][j] && board[0][j] == board[2][j] && board[0][j] != 0) {
                return true;
            }
        }

        // First diagonal
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != 0) {
            return true;
        }

        // Second diagonal
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != 0) {
            return true;
        }
        return false;
    }

    private void setWinner() {
        enableAllBoxes(false);
        playerToMoveTextView.setText("Game Over");
        if (PLAYER_X_TURN) {
            Toast.makeText(this, "Player X Wins!", Toast.LENGTH_SHORT).show();
            playerXScore++;
            playerXScoreboard.setText(String.valueOf(playerXScore));
        } else {
            Toast.makeText(this, "Player O Wins!", Toast.LENGTH_SHORT).show();
            playerOScore++;
            playerOScoreboard.setText(String.valueOf(playerOScore));
        }
    }

    public void setMoveByPlayerAt(int row, int column) {
        if (row == 0 && column == 0) {
            if (PLAYER_X_TURN) {
                row0col0.setText("X");
            } else {
                row0col0.setText("O");
            }
            row0col0.setEnabled(false);
        }
        if (row == 0 && column == 1) {
            if (PLAYER_X_TURN) {
                row0col1.setText("X");
            } else {
                row0col1.setText("O");
            }
            row0col1.setEnabled(false);
        }
        if (row == 0 && column == 2) {
            if (PLAYER_X_TURN) {
                row0col2.setText("X");
            } else {
                row0col2.setText("O");
            }
            row0col2.setEnabled(false);
        }
        if (row == 1 && column == 0) {
            if (PLAYER_X_TURN) {
                row1col0.setText("X");
            } else {
                row1col0.setText("O");
            }
            row1col0.setEnabled(false);
        }
        if (row == 1 && column == 1) {
            if (PLAYER_X_TURN) {
                row1col1.setText("X");
            } else {
                row1col1.setText("O");
            }
            row1col1.setEnabled(false);
        }
        if (row == 1 && column == 2) {
            if (PLAYER_X_TURN) {
                row1col2.setText("X");
            } else {
                row1col2.setText("O");
            }
            row1col2.setEnabled(false);
        }
        if (row == 2 && column == 0) {
            if (PLAYER_X_TURN) {
                row2col0.setText("X");
            } else {
                row2col0.setText("O");
            }
            row2col0.setEnabled(false);
        }
        if (row == 2 && column == 1) {
            if (PLAYER_X_TURN) {
                row2col1.setText("X");
            } else {
                row2col1.setText("O");
            }
            row2col1.setEnabled(false);
        }
        if (row == 2 && column == 2) {
            if (PLAYER_X_TURN) {
                row2col2.setText("X");
            } else {
                row2col2.setText("O");
            }
            row2col2.setEnabled(false);
        }
        if (PLAYER_X_TURN) {
            board[row][column] = 1;
        } else {
            board[row][column] = 4;
        }
    }

    private void computerPlay(int playerWithTurnNumber) {
        if (GAME_MODE == EASY_MODE) {
            playRandom();
        } else if (GAME_MODE == MEDIUM_MODE || GAME_MODE == IMPOSSIBLE_MODE) {
            playMediumOrImpossibleMode(playerWithTurnNumber);
        }
        numberOfMoves++;
        boolean thereIsAWinner = isThereAWinner();
        if (thereIsAWinner) {
            setWinner();
            return;
        }
        if (numberOfMoves == BOARD_SIZE * BOARD_SIZE && !(isThereAWinner())) {
            playerToMoveTextView.setText("Game Draw");
            Toast.makeText(this, "Game Draw", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!PLAYER_X_TURN) {
            playerToMoveTextView.setText("X Move");
        } else {
            playerToMoveTextView.setText("O Move");
        }
        PLAYER_X_TURN = !PLAYER_X_TURN;
    }

    private void playRandom() {
        int iIndex = 0;
        int jIndex = 1;
        while (!(play(iIndex, jIndex))) {
            // Keep trying until a successful move is played
            iIndex = randomNumberForBoardIndex.nextInt(BOARD_SIZE);
            jIndex = randomNumberForBoardIndex.nextInt(BOARD_SIZE);
        }
    }

    private void playMediumOrImpossibleMode(int playerWithTurnNumber) {
        boolean carry = true; // Is used so that only one module is executed.
        if (GAME_MODE == IMPOSSIBLE_MODE) {
            carry = winOrBlockMove(playerWithTurnNumber); // Checking for 2/3 win situation.
            if (!carry) {
                //isThereAWinner();
                enableAllBoxes(false);
                playerToMoveTextView.setText("Game Over");
                return;
            }
        }
        if ((GAME_MODE == MEDIUM_MODE || GAME_MODE == IMPOSSIBLE_MODE) && carry) {
            if (numberOfMoves == 0) {
                playRandom();
                return;
            } else if (numberOfMoves == 1) {
                if (!(play(1, 1))) {
                    // If the square at the center is already played, play any of the the corner squares
                    int i = 0;
                    int j = 2;
                    int c = randomNumberForBoardIndex.nextBoolean() ? i : j;
                    int d = randomNumberForBoardIndex.nextBoolean() ? i : j;
                    setMoveByPlayerAt(c, d);
                } else {
                    setMoveByPlayerAt(1, 1);
                }
                return;
            } else if (numberOfMoves > 1) {
                // playerWithTurnNumber: 1 for X and 4 for O
                if (PLAYER_X_TURN) {
                    carry = winOrBlockMove(4); // Checking for situation where loss may occur.
                } else {
                    carry = winOrBlockMove(1);
                }
            }
        }
        if (carry) {
            playRandom();
        }
    }

    boolean winOrBlockMove(int playerWithTurnNumber) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                //Checking corresponding row for 2/3 situation.
                if (board[i][0] + board[i][1] + board[i][2] == playerWithTurnNumber * 2) {
                    if (play(i, j)) {   // Play the move.
                        return false;
                    }
                }
                // Checking corresponding column for 2/3 situation.
                else if (board[0][j] + board[1][j] + board[2][j] == playerWithTurnNumber * 2) {
                    if (play(i, j)) {
                        return false;
                    }
                }
            }
        }
        // Checking first diagonal for 2/3.
        if (board[0][0] + board[1][1] + board[2][2] == playerWithTurnNumber * 2) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (play(i, i)) {
                    return false;
                }
            }
        }
        // Checking second diagonal for 2/3.
        else if (board[2][0] + board[1][1] + board[0][2] == playerWithTurnNumber * 2) {
            for (int i = 0, j = 2; i < BOARD_SIZE; i++, j--) {
                if (play(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean play(int row, int column) {
        // If square hasn't been played yet
        if (board[row][column] == 0) {
            setMoveByPlayerAt(row, column);
            return true;
        } else
            return false;
    }

    /*
    * Method to to be used to prevent implementation of spinner until user interacts with it
    */
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    /*
    * Method to handle spinner selection
    */
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (userIsInteracting) {
            switch (position) {
                case 0:
                    // Easy is clicked
                    GAME_MODE = EASY_MODE;
                    initGame(GAME_MODE);
                    resetScoreBoard();
                    break;
                case 1:
                    // Medium is clicked
                    GAME_MODE = MEDIUM_MODE;
                    initGame(GAME_MODE);
                    resetScoreBoard();
                    break;
                case 2:
                    // Impossible is clicked
                    GAME_MODE = IMPOSSIBLE_MODE;
                    initGame(GAME_MODE);
                    resetScoreBoard();
                    break;
                case 3:
                    // Two Players is clicked
                    GAME_MODE = TWO_PLAYER_MODE;
                    initGame(GAME_MODE);
                    resetScoreBoard();
                    break;
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void enableAllBoxes(boolean value) {
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

    private void resetScoreBoard() {
        playerXScore = 0;
        playerOScore = 0;
        playerXScoreboard.setText("-");
        playerOScoreboard.setText("-");
    }

    public void resetButton(View view) {
        initGame(GAME_MODE);
    }

    private void initGame(int gameMode) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                // Fill the board with zeros(0)
                board[row][column] = 0;
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

    private void resetBoard() {
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
        count = 0;
        count1 = 0;
    }

    public void playerXToMoveButton(View view) {
        playerXToMoveButton.setEnabled(false);
        playerOToMoveButton.setEnabled(false);
        PLAYER_X_TURN = true;
        playerToMoveTextView.setText("X Move");
        if (GAME_MODE == EASY_MODE
                || GAME_MODE == MEDIUM_MODE
                || GAME_MODE == IMPOSSIBLE_MODE) {
            computerPlay(PLAYER_X);
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
            computerPlay(PLAYER_O);
        }
    }

    /*private boolean isThereAWinner() {
        // Horizontal --- rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] != 0) {
                return true;
                *//*if (board[i][0] == 1) {
                    *//**//*enableAllBoxes(false);
                    Toast.makeText(this, "Player X Wins!\n" + (i + 1) + " row", Toast.LENGTH_SHORT).show();
                    playerToMoveTextView.setText("Game Over");
                    playerXScore++;
                    playerXScoreboard.setText(String.valueOf(playerXScore));*//**//*
                    return true;
                } else if (board[i][0] == 4) {
                    *//**//*enableAllBoxes(false);
                    Toast.makeText(this, "Player O Wins!\n" + (i + 1) + " row", Toast.LENGTH_SHORT).show();
                    playerToMoveTextView.setText("Game Over");
                    playerOScore++;
                    playerOScoreboard.setText(String.valueOf(playerOScore));*//**//*
                    return true;
                }*//*
                //Toast.makeText(this, "row win", Toast.LENGTH_SHORT).show();
                //return true;
            }
        }

        //Vertical --- columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j] == board[1][j] && board[0][j] == board[2][j] && board[0][j] != 0) {
                return true;
                *//*if (board[0][j] == 1) {
                    *//**//*enableAllBoxes(false);
                    Toast.makeText(this, "Player X Wins!\n" + (j + 1) + " column", Toast.LENGTH_SHORT).show();
                    playerToMoveTextView.setText("Game Over");
                    playerXScore++;
                    playerXScoreboard.setText(String.valueOf(playerXScore));*//**//*
                    return true;
                } else if (board[0][j] == 4) {
                    *//**//*enableAllBoxes(false);
                    Toast.makeText(this, "Player O Wins!\n" + (j + 1) + " column", Toast.LENGTH_SHORT).show();
                    playerToMoveTextView.setText("Game Over");
                    playerOScore++;
                    playerOScoreboard.setText(String.valueOf(playerOScore));*//**//*
                    return true;
                }*//*
            }
        }

        //First diagonal
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != 0) {
            return true;
            *//*if (board[0][0] == 1) {
                *//**//*enableAllBoxes(false);
                Toast.makeText(this, "Player X Wins!\nFirst Diagonal", Toast.LENGTH_SHORT).show();
                playerToMoveTextView.setText("Game Over");
                playerXScore++;
                playerXScoreboard.setText(String.valueOf(playerXScore));*//**//*
                return true;
            } else if (board[0][0] == 4) {
                *//**//*enableAllBoxes(false);
                Toast.makeText(this, "Player O Wins!\nFirst Diagonal", Toast.LENGTH_SHORT).show();
                playerToMoveTextView.setText("Game Over");
                playerOScore++;
                playerOScoreboard.setText(String.valueOf(playerOScore));*//**//*
                return true;
            }*//*
        }

        //Second diagonal
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != 0) {
            return true;
            *//*if (board[0][2] == 1) {
                *//**//*enableAllBoxes(false);
                Toast.makeText(this, "Player X Wins!\nSecond Diagonal", Toast.LENGTH_SHORT).show();
                playerToMoveTextView.setText("Game Over");
                playerXScore++;
                playerXScoreboard.setText(String.valueOf(playerXScore));*//**//*
                return true;
            } else if (board[0][2] == 4) {
                *//**//*enableAllBoxes(false);
                Toast.makeText(this, "Player O Wins!\nSecond Diagonal", Toast.LENGTH_SHORT).show();
                playerToMoveTextView.setText("Game Over");
                playerOScore++;
                playerOScoreboard.setText(String.valueOf(playerOScore));*//**//*
                return true;
            }*//*
        }
        return false;
    }*/
}
