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
import com.zizohanto.android.tictactoe.databinding.ActivityBoard5x5Binding
import com.zizohanto.android.tictactoe.utils.TicTacToeUtils
import java.util.*

/**
 * TicTacToe coordinates for each square on 5x5 Board
 * -----------------------------
 * (0,0) (0,1) (0,2) (0,3) (0,4)
 * (1,0) (1,1) (1,2) (1,3) (1,4)
 * (2,0) (2,1) (2,2) (2,3) (2,4)
 * (3,0) (3,1) (3,2) (3,3) (3,4)
 * (4,0) (4,1) (4,2) (4,3) (4,4)
 * -----------------------------
 */
class Board5x5Activity : AppCompatActivity(), View.OnClickListener {
    var board: Array<IntArray> = Array(BOARD_SIZE) { IntArray(BOARD_SIZE) }
    var GAME_MODE = TicTacToeUtils.SINGLE_PLAYER_EASY_MODE
    private var PLAYER_X_TURN = true
    var randomNumberForBoardIndex = Random()
    private var numberOfMoves = 0
    private var playerXScore = 0
    private var playerOScore = 0
    var oneDimArrayOfBoard: IntArray? = IntArray(BOARD_SIZE * BOARD_SIZE)

    private lateinit var binding: ActivityBoard5x5Binding

    private var userIsInteracting = false

