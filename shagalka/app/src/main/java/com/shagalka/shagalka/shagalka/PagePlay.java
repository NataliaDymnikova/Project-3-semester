package com.shagalka.shagalka.shagalka;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/// Class with view information. Draw lines.
public class PagePlay extends android.view.View {

    public PagePlay(Context context) {
        super(context);
        lines = new ArrayList<Point>();
        num = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < num - 1; i++) {
            canvas.drawLine(canvas.getWidth() / 2 + lines.get(i).x
                    , canvas.getHeight() / 2 + lines.get(i).y
                    , canvas.getWidth() / 2 + lines.get(i + 1).x
                    , canvas.getHeight() / 2 + lines.get(i + 1).y
                    , new Paint(Color.BLACK));
        }
    }

    /// Set new point to line.
    public void setNewPoint(Point point)
    {
        lines.add(num, point);
        num++;
        invalidate();
    }

    /// List of line's points.
    private List<Point> lines;
    /// Number of points.
    private int num;
}
