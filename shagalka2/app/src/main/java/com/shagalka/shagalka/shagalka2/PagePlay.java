package com.shagalka.shagalka.shagalka2;

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
        corner = -90;
        scale = 1;
        translate = new Point(0,0);
        multyTouch = new MultyTouch(this);
        this.setOnTouchListener(multyTouch);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (num > 0)
            canvas.drawText("translate" + lines.get(num - 1).x + "." + lines.get(num - 1).y
                    , 10, 10, new Paint(Color.BLACK));
        canvas.drawText("translate: " + translate.x + "." + translate.y
                , 10, 30, new Paint(Color.BLACK));
        canvas.drawText("corner: " + corner, 10, 50, new Paint(Color.BLACK));
        canvas.drawText("scale: " + scale, 10, 70, new Paint(Color.BLACK));

        canvas.translate(translate.x, translate.y);
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.rotate(corner);
        canvas.scale(scale, scale, 0, 0);

        for (int i = 0; i < num - 1; i++) {
            canvas.drawLine(lines.get(i).x, lines.get(i).y
                    , lines.get(i + 1).x, lines.get(i + 1).y, new Paint(Color.BLACK));
        }
    }

    /// Set new point to line.
    public void setNewPoint(Point point) {
        lines.add(num, point);
        num++;
        invalidate();
    }

    /// Set change of corner in degrees.
    public void setChangeCorner(float angle) {
        corner = angle;
    }

    /// Set change of scale.
    public void setChangeScale(float scale) {
        this.scale = scale;
    }

    /// Set change of translate.
    public void setChangeTranslate(Point translate) {
        this.translate = translate;
    }

    /// Set translate, corner, scale to begin.
    public void SetToZeroChange() {
        multyTouch.setToZero();
    }

    /// List of line's points.
    private List<Point> lines;
    /// Number of points.
    private int num;
    /// Corner to rotate canvas.
    private float corner;
    /// Translate canvas.
    private Point translate;
    /// Scale canvas.
    private float scale;
    /// Multy touch - translate, scale, rotate.
    private MultyTouch multyTouch;
}
