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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.zizohanto.android.tictactoe.databinding.ActivityBoard3x3Binding
import com.zizohanto.android.tictactoe.utils.TicTacToeUtils

/**
 * TicTacToe coordinates for each square on 3x3 Board
 * -----------------
 * (0,0) (0,1) (0,2)
 * (1,0) (1,1) (1,2)
 * (2,0) (2,1) (2,2)
 * -----------------
 */
class MainActivity : AppCompatActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: ActivityBoard3x3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoard3x3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        hideToolbarTitle()
        if (savedInstanceState != null) {
            with(viewModel) {
                val oneDimArrayBoard = savedInstanceState.getStringArray(STATE_BOARD)
                oneDimArrayBoard?.let {
                    convertBoardToTwoDim(it)
                }
                setIsPlayerXTurn(savedInstanceState.getBoolean(STATE_PLAYER_X_TURN))
                setNumberOfMoves(savedInstanceState.getInt(STATE_NUMBER_OF_MOVES))
                setPlayerXScore(savedInstanceState.getInt(STATE_PLAYER_X_SCORE))
                setPlayerOScore(savedInstanceState.getInt(STATE_PLAYER_O_SCORE))
                setGameMode(savedInstanceState.getInt(STATE_GAME_MODE))
            }
            with(binding.layoutTop) {
                playerXScoreboard.text = savedInstanceState.getString(STATE_PLAYER_X_SCOREBOARD)
                playerOScoreboard.text = savedInstanceState.getString(STATE_PLAYER_O_SCOREBOARD)
                playerToMoveTv.text = savedInstanceState.getString(STATE_PLAYER_TO_MOVE_TEXTVIEW)
            }
        }

        with(viewModel) {
            val mainActivity = this@MainActivity
            viewStates.observe(mainActivity, ::setViewStates)
            gameDraw.observe(mainActivity, ::gameDraw)
        }
        with(binding.layoutTop) {
            playerXToMove.setOnClickListener {
                enablePlayerToMoveButtons(false)
                viewModel.setIsPlayerXTurn(true)
                if (TicTacToeUtils.isSinglePlayerMode(viewModel.getGameMode())) {
                    viewModel.computerPlay(Board.PLAYER_X)
                }
            }
            playerOToMove.setOnClickListener {
                enablePlayerToMoveButtons(false)
                viewModel.setIsPlayerXTurn(false)
                if (TicTacToeUtils.isSinglePlayerMode(viewModel.getGameMode())) {
                    viewModel.computerPlay(Board.PLAYER_O)
                }
            }
        }
        binding.resetButton.btnReset.setOnClickListener { initGame(viewModel.getGameMode()) }
    }

    private fun setViewStates(viewStates: ViewStates) {
        when (viewStates) {
            is ViewStates.Idle -> {
                setBoardSizeSpinner()
                setGameModeSpinner()
                binding.layoutTop.playerXToMove.isSelected
                binding.toolbar.root.isVisible = true
                binding.layoutTop.root.isVisible = true
                binding.board3x3.root.isVisible = true
                binding.resetButton.root.isVisible = true
                enablePlayerToMoveButtons(true)
                with(binding.board3x3) {
                    row0Col0.board3x3ButtonO.setOnClickListener { viewModel.playMoveByPlayerAt(0, 0) }
                    row0Col1.board3x3ButtonO.setOnClickListener { viewModel.playMoveByPlayerAt(0, 1) }
                    row0Col2.board3x3ButtonO.setOnClickListener { viewModel.playMoveByPlayerAt(0, 2) }
                    row1Col0.board3x3ButtonO.setOnClickListener { viewModel.playMoveByPlayerAt(1, 0) }
                    row1Col1.board3x3ButtonO.setOnClickListener { viewModel.playMoveByPlayerAt(1, 1) }
                    row1Col2.board3x3ButtonO.setOnClickListener { viewModel.playMoveByPlayerAt(1, 2) }
                    row2Col0.board3x3ButtonO.setOnClickListener { viewModel.playMoveByPlayerAt(2, 0) }
                    row2Col1.board3x3ButtonO.setOnClickListener { viewModel.playMoveByPlayerAt(2, 1) }
                    row2Col2.board3x3ButtonO.setOnClickListener { viewModel.playMoveByPlayerAt(2, 2) }
                }
            }
            is ViewStates.Started -> {
                enablePlayerToMoveButtons(false)
                binding.layoutTop.playerXScoreboard.text = viewStates.playerXScore
                binding.layoutTop.playerOScoreboard.text = viewStates.playerOScore
                binding.layoutTop.playerToMoveTv.setText(viewStates.playerWithTurn)
                for (row in 0 until viewStates.boardSize) {
                    for (column in 0 until viewStates.boardSize) {
                        val buttonText = viewStates.board.get(row, column)
                        showMoveByPlayerAt(row, column, buttonText)
                    }
                }
            }
            is ViewStates.GameOver -> {
                enablePlayerToMoveButtons(false)
                enableAllBoxes(false)
                binding.layoutTop.playerXScoreboard.text = viewStates.playerXScore
                binding.layoutTop.playerOScoreboard.text = viewStates.playerOScore
                binding.layoutTop.playerToMoveTv.setText(viewStates.playerWithTurn)
                showGameStatusDialog(viewStates.winnerMessage)
            }
            is ViewStates.GameDraw -> TODO()
            is ViewStates.ViewHowTo -> TODO()
            is ViewStates.ViewLicenses -> TODO()
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar.toolbar)
    }

    private fun hideToolbarTitle() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
        binding.toolbar.boardSizeSpinner.adapter = ArrayAdapter.createFromResource(this,
                R.array.board_size_3x3_array, R.layout.spinner_item).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArray(STATE_BOARD, viewModel.getOneDimBoard())
        outState.putCharSequence(STATE_PLAYER_X_SCOREBOARD, binding.layoutTop.playerXScoreboard.text)
        outState.putCharSequence(STATE_PLAYER_O_SCOREBOARD, binding.layoutTop.playerOScoreboard.text)
        outState.putCharSequence(STATE_PLAYER_TO_MOVE_TEXTVIEW, binding.layoutTop.playerToMoveTv.text)
        outState.putInt(STATE_GAME_MODE, viewModel.getGameMode())
        outState.putInt(STATE_NUMBER_OF_MOVES, viewModel.getNumberOfMoves())
        outState.putBoolean(STATE_PLAYER_X_TURN, viewModel.isPlayerXTurn())
        outState.putInt(STATE_PLAYER_X_SCORE, viewModel.getPlayerXScore())
        outState.putInt(STATE_PLAYER_O_SCORE, viewModel.getPlayerOScore())
        super.onSaveInstanceState(outState)
    }

    private fun enablePlayerToMoveButtons(enableButtons: Boolean) {
        binding.layoutTop.playerXToMove.isEnabled = enableButtons
        binding.layoutTop.playerOToMove.isEnabled = enableButtons
    }

    private fun setGameModeSpinner() {
        binding.layoutTop.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> viewModel.setGameMode(TicTacToeUtils.SINGLE_PLAYER_EASY_MODE)
                    1 -> viewModel.setGameMode(TicTacToeUtils.SINGLE_PLAYER_MEDIUM_MODE)
                    2 -> viewModel.setGameMode(TicTacToeUtils.SINGLE_PLAYER_IMPOSSIBLE_MODE)
                    3 -> viewModel.setGameMode(TicTacToeUtils.TWO_PLAYER_MODE)
                }
                initGame(viewModel.getGameMode())
                resetScoreBoard()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.layoutTop.spinner.adapter = ArrayAdapter.createFromResource(this,
                R.array.level_or_player_type_array, R.layout.spinner_item).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun showGameStatusDialog(resId: Int) {
        val newFragment: DialogFragment = WinOrDrawDialog(resId)
        newFragment.show(supportFragmentManager, "WinOrDrawDialog")
    }

    private fun showMoveByPlayerAt(row: Int, column: Int, buttonText: String) {
        with(binding.board3x3) {
            if (row == 0 && column == 0) {
                with(row0Col0.board3x3ButtonO) {
                    text = buttonText
                    isEnabled = buttonText.isEmpty()
                }
            } else if (row == 0 && column == 1) {
                with(row0Col1.board3x3ButtonO) {
                    text = buttonText
                    isEnabled = buttonText.isEmpty()
                }
            } else if (row == 0 && column == 2) {
                with(row0Col2.board3x3ButtonO) {
                    text = buttonText
                    isEnabled = buttonText.isEmpty()
                }
            } else if (row == 1 && column == 0) {
                with(row1Col0.board3x3ButtonO) {
                    text = buttonText
                    isEnabled = buttonText.isEmpty()
                }
            } else if (row == 1 && column == 1) {
                with(row1Col1.board3x3ButtonO) {
                    text = buttonText
                    isEnabled = buttonText.isEmpty()
                }
            } else if (row == 1 && column == 2) {
                with(row1Col2.board3x3ButtonO) {
                    text = buttonText
                    isEnabled = buttonText.isEmpty()
                }
            } else if (row == 2 && column == 0) {
                with(row2Col0.board3x3ButtonO) {
                    text = buttonText
                    isEnabled = buttonText.isEmpty()
                }
            } else if (row == 2 && column == 1) {
                with(row2Col1.board3x3ButtonO) {
                    text = buttonText
                    isEnabled = buttonText.isEmpty()
                }
            } else {
                with(row2Col2.board3x3ButtonO) {
                    text = buttonText
                    isEnabled = buttonText.isEmpty()
                }
            }
        }
    }

    private fun gameDraw(resId: Int) {
        binding.layoutTop.playerToMoveTv.setText(resId)
        showGameStatusDialog(resId)
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
        viewModel.setPlayerXScore(0)
        viewModel.setPlayerOScore(0)
        binding.layoutTop.playerXScoreboard.text = "-"
        binding.layoutTop.playerOScoreboard.text = "-"
    }

    private fun initGame(gameMode: Int) {
        viewModel.initGame(gameMode)
        binding.layoutTop.playerXToMove.isSelected = true
        binding.layoutTop.playerToMoveTv.text = getString(R.string.notice_board)
        enablePlayerToMoveButtons(true)
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
        viewModel.setNumberOfMoves(0)
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
        menuInflater.inflate(R.menu.menu, menu)
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