package com.shagalka.shagalka.shagalka;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

/// Main class with main view.
/// This class is only about view and has one button - Play.
public class ViewPlay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        buttonPlay = (Button) findViewById(R.id.buttonPlay);
    }

    /// It works when button was clicked.
    /// Call ControllerPlay.
    public void onClickPlay(android.view.View view) {
        Intent intent = new Intent(this, ControllerPlay.class);
        startActivity(intent);
    }

    private Button buttonPlay;
}
