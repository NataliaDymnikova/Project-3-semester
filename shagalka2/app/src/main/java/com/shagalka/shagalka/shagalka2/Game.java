package com.shagalka.shagalka.shagalka2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.Timer;

/// Main class. Open begin page with button Go and play page.
public class Game extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirstPage();
    }

    private void FirstPage() {
        setContentView(R.layout.activite_first_page);
        buttonGo = (Button) findViewById(R.id.buttonGo);
    }

    // if button Go was clicked.
    public void onClickPlay(android.view.View view) {
        setContentView(R.layout.activity_game);

        pagePlay = new PagePlay(this);
        setContentView(pagePlay);

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

    /// View. Draw line with points.
    private PagePlay pagePlay;
    /// Model of app.
    private ModelShagalka model;
    /// button "Go" for start.
    private Button buttonGo;
}
