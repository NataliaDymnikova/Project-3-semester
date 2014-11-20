package com.shagalka.shagalka.shagalka;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/// Controller - connect view and model.
public class ControllerPlay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pagePlay = new PagePlay(this);
// немного временный код.
        pagePlay.setNewPoint(new Point(0, 0));
        pagePlay.setNewPoint(new Point(50,100));
        setContentView(pagePlay);

        // здесь будет инициализация пере енной с информацией.
        // а также получение данных и отправление в pagePlay.setNewPoint(Point).
    }

    /// View. Draw line with points.
    private PagePlay pagePlay;

    // Здесь будет переменная с информацией.
}
