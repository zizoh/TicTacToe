package com.example.android.tictactoe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.tictactoe.Utils.TicTacToeUtils;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.util.Random;

import static com.example.android.tictactoe.Utils.TicTacToeUtils.PLAYER_O_PLAYED_VALUE;
import static com.example.android.tictactoe.Utils.TicTacToeUtils.PLAYER_X_PLAYED_VALUE;
import static com.example.android.tictactoe.Utils.TicTacToeUtils.SINGLE_PLAYER_EASY_MODE;
import static com.example.android.tictactoe.Utils.TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE;
import static com.example.android.tictactoe.Utils.TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE;
import static com.example.android.tictactoe.Utils.TicTacToeUtils.TWO_PLAYER_MODE;
import static com.example.android.tictactoe.Utils.TicTacToeUtils.disableButton;
import static com.example.android.tictactoe.Utils.TicTacToeUtils.isSinglePlayerMode;
import static com.example.android.tictactoe.Utils.TicTacToeUtils.setTextOnButtonPlayed;

/**
 * TicTacToe coordinates for each square on 5x5 Board
 * -----------------------------
 * (0,0) (0,1) (0,2) (0,3) (0,4)
 * (1,0) (1,1) (1,2) (1,3) (1,4)
 * (2,0) (2,1) (2,2) (2,3) (2,4)
 * (3,0) (3,1) (3,2) (3,3) (3,4)
 * (4,0) (4,1) (4,2) (4,3) (4,4)
 * -----------------------------
 */

