package com.example.android.tictactoe;

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

@SuppressWarnings("RedundantCast")
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    boolean PLAYER_X = true;

    static final int BOARD_SIZE = 3;

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

    TextView tvInfo;

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
        playerOToMoveButton = (LinearLayout) findViewById(R.id.player_o_to_move);

        initGame();

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
                if (PLAYER_X) {
                    row0col0.setText("X");
                    board[0][0] = 1;
                } else {
                    row0col0.setText("O");
                    board[0][0] = 0;
                }
                row0col0.setEnabled(false);
                break;
            case R.id.row0_col1:
                if (PLAYER_X) {
                    row0col1.setText("X");
                    board[0][1] = 1;
                } else {
                    row0col1.setText("O");
                    board[0][1] = 0;
                }
                row0col1.setEnabled(false);
                break;
            case R.id.row0_col2:
                if (PLAYER_X) {
                    row0col2.setText("X");
                    board[0][2] = 1;
                } else {
                    row0col2.setText("O");
                    board[0][2] = 0;
                }
                row0col2.setEnabled(false);
                break;

            case R.id.row1_col0:
                if (PLAYER_X) {
                    row1col0.setText("X");
                    board[1][0] = 1;
                } else {
                    row1col0.setText("O");
                    board[1][0] = 0;
                }
                row1col0.setEnabled(false);
                break;

            case R.id.row1_col1:
                if (PLAYER_X) {
                    row1col1.setText("X");
                    board[1][1] = 1;
                } else {
                    row1col1.setText("O");
                    board[1][1] = 0;
                }
                row1col1.setEnabled(false);
                break;

            case R.id.row1_col2:
                if (PLAYER_X) {
                    row1col2.setText("X");
                    board[1][2] = 1;
                } else {
                    row1col2.setText("O");
                    board[1][2] = 0;
                }
                row1col2.setEnabled(false);
                break;

            case R.id.row2_col0:
                if (PLAYER_X) {
                    row2col0.setText("X");
                    board[2][0] = 1;
                } else {
                    row2col0.setText("O");
                    board[2][0] = 0;
                }
                row2col0.setEnabled(false);
                break;

            case R.id.row2_col1:
                if (PLAYER_X) {
                    row2col1.setText("X");
                    board[2][1] = 1;
                } else {
                    row2col1.setText("O");
                    board[2][1] = 0;
                }
                row2col1.setEnabled(false);
                break;
            case R.id.row2_col2:
                if (PLAYER_X) {
                    row2col2.setText("X");
                    board[2][2] = 1;
                } else {
                    row2col2.setText("O");
                    board[2][2] = 0;
                }
                row2col2.setEnabled(false);
                break;
        }
        numberOfMoves++;
        if (!PLAYER_X) {
            playerToMoveTextView.setText("X Move");
        } else {
            playerToMoveTextView.setText("O Move");
        }
        PLAYER_X = !PLAYER_X;

        if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
            //result("Game Draw");
            playerToMoveTextView.setText("Game Draw");
            Toast.makeText(this, "Game Draw", Toast.LENGTH_SHORT).show();
        }

        checkWinner();
    }

    private void checkWinner() {

        Log.d(TAG, "Inside checkWinner");

        //Horizontal --- rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                if (board[i][0] == 1) {
                    enableAllBoxes(false);
                    //result("Player X winner\n" + (i + 1) + " row");
                    playerToMoveTextView.setText("Game Over");
                    Toast.makeText(this, "Player X Wins!\n" + (i + 1) + " row", Toast.LENGTH_SHORT).show();
                    playerXScore++;
                    playerXScoreboard.setText(String.valueOf(playerXScore));
                    break;
                } else if (board[i][0] == 0) {
                    enableAllBoxes(false);
                    //result("Player O winner\n" + (i + 1) + " row");
                    playerToMoveTextView.setText("Game Over");
                    Toast.makeText(this, "Player O Wins!\n" + (i + 1) + " row", Toast.LENGTH_SHORT).show();
                    playerOScore++;
                    playerOScoreboard.setText(String.valueOf(playerOScore));
                    break;
                }
            }
        }

        //Vertical --- columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j] == board[1][j] && board[0][j] == board[2][j]) {
                if (board[0][j] == 1) {
                    enableAllBoxes(false);
                    //result("Player X winner\n" + (j + 1) + " column");
                    playerToMoveTextView.setText("Game Over");
                    Toast.makeText(this, "Player X Wins!\n" + (j + 1) + " column", Toast.LENGTH_SHORT).show();
                    playerXScore++;
                    playerXScoreboard.setText(String.valueOf(playerXScore));
                    break;
                } else if (board[0][j] == 0) {
                    enableAllBoxes(false);
                    //result("Player O winner\n" + (j + 1) + " column");
                    playerToMoveTextView.setText("Game Over");
                    Toast.makeText(this, "Player O Wins!\n" + (j + 1) + " column", Toast.LENGTH_SHORT).show();
                    playerOScore++;
                    playerOScoreboard.setText(String.valueOf(playerOScore));
                    break;
                }
            }
        }

        //First diagonal
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            if (board[0][0] == 1) {
                enableAllBoxes(false);
                //result("Player X winner\nFirst Diagonal");
                playerToMoveTextView.setText("Game Over");
                Toast.makeText(this, "Player X Wins!\nFirst Diagonal", Toast.LENGTH_SHORT).show();
                playerXScore++;
                playerXScoreboard.setText(String.valueOf(playerXScore));
            } else if (board[0][0] == 0) {
                enableAllBoxes(false);
                //result("Player O winner\nFirst Diagonal");
                playerToMoveTextView.setText("Game Over");
                Toast.makeText(this, "Player O Wins!\nFirst Diagonal", Toast.LENGTH_SHORT).show();
                playerOScore++;
                playerOScoreboard.setText(String.valueOf(playerOScore));
            }
        }

        //Second diagonal
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            if (board[0][2] == 1) {
                enableAllBoxes(false);
                //result("Player X winner\nSecond Diagonal");
                Toast.makeText(this, "Player X Wins!\nSecond Diagonal", Toast.LENGTH_SHORT).show();
                playerXScore++;
                playerXScoreboard.setText(String.valueOf(playerXScore));
            } else if (board[0][2] == 0) {
                enableAllBoxes(false);
                //result("Player O winner\nSecond Diagonal");
                Toast.makeText(this, "Player O Wins!\nSecond Diagonal", Toast.LENGTH_SHORT).show();
                playerOScore++;
                playerOScoreboard.setText(String.valueOf(playerOScore));
            }
        }
    }

    public void resetButton(View view) {
        resetBoard();
    }

    public void playerXToMoveButton(View view) {
        PLAYER_X = true;
        playerToMoveTextView.setText("X Move");
    }

    public void playerOToMoveButton(View view) {
        PLAYER_X = false;
        playerToMoveTextView.setText("O Move");
    }

    private void result(String winner) {
        Log.d(TAG, "Inside result");

        //setInfo(winner);
        //enableAllBoxes(false);
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

        initGame();

        //setInfo("Start Again!!!");

        Toast.makeText(this, "Board Reset", Toast.LENGTH_SHORT).show();
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

    private void setInfo(String text) {
        tvInfo.setText(text);
        tvInfo.setText(text);
    }

    private void initGame() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = -1;
            }
        }

        PLAYER_X = true;
        playerXToMoveButton.isSelected();
        playerToMoveTextView.setText("Start game or select player");
        playerXToMoveButton.setEnabled(true);
        playerOToMoveButton.setEnabled(true);
    }

    private void resetScoreBoard() {
        playerXScoreboard.setText("-");
        playerOScoreboard.setText("-");
    }

    /*
    * Method to handle spinner selection
    */
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Easy is clicked
                break;
            case 1:
                // Medium is clicked
                break;
            case 2:
                // Impossible is clicked
                break;
            case 3:
                // Two Players is clicked
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /*@Retention(SOURCE)
    @IntDef({SPACE, MOVE_X, MOVE_O})
    public @interface BoardState {
        int SPACE = 0;
        int MOVE_X = 1;
        int MOVE_O = 2;
    }

    public enum BoardPlayer {
        PLAYER_X(MOVE_X), PLAYER_O(MOVE_O);
        public int move = SPACE;

        BoardPlayer(int move) {
            this.move = move;
        }
    }*/
}
