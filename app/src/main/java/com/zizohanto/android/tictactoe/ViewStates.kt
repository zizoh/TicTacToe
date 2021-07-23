package com.zizohanto.android.tictactoe

sealed class ViewStates {
    class Idle(val boardSize: Int,
               val gameMode: Int,
               val playerXScore: Int,
               val playerOScore: Int,
               val playerWithTurn: Int,
               val board: Board) : ViewStates()

    class Started(val boardSize: Int,
                  val gameMode: Int,
                  val playerXScore: Int,
                  val playerOScore: Int,
                  val playerWithTurn: Int,
                  val board: Board) : ViewStates()

    class GameOver(val boardSize: Int,
                   val gameMode: Int,
                   val playerXScore: Int,
                   val playerOScore: Int,
                   val playerWithTurn: Int,
                   val board: Board,
                   val gameOverMessage: Int) : ViewStates()

    class GameDraw(val boardSize: Int,
                   val gameMode: Int,
                   val playerXScore: Int,
                   val playerOScore: Int,
                   val playerWithTurn: Int,
                   val board: Board,
                   val gameDrawMessage: Int) : ViewStates()

    class ViewHowTo(val boardSize: Int,
                    val gameMode: Int,
                    val playerXScore: Int,
                    val playerOScore: Int,
                    val playerWithTurn: Int,
                    val board: Board,
                    val howToMessage: Int) : ViewStates()

    class ViewLicenses(val boardSize: Int,
                       val gameMode: Int,
                       val playerXScore: Int,
                       val playerOScore: Int,
                       val playerWithTurn: Int,
                       val board: Board,
                       val licenses: Int) : ViewStates()
}