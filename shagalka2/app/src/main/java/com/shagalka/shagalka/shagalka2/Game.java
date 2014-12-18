package com.shagalka.shagalka.shagalka2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Timer;

/// Main class. Open begin page with button Go and play page.
public class Game extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirstPage();
    }

    // if button Go was clicked.
    public void onClickPlay(android.view.View view) {
        setContentView(R.layout.activity_game);
        PlayPage();
    }

    public void onClickBeginState(android.view.View view) {
        pagePlay.SetToZeroChange();
    }

    public void onClickYourPlace(android.view.View view) {
        pagePlay.ToLastPlace();
    }

   public void onCLickClean(android.view.View view) {
       gameLayout.removeAllViews();
       pagePlay = new PagePlay(this);
       gameLayout.addView(pagePlay);

       model = new ModelShagalka(this, pagePlay);
       model.onResume();
   }

    private void FirstPage() {
        setContentView(R.layout.activite_first_page);
        buttonGo = (Button) findViewById(R.id.buttonGo);
        buttonGo.setBackgroundColor(Color.argb(200, 140, 5, 185));
    }

    private void PlayPage() {

        buttonBeginState = (Button) findViewById(R.id.buttonStateBegin);
        buttonToTourPlace = (Button) findViewById(R.id.buttonToYourPlace);
        buttonClean = (Button) findViewById(R.id.buttonClean);
        buttonBeginState.setBackgroundColor(Color.argb(170, 10, 50, 180));
        buttonToTourPlace.setBackgroundColor(Color.argb(170, 10, 50, 200));
        buttonClean.setBackgroundColor(Color.argb(170, 10, 50, 190));

        pagePlay = new PagePlay(this);
        gameLayout = (FrameLayout)findViewById(R.id.viewLayout);
        gameLayout.addView(pagePlay);

        model = new ModelShagalka(this, pagePlay);
        model.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // View. Draw line with points.
    private PagePlay pagePlay;
    // Model of app.
    private ModelShagalka model;
    // button "Go" for start.
    private Button buttonGo;
    // Button on play layout for setting to zero translate, corer and scale.
    private Button buttonBeginState;
    //
    private Button buttonToTourPlace;
    //
    private Button buttonClean;
    // Play layout.
    private FrameLayout gameLayout;
}
