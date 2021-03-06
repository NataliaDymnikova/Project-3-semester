package com.shagalka.shagalka.shagalka2;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

/// This class is responsible for multiple touch.
/// Calculates translate, angle, scale.
public class MultyTouch implements View.OnTouchListener {

    public MultyTouch(PagePlay page) {
        pagePlay = page;
    }

    /// Handling touches and motions.
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // событие
        int actionMask = event.getActionMasked();
        // число касаний
        int pointerCount = event.getPointerCount();

        switch (actionMask) {
            case MotionEvent.ACTION_DOWN: // первое касание
                beginX[0] = event.getX(0);
                beginY[0] = event.getY(0);
            case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
                if (pointerCount == 2){
                    beginX[1] = event.getX(1);
                    beginY[1] = event.getY(1);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                beginX = endX.clone();
                beginY = endY.clone();
                break;

            case MotionEvent.ACTION_MOVE: // движение
                if (pointerCount <= 2) {
                    endX[0] = event.getX(0);
                    endY[0] = event.getY(0);
                    // if 1 touch.
                    if (pointerCount == 1) {
                        Point temp = Translate(beginX[0], beginY[0], endX[0], endY[0]);
                        translate.set(translate.x + temp.x, translate.y + temp.y);
                    }
                    // if multy touch.
                     else if (pointerCount == 2) {
                        endX[1] = event.getX(1);
                        endY[1] = event.getY(1);
                        float tempAngle = Rotate(beginX, beginY, endX, endY);
                        if (tempAngle > 90)
                            tempAngle = -tempAngle;
                        dAngle += tempAngle;
                        dScale *= Scale(beginX, beginY, endX, endY);
                    }
                }

               UpdateListener();

                break;
        }

        return true;
    }

    /// Return angle.
    public float Angle() {
        if (dAngle > 360)
            dAngle -= (360 * (int)(dAngle / 360 + 1));
        if (dAngle < 0)
            dAngle += (360 * (int)(dAngle / 360 + 1));
        return dAngle;
    }

    /// Return scale between 0.1 and 11.0.
    public float Scale() {
        if (dScale < (float)0.4)
            dScale = (float)0.4;
        if (dScale >= (float)11.0)
            dScale = (float)11.0;

        return dScale;
    }

    /// Return vector of translate.
    public Point Translate() {
        return translate;
    }

    /// Set all settings to zero.
    public void setToZero() {
        translate.set(0,0);
        dAngle = 0;
        dScale = 1;
        beginX[0] = beginX[1] = 0;
        beginY[0] = beginY[1] = 0;
        endX[0] = endX[1] = 0;
        endY[0] = endY[1] = 0;
        UpdateListener();
    }

    public void SetTranslate(Point point) {
        translate.set(point.x, point.y);
        dAngle = 0;
        dScale = 1;
        beginX[0] = beginX[1] = point.x;
        beginY[0] = beginY[1] = point.x;
        endX[0] = endX[1] = point.x;
        endY[0] = endY[1] = point.y;
        UpdateListener();
    }

    // Update listeners.
    private void UpdateListener() {
        pagePlay.setChangeTranslate(Translate());
        pagePlay.setChangeCorner(Angle());
        pagePlay.setChangeScale(Scale());
    }

    // Calculate translate between two points.
    private Point Translate(float x0, float y0, float x1, float y1) {
        return new Point((int)(x1 - x0), (int)(y1 - y0));
    }

    // Calculate angle between 2 vectors with begins x0, y0 and endings x1, y1.
    private float Rotate(float[] x0, float[] y0, float[] x1, float[] y1) {

        float distance0 = Distance(x0[0], y0[0], x0[1], y0[1]);
        float distance1 = Distance(x1[0], y1[0], x1[1], y1[1]);
        float temp;

        if (distance0 == 0)
            temp = distance1;
        else if (distance1 == 0)
            temp = distance0;
        else
            temp = distance0 * distance1;

        if (temp == 0)
            return 0;

        float result = ((x0[1] - x0[0]) * (x1[1] - x1[0])
                + (y0[1] - y0[0]) *  (y1[1] - y1[0])) / temp;
        if (result >= -1 && result <= 1) {
            return (float) Math.toDegrees(Math.acos(result));
        }
        return 0;
    }

    // Calculate scale.
    private float Scale(float[] x0, float[] y0, float[] x1, float[] y1)
    {
        float temp = Distance(x0[0], y0[0], x0[1], y0[1]);
        if (temp == 0)
            return 1;

        return  Distance(x1[0], y1[0], x1[1], y1[1]) / temp;
    }

    // Calculate distance between 2 points.
    private float Distance(float x0, float y0, float x1, float y1) {
        return (float)Math.sqrt(Square(x1 - x0) + Square(y1 - y0));
    }

    // Calculate square.
    private float Square(float x) {
        return x * x;
    }

    private Point translate = new Point();
    private float dScale = 1;
    private float dAngle = 0;

    private float[] beginX = new float[2];
    private float[] beginY = new float[2];
    private float[] endX = new float[2];
    private float[] endY = new float[2];

    // Listener.
    private PagePlay pagePlay;
}