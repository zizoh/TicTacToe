package com.example.android.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

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
    private static final int EASY_MODE = 1;
    private static final int MEDIUM_MODE = 2;
    private static final int IMPOSSIBLE_MODE = 3;
    private static final int TWO_PLAYER_MODE = 4;
    int GAME_MODE = EASY_MODE;
    public boolean PLAYER_X_TURN = true;
    private static final int PLAYER_X = 1;
    private static final int PLAYER_O = 4;
    public static final int EMPTY = 0;
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
            int count = 0;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (oneDimArrayOfBoard != null) {
                        board[i][j] = oneDimArrayOfBoard[count];
                        count++;
                    }
                }
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

        // Save the current values
        // Put the values in each square of board to 1D oneDimArrayOfBoard array
        // Since 2D board array can't be put in outState
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (oneDimArrayOfBoard != null) {
                    oneDimArrayOfBoard[count] = board[i][j];
                    count++;
                }
            }
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
        playerXToMoveButton.setEnabled(false);
        playerOToMoveButton.setEnabled(false);
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
        numberOfMoves++;
        boolean thereIsAWinner = isThereAWinner();
        if (thereIsAWinner) {
            setWinner();
            return;
        }
        if (PLAYER_X_TURN) {
            playerToMoveTextView.setText(R.string.o_move);
        } else {
            playerToMoveTextView.setText(R.string.x_move);
        }
        PLAYER_X_TURN = !PLAYER_X_TURN; // Switch the turn to next player

        if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
            if ((isThereAWinner())) {
                playerToMoveTextView.setText(R.string.game_over);
            } else {
                playerToMoveTextView.setText(R.string.game_draw);
                DialogFragment newFragment = new gameDrawDialogFragment();
                newFragment.show(getSupportFragmentManager(), "gameDraw");
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

    /* get the board value for position (i,j) */
    public int getBoardValue(int i, int j) {
        if (i < 0 || i >= BOARD_SIZE) {
            return EMPTY;
        }
        if (j < 0 || j >= BOARD_SIZE) {
            return EMPTY;
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
            DialogFragment newFragment = new playerXWinsDialogFragment();
            newFragment.show(getSupportFragmentManager(), "playerXWins");
        } else {
            playerOScore++;
            playerOScoreboard.setText(String.valueOf(playerOScore));
            DialogFragment newFragment = new playerOWinsDialogFragment();
            newFragment.show(getSupportFragmentManager(), "playerOWins");
        }
    }

    public void setMoveByPlayerAt(int row, int column) {
        if (row == 0 && column == 0) {
            if (PLAYER_X_TURN) {
                row0col0.setText(R.string.string_x);
            } else {
                row0col0.setText(R.string.string_o);
            }
            row0col0.setEnabled(false);
        }
        if (row == 0 && column == 1) {
            if (PLAYER_X_TURN) {
                row0col1.setText(R.string.string_x);
            } else {
                row0col1.setText(R.string.string_o);
            }
            row0col1.setEnabled(false);
        }
        if (row == 0 && column == 2) {
            if (PLAYER_X_TURN) {
                row0col2.setText(R.string.string_x);
            } else {
                row0col2.setText(R.string.string_o);
            }
            row0col2.setEnabled(false);
        }
        if (row == 0 && column == 3) {
            if (PLAYER_X_TURN) {
                row0col3.setText(R.string.string_x);
            } else {
                row0col3.setText(R.string.string_o);
            }
            row0col3.setEnabled(false);
        }
        if (row == 0 && column == 4) {
            if (PLAYER_X_TURN) {
                row0col4.setText(R.string.string_x);
            } else {
                row0col4.setText(R.string.string_o);
            }
            row0col4.setEnabled(false);
        }
        if (row == 1 && column == 0) {
            if (PLAYER_X_TURN) {
                row1col0.setText(R.string.string_x);
            } else {
                row1col0.setText(R.string.string_o);
            }
            row1col0.setEnabled(false);
        }
        if (row == 1 && column == 1) {
            if (PLAYER_X_TURN) {
                row1col1.setText(R.string.string_x);
            } else {
                row1col1.setText(R.string.string_o);
            }
            row1col1.setEnabled(false);
        }
        if (row == 1 && column == 2) {
            if (PLAYER_X_TURN) {
                row1col2.setText(R.string.string_x);
            } else {
                row1col2.setText(R.string.string_o);
            }
            row1col2.setEnabled(false);
        }
        if (row == 1 && column == 3) {
            if (PLAYER_X_TURN) {
                row1col3.setText(R.string.string_x);
            } else {
                row1col3.setText(R.string.string_o);
            }
            row1col3.setEnabled(false);
        }
        if (row == 1 && column == 4) {
            if (PLAYER_X_TURN) {
                row1col4.setText(R.string.string_x);
            } else {
                row1col4.setText(R.string.string_o);
            }
            row1col4.setEnabled(false);
        }
        if (row == 2 && column == 0) {
            if (PLAYER_X_TURN) {
                row2col0.setText(R.string.string_x);
            } else {
                row2col0.setText(R.string.string_o);
            }
            row2col0.setEnabled(false);
        }
        if (row == 2 && column == 1) {
            if (PLAYER_X_TURN) {
                row2col1.setText(R.string.string_x);
            } else {
                row2col1.setText(R.string.string_o);
            }
            row2col1.setEnabled(false);
        }
        if (row == 2 && column == 2) {
            if (PLAYER_X_TURN) {
                row2col2.setText(R.string.string_x);
            } else {
                row2col2.setText(R.string.string_o);
            }
            row2col2.setEnabled(false);
        }
        if (row == 2 && column == 3) {
            if (PLAYER_X_TURN) {
                row2col3.setText(R.string.string_x);
            } else {
                row2col3.setText(R.string.string_o);
            }
            row2col3.setEnabled(false);
        }
        if (row == 2 && column == 4) {
            if (PLAYER_X_TURN) {
                row2col4.setText(R.string.string_x);
            } else {
                row2col4.setText(R.string.string_o);
            }
            row2col4.setEnabled(false);
        }
        if (row == 3 && column == 0) {
            if (PLAYER_X_TURN) {
                row3col0.setText(R.string.string_x);
            } else {
                row3col0.setText(R.string.string_o);
            }
            row3col0.setEnabled(false);
        }
        if (row == 3 && column == 1) {
            if (PLAYER_X_TURN) {
                row3col1.setText(R.string.string_x);
            } else {
                row3col1.setText(R.string.string_o);
            }
            row3col1.setEnabled(false);
        }
        if (row == 3 && column == 2) {
            if (PLAYER_X_TURN) {
                row3col2.setText(R.string.string_x);
            } else {
                row3col2.setText(R.string.string_o);
            }
            row3col2.setEnabled(false);
        }
        if (row == 3 && column == 3) {
            if (PLAYER_X_TURN) {
                row3col3.setText(R.string.string_x);
            } else {
                row3col3.setText(R.string.string_o);
            }
            row3col3.setEnabled(false);
        }
        if (row == 3 && column == 4) {
            if (PLAYER_X_TURN) {
                row3col4.setText(R.string.string_x);
            } else {
                row3col4.setText(R.string.string_o);
            }
            row3col4.setEnabled(false);
        }
        if (row == 4 && column == 0) {
            if (PLAYER_X_TURN) {
                row4col0.setText(R.string.string_x);
            } else {
                row4col0.setText(R.string.string_o);
            }
            row4col0.setEnabled(false);
        }
        if (row == 4 && column == 1) {
            if (PLAYER_X_TURN) {
                row4col1.setText(R.string.string_x);
            } else {
                row4col1.setText(R.string.string_o);
            }
            row4col1.setEnabled(false);
        }
        if (row == 4 && column == 2) {
            if (PLAYER_X_TURN) {
                row4col2.setText(R.string.string_x);
            } else {
                row4col2.setText(R.string.string_o);
            }
            row4col2.setEnabled(false);
        }
        if (row == 4 && column == 3) {
            if (PLAYER_X_TURN) {
                row4col3.setText(R.string.string_x);
            } else {
                row4col3.setText(R.string.string_o);
            }
            row4col3.setEnabled(false);
        }
        if (row == 4 && column == 4) {
            if (PLAYER_X_TURN) {
                row4col4.setText(R.string.string_x);
            } else {
                row4col4.setText(R.string.string_o);
            }
            row4col4.setEnabled(false);
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
            playerToMoveTextView.setText(R.string.game_draw);
            DialogFragment newFragment = new gameDrawDialogFragment();
            newFragment.show(getSupportFragmentManager(), "gameDraw");
            return;
        }
        if (PLAYER_X_TURN) {
            playerToMoveTextView.setText(R.string.o_move);
        } else {
            playerToMoveTextView.setText(R.string.x_move);
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
        boolean noWinOrBlockMove = true; // Used so that only one module is executed.
        if (GAME_MODE == IMPOSSIBLE_MODE) {
            noWinOrBlockMove = winOrBlockMove(playerWithTurnNumber); // Checking for 2/3 win situation.
        }
        if ((GAME_MODE == MEDIUM_MODE || GAME_MODE == IMPOSSIBLE_MODE) && noWinOrBlockMove) {
            if (numberOfMoves == 0) {
                playRandom();
                return;
            } else if (numberOfMoves == 1) {
                if (!(play(2, 2))) {
                    // If the square at the center is already played, play any of the 4 squares indicated below
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
                    noWinOrBlockMove = winOrBlockMove(4); // Checking for situation where loss may occur.
                } else {
                    noWinOrBlockMove = winOrBlockMove(1);
                }
            }
        }
        if (noWinOrBlockMove) {
            playRandom();
        }
    }

    boolean winOrBlockMove(int playerWithTurnNumber) {
        boolean noWinOrBlock3by4Move = true; // Used so that only one module is executed.
        if (GAME_MODE == IMPOSSIBLE_MODE) {
            noWinOrBlock3by4Move = winOrBlock3by4Move(playerWithTurnNumber);
            if ((GAME_MODE == MEDIUM_MODE || GAME_MODE == IMPOSSIBLE_MODE) && noWinOrBlock3by4Move) {
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 1; j < BOARD_SIZE - 1; j++) {
                        //Checking corresponding mid row for 2/3 situation from top to bottom.
                        // (  ) (0,1) (0,2) (0,3)  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        if (board[i][1] + board[i][2] + board[i][3] == playerWithTurnNumber * 2) {
                            if (play(i, j)) {   // Play the move.
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
                            if (play(j, i)) {
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
                        if (play(i, i)) {
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
                        if (play(i, j)) {
                            return false;
                        }
                    }
                }
            }
        }
        if (noWinOrBlock3by4Move) {
            return true;
        }
        return false;
    }

    boolean winOrBlock3by4Move(int playerWithTurnNumber) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Checking left end of row for 3/4 situation from top to bottom
                // (0,0) (0,1) (0,2) (0,3) (   )
                // (   ) (   ) (   ) (   ) (   )
                // (   ) (   ) (   ) (   ) (   )
                // (   ) (   ) (   ) (   ) (   )
                // (   ) (   ) (   ) (   ) (   )
                if (board[i][0] + board[i][1] + board[i][2] + board[i][3] == playerWithTurnNumber * 3) {
                    if (play(i, j)) {   // Play the move.
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
                    if (play(i, j)) {
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
                    if (play(i, j + 1)) {   // Play the move.
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
                    if (play(i + 1, j)) {
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
                if (play(i, j)) {
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
                if (play(i, i)) {
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
                if (play(i, i)) {
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
                if (play(i, j)) {
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
                if (play(i, j)) {
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
                if (play(i, j)) {
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
                if (play(i, j)) {
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
                if (play(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean winOrBlock2by3Move(int playerWithTurnNumber) {
        // (   ) (0,1) (0,2) (0,3) (   )
        // (1,0) (   ) (   ) (   ) (1,4)
        // (2,0) (   ) (   ) (   ) (2,4)
        // (3,0) (   ) (   ) (   ) (3,4)
        // (   ) (4,1) (4,2) (4,3) (4,4)

        for (int j = 1; j < BOARD_SIZE - 1; j++) {
            //Checking corresponding row for 2/3 situation on top row
            // (  ) (0,1) (0,2) (0,3)  (  )
            // (  ) (   ) (   ) (   )  (  )
            // (  ) (   ) (   ) (   )  (  )
            // (  ) (   ) (   ) (   )  (  )
            // (  ) (   ) (   ) (   )  (  )
            if (board[0][1] + board[0][2] + board[0][3] == playerWithTurnNumber * 2) {
                if (play(0, j)) {   // Play the move.
                    return false;
                }
            }
        }
        for (int i = 1; i < BOARD_SIZE - 1; i++) {
            // Checking corresponding mid column for 2/3 situation on first column
            // (   ) (  ) (  ) (  )  (  )
            // (1,0) (  ) (  ) (  )  (  )
            // (2,0) (  ) (  ) (  )  (  )
            // (3,0) (  ) (  ) (  )  (  )
            // (   ) (  ) (  ) (  )  (  )
            if (board[1][0] + board[2][0] + board[3][0] == playerWithTurnNumber * 2) {
                if (play(i, 0)) {
                    return false;
                }
            }
        }

        for (int j = 1; j < BOARD_SIZE - 1; j++) {
            //Checking corresponding row for 2/3 situation on bottom row
            // (  ) (   ) (   ) (   )  (  )
            // (  ) (   ) (   ) (   )  (  )
            // (  ) (   ) (   ) (   )  (  )
            // (  ) (   ) (   ) (   )  (  )
            // (  ) (4,1) (4,2) (4,3)  (  )
            if (board[4][1] + board[4][2] + board[4][3] == playerWithTurnNumber * 2) {
                if (play(4, j)) {   // Play the move.
                    return false;
                }
            }
        }
        for (int i = 1; i < BOARD_SIZE - 1; i++) {
            // Checking corresponding mid column for 2/3 situation on last column
            // (  ) (  ) (  ) (  )  (   )
            // (  ) (  ) (  ) (  )  (1,4)
            // (  ) (  ) (  ) (  )  (2,4)
            // (  ) (  ) (  ) (  )  (3,4)
            // (  ) (  ) (  ) (  )  (   )
            if (board[1][4] + board[2][4] + board[3][4] == playerWithTurnNumber * 2) {
                if (play(i, 4)) {
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

    private void enableAllBoxes(boolean value) {
        row0col0.setEnabled(value);
        row0col1.setEnabled(value);
        row0col2.setEnabled(value);
        row0col3.setEnabled(value);
        row0col4.setEnabled(value);

        row1col0.setEnabled(value);
        row1col1.setEnabled(value);
        row1col2.setEnabled(value);
        row1col3.setEnabled(value);
        row1col4.setEnabled(value);

        row2col0.setEnabled(value);
        row2col1.setEnabled(value);
        row2col2.setEnabled(value);
        row2col3.setEnabled(value);
        row2col4.setEnabled(value);

        row3col0.setEnabled(value);
        row3col1.setEnabled(value);
        row3col2.setEnabled(value);
        row3col3.setEnabled(value);
        row3col4.setEnabled(value);

        row4col0.setEnabled(value);
        row4col1.setEnabled(value);
        row4col2.setEnabled(value);
        row4col3.setEnabled(value);
        row4col4.setEnabled(value);
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
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int column = 0; column < BOARD_SIZE; column++) {
                // Fill the board with zeros(0)
                board[row][column] = 0;
            }
        }
        PLAYER_X_TURN = true;
        playerXToMoveButton.isSelected();
        playerToMoveTextView.setText(R.string.notice_board);
        playerXToMoveButton.setEnabled(true);
        playerOToMoveButton.setEnabled(true);
        GAME_MODE = gameMode;
        resetBoard();
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
            playerXToMoveButton.setEnabled(false);
            playerOToMoveButton.setEnabled(false);
            PLAYER_X_TURN = true;
            playerToMoveTextView.setText(R.string.x_move);
            if (GAME_MODE == EASY_MODE
                    || GAME_MODE == MEDIUM_MODE
                    || GAME_MODE == IMPOSSIBLE_MODE) {
                computerPlay(PLAYER_X);
            }
        }
    };

    private View.OnClickListener playerOToMoveButtonListener = new View.OnClickListener() {
        public void onClick(View player_o_to_move) {
            playerXToMoveButton.setEnabled(false);
            playerOToMoveButton.setEnabled(false);
            PLAYER_X_TURN = false;
            playerToMoveTextView.setText(R.string.o_move);
            if (GAME_MODE == EASY_MODE
                    || GAME_MODE == MEDIUM_MODE
                    || GAME_MODE == IMPOSSIBLE_MODE) {
                computerPlay(PLAYER_O);
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

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}