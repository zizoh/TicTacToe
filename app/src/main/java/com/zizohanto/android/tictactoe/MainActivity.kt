package com.zizohanto.android.tictactoe

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.zizohanto.android.tictactoe.databinding.ActivityBoard3x3Binding
import com.zizohanto.android.tictactoe.utils.TicTacToeUtils
import java.util.*

/**
 * TicTacToe coordinates for each square on 3x3 Board
 * -----------------
 * (0,0) (0,1) (0,2)
 * (1,0) (1,1) (1,2)
 * (2,0) (2,1) (2,2)
 * -----------------
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {
    var board = Board(BOARD_SIZE)
    var GAME_MODE = TicTacToeUtils.SINGLE_PLAYER_EASY_MODE
    var isPlayerXTurn = true
    var randomNumberForBoardIndex = Random()
    private var numberOfMoves = 0
    private var playerXScore = 0
    private var playerOScore = 0

    private lateinit var binding: ActivityBoard3x3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoard3x3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        hideToolbarTitle()
        binding.layoutTop.playerXToMove.isSelected
        with(binding.board3x3) {
            row0Col0.board3x3ButtonO.setOnClickListener(this@MainActivity)
            row0Col1.board3x3ButtonO.setOnClickListener(this@MainActivity)
            row0Col2.board3x3ButtonO.setOnClickListener(this@MainActivity)
            row1Col0.board3x3ButtonO.setOnClickListener(this@MainActivity)
            row1Col1.board3x3ButtonO.setOnClickListener(this@MainActivity)
            row1Col2.board3x3ButtonO.setOnClickListener(this@MainActivity)
            row2Col0.board3x3ButtonO.setOnClickListener(this@MainActivity)
            row2Col1.board3x3ButtonO.setOnClickListener(this@MainActivity)
            row2Col2.board3x3ButtonO.setOnClickListener(this@MainActivity)
        }
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            // Put back values stored in 1D oneDimArrayOfBoard into 2D board
            val oneDimArrayBoard = savedInstanceState.getStringArray(STATE_BOARD)
            oneDimArrayBoard?.let {
                board = board.convertBoardToTwoDim(it)
            }
            isPlayerXTurn = savedInstanceState.getBoolean(STATE_PLAYER_X_TURN)
            numberOfMoves = savedInstanceState.getInt(STATE_NUMBER_OF_MOVES)
            playerXScore = savedInstanceState.getInt(STATE_PLAYER_X_SCORE)
            playerOScore = savedInstanceState.getInt(STATE_PLAYER_O_SCORE)
            GAME_MODE = savedInstanceState.getInt(STATE_GAME_MODE)
            binding.layoutTop.playerXScoreboard.text = savedInstanceState.getString(STATE_PLAYER_X_SCOREBOARD)
            binding.layoutTop.playerOScoreboard.text = savedInstanceState.getString(STATE_PLAYER_O_SCOREBOARD)
            binding.layoutTop.playerToMoveTv.text = savedInstanceState.getString(STATE_PLAYER_TO_MOVE_TEXTVIEW)
        }
        binding.layoutTop.playerXToMove.setOnClickListener {
            enablePlayerToMoveButtons(false)
            isPlayerXTurn = true
            binding.layoutTop.playerToMoveTv.text = getString(R.string.x_move)
            if (TicTacToeUtils.isSinglePlayerMode(GAME_MODE)) {
                computerPlay(Board.PLAYER_X)
            }
        }
        binding.layoutTop.playerOToMove.setOnClickListener {
            enablePlayerToMoveButtons(false)
            isPlayerXTurn = false
            binding.layoutTop.playerToMoveTv.text = getString(R.string.o_move)
            if (TicTacToeUtils.isSinglePlayerMode(GAME_MODE)) {
                computerPlay(Board.PLAYER_O)
            }
        }
        binding.resetButton.btnReset.setOnClickListener { initGame(GAME_MODE) }
        setBoardSizeSpinner()
        setGameModeSpinner()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar.toolbar)
    }

    private fun hideToolbarTitle() {
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setBoardSizeSpinner() {
        binding.toolbar.boardSizeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                    }
                    1 -> {
                        val myIntent = Intent(this@MainActivity, Board5x5Activity::class.java)
                        this@MainActivity.startActivity(myIntent)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        // Create an ArrayAdapter using the string array defined and spinner_item.xml
        val adapterBoardSpinner = ArrayAdapter.createFromResource(this,
                R.array.board_size_3x3_array, R.layout.spinner_item)
        // Layout to use when the list of choices appears
        adapterBoardSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding.toolbar.boardSizeSpinner.adapter = adapterBoardSpinner
    }

    public override fun onSaveInstanceState(outState: Bundle) {

        // Put the values in each square of board to oneDimArrayOfBoard array
        // Since 2D board array can't be put in outState
        val oneDimArrayBoard = board.convertBoardToOneDim(board)
        outState.putStringArray(STATE_BOARD, oneDimArrayBoard)
        outState.putCharSequence(STATE_PLAYER_X_SCOREBOARD, binding.layoutTop.playerXScoreboard.text)
        outState.putCharSequence(STATE_PLAYER_O_SCOREBOARD, binding.layoutTop.playerOScoreboard.text)
        outState.putCharSequence(STATE_PLAYER_TO_MOVE_TEXTVIEW, binding.layoutTop.playerToMoveTv.text)
        outState.putInt(STATE_GAME_MODE, GAME_MODE)
        outState.putInt(STATE_NUMBER_OF_MOVES, numberOfMoves)
        outState.putBoolean(STATE_PLAYER_X_TURN, isPlayerXTurn)
        outState.putInt(STATE_PLAYER_X_SCORE, playerXScore)
        outState.putInt(STATE_PLAYER_O_SCORE, playerOScore)

        // Call to superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState)
    }

    override fun onClick(v: View) {
        enablePlayerToMoveButtons(false)
        when (v.id) {
            R.id.row0_col0 -> setMoveByPlayerAt(0, 0)
            R.id.row0_col1 -> setMoveByPlayerAt(0, 1)
            R.id.row0_col2 -> setMoveByPlayerAt(0, 2)
            R.id.row1_col0 -> setMoveByPlayerAt(1, 0)
            R.id.row1_col1 -> setMoveByPlayerAt(1, 1)
            R.id.row1_col2 -> setMoveByPlayerAt(1, 2)
            R.id.row2_col0 -> setMoveByPlayerAt(2, 0)
            R.id.row2_col1 -> setMoveByPlayerAt(2, 1)
            R.id.row2_col2 -> setMoveByPlayerAt(2, 2)
        }
        if (isThereAWinner) {
            setWinner()
        } else {
            indicatePlayerWithTurn(isPlayerXTurn)
            switchTurn()
            numberOfMoves++
            if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
                gameDraw()
            } else {
                gameOn()
            }
        }
    }

    private fun gameOn() {
        if (TicTacToeUtils.isSinglePlayerMode(GAME_MODE)) {
            if (isPlayerXTurn) {
                computerPlay(Board.PLAYER_X)
            } else {
                computerPlay(Board.PLAYER_O)
            }
        }
    }

    private fun indicatePlayerWithTurn(playerWithTurn: Boolean) {
        if (playerWithTurn) {
            binding.layoutTop.playerToMoveTv.text = getString(R.string.o_move)
        } else {
            binding.layoutTop.playerToMoveTv.text = getString(R.string.x_move)
        }
    }

    private fun enablePlayerToMoveButtons(enableButtons: Boolean) {
        binding.layoutTop.playerXToMove.isEnabled = enableButtons
        binding.layoutTop.playerOToMove.isEnabled = enableButtons
    }

    private fun setGameModeSpinner() {
        binding.layoutTop.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> GAME_MODE = TicTacToeUtils.SINGLE_PLAYER_EASY_MODE
                    1 -> GAME_MODE = TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE
                    2 -> GAME_MODE = TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE
                    3 -> GAME_MODE = TicTacToeUtils.TWO_PLAYER_MODE
                }
                initGame(GAME_MODE)
                resetScoreBoard()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val adapterGameMode = ArrayAdapter.createFromResource(this,
                R.array.level_or_player_type_array, R.layout.spinner_item)
        adapterGameMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.layoutTop.spinner.adapter = adapterGameMode
    }

    /*
     * Get the board value for position (i,j)
     */
    private fun getBoardValue(i: Int, j: Int): String {
        if (i < 0 || i >= BOARD_SIZE) {
            return Board.NOT_PLAYED
        }
        return if (j < 0 || j >= BOARD_SIZE) {
            Board.NOT_PLAYED
        } else board.get(i, j)
    }

    private fun setWinner() {
        enableAllBoxes(false)
        binding.layoutTop.playerToMoveTv.text = getString(R.string.game_over)
        if (isPlayerXTurn) {
            playerXScore++
            binding.layoutTop.playerXScoreboard.text = playerXScore.toString()
            showWinOrDrawDialog(getString(R.string.player_x_wins))
        } else {
            playerOScore++
            binding.layoutTop.playerOScoreboard.text = playerOScore.toString()
            showWinOrDrawDialog(getString(R.string.player_o_wins))
        }
    }

    private fun showWinOrDrawDialog(message: String) {
        val newFragment: DialogFragment = WinOrDrawDialog(message)
        newFragment.show(supportFragmentManager, "WinOrDrawDialog")
    }

    private fun setMoveByPlayerAt(row: Int, column: Int) {
        with(binding.board3x3) {
            if (row == 0 && column == 0) {
                TicTacToeUtils.setTextOnButtonPlayed(isPlayerXTurn, row0Col0.board3x3ButtonO)
                TicTacToeUtils.disableButton(row0Col0.board3x3ButtonO)
            } else if (row == 0 && column == 1) {
                TicTacToeUtils.setTextOnButtonPlayed(isPlayerXTurn, row0Col1.board3x3ButtonO)
                TicTacToeUtils.disableButton(row0Col1.board3x3ButtonO)
            } else if (row == 0 && column == 2) {
                TicTacToeUtils.setTextOnButtonPlayed(isPlayerXTurn, row0Col2.board3x3ButtonO)
                TicTacToeUtils.disableButton(row0Col2.board3x3ButtonO)
            } else if (row == 1 && column == 0) {
                TicTacToeUtils.setTextOnButtonPlayed(isPlayerXTurn, row1Col0.board3x3ButtonO)
                TicTacToeUtils.disableButton(row1Col0.board3x3ButtonO)
            } else if (row == 1 && column == 1) {
                TicTacToeUtils.setTextOnButtonPlayed(isPlayerXTurn, row1Col1.board3x3ButtonO)
                TicTacToeUtils.disableButton(row1Col1.board3x3ButtonO)
            } else if (row == 1 && column == 2) {
                TicTacToeUtils.setTextOnButtonPlayed(isPlayerXTurn, row1Col2.board3x3ButtonO)
                TicTacToeUtils.disableButton(row1Col2.board3x3ButtonO)
            } else if (row == 2 && column == 0) {
                TicTacToeUtils.setTextOnButtonPlayed(isPlayerXTurn, row2Col0.board3x3ButtonO)
                TicTacToeUtils.disableButton(row2Col0.board3x3ButtonO)
            } else if (row == 2 && column == 1) {
                TicTacToeUtils.setTextOnButtonPlayed(isPlayerXTurn, row2Col1.board3x3ButtonO)
                TicTacToeUtils.disableButton(row2Col1.board3x3ButtonO)
            } else if (row == 2 && column == 2) {
                TicTacToeUtils.setTextOnButtonPlayed(isPlayerXTurn, row2Col2.board3x3ButtonO)
                TicTacToeUtils.disableButton(row2Col2.board3x3ButtonO)
            }
        }
        if (isPlayerXTurn) {
            board.set(row, column, Board.PLAYER_X)
        } else {
            board.set(row, column, Board.PLAYER_O)
        }
    }

    private fun computerPlay(playerWithTurn: String) {
        if (GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_EASY_MODE) {
            playRandom()
        } else if (GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE
                || GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            playMediumOrImpossibleMode(playerWithTurn)
        }
        if (isThereAWinner) {
            setWinner()
        } else {
            numberOfMoves++
            if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
                gameDraw()
            } else {
                indicatePlayerWithTurn(isPlayerXTurn)
                switchTurn()
            }
        }
    }

    private fun switchTurn() {
        isPlayerXTurn = !isPlayerXTurn
    }

    private fun gameDraw() {
        binding.layoutTop.playerToMoveTv.text = getString(R.string.game_draw)
        showWinOrDrawDialog(getString(R.string.game_draw))
    }

    private fun playRandom() {
        var iIndex = 0
        var jIndex = 1
        while (!canPlay(iIndex, jIndex)) {
            // Keep trying until a successful move is played
            iIndex = randomNumberForBoardIndex.nextInt(BOARD_SIZE)
            jIndex = randomNumberForBoardIndex.nextInt(BOARD_SIZE)
        }
    }

    private fun playMediumOrImpossibleMode(playerWithTurn: String) {
        var noWinOrBlock = true // Is used so that only one module is executed.
        if (GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            noWinOrBlock = winOrBlockMove(playerWithTurn) // Checking for 2/3 win situation.
            if (!noWinOrBlock) {
                enableAllBoxes(false)
                binding.layoutTop.playerToMoveTv.text = getString(R.string.game_over)
                return
            }
        }
        if ((GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE || GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) && noWinOrBlock) {
            when {
                numberOfMoves == 0 -> {
                    playRandom()
                    return
                }
                numberOfMoves == 1 -> {
                    if (GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
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
                    noWinOrBlock = if (isPlayerXTurn) {
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

    private fun playAnyCornerButton() {
        val i = 0
        val j = 2
        val c = if (randomNumberForBoardIndex.nextBoolean()) i else j
        val d = if (randomNumberForBoardIndex.nextBoolean()) i else j
        setMoveByPlayerAt(c, d)
    }

    private fun winOrBlockMove(playerWithTurn: String): Boolean {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
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
            for (i in 0 until BOARD_SIZE) {
                if (canPlay(i, i)) {
                    return false
                }
            }
        } else if ((board.get(0, 2) + board.get(1, 1) + board.get(2, 0)).contains(playerWithTurn)) {
            var i = 0
            var j = 2
            while (i < BOARD_SIZE) {
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
            setMoveByPlayerAt(row, column)
            true
        } else false
    }

    private fun enableAllBoxes(enableBoxes: Boolean) {
        with(binding.board3x3) {
            row0Col0.board3x3ButtonO.isEnabled = enableBoxes
            row0Col1.board3x3ButtonO.isEnabled = enableBoxes
            row0Col2.board3x3ButtonO.isEnabled = enableBoxes
            row1Col0.board3x3ButtonO.isEnabled = enableBoxes
            row1Col1.board3x3ButtonO.isEnabled = enableBoxes
            row1Col2.board3x3ButtonO.isEnabled = enableBoxes
            row2Col0.board3x3ButtonO.isEnabled = enableBoxes
            row2Col1.board3x3ButtonO.isEnabled = enableBoxes
            row2Col2.board3x3ButtonO.isEnabled = enableBoxes
        }
    }

    private fun resetScoreBoard() {
        playerXScore = 0
        playerOScore = 0
        binding.layoutTop.playerXScoreboard.text = "-"
        binding.layoutTop.playerOScoreboard.text = "-"
    }

    private fun initGame(gameMode: Int) {
        board = Board(BOARD_SIZE)
        isPlayerXTurn = true
        binding.layoutTop.playerXToMove.isSelected
        binding.layoutTop.playerToMoveTv.text = getString(R.string.notice_board)
        enablePlayerToMoveButtons(true)
        GAME_MODE = gameMode
        resetBoard()
    }

    private fun resetBoard() {
        val emptyString = Board.NOT_PLAYED
        with(binding.board3x3) {
            row0Col0.board3x3ButtonO.text = emptyString
            row0Col1.board3x3ButtonO.text = emptyString
            row0Col2.board3x3ButtonO.text = emptyString
            row1Col0.board3x3ButtonO.text = emptyString
            row1Col1.board3x3ButtonO.text = emptyString
            row1Col2.board3x3ButtonO.text = emptyString
            row2Col0.board3x3ButtonO.text = emptyString
            row2Col1.board3x3ButtonO.text = emptyString
            row2Col2.board3x3ButtonO.text = emptyString
        }
        enableAllBoxes(true)
        numberOfMoves = 0
    }

    /*
     * Returns true if the last canPlay was a win
     */
    private val isThereAWinner: Boolean
        get() {
            val token: String = if (isPlayerXTurn) {
                Board.PLAYER_X
            } else {
                Board.PLAYER_O
            }
            val DI = intArrayOf(-1, 0, 1, 1)
            val DJ = intArrayOf(1, 1, 1, 0)
            for (i in 0 until BOARD_SIZE) for (j in 0 until BOARD_SIZE) {

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.how_to_menu -> {
                createHowToWebView()
                true
            }
            R.id.license_menu -> {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_license_title))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createHowToWebView() {
        val webView = WebView(this)
        webView.loadUrl("file:///android_asset/how_to.html")
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.how_to_dialog_title))
                .setView(webView)
                .setCancelable(true)
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val STATE_BOARD = "BOARD"
        const val STATE_PLAYER_X_SCOREBOARD = "PLAYER_X_SCOREBOARD"
        const val STATE_PLAYER_O_SCOREBOARD = "PLAYER_O_SCOREBOARD"
        const val STATE_PLAYER_TO_MOVE_TEXTVIEW = "PLAYER_TO_MOVE_TEXTVIEW"
        const val STATE_GAME_MODE = "GAME_MODE"
        const val STATE_PLAYER_X_TURN = "PLAYER_X_TURN"
        const val STATE_NUMBER_OF_MOVES = "NUMBER_OF_MOVES"
        const val STATE_PLAYER_X_SCORE = "PLAYER_X_SCORE"
        const val STATE_PLAYER_O_SCORE = "PLAYER_O_SCORE"
        const val BOARD_SIZE = 3
    }
}