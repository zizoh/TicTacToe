package com.zizohanto.android.tictactoe

class Board(private val size: Int) {
    private val board: Array<Array<String>> = Array(size) { Array(size) { NOT_PLAYED } }

    fun get(row: Int, column: Int): String {
        return board[row][column]
    }

    fun set(row: Int, column: Int, value: String) {
        board[row][column] = value
    }

    fun convertBoardToTwoDim(oneDimBoard: Array<String>): Board {
        val board = Board(size)
        var count = 0
        for (i in 0 until size) {
            for (j in 0 until size) {
                board.set(i, j, oneDimBoard[count])
                count++
            }
        }
        return board
    }

    fun convertBoardToOneDim(board: Board): Array<String> {
        val oneDimBoard = Array(size * size) { NOT_PLAYED }
        var count = 0
        for (i in 0 until size) {
            for (j in 0 until size) {
                oneDimBoard[count] = board.get(i, j)
                count++
            }
        }
        return oneDimBoard
    }

    companion object {
        const val NOT_PLAYED = ""
        const val PLAYER_X = "X"
        const val PLAYER_O = "O"
    }
}