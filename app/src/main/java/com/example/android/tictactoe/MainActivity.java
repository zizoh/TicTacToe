package com.example.android.tictactoe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
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

import com.example.android.tictactoe.utils.TicTacToeUtils;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.util.Random;

import static com.example.android.tictactoe.utils.TicTacToeUtils.PLAYER_O_PLAYED_VALUE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.PLAYER_X_PLAYED_VALUE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.SINGLE_PLAYER_EASY_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.TWO_PLAYER_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.disableButton;
import static com.example.android.tictactoe.utils.TicTacToeUtils.isSinglePlayerMode;
import static com.example.android.tictactoe.utils.TicTacToeUtils.setTextOnButtonPlayed;

/**
 * TicTacToe coordinates for each square on 3x3 Board
 * -----------------
 * (0,0) (0,1) (0,2)
 * (1,0) (1,1) (1,2)
 * (2,0) (2,1) (2,2)
 * -----------------
 */

@SuppressWarnings("RedundantCast")
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Keys to identify the data saved
    static final String STATE_BOARD = "BOARD";
    static final String STATE_PLAYER_X_SCOREBOARD = "PLAYER_X_SCOREBOARD";
    static final String STATE_PLAYER_O_SCOREBOARD = "PLAYER_O_SCOREBOARD";
    static final String STATE_PLAYER_TO_MOVE_TEXTVIEW = "PLAYER_TO_MOVE_TEXTVIEW";
    static final String STATE_GAME_MODE = "GAME_MODE";
    static final String STATE_PLAYER_X_TURN = "PLAYER_X_TURN";
    static final String STATE_NUMBER_OF_MOVES = "NUMBER_OF_MOVES";
    static final String STATE_PLAYER_X_SCORE = "PLAYER_X_SCORE";
    static final String STATE_PLAYER_O_SCORE = "PLAYER_O_SCORE";

    static final int BOARD_SIZE = 3;
    int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    int GAME_MODE = SINGLE_PLAYER_EASY_MODE;
    boolean PLAYER_X_TURN = true;
    Random randomNumberForBoardIndex = new Random();
    int[] oneDimArrayBoard = new int[BOARD_SIZE * BOARD_SIZE];
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

    private boolean userIsInteracting;

    /*
     * Listener to handle game mode spinner selection
     */
    private AdapterView.OnItemSelectedListener gameModeOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userIsInteracting) {
                switch (position) {
                    case 0:
                        // Easy is selected
                        GAME_MODE = SINGLE_PLAYER_EASY_MODE;
                        break;
                    case 1:
                        // Medium is selected
                        GAME_MODE = SINGLE_PLAYER_MEDIUM_MODE;
                        break;
                    case 2:
                        // Impossible is selected
                        GAME_MODE = SINGLE_PLAYER_IMPOSSIBLE_MODE;
                        break;
                    case 3:
                        // Two Players is selected
                        GAME_MODE = TWO_PLAYER_MODE;
                        break;
                }
                initGame(GAME_MODE);
                resetScoreBoard();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    private View.OnClickListener resetButtonListener = new View.OnClickListener() {
        public void onClick(View reset) {
            initGame(GAME_MODE);
        }
    };
    private View.OnClickListener playerXToMoveButtonListener = new View.OnClickListener() {
        public void onClick(View player_x_to_move) {
            enablePlayerToMoveButtons(false);
            PLAYER_X_TURN = true;
            playerToMoveTextView.setText(R.string.x_move);
            if (isSinglePlayerMode(GAME_MODE)) {
                computerPlay(PLAYER_X_PLAYED_VALUE);
            }
        }
    };
    private View.OnClickListener playerOToMoveButtonListener = new View.OnClickListener() {
        public void onClick(View player_o_to_move) {
            enablePlayerToMoveButtons(false);
            PLAYER_X_TURN = false;
            playerToMoveTextView.setText(R.string.o_move);
            if (isSinglePlayerMode(GAME_MODE)) {
                computerPlay(PLAYER_O_PLAYED_VALUE);
            }
        }
    };

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_board_3x3);

        setToolbar();
        hideToolbarTitle();

        playerXScoreboard = (TextView) findViewById(R.id.player_x_scoreboard);
        playerOScoreboard = (TextView) findViewById(R.id.player_o_scoreboard);
        playerToMoveTextView = (TextView) findViewById(R.id.player_to_move_tv);

        playerXToMoveButton = (LinearLayout) findViewById(R.id.player_x_to_move);
        playerXToMoveButton.isSelected();
        playerOToMoveButton = (LinearLayout) findViewById(R.id.player_o_to_move);

        row0col0 = (Button) findViewById(R.id.row0_col0);
        row0col1 = (Button) findViewById(R.id.row0_col1);
        row0col2 = (Button) findViewById(R.id.row0_col2);

        row1col0 = (Button) findViewById(R.id.row1_col0);
        row1col1 = (Button) findViewById(R.id.row1_col1);
        row1col2 = (Button) findViewById(R.id.row1_col2);

        row2col0 = (Button) findViewById(R.id.row2_col0);
        row2col1 = (Button) findViewById(R.id.row2_col1);
        row2col2 = (Button) findViewById(R.id.row2_col2);

        Button resetButton = (Button) findViewById(R.id.btn_reset);

        row0col0.setOnClickListener(this);
        row0col1.setOnClickListener(this);
        row0col2.setOnClickListener(this);

        row1col0.setOnClickListener(this);
        row1col1.setOnClickListener(this);
        row1col2.setOnClickListener(this);

        row2col0.setOnClickListener(this);
        row2col1.setOnClickListener(this);
        row2col2.setOnClickListener(this);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            // Put back values stored in 1D oneDimArrayOfBoard into 2D board
            oneDimArrayBoard = savedInstanceState.getIntArray(STATE_BOARD);
            board = TicTacToeUtils.convertBoardToTwoDim(BOARD_SIZE, oneDimArrayBoard);

            PLAYER_X_TURN = savedInstanceState.getBoolean(STATE_PLAYER_X_TURN);
            numberOfMoves = savedInstanceState.getInt(STATE_NUMBER_OF_MOVES);
            playerXScore = savedInstanceState.getInt(STATE_PLAYER_X_SCORE);
            playerOScore = savedInstanceState.getInt(STATE_PLAYER_O_SCORE);
            GAME_MODE = savedInstanceState.getInt(STATE_GAME_MODE);
            playerXScoreboard.setText(savedInstanceState.getString(STATE_PLAYER_X_SCOREBOARD));
            playerOScoreboard.setText(savedInstanceState.getString(STATE_PLAYER_O_SCOREBOARD));
            playerToMoveTextView.setText(savedInstanceState.getString(STATE_PLAYER_TO_MOVE_TEXTVIEW));
        }

        playerXToMoveButton.setOnClickListener(playerXToMoveButtonListener);
        playerOToMoveButton.setOnClickListener(playerOToMoveButtonListener);
        resetButton.setOnClickListener(resetButtonListener);

        setBoardSizeSpinner();

        setGameModeSpinner();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void hideToolbarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setBoardSizeSpinner() {
        Spinner spinnerBoard3x3 = (Spinner) findViewById(R.id.board_size_spinner);
        spinnerBoard3x3.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array defined and spinner_item.xml
        ArrayAdapter<CharSequence> adapterBoardSpinner = ArrayAdapter.createFromResource(this,
                R.array.board_size_3x3_array, R.layout.spinner_item);
        // Layout to use when the list of choices appears
        adapterBoardSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBoard3x3.setAdapter(adapterBoardSpinner);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Put the values in each square of board to oneDimArrayOfBoard array
        // Since 2D board array can't be put in outState
        oneDimArrayBoard = TicTacToeUtils.convertBoardToOneDim(BOARD_SIZE, board);

        outState.putIntArray(STATE_BOARD, oneDimArrayBoard);
        outState.putCharSequence(STATE_PLAYER_X_SCOREBOARD, playerXScoreboard.getText());
        outState.putCharSequence(STATE_PLAYER_O_SCOREBOARD, playerOScoreboard.getText());
        outState.putCharSequence(STATE_PLAYER_TO_MOVE_TEXTVIEW, playerToMoveTextView.getText());
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

    private void enablePlayerToMoveButtons(Boolean enableButtons) {
        playerXToMoveButton.setEnabled(enableButtons);
        playerOToMoveButton.setEnabled(enableButtons);
    }

    private void setGameModeSpinner() {
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

    /*
     * Get the board value for position (i,j)
     */
    private int getBoardValue(int i, int j) {
        if (i < 0 || i >= BOARD_SIZE) {
            return TicTacToeUtils.NON_PLAYED_VALUE;
        }
        if (j < 0 || j >= BOARD_SIZE) {
            return TicTacToeUtils.NON_PLAYED_VALUE;
        }
        return board[i][j];
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
        } else if (row == 1 && column == 0) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row1col0);
            disableButton(row1col0);
        } else if (row == 1 && column == 1) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row1col1);
            disableButton(row1col1);
        } else if (row == 1 && column == 2) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row1col2);
            disableButton(row1col2);
        } else if (row == 2 && column == 0) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row2col0);
            disableButton(row2col0);
        } else if (row == 2 && column == 1) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row2col1);
            disableButton(row2col1);
        } else if (row == 2 && column == 2) {
            setTextOnButtonPlayed(PLAYER_X_TURN, row2col2);
            disableButton(row2col2);
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

    private void switchTurn() {
        PLAYER_X_TURN = !PLAYER_X_TURN;
    }

    private void gameDraw() {
        playerToMoveTextView.setText(R.string.game_draw);
        showWinOrDrawDialog(getString(R.string.game_draw));
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
        boolean noWinOrBlock = true; // Is used so that only one module is executed.
        if (GAME_MODE == SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            noWinOrBlock = winOrBlockMove(playerWithTurnNumber); // Checking for 2/3 win situation.
            if (!noWinOrBlock) {
                enableAllBoxes(false);
                playerToMoveTextView.setText(R.string.game_over);
                return;
            }
        }
        if ((GAME_MODE == SINGLE_PLAYER_MEDIUM_MODE || GAME_MODE == SINGLE_PLAYER_IMPOSSIBLE_MODE) && noWinOrBlock) {
            if (numberOfMoves == 0) {
                playRandom();
                return;
            } else if (numberOfMoves == 1) {
                if (GAME_MODE == SINGLE_PLAYER_IMPOSSIBLE_MODE) {
                    if (!(canPlay(1, 1))) {
                        playAnyCornerButton();
                    } else {
                        setMoveByPlayerAt(1, 1);
                    }
                } else {
                    playRandom();
                }
                return;
            } else if (numberOfMoves > 1) {
                // playerWithTurnNumber: 1 for X and 4 for O
                if (PLAYER_X_TURN) {
                    noWinOrBlock = winOrBlockMove(4); // Checking for situation where loss may occur.
                } else {
                    noWinOrBlock = winOrBlockMove(1);
                }
            }
        }
        if (noWinOrBlock) {
            playRandom();
        }
    }

    private void playAnyCornerButton() {
        int i = 0;
        int j = 2;
        int c = randomNumberForBoardIndex.nextBoolean() ? i : j;
        int d = randomNumberForBoardIndex.nextBoolean() ? i : j;
        setMoveByPlayerAt(c, d);
    }

    private boolean winOrBlockMove(int playerWithTurnNumber) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                //Checking corresponding row for 2/3 situation
                if (board[i][0] + board[i][1] + board[i][2] == playerWithTurnNumber * 2) {
                    if (canPlay(i, j)) {   // Play the move.
                        return false;
                    }
                }
                // Checking corresponding column for 2/3 situation
                else if (board[0][j] + board[1][j] + board[2][j] == playerWithTurnNumber * 2) {
                    if (canPlay(i, j)) {
                        return false;
                    }
                }
            }
        }
        // Checking left-to-right diagonal for 2/3
        if (board[0][0] + board[1][1] + board[2][2] == playerWithTurnNumber * 2) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (canPlay(i, i)) {
                    return false;
                }
            }
        }
        // Checking right-to-left diagonal for 2/3
        else if (board[0][2] + board[1][1] + board[2][0] == playerWithTurnNumber * 2) {
            for (int i = 0, j = 2; i < BOARD_SIZE; i++, j--) {
                if (canPlay(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canPlay(int row, int column) {
        // If square hasn't been played yet
        if (board[row][column] == 0) {
            setMoveByPlayerAt(row, column);
            return true;
        } else
            return false;
    }

    private void enableAllBoxes(boolean enableBoxes) {
        row0col0.setEnabled(enableBoxes);
        row0col1.setEnabled(enableBoxes);
        row0col2.setEnabled(enableBoxes);

        row1col0.setEnabled(enableBoxes);
        row1col1.setEnabled(enableBoxes);
        row1col2.setEnabled(enableBoxes);

        row2col0.setEnabled(enableBoxes);
        row2col1.setEnabled(enableBoxes);
        row2col2.setEnabled(enableBoxes);
    }

    private void resetScoreBoard() {
        playerXScore = 0;
        playerOScore = 0;
        playerXScoreboard.setText("-");
        playerOScoreboard.setText("-");
    }

    private void initGame(int gameMode) {
        board = TicTacToeUtils.initBoardWithZeros(BOARD_SIZE);

        PLAYER_X_TURN = true;
        playerXToMoveButton.isSelected();
        playerToMoveTextView.setText(R.string.notice_board);
        enablePlayerToMoveButtons(true);
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
    }

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
                    break;
                case 1:
                    Intent myIntent = new Intent(MainActivity.this, Board5x5Activity.class);
                    MainActivity.this.startActivity(myIntent);
                    break;
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    /*
     * Returns true if the last canPlay was a win
     */
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
                        if (count == 3) {
                            return true;
                        }
                    }
                }
            }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.how_to_menu:
                createHowToWebView();
                return true;
            case R.id.license_menu:
                startActivity(new Intent(this, OssLicensesMenuActivity.class));
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_license));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createHowToWebView() {
        WebView webView = new WebView(this);
        webView.loadUrl("file:///android_asset/how_to.html");
        new AlertDialog.Builder(this)
                .setTitle(R.string.how_to_dialog_title)
                .setView(webView)
                .setCancelable(true)
                .show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}