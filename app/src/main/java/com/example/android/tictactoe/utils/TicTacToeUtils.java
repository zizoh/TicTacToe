package com.example.android.tictactoe.utils;

import android.widget.Button;

import com.example.android.tictactoe.R;

public class TicTacToeUtils {

    public static final int SINGLE_PLAYER_EASY_MODE = 1;
    public static final int SINGLE_PLAYER_MEDIUM_MODE = 2;
    public static final int SINGLE_PLAYER_IMPOSSIBLE_MODE = 3;
    public static final int TWO_PLAYER_MODE = 4;
    public static final int PLAYER_X_PLAYED_VALUE = 1;
    public static final int PLAYER_O_PLAYED_VALUE = 4;
    public static final int NON_PLAYED_VALUE = 0;

    private TicTacToeUtils() { /* cannot be instantiated */ }

    /**
     * Returns true if game is played in single player mode.
     *
     * @param gameMode the game mode selected by the user
     */
    public static boolean isSinglePlayerMode(int gameMode) {
        if (gameMode == SINGLE_PLAYER_EASY_MODE
                || gameMode == SINGLE_PLAYER_MEDIUM_MODE
                || gameMode == SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            return true;
        } else return false;
    }

    /**
     * Returns an array of values from the two dimensional array board.
     * Conversion is done because savedInstantState can not save two dimensional arrays.
     *
     * @param twoDimBoard a two dimensional array to be converted.
     * @param boardSize   the size of the board
     * @return oneDimBoard an array of integers
     */
    public static int[] convertBoardToOneDim(int boardSize, int[][] twoDimBoard) {
        int[] oneDimBoard = new int[boardSize * boardSize];
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                oneDimBoard[count] = twoDimBoard[i][j];
                count++;
            }
        }
        return oneDimBoard;
    }

    /**
     * Returns a two dimensional array board of values from the one dimensional array.
     *
     * @param oneDimBoard an array to be converted.
     * @param boardSize   the size of the board
     * @return twoDimBoard a two dimensional array of integers for the board
     */
    public static int[][] convertBoardToTwoDim(int boardSize, int[] oneDimBoard) {
        int[][] twoDimBoard = new int[boardSize][boardSize];
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                twoDimBoard[i][j] = oneDimBoard[count];
                count++;
            }
        }
        return twoDimBoard;
    }

    public static int[][] initBoardWithZeros(int boardSize) {
        int[][] board = new int[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                // Fill the board with zeros(0)
                board[row][column] = 0;
            }
        }
        return board;
    }

    public static void setTextOnButtonPlayed(boolean PLAYER_X_TURN, Button row0col0) {
        if (PLAYER_X_TURN) {
            row0col0.setText(R.string.string_x);
        } else {
            row0col0.setText(R.string.string_o);
        }
    }

    public static void disableButton(Button row0col0) {
        row0col0.setEnabled(false);
    }

}
