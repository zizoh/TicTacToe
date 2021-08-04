package com.zizohanto.android.tictactoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zizohanto.android.tictactoe.utils.TicTacToeUtils
import java.util.*

class MainActivityViewModel : ViewModel() {

    private val boardSize = MainActivity.BOARD_SIZE
    private var board = Board(boardSize)

    private var randomNumberForBoardIndex = Random()
    private var isPlayerXTurn = true
    private var gameMode = TicTacToeUtils.SINGLE_PLAYER_EASY_MODE
    private var numberOfMoves = 0
    private var playerXScore = 0
    private var playerOScore = 0

    private val _viewState: MutableLiveData<ViewStates> = MutableLiveData()
    val viewStates: LiveData<ViewStates> = _viewState

    private val _enableAllBoxes: MutableLiveData<Boolean> = MutableLiveData()
    val enableAllBoxes: LiveData<Boolean> = _enableAllBoxes

    private val _gameOver: MutableLiveData<Int> = MutableLiveData()
    val gameOver: LiveData<Int> = _gameOver

    private val _playerToMoveText: MutableLiveData<Int> = MutableLiveData()
    val playerToMoveText: LiveData<Int> = _playerToMoveText

    private val _gameDraw: MutableLiveData<Int> = MutableLiveData()
    val gameDraw: LiveData<Int> = _gameDraw

    private val _playerXScore: MutableLiveData<String> = MutableLiveData()
    val playerXScoreString: LiveData<String> = _playerXScore

    private val _playerOScore: MutableLiveData<String> = MutableLiveData()
    val playerOScoreString: LiveData<String> = _playerOScore

    private val _showDialog: MutableLiveData<Int> = MutableLiveData()
    val showDialog: LiveData<Int> = _showDialog

    init {
        _viewState.value = ViewStates.Idle(
                boardSize,
                gameMode,
                playerXScore.toString(),
                playerOScore.toString(),
                R.string.notice_board,
                board
        )
    }

    /*
     * Get the board value for position (i,j)
     */
    private fun getBoardValue(i: Int, j: Int): String {
        if (i < 0 || i >= boardSize) {
            return Board.NOT_PLAYED
        }
        return if (j < 0 || j >= boardSize) {
            Board.NOT_PLAYED
        } else board.get(i, j)
    }

    fun initGame(gameMode: Int) {
        board = Board(boardSize)
        isPlayerXTurn = true
        setGameMode(gameMode)
    }

    fun convertBoardToTwoDim(oneDimBoard: Array<String>) {
        board = board.convertBoardToTwoDim(oneDimBoard)
    }

    fun getOneDimBoard(): Array<String> {
        return board.convertBoardToOneDim(board)
    }

    fun isPlayerXTurn() = isPlayerXTurn

    fun setIsPlayerXTurn(isPlayerXTurn: Boolean) {
        this.isPlayerXTurn = isPlayerXTurn
    }

    fun setNumberOfMoves(moves: Int) {
        numberOfMoves = moves
    }

    fun getNumberOfMoves() = numberOfMoves

    fun getPlayerXScore() = playerXScore

    fun setPlayerXScore(score: Int) {
        playerXScore = score
    }

    fun getPlayerOScore() = playerOScore

    fun setPlayerOScore(score: Int) {
        playerOScore = score
    }

    fun getGameMode() = gameMode

    fun setGameMode(mode: Int) {
        gameMode = mode
    }

    private fun setMove(row: Int, column: Int, playerWithTurn: String) {
        board.set(row, column, playerWithTurn)
    }