@SuppressWarnings("RedundantCast")
public class Board5x5Activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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

    static final int BOARD_SIZE = 5;
    int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    int GAME_MODE = SINGLE_PLAYER_EASY_MODE;
    private boolean PLAYER_X_TURN = true;
    Random randomNumberForBoardIndex = new Random();

    private int numberOfMoves = 0;
    private int playerXScore = 0;
    private int playerOScore = 0;

    int[] oneDimArrayOfBoard = new int[BOARD_SIZE * BOARD_SIZE];

    private Button row0col0;
    private Button row0col1;
    private Button row0col2;
    private Button row0col3;
    private Button row0col4;

    private Button row1col0;
    private Button row1col1;
    private Button row1col2;
    private Button row1col3;
    private Button row1col4;

    private Button row2col0;
    private Button row2col1;
    private Button row2col2;
    private Button row2col3;
    private Button row2col4;

    private Button row3col0;
    private Button row3col1;
    private Button row3col2;
    private Button row3col3;
    private Button row3col4;

    private Button row4col0;
    private Button row4col1;
    private Button row4col2;
    private Button row4col3;
    private Button row4col4;

    private TextView playerXScoreboard;
    private TextView playerOScoreboard;
    private TextView playerToMoveTextView;

    private LinearLayout playerXToMoveButton;
    private LinearLayout playerOToMoveButton;

    private boolean userIsInteracting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_board_5_5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        playerXScoreboard = (TextView) findViewById(R.id.player_x_scoreboard);
        playerOScoreboard = (TextView) findViewById(R.id.player_o_scoreboard);
        playerToMoveTextView = (TextView) findViewById(R.id.player_to_move_textview);

        playerXToMoveButton = (LinearLayout) findViewById(R.id.player_x_to_move);
        playerXToMoveButton.isSelected();
        playerOToMoveButton = (LinearLayout) findViewById(R.id.player_o_to_move);

        row0col0 = (Button) findViewById(R.id.row0_col0);
        row0col1 = (Button) findViewById(R.id.row0_col1);
        row0col2 = (Button) findViewById(R.id.row0_col2);
        row0col3 = (Button) findViewById(R.id.row0_col3);
        row0col4 = (Button) findViewById(R.id.row0_col4);

        row1col0 = (Button) findViewById(R.id.row1_col0);
        row1col1 = (Button) findViewById(R.id.row1_col1);
        row1col2 = (Button) findViewById(R.id.row1_col2);
        row1col3 = (Button) findViewById(R.id.row1_col3);
        row1col4 = (Button) findViewById(R.id.row1_col4);

        row2col0 = (Button) findViewById(R.id.row2_col0);
        row2col1 = (Button) findViewById(R.id.row2_col1);
        row2col2 = (Button) findViewById(R.id.row2_col2);
        row2col3 = (Button) findViewById(R.id.row2_col3);
        row2col4 = (Button) findViewById(R.id.row2_col4);

        row3col0 = (Button) findViewById(R.id.row3_col0);
        row3col1 = (Button) findViewById(R.id.row3_col1);
        row3col2 = (Button) findViewById(R.id.row3_col2);
        row3col3 = (Button) findViewById(R.id.row3_col3);
        row3col4 = (Button) findViewById(R.id.row3_col4);

        row4col0 = (Button) findViewById(R.id.row4_col0);
        row4col1 = (Button) findViewById(R.id.row4_col1);
        row4col2 = (Button) findViewById(R.id.row4_col2);
        row4col3 = (Button) findViewById(R.id.row4_col3);
        row4col4 = (Button) findViewById(R.id.row4_col4);

        Button resetButton = (Button) findViewById(R.id.reset);

        row0col0.setOnClickListener(this);
        row0col1.setOnClickListener(this);
        row0col2.setOnClickListener(this);
        row0col3.setOnClickListener(this);
        row0col4.setOnClickListener(this);

        row1col0.setOnClickListener(this);
        row1col1.setOnClickListener(this);
        row1col2.setOnClickListener(this);
        row1col3.setOnClickListener(this);
        row1col4.setOnClickListener(this);

        row2col0.setOnClickListener(this);
        row2col1.setOnClickListener(this);
        row2col2.setOnClickListener(this);
        row2col3.setOnClickListener(this);
        row2col4.setOnClickListener(this);

        row3col0.setOnClickListener(this);
        row3col1.setOnClickListener(this);
        row3col2.setOnClickListener(this);
        row3col3.setOnClickListener(this);
        row3col4.setOnClickListener(this);

        row4col0.setOnClickListener(this);
        row4col1.setOnClickListener(this);
        row4col2.setOnClickListener(this);
        row4col3.setOnClickListener(this);
        row4col4.setOnClickListener(this);

        playerXToMoveButton.setOnClickListener(playerXToMoveButtonListener);
        playerOToMoveButton.setOnClickListener(playerOToMoveButtonListener);
        resetButton.setOnClickListener(resetButtonListener);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            // Put back values stored in 1D oneDimArrayOfBoard into 2D board
            oneDimArrayOfBoard = savedInstanceState.getIntArray(STATE_BOARD);
            if (null != oneDimArrayOfBoard) {
                board = TicTacToeUtils.convertBoardToTwoDim(BOARD_SIZE, oneDimArrayOfBoard);
            }

            PLAYER_X_TURN = savedInstanceState.getBoolean(STATE_PLAYER_X_TURN);
            numberOfMoves = savedInstanceState.getInt(STATE_NUMBER_OF_MOVES);
            playerXScore = savedInstanceState.getInt(STATE_PLAYER_X_SCORE);
            playerOScore = savedInstanceState.getInt(STATE_PLAYER_O_SCORE);
            GAME_MODE = savedInstanceState.getInt(STATE_GAME_MODE);
            playerXScoreboard.setText(savedInstanceState.getString(PLAYER_X_SCOREBOARD_KEY));
            playerOScoreboard.setText(savedInstanceState.getString(PLAYER_O_SCOREBOARD_KEY));
            playerToMoveTextView.setText(savedInstanceState.getString(PLAYER_TO_MOVE_TEXTVIEW_KEY));
        }

        Spinner spinnerBoard5x5 = (Spinner) findViewById(R.id.board_size_spinner);
        spinnerBoard5x5.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array defined and spinner_item.xml
        ArrayAdapter<CharSequence> adapterBoardSpinner = ArrayAdapter.createFromResource(this,
                R.array.board_size_5x5_array, R.layout.spinner_item);
        // Layout to use when the list of choices appears
        adapterBoardSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBoard5x5.setAdapter(adapterBoardSpinner);

        Spinner spinnerGameMode = (Spinner) findViewById(R.id.spinner);

        spinnerGameMode.setOnItemSelectedListener(gameModeOnItemSelectedListener);
        // Create an ArrayAdapter using the string array defined and spinner_item.xml
        ArrayAdapter<CharSequence> adapterGameMode = ArrayAdapter.createFromResource(this,
                R.array.level_or_player_type_array, R.layout.spinner_item);
        // Layout to use when the list of choices appears
        adapterGameMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerGameMode.setAdapter(adapterGameMode);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (null != board){
          oneDimArrayOfBoard = TicTacToeUtils.convertBoardToOneDim(BOARD_SIZE, board);
        }

        outState.putIntArray(STATE_BOARD, oneDimArrayOfBoard);
        outState.putCharSequence(PLAYER_X_SCOREBOARD_KEY, playerXScoreboard.getText());
        outState.putCharSequence(PLAYER_O_SCOREBOARD_KEY, playerOScoreboard.getText());
        outState.putCharSequence(PLAYER_TO_MOVE_TEXTVIEW_KEY, playerToMoveTextView.getText());
        outState.putInt(STATE_GAME_MODE, GAME_MODE);
        outState.putInt(STATE_NUMBER_OF_MOVES, numberOfMoves);
        outState.putBoolean(STATE_PLAYER_X_TURN, PLAYER_X_TURN);
        outState.putInt(STATE_PLAYER_X_SCORE, playerXScore);
        outState.putInt(STATE_PLAYER_O_SCORE, playerOScore);

        // Call to superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        enablePlayerToMoveButtons(false);
        switch (v.getId()) {
            case R.id.row0_col0:
                setMoveByPlayerAt(0, 0);
                break;
            case R.id.row0_col1:
                setMoveByPlayerAt(0, 1);
                break;
            case R.id.row0_col2:
                setMoveByPlayerAt(0, 2);
                break;
            case R.id.row0_col3:
                setMoveByPlayerAt(0, 3);
                break;
            case R.id.row0_col4:
                setMoveByPlayerAt(0, 4);
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
            case R.id.row1_col3:
                setMoveByPlayerAt(1, 3);
                break;
            case R.id.row1_col4:
                setMoveByPlayerAt(1, 4);
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
            case R.id.row2_col3:
                setMoveByPlayerAt(2, 3);
                break;
            case R.id.row2_col4:
                setMoveByPlayerAt(2, 4);
                break;
            case R.id.row3_col0:
                setMoveByPlayerAt(3, 0);
                break;
            case R.id.row3_col1:
                setMoveByPlayerAt(3, 1);
                break;
            case R.id.row3_col2:
                setMoveByPlayerAt(3, 2);
                break;
            case R.id.row3_col3:
                setMoveByPlayerAt(3, 3);
                break;
            case R.id.row3_col4:
                setMoveByPlayerAt(3, 4);
                break;
            case R.id.row4_col0:
                setMoveByPlayerAt(4, 0);
                break;
            case R.id.row4_col1:
                setMoveByPlayerAt(4, 1);
                break;
            case R.id.row4_col2:
                setMoveByPlayerAt(4, 2);
                break;
            case R.id.row4_col3:
                setMoveByPlayerAt(4, 3);
                break;
            case R.id.row4_col4:
                setMoveByPlayerAt(4, 4);
                break;
        }
        if (isThereAWinner()) {
            setWinner();
        } else {
            indicatePlayerWithTurn(PLAYER_X_TURN);
            switchTurn();
            numberOfMoves++;
            if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
                gameDraw();
            } else {
                gameOn();
            }
        }
    }

    private void switchTurn() {
        PLAYER_X_TURN = !PLAYER_X_TURN;
    }

    public void gameOn() {
        if (isSinglePlayerMode(GAME_MODE)) {
            if (PLAYER_X_TURN) {
                computerPlay(PLAYER_X_PLAYED_VALUE);
            } else {
                computerPlay(PLAYER_O_PLAYED_VALUE);
            }
        }
    }

    private void indicatePlayerWithTurn(Boolean playerWithTurn) {
        if (playerWithTurn) {
            playerToMoveTextView.setText(R.string.o_move);
        } else {
            playerToMoveTextView.setText(R.string.x_move);
        }
    }

    /* Get the board value for position (i,j) */
    public int getBoardValue(int i, int j) {
        if (i < 0 || i >= BOARD_SIZE) {
            return TicTacToeUtils.NON_PLAYED_VALUE;
        }
        if (j < 0 || j >= BOARD_SIZE) {
            return TicTacToeUtils.NON_PLAYED_VALUE;
        }
        return board[i][j];
    }

    private boolean isThereAWinner() {
        int token;
        if (PLAYER_X_TURN) {
            token = 1;
        } else {
            token = 4;
        }
        final int DI[] = {-1, 0, 1, 1};
        final int DJ[] = {1, 1, 1, 0};
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++) {

                // Skip if the token in board[i][j] is not equal to current token
                if (board[i][j] != token) continue;
                for (int k = 0; k < 4; k++) {
                    int count = 0;
                    while (getBoardValue(i + DI[k] * count, j + DJ[k] * count) == token) {
                        count++;
                        if (count == 4) {
                            return true;
                        }
                    }
                }
            }
        return false;
    }

    private void setWinner() {
        enableAllBoxes(false);
        playerToMoveTextView.setText(R.string.game_over);
        if (PLAYER_X_TURN) {
            playerXScore++;
            playerXScoreboard.setText(String.valueOf(playerXScore));
            showWinOrDrawDialog(getString(R.string.player_x_wins));
        } else {
            playerOScore++;
            playerOScoreboard.setText(String.valueOf(playerOScore));
            showWinOrDrawDialog(getString(R.string.player_o_wins));
        }
    }

    private void gameDraw() {
        playerToMoveTextView.setText(R.string.game_draw);
        showWinOrDrawDialog(getString(R.string.game_draw));
    }

    private void showWinOrDrawDialog(String message) {
        DialogFragment newFragment = new WinOrDrawDialog(message);
        newFragment.show(getSupportFragmentManager(), "WinOrDrawDialog");
    }

    private void setMoveByPlayerAt(int row, int column) {
        if (row == 0 && column == 0) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row0col0);
            disableButton(row0col0);
        } else if (row == 0 && column == 1) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row0col1);
            disableButton(row0col1);
        } else if (row == 0 && column == 2) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row0col2);
            disableButton(row0col2);
        } else if (row == 0 && column == 3) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row0col3);
            disableButton(row0col3);
        } else if (row == 0 && column == 4) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row0col4);
            disableButton(row0col4);
        } else if (row == 1 && column == 0) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row1col0);
            disableButton(row1col0);
        } else if (row == 1 && column == 1) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row1col1);
            disableButton(row1col1);
        } else if (row == 1 && column == 2) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row1col2);
            disableButton(row1col2);
        } else if (row == 1 && column == 3) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row1col3);
            disableButton(row1col3);
        } else if (row == 1 && column == 4) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row1col4);
            disableButton(row1col4);
        } else if (row == 2 && column == 0) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row2col0);
            disableButton(row2col0);
        } else if (row == 2 && column == 1) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row2col1);
            disableButton(row2col1);
        } else if (row == 2 && column == 2) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row2col2);
            disableButton(row2col2);
        } else if (row == 2 && column == 3) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row2col3);
            disableButton(row2col3);
        } else if (row == 2 && column == 4) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row2col4);
            disableButton(row2col4);
        } else if (row == 3 && column == 0) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row3col0);
            disableButton(row3col0);
        } else if (row == 3 && column == 1) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row3col1);
            disableButton(row3col1);
        } else if (row == 3 && column == 2) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row3col2);
            disableButton(row3col2);
        } else if (row == 3 && column == 3) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row3col3);
            disableButton(row3col3);
        } else if (row == 3 && column == 4) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row3col4);
            disableButton(row3col4);
        } else if (row == 4 && column == 0) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row4col0);
            disableButton(row4col0);
        } else if (row == 4 && column == 1) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row4col1);
            disableButton(row4col1);
        } else if (row == 4 && column == 2) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row4col2);
            disableButton(row4col2);
        } else if (row == 4 && column == 3) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row4col3);
            disableButton(row4col3);
        } else if (row == 4 && column == 4) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row4col4);
            disableButton(row4col4);
        }
        if (PLAYER_X_TURN) {
            board[row][column] = 1;
        } else {
            board[row][column] = 4;
        }
    }

    private void computerPlay(int playerWithTurnNumber) {
        if (GAME_MODE == SINGLE_PLAYER_EASY_MODE) {
            playRandom();
        } else if (GAME_MODE == SINGLE_PLAYER_MEDIUM_MODE
                || GAME_MODE == SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            playMediumOrImpossibleMode(playerWithTurnNumber);
        }
        if (isThereAWinner()) {
            setWinner();
        } else {
            numberOfMoves++;
            if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
                gameDraw();
            } else {
                indicatePlayerWithTurn(PLAYER_X_TURN);
                switchTurn();
            }
        }
    }

    private void playRandom() {
        int iIndex = 0;
        int jIndex = 1;
        while (!(canPlay(iIndex, jIndex))) {
            // Keep trying until a successful move is played
            iIndex = randomNumberForBoardIndex.nextInt(BOARD_SIZE);
            jIndex = randomNumberForBoardIndex.nextInt(BOARD_SIZE);
        }
    }

    private void playMediumOrImpossibleMode(int playerWithTurnNumber) {
        boolean noWinOrBlockMove = true; // Used so that only one module is executed.
        if (GAME_MODE == SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            noWinOrBlockMove = winOrBlockMove4By4(playerWithTurnNumber); // Checking for 2/3 win situation.
        }
        if ((GAME_MODE == SINGLE_PLAYER_MEDIUM_MODE
                || GAME_MODE == SINGLE_PLAYER_IMPOSSIBLE_MODE)
                && noWinOrBlockMove) {
            if (numberOfMoves == 0) {
                playRandom();
                return;
            } else if (numberOfMoves == 1) {
                if (!(canPlay(2, 2))) {
                    // If the square at the center is already played, play any of the 4 positions indicated below
                    // (  ) (   ) (   ) (   ) (  )
                    // (  ) (1,1) (   ) (1,3) (  )
                    // (  ) (   ) (   ) (   ) (  )
                    // (  ) (3,1) (   ) (3,3) (  )
                    // (  ) (   ) (   ) (   ) (  )
                    int i = 1;
                    int j = 3;
                    int c = randomNumberForBoardIndex.nextBoolean() ? i : j;
                    int d = randomNumberForBoardIndex.nextBoolean() ? i : j;
                    setMoveByPlayerAt(c, d);
                } else {
                    setMoveByPlayerAt(2, 2);
                }
                return;
            } else if (numberOfMoves > 1) {
                // playerWithTurnNumber: 1 for X and 4 for O
                if (PLAYER_X_TURN) {
                    // Checking for situation where loss may occur.
                    noWinOrBlockMove = winOrBlockMove4By4(4);
                } else {
                    noWinOrBlockMove = winOrBlockMove4By4(1);
                }
            }
        }
        if (noWinOrBlockMove) {
            playRandom();
        }
    }

    boolean winOrBlockMove4By4(int playerWithTurnNumber) {
        boolean noWinOrBlock3By4Move = true; // Used so that only one module is executed.
        if (GAME_MODE == SINGLE_PLAYER_MEDIUM_MODE
                || GAME_MODE == SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            noWinOrBlock3By4Move = winOrBlock3By4Move(playerWithTurnNumber);
            if ((GAME_MODE == SINGLE_PLAYER_IMPOSSIBLE_MODE) && noWinOrBlock3By4Move) {
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 1; j < BOARD_SIZE - 1; j++) {
                        // Checking corresponding mid row for 2/3 situation from top to bottom.
                        // (  ) (0,1) (0,2) (0,3)  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        if (board[i][1] + board[i][2] + board[i][3] == playerWithTurnNumber * 2) {
                            if (canPlay(i, j)) {   // Play the move.
                                return false;
                            }
                        }
                        // Checking corresponding mid column for 2/3 situation from left to right.
                        // (   ) (  ) (  ) (  )  (  )
                        // (1,0) (  ) (  ) (  )  (  )
                        // (2,0) (  ) (  ) (  )  (  )
                        // (3,0) (  ) (  ) (  )  (  )
                        // (   ) (  ) (  ) (  )  (  )
                        else if (board[1][i] + board[2][i] + board[3][i] == playerWithTurnNumber * 2) {
                            if (canPlay(j, i)) {
                                return false;
                            }
                        }
                    }
                }
                // Checking left-to-right mid diagonal for 2/3 situation.
                // (  ) (   ) (   ) (  )  (  )
                // (  ) (1,1) (   ) (  ) (  )
                // (  ) (   ) (2,2) (  ) (  )
                // (  ) (   ) (   ) (3,3) (  )
                // (  ) (   ) (   ) (  )  (  )
                if (board[1][1] + board[2][2] + board[3][3] == playerWithTurnNumber * 2) {
                    for (int i = 1; i < BOARD_SIZE - 1; i++) {
                        if (canPlay(i, i)) {
                            return false;
                        }
                    }
                }
                // Checking right-to-left mid diagonal for 2/3 situation.
                // (  ) (   ) (   ) (   )  (  )
                // (  ) (   ) (   ) (1,3) (  )
                // (  ) (   ) (2,2) (   ) (  )
                // (  ) (3,1) (   ) (   ) (  )
                // (  ) (   ) (   ) (  )  (  )
                else if (board[1][3] + board[2][2] + board[3][1] == playerWithTurnNumber * 2) {
                    for (int i = 1, j = 3; i < BOARD_SIZE - 1; i++, j--) {
                        if (canPlay(i, j)) {
                            return false;
                        }
                    }
                }
            }
        }
        if (noWinOrBlock3By4Move) {
            return true;
        }
        return false;
    }

    boolean winOrBlock3By4Move(int playerWithTurnNumber) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Checking left end of row for 3/4 situation from top to bottom
                // (0,0) (0,1) (0,2) (0,3) (   )
                // (   ) (   ) (   ) (   ) (   )
                // (   ) (   ) (   ) (   ) (   )
                // (   ) (   ) (   ) (   ) (   )
                // (   ) (   ) (   ) (   ) (   )
                if (board[i][0] + board[i][1] + board[i][2] + board[i][3] == playerWithTurnNumber * 3) {
                    if (canPlay(i, j)) {   // Play the move.
                        return false;
                    }
                }

                // Checking top end of column for 3/4 situation from left to right
                // (0,0) (  ) (  ) (  ) (  )
                // (1,0) (  ) (  ) (  ) (  )
                // (2,0) (  ) (  ) (  ) (  )
                // (3,0) (  ) (  ) (  ) (  )
                // (   ) (  ) (  ) (  ) (  )
                else if (board[0][j] + board[1][j] + board[2][j] + board[3][j] == playerWithTurnNumber * 3) {
                    if (canPlay(i, j)) {
                        return false;
                    }
                }
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Checking right end of row for 3/4 situation from top to bottom
                // (  ) (0,1) (0,2) (0,3) (0,4)
                // (  ) (   ) (   ) (   ) (   )
                // (  ) (   ) (   ) (   ) (   )
                // (  ) (   ) (   ) (   ) (   )
                // (  ) (   ) (   ) (   ) (   )
                if (board[i][1] + board[i][2] + board[i][3] + board[i][4] == playerWithTurnNumber * 3) {
                    if (canPlay(i, j + 1)) {   // Play the move.
                        return false;
                    }
                }

                // Checking bottom end of column for 3/4 situation from left to right
                // (   ) (  ) (  ) (  ) (  )
                // (1,0) (  ) (  ) (  ) (  )
                // (2,0) (  ) (  ) (  ) (  )
                // (3,0) (  ) (  ) (  ) (  )
                // (4,0) (  ) (  ) (  ) (  )
                if (board[1][j] + board[2][j] + board[3][j] + board[4][j] == playerWithTurnNumber * 3) {
                    if (canPlay(i + 1, j)) {
                        return false;
                    }
                }
            }
        }

        // Checking first left-to-right diagonal for 3/4 situation
        // (  ) (0,1) (   ) (   ) (   )
        // (  ) (   ) (1,2) (   ) (   )
        // (  ) (   ) (   ) (2,3) (   )
        // (  ) (   ) (   ) (   ) (3,4)
        // (  ) (   ) (   ) (   ) (   )
        if (board[0][1] + board[1][2] + board[2][3] + board[3][4] == playerWithTurnNumber * 3) {
            for (int i = 0, j = 1; i < BOARD_SIZE - 1; i++, j++) {
                if (canPlay(i, j)) {
                    return false;
                }
            }
        }

        // Checking top end of second left-to-right diagonal for 3/4 situation
        // (0,0) (   ) (   ) (   ) (  )
        // (   ) (1,1) (   ) (   ) (  )
        // (   ) (   ) (2,2) (   ) (  )
        // (   ) (   ) (   ) (3,3) (  )
        // (   ) (   ) (   ) (   ) (  )
        if (board[0][0] + board[1][1] + board[2][2] + board[3][3] == playerWithTurnNumber * 3) {
            for (int i = 0; i < BOARD_SIZE - 1; i++) {
                if (canPlay(i, i)) {
                    return false;
                }
            }
        }
        // Checking bottom end of second left-to-right diagonal for 3/4 situation
        // (  ) (   ) (   ) (   ) (   )
        // (  ) (1,1) (   ) (   ) (   )
        // (  ) (   ) (2,2) (   ) (   )
        // (  ) (   ) (   ) (3,3) (   )
        //    ) (   ) (   ) (   ) (4,4)
        if (board[1][1] + board[2][2] + board[3][3] + board[4][4] == playerWithTurnNumber * 3) {
            for (int i = 1; i < BOARD_SIZE; i++) {
                if (canPlay(i, i)) {
                    return false;
                }
            }
        }
        // Checking third left-to-right diagonal for 3/4 situation
        // (   ) (   ) (   ) (   ) (  )
        // (1,0) (   ) (   ) (   ) (  )
        // (   ) (2,1) (   ) (   ) (  )
        // (   ) (   ) (3,2) (   ) (  )
        // (   ) (   ) (   ) (4,3) (  )
        if (board[1][0] + board[2][1] + board[3][2] + board[4][3] == playerWithTurnNumber * 3) {
            for (int i = 1, j = 0; i < BOARD_SIZE; i++, j++) {
                if (canPlay(i, j)) {
                    return false;
                }
            }
        }
        // Checking first right_to_left diagonal for 3/4 situation
        // (   ) (   ) (   ) (0,3) (  )
        // (   ) (   ) (1,2) (   ) (  )
        // (   ) (2,1) (   ) (   ) (  )
        // (3,0) (   ) (   ) (   ) (  )
        // (   ) (   ) (   ) (   ) (  )
        if (board[0][3] + board[1][2] + board[2][1] + board[3][0] == playerWithTurnNumber * 3) {
            for (int i = 0, j = 3; i < BOARD_SIZE - 1; i++, j--) {
                if (canPlay(i, j)) {
                    return false;
                }
            }
        }
        // Checking top end of second right-to-left diagonal for 3/4 situation
        // (  ) (   ) (   ) (   ) (0,4)
        // (  ) (   ) (   ) (1,3) (   )
        // (  ) (   ) (2,2) (   ) (   )
        // (  ) (3,1) (   ) (   ) (   )
        // (  ) (   ) (   ) (   ) (   )
        if (board[0][4] + board[1][3] + board[2][2] + board[3][1] == playerWithTurnNumber * 3) {
            for (int i = 0, j = 4; i < BOARD_SIZE - 1; i++, j--) {
                if (canPlay(i, j)) {
                    return false;
                }
            }
        }
        // Checking bottom end of second right-to-left diagonal for 3/4 situation
        // (   ) (   ) (   ) (   ) (  )
        // (   ) (   ) (   ) (1,3) (  )
        // (   ) (   ) (2,2) (   ) (  )
        // (   ) (3,1) (   ) (   ) (  )
        // (4,0) (   ) (   ) (   ) (  )
        else if (board[1][3] + board[2][2] + board[3][1] + board[4][0] == playerWithTurnNumber * 3) {
            for (int i = 1, j = 3; i < BOARD_SIZE; i++, j--) {
                if (canPlay(i, j)) {
                    return false;
                }
            }
        }
        // Checking third right_to_left diagonal for 3/4 situation
        // (  ) (   ) (   ) (   ) (   )
        // (  ) (   ) (   ) (   ) (1,4)
        // (  ) (   ) (   ) (2,3) (   )
        // (  ) (   ) (3,2) (   ) (   )
        // (  ) (4,1) (   ) (   ) (   )
        if (board[1][4] + board[2][3] + board[3][2] + board[4][1] == playerWithTurnNumber * 3) {
            for (int i = 1, j = 4; i < BOARD_SIZE; i++, j--) {
                if (canPlay(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean canPlay(int row, int column) {
        // If square hasn't been played yet
        if (board[row][column] == 0) {
            setMoveByPlayerAt(row, column);
            return true;
        } else
            return false;
    }

    private void enableAllBoxes(boolean b) {
        row0col0.setEnabled(b);
        row0col1.setEnabled(b);
        row0col2.setEnabled(b);
        row0col3.setEnabled(b);
        row0col4.setEnabled(b);

        row1col0.setEnabled(b);
        row1col1.setEnabled(b);
        row1col2.setEnabled(b);
        row1col3.setEnabled(b);
        row1col4.setEnabled(b);

        row2col0.setEnabled(b);
        row2col1.setEnabled(b);
        row2col2.setEnabled(b);
        row2col3.setEnabled(b);
        row2col4.setEnabled(b);

        row3col0.setEnabled(b);
        row3col1.setEnabled(b);
        row3col2.setEnabled(b);
        row3col3.setEnabled(b);
        row3col4.setEnabled(b);

        row4col0.setEnabled(b);
        row4col1.setEnabled(b);
        row4col2.setEnabled(b);
        row4col3.setEnabled(b);
        row4col4.setEnabled(b);
    }

    private void resetScoreBoard() {
        playerXScore = 0;
        playerOScore = 0;
        playerXScoreboard.setText("-");
        playerOScoreboard.setText("-");
    }

    private View.OnClickListener resetButtonListener = new View.OnClickListener() {
        public void onClick(View reset) {
            initGame(GAME_MODE);
        }
    };

    private void initGame(int gameMode) {
        board = TicTacToeUtils.initBoardWithZeros(BOARD_SIZE);
        PLAYER_X_TURN = true;
        playerXToMoveButton.isSelected();
        playerToMoveTextView.setText(R.string.notice_board);
        enablePlayerToMoveButtons(true);
        GAME_MODE = gameMode;
        resetBoard();
    }

    private void enablePlayerToMoveButtons(boolean enableButtons) {
        playerXToMoveButton.setEnabled(enableButtons);
        playerOToMoveButton.setEnabled(enableButtons);
    }

    private void resetBoard() {
        row0col0.setText("");
        row0col1.setText("");
        row0col2.setText("");
        row0col3.setText("");
        row0col4.setText("");

        row1col0.setText("");
        row1col1.setText("");
        row1col2.setText("");
        row1col3.setText("");
        row1col4.setText("");

        row2col0.setText("");
        row2col1.setText("");
        row2col2.setText("");
        row2col3.setText("");
        row2col4.setText("");

        row3col0.setText("");
        row3col1.setText("");
        row3col2.setText("");
        row3col3.setText("");
        row3col4.setText("");

        row4col0.setText("");
        row4col1.setText("");
        row4col2.setText("");
        row4col3.setText("");
        row4col4.setText("");

        enableAllBoxes(true);
        numberOfMoves = 0;
    }

    private View.OnClickListener playerXToMoveButtonListener = new View.OnClickListener() {
        public void onClick(View player_x_to_move) {
            enablePlayerToMoveButtons(false);
            PLAYER_X_TURN = true;
            playerToMoveTextView.setText(R.string.x_move);
            if (isSinglePlayerMode(GAME_MODE)) {
                computerPlay(TicTacToeUtils.PLAYER_X_PLAYED_VALUE);
            }
        }
    };

    private View.OnClickListener playerOToMoveButtonListener = new View.OnClickListener() {
        public void onClick(View player_o_to_move) {
            enablePlayerToMoveButtons(false);
            PLAYER_X_TURN = false;
            playerToMoveTextView.setText(R.string.o_move);
            if (isSinglePlayerMode(GAME_MODE)) {
                computerPlay(TicTacToeUtils.PLAYER_O_PLAYED_VALUE);
            }
        }
    };

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    /*
    * Method to handle board size spinner selection
    */
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (userIsInteracting) {
            switch (position) {
                case 0:
                    // 5x5 Board is clicked
                    break;
                case 1:
                    // 3x3 Board is clicked
                    Intent myIntent = new Intent(Board5x5Activity.this, MainActivity.class);
                    Board5x5Activity.this.startActivity(myIntent);
                    break;
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /*
    * Listener to handle game mode spinner selection
    */
    private AdapterView.OnItemSelectedListener gameModeOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userIsInteracting) {
                switch (position) {
                    case 0:
                        // Easy is clicked
                        GAME_MODE = SINGLE_PLAYER_EASY_MODE;
                        initGame(GAME_MODE);
                        resetScoreBoard();
                        break;
                    case 1:
                        // Medium is clicked
                        GAME_MODE = SINGLE_PLAYER_MEDIUM_MODE;
                        initGame(GAME_MODE);
                        resetScoreBoard();
                        break;
                    case 2:
                        // Impossible is clicked
                        GAME_MODE = SINGLE_PLAYER_IMPOSSIBLE_MODE;
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

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.how_to_menu:
                // User chose the "How-to" item, show instructions on how to canPlay the game
                WebView webView = new WebView(this);
                webView.loadUrl("file:///android_asset/how_to.html");
                new AlertDialog.Builder(this)
                        .setTitle(R.string.how_to_dialog_title)
                        .setView(webView)
                        .setCancelable(true)
                        .show();
                return true;
            case R.id.license_menu:
                // User chose the "Licenses" item
                // Start the Activity used to display a list of all third party licenses in
                // res/raw/third_party_license_metadata generated by oss licenses gradle plugin.
                startActivity(new Intent(this, OssLicensesMenuActivity.class));
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_license));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}