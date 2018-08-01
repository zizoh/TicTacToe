package com.example.android.tictactoe.board3x3;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.tictactoe.Board5x5Activity;
import com.example.android.tictactoe.BoardContract;
import com.example.android.tictactoe.R;
import com.example.android.tictactoe.WinOrDrawDialog;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.util.Random;

import static com.example.android.tictactoe.utils.TicTacToeUtils.SINGLE_PLAYER_EASY_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.TWO_PLAYER_MODE;
import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class Board3x3Fragment extends Fragment implements BoardContract.View,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Keys to identify the data saved
    static final String BOARD_KEY = "BOARD";
    static final String PLAYER_X_SCOREBOARD_KEY = "PLAYER_X_SCOREBOARD";
    static final String PLAYER_O_SCOREBOARD_KEY = "PLAYER_O_SCOREBOARD";
    static final String PLAYER_TO_MOVE_TEXTVIEW_KEY = "PLAYER_TO_MOVE_TEXTVIEW";
    static final String GAME_MODE_KEY = "GAME_MODE";
    static final String PLAYER_X_TURN_KEY = "PLAYER_X_TURN";
    static final String NUMBER_OF_MOVES_KEY = "NUMBER_OF_MOVES";
    static final String PLAYER_X_SCORE_KEY = "PLAYER_X_SCORE";
    static final String PLAYER_O_SCORE_KEY = "PLAYER_O_SCORE";
    static final int BOARD_SIZE = 3;
    BoardContract.Presenter mPresenter;
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
    private View.OnClickListener playerXToMoveButtonListener = new View.OnClickListener() {
        public void onClick(View player_x_to_move) {
            enablePlayerToMoveButtons(false);
            playerToMoveTextView.setText(R.string.x_move);
            mPresenter.setPlayerWithTurn(true);
        }
    };
    private View.OnClickListener playerOToMoveButtonListener = new View.OnClickListener() {
        public void onClick(View player_o_to_move) {
            enablePlayerToMoveButtons(false);
            playerToMoveTextView.setText(R.string.o_move);
            mPresenter.setPlayerWithTurn(false);
        }
    };
    private View.OnClickListener resetButtonListener = new View.OnClickListener() {
        public void onClick(View reset) {
            mPresenter.restartGame();
        }
    };
    /*
     * Listener to handle game mode spinner selection
     */
    private AdapterView.OnItemSelectedListener gameModeOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    // Easy is clicked
                    mPresenter.setGameMode(SINGLE_PLAYER_EASY_MODE);
                    break;
                case 1:
                    // Medium is clicked
                    mPresenter.setGameMode(SINGLE_PLAYER_MEDIUM_MODE);
                    break;
                case 2:
                    // Impossible is clicked
                    mPresenter.setGameMode(SINGLE_PLAYER_IMPOSSIBLE_MODE);
                    break;
                case 3:
                    // Two Players is clicked
                    mPresenter.setGameMode(TWO_PLAYER_MODE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    public Board3x3Fragment() {
        // Requires empty public constructor
    }

    public static Board3x3Fragment newInstance() {
        return new Board3x3Fragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull BoardContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.board3x3_frag, container, false);
        setHasOptionsMenu(true);

        playerXScoreboard = (TextView) root.findViewById(R.id.player_x_scoreboard);
        playerOScoreboard = (TextView) root.findViewById(R.id.player_o_scoreboard);
        playerToMoveTextView = (TextView) root.findViewById(R.id.player_to_move_tv);

        playerXToMoveButton = (LinearLayout) root.findViewById(R.id.player_x_to_move);
        playerXToMoveButton.isSelected();
        playerOToMoveButton = (LinearLayout) root.findViewById(R.id.player_o_to_move);

        row0col0 = (Button) root.findViewById(R.id.row0_col0);
        row0col1 = (Button) root.findViewById(R.id.row0_col1);
        row0col2 = (Button) root.findViewById(R.id.row0_col2);

        row1col0 = (Button) root.findViewById(R.id.row1_col0);
        row1col1 = (Button) root.findViewById(R.id.row1_col1);
        row1col2 = (Button) root.findViewById(R.id.row1_col2);

        row2col0 = (Button) root.findViewById(R.id.row2_col0);
        row2col1 = (Button) root.findViewById(R.id.row2_col1);
        row2col2 = (Button) root.findViewById(R.id.row2_col2);

        Button resetButton = (Button) root.findViewById(R.id.reset);

        row0col0.setOnClickListener(this);
        row0col1.setOnClickListener(this);
        row0col2.setOnClickListener(this);

        row1col0.setOnClickListener(this);
        row1col1.setOnClickListener(this);
        row1col2.setOnClickListener(this);

        row2col0.setOnClickListener(this);
        row2col1.setOnClickListener(this);
        row2col2.setOnClickListener(this);

        playerXToMoveButton.setOnClickListener(playerXToMoveButtonListener);
        playerOToMoveButton.setOnClickListener(playerOToMoveButtonListener);
        resetButton.setOnClickListener(resetButtonListener);

        Spinner spinnerBoard3x3 = (Spinner) getActivity().findViewById(R.id.board_size_spinner);
        spinnerBoard3x3.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array defined and spinner_item.xml
        ArrayAdapter<CharSequence> adapterBoardSpinner = ArrayAdapter.createFromResource(getContext(),
                R.array.board_size_3x3_array, R.layout.spinner_item);
        // Layout to use when the list of choices appears
        adapterBoardSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBoard3x3.setAdapter(adapterBoardSpinner);

        Spinner spinnerGameMode = (Spinner) root.findViewById(R.id.spinner);
        spinnerGameMode.setOnItemSelectedListener(gameModeOnItemSelectedListener);
        // Create an ArrayAdapter using the string array defined and spinner_item.xml
        ArrayAdapter<CharSequence> adapterGameMode = ArrayAdapter.createFromResource(getContext(),
                R.array.level_or_player_type_array, R.layout.spinner_item);
        // Layout to use when the list of choices appears
        adapterGameMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerGameMode.setAdapter(adapterGameMode);

        return root;
    }

    @Override
    public void onClick(View v) {
        enablePlayerToMoveButtons(false);
        switch (v.getId()) {
            case R.id.row0_col0:
                mPresenter.setMoveByPlayerAt(0, 0);
                break;
            case R.id.row0_col1:
                mPresenter.setMoveByPlayerAt(0, 1);
                break;
            case R.id.row0_col2:
                mPresenter.setMoveByPlayerAt(0, 2);
                break;
            case R.id.row1_col0:
                mPresenter.setMoveByPlayerAt(1, 0);
                break;
            case R.id.row1_col1:
                mPresenter.setMoveByPlayerAt(1, 1);
                break;
            case R.id.row1_col2:
                mPresenter.setMoveByPlayerAt(1, 2);
                break;
            case R.id.row2_col0:
                mPresenter.setMoveByPlayerAt(2, 0);
                break;
            case R.id.row2_col1:
                mPresenter.setMoveByPlayerAt(2, 1);
                break;
            case R.id.row2_col2:
                mPresenter.setMoveByPlayerAt(2, 2);
                break;
        }

    }

    @Override
    public void initBoard() {
        resetBoard();
        playerXToMoveButton.isSelected();
        playerToMoveTextView.setText(R.string.notice_board);
        enablePlayerToMoveButtons(true);
    }

    private void enablePlayerToMoveButtons(Boolean enableButtons) {
        playerXToMoveButton.setEnabled(enableButtons);
        playerOToMoveButton.setEnabled(enableButtons);
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
    }

    @Override
    public void showPlayerOScore(int score) {
        playerXScoreboard.setText(String.valueOf(score));
    }

    @Override
    public void showPlayerXScore(int score) {
        playerXScoreboard.setText(String.valueOf(score));
    }

    @Override
    public void enableAllBoxes(boolean enableBoxes) {
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

    @Override
    public void showPlayerWithTurn(Boolean playerXTurn) {
        if (playerXTurn) {
            playerToMoveTextView.setText(R.string.o_move);
        } else {
            playerToMoveTextView.setText(R.string.x_move);
        }
    }

    @Override
    public void showMoveByPlayer(int row, int column, int player) {
        markButtonPlayed(row, column, player);
    }

    private void markButtonPlayed(int row, int column, int player) {
        if (row == 0 && column == 0) {
            row0col0.setText(player);
            disableButton(row0col0);
        } else if (row == 0 && column == 1) {
            row0col1.setText(player);
            disableButton(row0col1);
        } else if (row == 0 && column == 2) {
            row0col2.setText(player);
            disableButton(row0col2);
        } else if (row == 1 && column == 0) {
            row1col0.setText(player);
            disableButton(row1col0);
        } else if (row == 1 && column == 1) {
            row1col1.setText(player);
            disableButton(row1col1);
        } else if (row == 1 && column == 2) {
            row1col2.setText(player);
            disableButton(row1col2);
        } else if (row == 2 && column == 0) {
            row2col0.setText(player);
            disableButton(row2col0);
        } else if (row == 2 && column == 1) {
            row2col1.setText(player);
            disableButton(row2col1);
        } else if (row == 2 && column == 2) {
            row2col2.setText(player);
            disableButton(row2col2);
        }
    }

    @Override
    public void showPlayerXWins() {
        showWinOrDrawDialog(getString(R.string.player_x_wins));
    }

    @Override
    public void showPlayerOWins() {
        showWinOrDrawDialog(getString(R.string.player_o_wins));
    }

    public void showWinOrDrawDialog(String winOrDraw) {
        DialogFragment newFragment = new WinOrDrawDialog(winOrDraw);
        newFragment.show(getChildFragmentManager(), "WinOrDrawDialog");
    }

    @Override
    public void showGameOver() {
        playerToMoveTextView.setText(R.string.game_over);
    }

    @Override
    public void showGameDraw() {
        playerToMoveTextView.setText(R.string.game_draw);
        showWinOrDrawDialog(getString(R.string.game_draw));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.how_to_menu:
                WebView webView = new WebView(getContext());
                webView.loadUrl("file:///android_asset/how_to.html");
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.how_to_dialog_title)
                        .setView(webView)
                        .setCancelable(true)
                        .show();
                return true;
            case R.id.license_menu:
                startActivity(new Intent(getActivity(), OssLicensesMenuActivity.class));
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_license));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    private void disableButton(Button rowColumn) {
        rowColumn.setEnabled(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                break;
            case 1:
                Intent myIntent = new Intent(getActivity(), Board5x5Activity.class);
                startActivity(myIntent);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
