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
        corner = 0;
        scale = 1;
        translate = new Point(0,0);
        multyTouch = new MultyTouch(this);
        this.setOnTouchListener(multyTouch);
        coefTrans = 9;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        if (num > 0)
            canvas.drawText("Ваше положение: " + lines.get(num - 1).x + "," + lines.get(num - 1).y
                    , 10, 30, paint);
        canvas.drawText("Масштаб: " + scale, 10, 60, paint);
        canvas.drawText("Шаги: " + steps, 10, 90, paint);

        float translateX = (float)(translate.x * Math.cos(corner) + translate.y * Math.sin(corner));
        float translateY = (float)(-translate.x * Math.sin(corner) + translate.y * Math.cos(corner));

        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.translate(translate.x, translate.y);
        canvas.rotate(corner);
        canvas.scale(scale, scale, 0, 0);

        // Сетка.
        int width = 100;
        int beginX = (int)((-canvas.getWidth() * 2 - translateX) / scale);
        int beginY = (int)((-canvas.getHeight() * 2 - translateY) / scale);
        int endX = (int)((canvas.getWidth() * 2 - translateX) / scale);
        int endY = (int)((canvas.getHeight() * 2 - translateY) / scale);
        beginX -= beginX % width;
        beginY -= beginY % width;
        endX -= endX % width;
        endY -= endY % width;
        for (int i = beginX - width; i <= endX + width; i += width)
            for (int j = beginY - width; j <= endY + width; j +=width) {
                canvas.drawLine(i, beginY - width, i, endY + width, paint);
                canvas.drawLine(beginX - width, j, endX + width, j, paint);
            }

        paint.setStrokeWidth(3);

        for (int i = 0; i < num - 1; i++) {
            canvas.drawLine(lines.get(i).x, lines.get(i).y
                    , lines.get(i + 1).x, lines.get(i + 1).y, paint);
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
        this.translate.set(translate.x / (int)coefTrans, translate.y / (int)coefTrans);
    }

    /// Set translate, corner, scale to begin.
    public void SetToZeroChange() {
        multyTouch.setToZero();
    }

    public void ToLastPlace() {
        if (num == 0)
            multyTouch.setToZero();
        Point temp = lines.get(num - 1);
        multyTouch.SetTranslate(new Point(-temp.x * (int)coefTrans, -temp.y * (int)coefTrans));
    }

    public void setNumberOfSteps(int steps) {
      this.steps = steps;
    }

    // List of line's points.
    private List<Point> lines;
    // Number of points.
    private int num;
    // Corner to rotate canvas.
    private float corner;
    // Translate canvas.
    private Point translate;
    // Scale canvas.
    private float scale;
    // Multy touch - translate, scale, rotate.
    private MultyTouch multyTouch;
    // Coefficient to translate, scale and rotate;
    private float coefTrans;
    // Number os steps.
    private int steps;
}