    private fun winOrBlockMove(playerWithTurn: String): Boolean {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                //Checking corresponding row for 2/3 situation
                if ((board.get(i, 0) + board.get(i, 1) + board.get(i, 2)).contains(playerWithTurn)) {
                    if (positionIsNotPlayed(i, j)) {   // Play the move.
                        setMoveByPlayerAt(i, j)
                        return false
                    }
                } else if ((board.get(0, j) + board.get(1, j) + board.get(2, j)).contains(playerWithTurn)) {
                    if (positionIsNotPlayed(i, j)) {
                        setMoveByPlayerAt(i, j)
                        return false
                    }
                }
            }
        }
        // Checking left-to-right diagonal for 2/3
        if ((board.get(0, 0) + board.get(1, 1) + board.get(2, 2)).contains(playerWithTurn)) {
            for (i in 0 until boardSize) {
                if (positionIsNotPlayed(i, i)) {
                    setMoveByPlayerAt(i, i)
                    return false
                }
            }
        } else if ((board.get(0, 2) + board.get(1, 1) + board.get(2, 0)).contains(playerWithTurn)) {
            var i = 0
            var j = 2
            while (i < boardSize) {
                if (positionIsNotPlayed(i, j)) {
                    setMoveByPlayerAt(i, j)
                    return false
                }
                i++
                j--
            }
        }
        return true
    }

    private fun positionIsNotPlayed(row: Int, column: Int): Boolean = board.get(row, column) == Board.NOT_PLAYED

    fun playMoveByPlayerAt(row: Int, column: Int) {
        setMoveByPlayerAt(row, column)
        val isGameOver = isGameOver()
        if (!isGameOver) {
            passTurn()
        }
    }

    private fun setMoveByPlayerAt(row: Int, column: Int) {
        if (isPlayerXTurn) {
            setMove(row, column, Board.PLAYER_X)
        } else {
            setMove(row, column, Board.PLAYER_O)
        }
        val playerWithTurn = if (!isPlayerXTurn) R.string.x_move
        else R.string.o_move
        _viewState.value = ViewStates.Started(
                boardSize,
                gameMode,
                playerXScore.toString(),
                playerOScore.toString(),
                playerWithTurn,
                board
        )
    }

    fun computerPlay(playerWithTurn: String) {
        if (gameMode == TicTacToeUtils.SINGLE_PLAYER_EASY_MODE) {
            playRandom()
        } else if (gameMode == TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE
                || gameMode == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            playMediumOrImpossibleMode(playerWithTurn)
        }
        isGameOver()
    }

    private fun isGameOver(): Boolean {
        if (isThereAWinner()) {
            setWinner()
            return true
        } else {
            numberOfMoves++
            if (numberOfMoves == boardSize * boardSize) {
                _gameDraw.value = R.string.game_draw
                return true
            }
        }
        switchTurn()
        return false
    }

    private fun playRandom() {
        var iIndex = 0
        var jIndex = 1
        while (!positionIsNotPlayed(iIndex, jIndex)) {
            // Keep trying until a successful move is played
            iIndex = randomNumberForBoardIndex.nextInt(boardSize)
            jIndex = randomNumberForBoardIndex.nextInt(boardSize)
        }
        setMoveByPlayerAt(iIndex, jIndex)
    }

    private fun playAnyCornerButton() {
        val i = 0
        val j = 2
        val c = if (randomNumberForBoardIndex.nextBoolean()) i else j
        val d = if (randomNumberForBoardIndex.nextBoolean()) i else j
        setMoveByPlayerAt(c, d)
    }

    private fun playMediumOrImpossibleMode(playerWithTurn: String) {
        var noWinOrBlock = true // Is used so that only one module is executed.
        if (gameMode == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            noWinOrBlock = winOrBlockMove(playerWithTurn) // Checking for 2/3 win situation.
            if (!noWinOrBlock) {
                _enableAllBoxes.value = false
                _gameOver.value = R.string.game_over
                return
            }
        }
        if ((gameMode == TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE || gameMode == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) && noWinOrBlock) {
            when {
                numberOfMoves == 0 -> {
                    playRandom()
                    return
                }
                numberOfMoves == 1 -> {
                    if (gameMode == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
                        if (positionIsNotPlayed(1, 1)) {
                            setMoveByPlayerAt(1, 1)
                        } else {
                            playAnyCornerButton()
                        }
                    } else {
                        playRandom()
                    }
                    return
                }
                numberOfMoves > 1 -> {
                    noWinOrBlock = if (isPlayerXTurn()) {
                        winOrBlockMove("OO")
                    } else {
                        winOrBlockMove("XX")
                    }
                }
            }
        }
        if (noWinOrBlock) {
            playRandom()
        }
    }

    private fun isThereAWinner(): Boolean {
        val token: String = if (isPlayerXTurn) {
            Board.PLAYER_X
        } else {
            Board.PLAYER_O
        }
        val DI = intArrayOf(-1, 0, 1, 1)
        val DJ = intArrayOf(1, 1, 1, 0)
        for (i in 0 until boardSize) for (j in 0 until boardSize) {

            // Skip if the token in board.get(i,j) is not equal to current token
            if (board.get(i, j) != token) continue
            for (k in 0..3) {
                var count = 0
                while (getBoardValue(i + DI[k] * count, j + DJ[k] * count) == token) {
                    count++
                    if (count == 3) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun setWinner() {
        _enableAllBoxes.value = false
        _playerToMoveText.value = R.string.game_over
        addPlayerScore()
        if (isPlayerXTurn()) {
            _playerXScore.value = playerXScore.toString()
            _showDialog.value = R.string.player_x_wins
        } else {
            _playerOScore.value = playerOScore.toString()
            _showDialog.value = R.string.player_o_wins
        }
    }

    private fun addPlayerScore() {
        if (isPlayerXTurn) {
            playerXScore++
        } else {
            playerOScore++
        }
    }

    private fun switchTurn() {
        isPlayerXTurn = !isPlayerXTurn
    }

    private fun passTurn() {
        if (TicTacToeUtils.isSinglePlayerMode(getGameMode())) {
            if (isPlayerXTurn) {
                computerPlay(Board.PLAYER_X)
            } else {
                computerPlay(Board.PLAYER_O)
            }
        }
    }
}