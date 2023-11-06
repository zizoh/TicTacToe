package com.zizohanto.android.tictactoe

sealed class ViewStates {
    class Idle(
        val isToolbarVisible: Boolean = true,
        val isTopLayoutVisible: Boolean = true,
        val isBoard3x3Visible: Boolean = true,
        val isResetButtonVisible: Boolean = true,
        val enablePlayerToMoveButtons: Boolean = true,
        val boardSize: Int,
        val gameMode: Int,
        val playerXScore: String,
        val playerOScore: String,
        val playerWithTurn: Int,
        val board: Board
    ) : ViewStates()

    class Started(
        val boardSize: Int,
        val gameMode: Int,
        val playerXScore: String,
        val playerOScore: String,
        val playerWithTurn: Int,
        val board: Board
    ) : ViewStates()

    class GameOver(
        val boardSize: Int,
        val gameMode: Int,
        val playerXScore: String,
        val playerOScore: String,
        val playerWithTurn: Int,
        val board: Board,
        val winnerMessage: Int
    ) : ViewStates()

    class GameDraw(
        val boardSize: Int,
        val gameMode: Int,
        val playerXScore: String,
        val playerOScore: String,
        val playerWithTurn: Int,
        val board: Board,
        val gameDrawMessage: Int
    ) : ViewStates()

    class ViewHowTo(val howToMessage: Int) : ViewStates()

    object ViewLicenses : ViewStates()
}