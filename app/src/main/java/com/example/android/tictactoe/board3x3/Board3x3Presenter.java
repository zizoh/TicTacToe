package com.example.android.tictactoe.board3x3;

import android.support.annotation.NonNull;

import com.example.android.tictactoe.BoardContract;
import com.example.android.tictactoe.R;
import com.example.android.tictactoe.utils.TicTacToeUtils;

import java.util.Random;

import static com.example.android.tictactoe.utils.TicTacToeUtils.PLAYER_O_PLAYED_VALUE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.PLAYER_X_PLAYED_VALUE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.SINGLE_PLAYER_EASY_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE;
import static com.example.android.tictactoe.utils.TicTacToeUtils.isSinglePlayerMode;
import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class Board3x3Presenter implements BoardContract.Presenter {

    static final int BOARD_SIZE = 3;
    private final BoardContract.View mBoard3x3View;
    int GAME_MODE = SINGLE_PLAYER_EASY_MODE;
    int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    boolean PLAYER_X_TURN = true;
    Random randomNumberForBoardIndex = new Random();
    int[] oneDimArrayBoard = new int[BOARD_SIZE * BOARD_SIZE];
    private int numberOfMoves = 0;
    private int mPlayerXScore = 0;
    private int mPlayerOScore = 0;

    public Board3x3Presenter(@NonNull BoardContract.View board3x3View) {
        mBoard3x3View = checkNotNull(board3x3View, "board3x3View cannot be null!");

        board3x3View.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void setBoardSize(int boardSize) {

    }

    @Override
    public void setGameMode(int gameMode) {
        GAME_MODE = gameMode;
        initGame(GAME_MODE);
    }

    private void initGame(int gameMode) {
        board = TicTacToeUtils.initBoardWithZeros(BOARD_SIZE);
        numberOfMoves = 0;
        PLAYER_X_TURN = true;
        GAME_MODE = gameMode;
        mBoard3x3View.initBoard();
    }

    @Override
    public void setPlayerXScore() {
        mPlayerXScore++;
        mBoard3x3View.showPlayerXScore(mPlayerXScore);
    }

    @Override
    public void setPlayerOScore() {
        mPlayerOScore++;
        mBoard3x3View.showPlayerOScore(mPlayerOScore);
    }

    @Override
    public void setPlayerWithTurn(Boolean playerXTurn) {
        PLAYER_X_TURN = playerXTurn;
        if (isSinglePlayerMode(GAME_MODE)) {
            if (PLAYER_X_TURN) {
                computerPlay(PLAYER_X_PLAYED_VALUE);
            } else {
                computerPlay(PLAYER_O_PLAYED_VALUE);
            }
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
                mBoard3x3View.showGameDraw();
            } else {
                mBoard3x3View.showPlayerWithTurn(PLAYER_X_TURN);
                switchTurn();
            }
        }
    }

    private void setWinner() {
        mBoard3x3View.enableAllBoxes(false);
        mBoard3x3View.showGameOver();
        if (PLAYER_X_TURN) {
            mPlayerXScore++;
            mBoard3x3View.showPlayerXWins();
            mBoard3x3View.showPlayerXScore(mPlayerXScore);
        } else {
            mPlayerOScore++;
            mBoard3x3View.showPlayerOWins();
            mBoard3x3View.showPlayerOScore(mPlayerOScore);
        }
    }

    private void playMediumOrImpossibleMode(int playerWithTurnNumber) {
        boolean noWinOrBlock = true; // Is used so that only one module is executed.
        if (GAME_MODE == SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            noWinOrBlock = winOrBlockMove(playerWithTurnNumber); // Checking for 2/3 win situation.
            if (!noWinOrBlock) {
                mBoard3x3View.enableAllBoxes(false);
                mBoard3x3View.showGameOver();
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

    private void playAnyCornerButton() {
        int i = 0;
        int j = 2;
        int c = randomNumberForBoardIndex.nextBoolean() ? i : j;
        int d = randomNumberForBoardIndex.nextBoolean() ? i : j;
        setMoveByPlayerAt(c, d);
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

    private void switchTurn() {
        PLAYER_X_TURN = !PLAYER_X_TURN;
    }

    private boolean canPlay(int row, int column) {
        // If square hasn't been played yet
        if (board[row][column] == 0) {
            if (isSinglePlayerMode(GAME_MODE)) {
                setMoveByComputerAt(row, column);
            } else {
                setMoveByPlayerAt(row, column);
            }
            return true;
        } else
            return false;
    }

    private void setMoveByComputerAt(int row, int column) {
        if (PLAYER_X_TURN) {
            mBoard3x3View.showMoveByPlayer(row, column, (R.string.string_x));
            board[row][column] = 1;
        } else {
            mBoard3x3View.showMoveByPlayer(row, column, (R.string.string_o));
            board[row][column] = 4;
        }
    }

    @Override
    public void setMoveByPlayerAt(int row, int column) {
        if (PLAYER_X_TURN) {
            mBoard3x3View.showMoveByPlayer(row, column, (R.string.string_x));
            board[row][column] = 1;
        } else {
            mBoard3x3View.showMoveByPlayer(row, column, (R.string.string_o));
            board[row][column] = 4;
        }

        if (isThereAWinner()) {
            setWinner();
        } else {
            mBoard3x3View.showPlayerWithTurn(PLAYER_X_TURN);
            switchTurn();
            numberOfMoves++;
            if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
                gameDraw();
            } else {
                gameOn();
            }
        }
    }

    /*
     * Returns true if the last play was a win
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

    private void gameDraw() {
        mBoard3x3View.showGameDraw();
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

    @Override
    public void restartGame() {
        initGame(GAME_MODE);
    }
}