    private val playerXToMoveButtonListener = View.OnClickListener {
        enablePlayerToMoveButtons(false)
        PLAYER_X_TURN = true
        binding.layoutTop.playerToMoveTv.text = getString(R.string.x_move)
        if (TicTacToeUtils.isSinglePlayerMode(GAME_MODE)) {
            computerPlay(TicTacToeUtils.PLAYER_X_PLAYED_VALUE)
        }
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
                if (userIsInteracting) {
                    when (position) {
                        0 -> {
                        }
                        1 -> {
                            val myIntent = Intent(this@Board5x5Activity, MainActivity::class.java)
                            this@Board5x5Activity.startActivity(myIntent)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        // Create an ArrayAdapter using the string array defined and spinner_item.xml
        val adapterBoardSpinner = ArrayAdapter.createFromResource(this,
                R.array.board_size_5x5_array, R.layout.spinner_item)
        // Layout to use when the list of choices appears
        adapterBoardSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding.toolbar.boardSizeSpinner.adapter = adapterBoardSpinner
    }

    private fun setGameModeSpinner() {
        binding.layoutTop.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                if (userIsInteracting) {
                    when (position) {
                        0 -> GAME_MODE = TicTacToeUtils.SINGLE_PLAYER_EASY_MODE
                        1 -> GAME_MODE = TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE
                        2 -> GAME_MODE = TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE
                        3 -> GAME_MODE = TicTacToeUtils.TWO_PLAYER_MODE
                    }
                    initGame(GAME_MODE)
                    resetScoreBoard()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        // Create an ArrayAdapter using the string array defined and spinner_item.xml
        val adapterGameMode = ArrayAdapter.createFromResource(this,
                R.array.level_or_player_type_array, R.layout.spinner_item)
        // Layout to use when the list of choices appears
        adapterGameMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding.layoutTop.spinner.adapter = adapterGameMode
    }

    override fun onClick(v: View) {
        enablePlayerToMoveButtons(false)
        when (v.id) {
            R.id.row0_col0 -> setMoveByPlayerAt(0, 0)
            R.id.row0_col1 -> setMoveByPlayerAt(0, 1)
            R.id.row0_col2 -> setMoveByPlayerAt(0, 2)
            R.id.row0_col3 -> setMoveByPlayerAt(0, 3)
            R.id.row0_col4 -> setMoveByPlayerAt(0, 4)
            R.id.row1_col0 -> setMoveByPlayerAt(1, 0)
            R.id.row1_col1 -> setMoveByPlayerAt(1, 1)
            R.id.row1_col2 -> setMoveByPlayerAt(1, 2)
            R.id.row1_col3 -> setMoveByPlayerAt(1, 3)
            R.id.row1_col4 -> setMoveByPlayerAt(1, 4)
            R.id.row2_col0 -> setMoveByPlayerAt(2, 0)
            R.id.row2_col1 -> setMoveByPlayerAt(2, 1)
            R.id.row2_col2 -> setMoveByPlayerAt(2, 2)
            R.id.row2_col3 -> setMoveByPlayerAt(2, 3)
            R.id.row2_col4 -> setMoveByPlayerAt(2, 4)
            R.id.row3_col0 -> setMoveByPlayerAt(3, 0)
            R.id.row3_col1 -> setMoveByPlayerAt(3, 1)
            R.id.row3_col2 -> setMoveByPlayerAt(3, 2)
            R.id.row3_col3 -> setMoveByPlayerAt(3, 3)
            R.id.row3_col4 -> setMoveByPlayerAt(3, 4)
            R.id.row4_col0 -> setMoveByPlayerAt(4, 0)
            R.id.row4_col1 -> setMoveByPlayerAt(4, 1)
            R.id.row4_col2 -> setMoveByPlayerAt(4, 2)
            R.id.row4_col3 -> setMoveByPlayerAt(4, 3)
            R.id.row4_col4 -> setMoveByPlayerAt(4, 4)
        }
        if (isThereAWinner) {
            setWinner()
        } else {
            indicatePlayerWithTurn(PLAYER_X_TURN)
            switchTurn()
            numberOfMoves++
            if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
                gameDraw()
            } else {
                gameOn()
            }
        }
    }

    private fun switchTurn() {
        PLAYER_X_TURN = !PLAYER_X_TURN
    }

    private fun gameOn() {
        if (TicTacToeUtils.isSinglePlayerMode(GAME_MODE)) {
            if (PLAYER_X_TURN) {
                computerPlay(TicTacToeUtils.PLAYER_X_PLAYED_VALUE)
            } else {
                computerPlay(TicTacToeUtils.PLAYER_O_PLAYED_VALUE)
            }
        }
    }

    private val playerOToMoveButtonListener = View.OnClickListener {
        enablePlayerToMoveButtons(false)
        PLAYER_X_TURN = false
        binding.layoutTop.playerToMoveTv.text = getString(R.string.o_move)
        if (TicTacToeUtils.isSinglePlayerMode(GAME_MODE)) {
            computerPlay(TicTacToeUtils.PLAYER_O_PLAYED_VALUE)
        }
    }

    /* Get the board value for position (i,j) */
    private fun getBoardValue(i: Int, j: Int): Int {
        if (i < 0 || i >= BOARD_SIZE) {
            return TicTacToeUtils.NON_PLAYED_VALUE
        }
        return if (j < 0 || j >= BOARD_SIZE) {
            TicTacToeUtils.NON_PLAYED_VALUE
        } else board[i][j]
    }

    // Skip if the token in board[i][j] is not equal to current token
    private val isThereAWinner: Boolean
        get() {
            val token: Int = if (PLAYER_X_TURN) {
                1
            } else {
                4
            }
            val DI = intArrayOf(-1, 0, 1, 1)
            val DJ = intArrayOf(1, 1, 1, 0)
            for (i in 0 until BOARD_SIZE) for (j in 0 until BOARD_SIZE) {

                // Skip if the token in board[i][j] is not equal to current token
                if (board[i][j] != token) continue
                for (k in 0..3) {
                    var count = 0
                    while (getBoardValue(i + DI[k] * count, j + DJ[k] * count) == token) {
                        count++
                        if (count == 4) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoard5x5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        hideToolbarTitle()
        binding.layoutTop.playerXToMove.isSelected
        with(binding.board5x5) {
            row0Col0.button5x5.setOnClickListener(this@Board5x5Activity)
            row0Col1.button5x5.setOnClickListener(this@Board5x5Activity)
            row0Col2.button5x5.setOnClickListener(this@Board5x5Activity)
            row0Col3.button5x5.setOnClickListener(this@Board5x5Activity)
            row0Col4.button5x5.setOnClickListener(this@Board5x5Activity)
            row1Col0.button5x5.setOnClickListener(this@Board5x5Activity)
            row1Col1.button5x5.setOnClickListener(this@Board5x5Activity)
            row1Col2.button5x5.setOnClickListener(this@Board5x5Activity)
            row1Col3.button5x5.setOnClickListener(this@Board5x5Activity)
            row1Col4.button5x5.setOnClickListener(this@Board5x5Activity)
            row2Col0.button5x5.setOnClickListener(this@Board5x5Activity)
            row2Col1.button5x5.setOnClickListener(this@Board5x5Activity)
            row2Col2.button5x5.setOnClickListener(this@Board5x5Activity)
            row2Col3.button5x5.setOnClickListener(this@Board5x5Activity)
            row2Col4.button5x5.setOnClickListener(this@Board5x5Activity)
            row3Col0.button5x5.setOnClickListener(this@Board5x5Activity)
            row3Col1.button5x5.setOnClickListener(this@Board5x5Activity)
            row3Col2.button5x5.setOnClickListener(this@Board5x5Activity)
            row3Col3.button5x5.setOnClickListener(this@Board5x5Activity)
            row3Col4.button5x5.setOnClickListener(this@Board5x5Activity)
            row4Col0.button5x5.setOnClickListener(this@Board5x5Activity)
            row4Col1.button5x5.setOnClickListener(this@Board5x5Activity)
            row4Col2.button5x5.setOnClickListener(this@Board5x5Activity)
            row4Col3.button5x5.setOnClickListener(this@Board5x5Activity)
            row4Col4.button5x5.setOnClickListener(this@Board5x5Activity)
        }
        binding.layoutTop.playerXToMove.setOnClickListener(playerXToMoveButtonListener)
        binding.layoutTop.playerOToMove.setOnClickListener(playerOToMoveButtonListener)
        binding.resetButton.btnReset.setOnClickListener(resetButtonListener)
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            // Put back values stored in 1D oneDimArrayOfBoard into 2D board
            oneDimArrayOfBoard = savedInstanceState.getIntArray(STATE_BOARD)
            oneDimArrayOfBoard?.let {
                board = TicTacToeUtils.convertBoardToTwoDim(BOARD_SIZE, it)
            }
            PLAYER_X_TURN = savedInstanceState.getBoolean(STATE_PLAYER_X_TURN)
            numberOfMoves = savedInstanceState.getInt(STATE_NUMBER_OF_MOVES)
            playerXScore = savedInstanceState.getInt(STATE_PLAYER_X_SCORE)
            playerOScore = savedInstanceState.getInt(STATE_PLAYER_O_SCORE)
            GAME_MODE = savedInstanceState.getInt(STATE_GAME_MODE)
            binding.layoutTop.playerXScoreboard.text = savedInstanceState.getString(STATE_PLAYER_X_SCOREBOARD)
            binding.layoutTop.playerOScoreboard.text = savedInstanceState.getString(STATE_PLAYER_O_SCOREBOARD)
            binding.layoutTop.playerToMoveTv.text = savedInstanceState.getString(STATE_PLAYER_TO_MOVE_TEXTVIEW)
        }
        setBoardSizeSpinner()
        setGameModeSpinner()
    }

    private fun indicatePlayerWithTurn(playerWithTurn: Boolean) {
        if (playerWithTurn) {
            binding.layoutTop.playerToMoveTv.text = getString(R.string.o_move)
        } else {
            binding.layoutTop.playerToMoveTv.text = getString(R.string.x_move)
        }
    }

    private fun showWinOrDrawDialog(resId: Int) {
        val newFragment: DialogFragment = WinOrDrawDialog(resId)
        newFragment.show(supportFragmentManager, "WinOrDrawDialog")
    }

    private fun setMoveByPlayerAt(row: Int, column: Int) {
        with(binding.board5x5) {
            if (row == 0 && column == 0) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row0Col0.button5x5)
                TicTacToeUtils.disableButton(row0Col0.button5x5)
            } else if (row == 0 && column == 1) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row0Col1.button5x5)
                TicTacToeUtils.disableButton(row0Col1.button5x5)
            } else if (row == 0 && column == 2) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row0Col2.button5x5)
                TicTacToeUtils.disableButton(row0Col2.button5x5)
            } else if (row == 0 && column == 3) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row0Col3.button5x5)
                TicTacToeUtils.disableButton(row0Col3.button5x5)
            } else if (row == 0 && column == 4) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row0Col4.button5x5)
                TicTacToeUtils.disableButton(row0Col4.button5x5)
            } else if (row == 1 && column == 0) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row1Col0.button5x5)
                TicTacToeUtils.disableButton(row1Col0.button5x5)
            } else if (row == 1 && column == 1) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row1Col1.button5x5)
                TicTacToeUtils.disableButton(row1Col1.button5x5)
            } else if (row == 1 && column == 2) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row1Col2.button5x5)
                TicTacToeUtils.disableButton(row1Col2.button5x5)
            } else if (row == 1 && column == 3) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row1Col3.button5x5)
                TicTacToeUtils.disableButton(row1Col3.button5x5)
            } else if (row == 1 && column == 4) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row1Col4.button5x5)
                TicTacToeUtils.disableButton(row1Col4.button5x5)
            } else if (row == 2 && column == 0) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row2Col0.button5x5)
                TicTacToeUtils.disableButton(row2Col0.button5x5)
            } else if (row == 2 && column == 1) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row2Col1.button5x5)
                TicTacToeUtils.disableButton(row2Col1.button5x5)
            } else if (row == 2 && column == 2) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row2Col2.button5x5)
                TicTacToeUtils.disableButton(row2Col2.button5x5)
            } else if (row == 2 && column == 3) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row2Col3.button5x5)
                TicTacToeUtils.disableButton(row2Col3.button5x5)
            } else if (row == 2 && column == 4) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row2Col4.button5x5)
                TicTacToeUtils.disableButton(row2Col4.button5x5)
            } else if (row == 3 && column == 0) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row3Col0.button5x5)
                TicTacToeUtils.disableButton(row3Col0.button5x5)
            } else if (row == 3 && column == 1) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row3Col1.button5x5)
                TicTacToeUtils.disableButton(row3Col1.button5x5)
            } else if (row == 3 && column == 2) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row3Col2.button5x5)
                TicTacToeUtils.disableButton(row3Col2.button5x5)
            } else if (row == 3 && column == 3) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row3Col3.button5x5)
                TicTacToeUtils.disableButton(row3Col3.button5x5)
            } else if (row == 3 && column == 4) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row3Col4.button5x5)
                TicTacToeUtils.disableButton(row3Col4.button5x5)
            } else if (row == 4 && column == 0) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row4Col0.button5x5)
                TicTacToeUtils.disableButton(row4Col0.button5x5)
            } else if (row == 4 && column == 1) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row4Col1.button5x5)
                TicTacToeUtils.disableButton(row4Col1.button5x5)
            } else if (row == 4 && column == 2) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row4Col2.button5x5)
                TicTacToeUtils.disableButton(row4Col2.button5x5)
            } else if (row == 4 && column == 3) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row4Col3.button5x5)
                TicTacToeUtils.disableButton(row4Col3.button5x5)
            } else if (row == 4 && column == 4) {
                TicTacToeUtils.setTextOnButtonPlayed(PLAYER_X_TURN, row4Col4.button5x5)
                TicTacToeUtils.disableButton(row4Col4.button5x5)
            }
        }
        if (PLAYER_X_TURN) {
            board[row][column] = 1
        } else {
            board[row][column] = 4
        }
    }

    private fun computerPlay(playerWithTurnNumber: Int) {
        if (GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_EASY_MODE) {
            playRandom()
        } else if (GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE
                || GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            playMediumOrImpossibleMode(playerWithTurnNumber)
        }
        if (isThereAWinner) {
            setWinner()
        } else {
            numberOfMoves++
            if (numberOfMoves == BOARD_SIZE * BOARD_SIZE) {
                gameDraw()
            } else {
                indicatePlayerWithTurn(PLAYER_X_TURN)
                switchTurn()
            }
        }
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

    private fun playMediumOrImpossibleMode(playerWithTurnNumber: Int) {
        var noWinOrBlockMove = true // Used so that only one module is executed.
        if (GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            noWinOrBlockMove = winOrBlockMove4By4(playerWithTurnNumber) // Checking for 2/3 win situation.
        }
        if ((GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE
                        || GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE)
                && noWinOrBlockMove) {
            if (numberOfMoves == 0) {
                playRandom()
                return
            } else if (numberOfMoves == 1) {
                if (!canPlay(2, 2)) {
                    // If the square at the center is already played, play any of the 4 positions indicated below
                    // (  ) (   ) (   ) (   ) (  )
                    // (  ) (1,1) (   ) (1,3) (  )
                    // (  ) (   ) (   ) (   ) (  )
                    // (  ) (3,1) (   ) (3,3) (  )
                    // (  ) (   ) (   ) (   ) (  )
                    val i = 1
                    val j = 3
                    val c = if (randomNumberForBoardIndex.nextBoolean()) i else j
                    val d = if (randomNumberForBoardIndex.nextBoolean()) i else j
                    setMoveByPlayerAt(c, d)
                } else {
                    setMoveByPlayerAt(2, 2)
                }
                return
            } else if (numberOfMoves > 1) {
                // playerWithTurnNumber: 1 for X and 4 for O
                noWinOrBlockMove = if (PLAYER_X_TURN) {
                    // Checking for situation where loss may occur.
                    winOrBlockMove4By4(4)
                } else {
                    winOrBlockMove4By4(1)
                }
            }
        }
        if (noWinOrBlockMove) {
            playRandom()
        }
    }

    private fun winOrBlockMove4By4(playerWithTurnNumber: Int): Boolean {
        var noWinOrBlock3By4Move = true // Used so that only one module is executed.
        if (GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE
                || GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE) {
            noWinOrBlock3By4Move = winOrBlock3By4Move(playerWithTurnNumber)
            if (GAME_MODE == TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE && noWinOrBlock3By4Move) {
                for (i in 0 until BOARD_SIZE) {
                    for (j in 1 until BOARD_SIZE - 1) {
                        // Checking corresponding mid row for 2/3 situation from top to bottom.
                        // (  ) (0,1) (0,2) (0,3)  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        // (  ) (   ) (   ) (   )  (  )
                        if (board[i][1] + board[i][2] + board[i][3] == playerWithTurnNumber * 2) {
                            if (canPlay(i, j)) {   // Play the move.
                                return false
                            }
                        } else if (board[1][i] + board[2][i] + board[3][i] == playerWithTurnNumber * 2) {
                            if (canPlay(j, i)) {
                                return false
                            }
                        }
                    }
                }
                // Checking left-to-right mid diagonal for 2/3 situation.
                // (  ) (   ) (   ) (  )  (  )
                // (  ) (1,1) (   ) (  ) (  )
                // (  ) (   ) (2,2) (  ) (  )
                // (  ) (   ) (   ) (3,3) (  )
                // (  ) (   ) (   ) (  )  (  )
                if (board[1][1] + board[2][2] + board[3][3] == playerWithTurnNumber * 2) {
                    for (i in 1 until BOARD_SIZE - 1) {
                        if (canPlay(i, i)) {
                            return false
                        }
                    }
                } else if (board[1][3] + board[2][2] + board[3][1] == playerWithTurnNumber * 2) {
                    var i = 1
                    var j = 3
                    while (i < BOARD_SIZE - 1) {
                        if (canPlay(i, j)) {
                            return false
                        }
                        i++
                        j--
                    }
                }
            }
        }
        return noWinOrBlock3By4Move
    }

    private fun winOrBlock3By4Move(playerWithTurnNumber: Int): Boolean {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                // Checking left end of row for 3/4 situation from top to bottom
                // (0,0) (0,1) (0,2) (0,3) (   )
                // (   ) (   ) (   ) (   ) (   )
                // (   ) (   ) (   ) (   ) (   )
                // (   ) (   ) (   ) (   ) (   )
                // (   ) (   ) (   ) (   ) (   )
                if (board[i][0] + board[i][1] + board[i][2] + board[i][3] == playerWithTurnNumber * 3) {
                    if (canPlay(i, j)) {   // Play the move.
                        return false
                    }
                } else if (board[0][j] + board[1][j] + board[2][j] + board[3][j] == playerWithTurnNumber * 3) {
                    if (canPlay(i, j)) {
                        return false
                    }
                }
            }
        }
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                // Checking right end of row for 3/4 situation from top to bottom
                // (  ) (0,1) (0,2) (0,3) (0,4)
                // (  ) (   ) (   ) (   ) (   )
                // (  ) (   ) (   ) (   ) (   )
                // (  ) (   ) (   ) (   ) (   )
                // (  ) (   ) (   ) (   ) (   )
                if (board[i][1] + board[i][2] + board[i][3] + board[i][4] == playerWithTurnNumber * 3) {
                    if (canPlay(i, j + 1)) {   // Play the move.
                        return false
                    }
                }

                // Checking bottom end of column for 3/4 situation from left to right
                // (   ) (  ) (  ) (  ) (  )
                // (1,0) (  ) (  ) (  ) (  )
                // (2,0) (  ) (  ) (  ) (  )
                // (3,0) (  ) (  ) (  ) (  )
                // (4,0) (  ) (  ) (  ) (  )
                if (board[1][j] + board[2][j] + board[3][j] + board[4][j] == playerWithTurnNumber * 3) {
                    if (canPlay(i + 1, j)) {
                        return false
                    }
                }
            }
        }

        // Checking first left-to-right diagonal for 3/4 situation
        // (  ) (0,1) (   ) (   ) (   )
        // (  ) (   ) (1,2) (   ) (   )
        // (  ) (   ) (   ) (2,3) (   )
        // (  ) (   ) (   ) (   ) (3,4)
        // (  ) (   ) (   ) (   ) (   )
        if (board[0][1] + board[1][2] + board[2][3] + board[3][4] == playerWithTurnNumber * 3) {
            var i = 0
            var j = 1
            while (i < BOARD_SIZE - 1) {
                if (canPlay(i, j)) {
                    return false
                }
                i++
                j++
            }
        }

        // Checking top end of second left-to-right diagonal for 3/4 situation
        // (0,0) (   ) (   ) (   ) (  )
        // (   ) (1,1) (   ) (   ) (  )
        // (   ) (   ) (2,2) (   ) (  )
        // (   ) (   ) (   ) (3,3) (  )
        // (   ) (   ) (   ) (   ) (  )
        if (board[0][0] + board[1][1] + board[2][2] + board[3][3] == playerWithTurnNumber * 3) {
            for (i in 0 until BOARD_SIZE - 1) {
                if (canPlay(i, i)) {
                    return false
                }
            }
        }
        // Checking bottom end of second left-to-right diagonal for 3/4 situation
        // (  ) (   ) (   ) (   ) (   )
        // (  ) (1,1) (   ) (   ) (   )
        // (  ) (   ) (2,2) (   ) (   )
        // (  ) (   ) (   ) (3,3) (   )
        //    ) (   ) (   ) (   ) (4,4)
        if (board[1][1] + board[2][2] + board[3][3] + board[4][4] == playerWithTurnNumber * 3) {
            for (i in 1 until BOARD_SIZE) {
                if (canPlay(i, i)) {
                    return false
                }
            }
        }
        // Checking third left-to-right diagonal for 3/4 situation
        // (   ) (   ) (   ) (   ) (  )
        // (1,0) (   ) (   ) (   ) (  )
        // (   ) (2,1) (   ) (   ) (  )
        // (   ) (   ) (3,2) (   ) (  )
        // (   ) (   ) (   ) (4,3) (  )
        if (board[1][0] + board[2][1] + board[3][2] + board[4][3] == playerWithTurnNumber * 3) {
            var i = 1
            var j = 0
            while (i < BOARD_SIZE) {
                if (canPlay(i, j)) {
                    return false
                }
                i++
                j++
            }
        }
        // Checking first right_to_left diagonal for 3/4 situation
        // (   ) (   ) (   ) (0,3) (  )
        // (   ) (   ) (1,2) (   ) (  )
        // (   ) (2,1) (   ) (   ) (  )
        // (3,0) (   ) (   ) (   ) (  )
        // (   ) (   ) (   ) (   ) (  )
        if (board[0][3] + board[1][2] + board[2][1] + board[3][0] == playerWithTurnNumber * 3) {
            var i = 0
            var j = 3
            while (i < BOARD_SIZE - 1) {
                if (canPlay(i, j)) {
                    return false
                }
                i++
                j--
            }
        }
        // Checking top end of second right-to-left diagonal for 3/4 situation
        // (  ) (   ) (   ) (   ) (0,4)
        // (  ) (   ) (   ) (1,3) (   )
        // (  ) (   ) (2,2) (   ) (   )
        // (  ) (3,1) (   ) (   ) (   )
        // (  ) (   ) (   ) (   ) (   )
        if (board[0][4] + board[1][3] + board[2][2] + board[3][1] == playerWithTurnNumber * 3) {
            var i = 0
            var j = 4
            while (i < BOARD_SIZE - 1) {
                if (canPlay(i, j)) {
                    return false
                }
                i++
                j--
            }
        } else if (board[1][3] + board[2][2] + board[3][1] + board[4][0] == playerWithTurnNumber * 3) {
            var i = 1
            var j = 3
            while (i < BOARD_SIZE) {
                if (canPlay(i, j)) {
                    return false
                }
                i++
                j--
            }
        }
        // Checking third right_to_left diagonal for 3/4 situation
        // (  ) (   ) (   ) (   ) (   )
        // (  ) (   ) (   ) (   ) (1,4)
        // (  ) (   ) (   ) (2,3) (   )
        // (  ) (   ) (3,2) (   ) (   )
        // (  ) (4,1) (   ) (   ) (   )
        if (board[1][4] + board[2][3] + board[3][2] + board[4][1] == playerWithTurnNumber * 3) {
            var i = 1
            var j = 4
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
        // If square hasn't been played yet
        return if (board[row][column] == 0) {
            setMoveByPlayerAt(row, column)
            true
        } else false
    }

    private fun enableAllBoxes(b: Boolean) {
        with(binding.board5x5) {
            row0Col0.button5x5.isEnabled = b
            row0Col1.button5x5.isEnabled = b
            row0Col2.button5x5.isEnabled = b
            row0Col3.button5x5.isEnabled = b
            row0Col4.button5x5.isEnabled = b
            row1Col0.button5x5.isEnabled = b
            row1Col1.button5x5.isEnabled = b
            row1Col2.button5x5.isEnabled = b
            row1Col3.button5x5.isEnabled = b
            row1Col4.button5x5.isEnabled = b
            row2Col0.button5x5.isEnabled = b
            row2Col1.button5x5.isEnabled = b
            row2Col2.button5x5.isEnabled = b
            row2Col3.button5x5.isEnabled = b
            row2Col4.button5x5.isEnabled = b
            row3Col0.button5x5.isEnabled = b
            row3Col1.button5x5.isEnabled = b
            row3Col2.button5x5.isEnabled = b
            row3Col3.button5x5.isEnabled = b
            row3Col4.button5x5.isEnabled = b
            row4Col0.button5x5.isEnabled = b
            row4Col1.button5x5.isEnabled = b
            row4Col2.button5x5.isEnabled = b
            row4Col3.button5x5.isEnabled = b
            row4Col4.button5x5.isEnabled = b
        }
    }

    private fun resetScoreBoard() {
        playerXScore = 0
        playerOScore = 0
        binding.layoutTop.playerXScoreboard.text = "-"
        binding.layoutTop.playerOScoreboard.text = "-"
    }

    private val resetButtonListener = View.OnClickListener { initGame(GAME_MODE) }
    private fun setWinner() {
        enableAllBoxes(false)
        binding.layoutTop.playerToMoveTv.text = getString(R.string.game_over)
        if (PLAYER_X_TURN) {
            playerXScore++
            binding.layoutTop.playerXScoreboard.text = playerXScore.toString()
            showWinOrDrawDialog(R.string.player_x_wins)
        } else {
            playerOScore++
            binding.layoutTop.playerOScoreboard.text = playerOScore.toString()
            showWinOrDrawDialog(R.string.player_o_wins)
        }
    }

    private fun enablePlayerToMoveButtons(enableButtons: Boolean) {
        binding.layoutTop.playerXToMove.isEnabled = enableButtons
        binding.layoutTop.playerOToMove.isEnabled = enableButtons
    }

    private fun resetBoard() {
        val emptyString = ""
        with(binding.board5x5) {
            row0Col0.button5x5.text = emptyString
            row0Col1.button5x5.text = emptyString
            row0Col2.button5x5.text = emptyString
            row0Col3.button5x5.text = emptyString
            row0Col4.button5x5.text = emptyString
            row1Col0.button5x5.text = emptyString
            row1Col1.button5x5.text = emptyString
            row1Col2.button5x5.text = emptyString
            row1Col3.button5x5.text = emptyString
            row1Col4.button5x5.text = emptyString
            row2Col0.button5x5.text = emptyString
            row2Col1.button5x5.text = emptyString
            row2Col2.button5x5.text = emptyString
            row2Col3.button5x5.text = emptyString
            row2Col4.button5x5.text = emptyString
            row3Col0.button5x5.text = emptyString
            row3Col1.button5x5.text = emptyString
            row3Col2.button5x5.text = emptyString
            row3Col3.button5x5.text = emptyString
            row3Col4.button5x5.text = emptyString
            row4Col0.button5x5.text = emptyString
            row4Col1.button5x5.text = emptyString
            row4Col2.button5x5.text = emptyString
            row4Col3.button5x5.text = emptyString
            row4Col4.button5x5.text = emptyString
        }
        enableAllBoxes(true)
        numberOfMoves = 0
    }

    private fun gameDraw() {
        binding.layoutTop.playerToMoveTv.text = getString(R.string.game_draw)
        showWinOrDrawDialog(R.string.game_draw)
    }

    private fun initGame(gameMode: Int) {
        board = TicTacToeUtils.initBoardWithZeros(BOARD_SIZE)
        PLAYER_X_TURN = true
        binding.layoutTop.playerXToMove.isSelected
        binding.layoutTop.playerToMoveTv.text = getString(R.string.notice_board)
        enablePlayerToMoveButtons(true)
        GAME_MODE = gameMode
        resetBoard()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        userIsInteracting = true
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        oneDimArrayOfBoard = TicTacToeUtils.convertBoardToOneDim(BOARD_SIZE, board)
        outState.putIntArray(STATE_BOARD, oneDimArrayOfBoard)
        outState.putCharSequence(STATE_PLAYER_X_SCOREBOARD, binding.layoutTop.playerXScoreboard.text)
        outState.putCharSequence(STATE_PLAYER_O_SCOREBOARD, binding.layoutTop.playerOScoreboard.text)
        outState.putCharSequence(STATE_PLAYER_TO_MOVE_TEXTVIEW, binding.layoutTop.playerToMoveTv.text)
        outState.putInt(STATE_GAME_MODE, GAME_MODE)
        outState.putInt(STATE_NUMBER_OF_MOVES, numberOfMoves)
        outState.putBoolean(STATE_PLAYER_X_TURN, PLAYER_X_TURN)
        outState.putInt(STATE_PLAYER_X_SCORE, playerXScore)
        outState.putInt(STATE_PLAYER_O_SCORE, playerOScore)

        // Call to superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState)
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

    override fun onBackPressed() {
        // Do nothing
    }

    companion object {
        // Keys to identify the data saved
        const val STATE_BOARD = "BOARD"
        const val STATE_PLAYER_X_SCOREBOARD = "PLAYER_X_SCOREBOARD"
        const val STATE_PLAYER_O_SCOREBOARD = "PLAYER_O_SCOREBOARD"
        const val STATE_PLAYER_TO_MOVE_TEXTVIEW = "PLAYER_TO_MOVE_TEXTVIEW"
        const val STATE_GAME_MODE = "GAME_MODE"
        const val STATE_PLAYER_X_TURN = "PLAYER_X_TURN"
        const val STATE_NUMBER_OF_MOVES = "NUMBER_OF_MOVES"
        const val STATE_PLAYER_X_SCORE = "PLAYER_X_SCORE"
        const val STATE_PLAYER_O_SCORE = "PLAYER_O_SCORE"
        const val BOARD_SIZE = 5
    }
}