package com.example.android.tictactoe;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

@SuppressWarnings("RedundantCast")
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private boolean userIsInteracting;
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ViewPager viewPager = (ViewPager) findViewById(R.id.board_viewpager);
        viewPager.setAdapter(new BoardTypeFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.board_tab);
        tabLayout.setupWithViewPager(viewPager);

        Spinner spinnerBoard3x3 = (Spinner) findViewById(R.id.board_size_spinner);
        spinnerBoard3x3.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner spinner_dropdown_item
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.board_size_3x3_array, R.layout.spinner_item);
        // Layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBoard3x3.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Call to superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    /*
    * Method to handle spinner selection
    */
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (userIsInteracting) {
            switch (position) {
                case 0:
                    // 3x3 Board is clicked
                    //initGame(GAME_MODE);
                    //resetScoreBoard();
                    break;
                case 1:
                    // Medium is clicked
                    //initGame(GAME_MODE);
                    //resetScoreBoard();
                    break;
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
