package com.zizohanto.android.tictactoe.utils

import android.widget.Button
import com.zizohanto.android.tictactoe.R

object TicTacToeUtils {
    const val SINGLE_PLAYER_EASY_MODE = 1
    const val SINGLE_PLAYER_MEDIUM_MODE = 2
    const val SINGLE_PLAYER_IMPOSSIBLE_MODE = 3
    const val TWO_PLAYER_MODE = 4
    const val PLAYER_X_PLAYED_VALUE = 1
    const val PLAYER_O_PLAYED_VALUE = 4
    const val NON_PLAYED_VALUE = 0

    /**
     * Returns true if game is played in single player mode.
     *
     * @param gameMode the game mode selected by the user
     */
    fun isSinglePlayerMode(gameMode: Int) =
            gameMode == SINGLE_PLAYER_EASY_MODE
                    || gameMode == SINGLE_PLAYER_MEDIUM_MODE
                    || gameMode == SINGLE_PLAYER_IMPOSSIBLE_MODE

    /**
     * Returns an array of values from the two dimensional array board.
     * Conversion is done because savedInstantState can not save two dimensional arrays.
     *
     * @param twoDimBoard a two dimensional array to be converted.
     * @param boardSize   the size of the board
     * @return oneDimBoard an array of integers
     */
    fun convertBoardToOneDim(boardSize: Int, twoDimBoard: Array<IntArray>): IntArray {
        val oneDimBoard = IntArray(boardSize * boardSize)
        var count = 0
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                oneDimBoard[count] = twoDimBoard[i][j]
                count++
            }
        }
        return oneDimBoard
    }

    /**
     * Returns a two dimensional array board of values from the one dimensional array.
     *
     * @param oneDimBoard an array to be converted.
     * @param boardSize   the size of the board
     * @return twoDimBoard a two dimensional array of integers for the board
     */
    fun convertBoardToTwoDim(boardSize: Int, oneDimBoard: IntArray): Array<IntArray> {
        val twoDimBoard = Array(boardSize) { IntArray(boardSize) }
        var count = 0
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                twoDimBoard[i][j] = oneDimBoard[count]
                count++
            }
        }
        return twoDimBoard
    }

    fun initBoardWithZeros(boardSize: Int): Array<IntArray> {
        val board = Array(boardSize) { IntArray(boardSize) }
        for (row in 0 until boardSize) {
            for (column in 0 until boardSize) {
                // Fill the board with zeros(0)
                board[row][column] = 0
            }
        }
        return board
    }

    fun setTextOnButtonPlayed(isPlayerXTurn: Boolean, row0col0: Button) {
        if (isPlayerXTurn) {
            row0col0.setText(R.string.string_x)
        } else {
            row0col0.setText(R.string.string_o)
        }
    }

    fun disableButton(row0col0: Button) {
        row0col0.isEnabled = false
    }
}