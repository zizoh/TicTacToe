package com.zizohanto.android.tictactoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zizohanto.android.tictactoe.utils.TicTacToeUtils
import java.util.*

class MainActivityViewModel : ViewModel() {

    private var board = Board(MainActivity.BOARD_SIZE)

    private var randomNumberForBoardIndex = Random()
    private var isPlayerXTurn = true
    private var gameMode = TicTacToeUtils.SINGLE_PLAYER_EASY_MODE
    private var numberOfMoves = 0
    private var playerXScore = 0
    private var playerOScore = 0

    private val _viewState: MutableLiveData<ViewStates> = MutableLiveData()
    val viewStates: LiveData<ViewStates> = _viewState

    private val _playAt: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    val playAt: LiveData<Pair<Int, Int>> = _playAt

    private val _enableAllBoxes: MutableLiveData<Boolean> = MutableLiveData()
    val enableAllBoxes: LiveData<Boolean> = _enableAllBoxes

    private val _gameOver: MutableLiveData<Int> = MutableLiveData()
    val gameOver: LiveData<Int> = _gameOver

    private val _indicatePlayerWithTurn: MutableLiveData<Boolean> = MutableLiveData()
    val indicatePlayerWithTurn: LiveData<Boolean> = _indicatePlayerWithTurn

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
                MainActivity.BOARD_SIZE,
                gameMode,
                playerXScore,
                playerOScore,
                R.string.notice_board,
                board
        )
    }

    private fun isThereAWinner(): Boolean {
        val token: String = if (isPlayerXTurn) {
            Board.PLAYER_X
        } else {
            Board.PLAYER_O
        }
        val DI = intArrayOf(-1, 0, 1, 1)
        val DJ = intArrayOf(1, 1, 1, 0)
        for (i in 0 until MainActivity.BOARD_SIZE) for (j in 0 until MainActivity.BOARD_SIZE) {

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

    /*
     * Get the board value for position (i,j)
     */
    private fun getBoardValue(i: Int, j: Int): String {
        if (i < 0 || i >= MainActivity.BOARD_SIZE) {
            return Board.NOT_PLAYED
        }
        return if (j < 0 || j >= MainActivity.BOARD_SIZE) {
            Board.NOT_PLAYED
        } else board.get(i, j)
    }

    fun initGame(gameMode: Int) {
        board = Board(MainActivity.BOARD_SIZE)
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
        _playerToMoveText.value = if (isPlayerXTurn) R.string.x_move
        else R.string.o_move
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
        for (i in 0 until MainActivity.BOARD_SIZE) {
            for (j in 0 until MainActivity.BOARD_SIZE) {
                //Checking corresponding row for 2/3 situation
                if ((board.get(i, 0) + board.get(i, 1) + board.get(i, 2)).contains(playerWithTurn)) {
                    if (canPlay(i, j)) {   // Play the move.
                        return false
                    }
                } else if ((board.get(0, j) + board.get(1, j) + board.get(2, j)).contains(playerWithTurn)) {
                    if (canPlay(i, j)) {
                        return false
                    }
                }
            }
        }
        // Checking left-to-right diagonal for 2/3
        if ((board.get(0, 0) + board.get(1, 1) + board.get(2, 2)).contains(playerWithTurn)) {
            for (i in 0 until MainActivity.BOARD_SIZE) {
                if (canPlay(i, i)) {
                    return false
                }
            }
        } else if ((board.get(0, 2) + board.get(1, 1) + board.get(2, 0)).contains(playerWithTurn)) {
            var i = 0
            var j = 2
            while (i < MainActivity.BOARD_SIZE) {
                if (canPlay(i, j)) {
                    return false
                }
                i++
                j--
            }
        }
        return true
    }

    private fun canPlay(row: Int, column: Int): Boolean {
        return if (board.get(row, column) == Board.NOT_PLAYED) {
            _playAt.value = Pair(row, column)
            setMoveByPlayerAt(row, column)
            true
        } else false
    }

    fun setMoveByPlayerAt(row: Int, column: Int) {
        if (isPlayerXTurn) {
            setMove(row, column, Board.PLAYER_X)
        } else {
            setMove(row, column, Board.PLAYER_O)
        }
    }

    private fun playRandom() {
        var iIndex = 0
        var jIndex = 1
        while (!canPlay(iIndex, jIndex)) {
            // Keep trying until a successful move is played
            iIndex = randomNumberForBoardIndex.nextInt(MainActivity.BOARD_SIZE)
            jIndex = randomNumberForBoardIndex.nextInt(MainActivity.BOARD_SIZE)
        }
    }

    private fun playAnyCornerButton() {
        val i = 0
        val j = 2
        val c = if (randomNumberForBoardIndex.nextBoolean()) i else j
        val d = if (randomNumberForBoardIndex.nextBoolean()) i else j
        _playAt.value = Pair(c, d)
        setMoveByPlayerAt(c, d)
    }

    private fun switchTurn() {
        isPlayerXTurn = !isPlayerXTurn
    }

    fun computerPlay(playerWithTurn: String) {
        if (gameMode == TicTacToeUtils.SINGLE_PLAYER_EASY_MODE) {
            playRandom()
        } else if (gameMode == TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE
                || gameMode == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            playMediumOrImpossibleMode(playerWithTurn)
        }
        if (isThereAWinner()) {
            setWinner()
        } else {
            numberOfMoves++
            if (numberOfMoves == MainActivity.BOARD_SIZE * MainActivity.BOARD_SIZE) {
                _gameDraw.value = R.string.game_draw
            } else {
                _indicatePlayerWithTurn.value = isPlayerXTurn()
                switchTurn()
            }
        }
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
                        if (!canPlay(1, 1)) {
                            playAnyCornerButton()
                        } else {
                            setMoveByPlayerAt(1, 1)
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

    fun checkMove() {
        if (isThereAWinner()) {
            setWinner()
        } else {
            _indicatePlayerWithTurn.value = isPlayerXTurn()
            switchTurn()
            numberOfMoves++
            if (numberOfMoves == MainActivity.BOARD_SIZE * MainActivity.BOARD_SIZE) {
                _gameDraw.value = R.string.game_draw
            } else {
                gameOn()
            }
        }
    }

    private fun gameOn() {
        if (TicTacToeUtils.isSinglePlayerMode(getGameMode())) {
            if (isPlayerXTurn) {
                computerPlay(Board.PLAYER_X)
            } else {
                computerPlay(Board.PLAYER_O)
            }
        }
    }
}