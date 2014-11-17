package com.shagalka.shagalka.shagalka;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/// This class draw lines.
public class PlayPage extends View {

    public PlayPage(Context context) {
        super(context);
        lines = new ArrayList<Point>();
        num = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (num == 0) {
            lines.add(new Point(0, 0));
            num++;
            lines.add(new Point(canvas.getWidth() / 2, canvas.getHeight() / 2));
            num++;
        }

        //canvas.drawLine(0, 0, 100, 100, new Paint(Color.BLACK));
        for (int i = 0; i < num - 1; i++) {
            canvas.drawLine(lines.get(i).x, lines.get(i).y, lines.get(i + 1).x, lines.get(i + 1).y
                    , new Paint(Color.BLACK));
        }
    }

    public void stepDraw(int endX, int endY) {
        lines.set(num, new Point(endX + num, endY));
        num++;
        invalidate();
    }

    private List<Point> lines;
    private int num;
}
