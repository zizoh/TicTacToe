package com.example.android.tictactoe.board3x3;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.tictactoe.R;
import com.example.android.tictactoe.utils.ActivityUtils;

public class Board3x3Activity extends AppCompatActivity {

    private static final String TAG = "Board3x3Activity";

    private Board3x3Presenter mBoard3x3Presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.board3x3_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Board3x3Fragment board3x3Fragment =
                (Board3x3Fragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (board3x3Fragment == null) {
            // Create the fragment
            board3x3Fragment = Board3x3Fragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), board3x3Fragment, R.id.content_frame);
        }

        // Create the presenter
        mBoard3x3Presenter = new Board3x3Presenter(board3x3Fragment);
    }
}
