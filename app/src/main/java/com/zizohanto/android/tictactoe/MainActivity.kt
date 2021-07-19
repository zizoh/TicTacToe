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
class MainActivity : AppCompatActivity(), View.OnClickListener {

    val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: ActivityBoard3x3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoard3x3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        hideToolbarTitle()
        binding.layoutTop.playerXToMove.isSelected
        val mainActivity = this@MainActivity
        with(binding.board3x3) {
            row0Col0.board3x3ButtonO.setOnClickListener(mainActivity)
            row0Col1.board3x3ButtonO.setOnClickListener(mainActivity)
            row0Col2.board3x3ButtonO.setOnClickListener(mainActivity)
            row1Col0.board3x3ButtonO.setOnClickListener(mainActivity)
            row1Col1.board3x3ButtonO.setOnClickListener(mainActivity)
            row1Col2.board3x3ButtonO.setOnClickListener(mainActivity)
            row2Col0.board3x3ButtonO.setOnClickListener(mainActivity)
            row2Col1.board3x3ButtonO.setOnClickListener(mainActivity)
            row2Col2.board3x3ButtonO.setOnClickListener(mainActivity)
        }
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
            playAt.observe(mainActivity) { showMoveByPlayerAt(it.first, it.second) }
            enableAllBoxes.observe(mainActivity, ::enableAllBoxes)
            indicatePlayerWithTurn.observe(mainActivity, ::indicatePlayerWithTurn)
            gameOver.observe(mainActivity, ::gameOver)
            playerToMoveText.observe(mainActivity, ::showPlayerToMoveText)
            gameDraw.observe(mainActivity, ::gameDraw)
            playerXScoreString.observe(mainActivity, ::playerXScore)
            playerOScoreString.observe(mainActivity, ::playerOScore)
            showDialog.observe(mainActivity, ::showGameStatusDialog)
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
        setBoardSizeSpinner()
        setGameModeSpinner()
    }

    private fun playerXScore(score: String) {
        binding.layoutTop.playerXScoreboard.text = score
    }

    private fun playerOScore(score: String) {
        binding.layoutTop.playerOScoreboard.text = score
    }

    private fun showPlayerToMoveText(resId: Int) {
        binding.layoutTop.playerToMoveTv.setText(resId)
    }

    private fun gameOver(resId: Int) {
        binding.layoutTop.playerToMoveTv.setText(resId)
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

    override fun onClick(v: View) {
        enablePlayerToMoveButtons(false)
        when (v.id) {
            R.id.row0_col0 -> showMoveByPlayerAt(0, 0)
            R.id.row0_col1 -> showMoveByPlayerAt(0, 1)
            R.id.row0_col2 -> showMoveByPlayerAt(0, 2)
            R.id.row1_col0 -> showMoveByPlayerAt(1, 0)
            R.id.row1_col1 -> showMoveByPlayerAt(1, 1)
            R.id.row1_col2 -> showMoveByPlayerAt(1, 2)
            R.id.row2_col0 -> showMoveByPlayerAt(2, 0)
            R.id.row2_col1 -> showMoveByPlayerAt(2, 1)
            R.id.row2_col2 -> showMoveByPlayerAt(2, 2)
        }
        viewModel.checkMove()
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

    private fun showMoveByPlayerAt(row: Int, column: Int) {
        val isPlayerXTurn = viewModel.isPlayerXTurn()
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
        viewModel.setMoveByPlayerAt(row, column)
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