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
        // индекс касания
        int pointerIndex = event.getActionIndex();
        // число касаний
        int pointerCount = event.getPointerCount();

        switch (actionMask) {
            case MotionEvent.ACTION_DOWN: // первое касание
                beginX[0] = event.getX(event.getPointerId(0));
                beginY[0] = event.getY(event.getPointerId(0));
            case MotionEvent.ACTION_POINTER_DOWN: // последующие касания
                if (pointerCount == 2){
                    beginX[1] = event.getX(event.getPointerId(1));
                    beginY[1] = event.getY(event.getPointerId(1));
                }
                break;

            case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                beginX = endX.clone();
                beginY = endY.clone();
                return false;

            case MotionEvent.ACTION_MOVE: // движение
                if (pointerCount <= 2) {
                    endX[0] = event.getX(event.getPointerId(0));
                    endY[0] = event.getY(event.getPointerId(0));
                    // if 1 touch.
                    if (pointerCount == 1) {
                        Point temp = Translate(beginX[0], beginY[0], endX[0], endY[0]);
                        translate.set(translate.x + temp.x / coeffTranslate
                                , translate.y + temp.y / coeffTranslate);
                    }
                    // if multy touch.
                    if (pointerCount == 2) {
                        endX[1] = event.getX(event.getPointerId(1));
                        endY[1] = event.getY(event.getPointerId(1));
                        dAngle += Rotate(beginX, beginY, endX, endY);
                        dScale *= Scale(beginX, beginY, endX, endY);
                        if (dScale <= (float)0.1)
                            dScale = (float)0.1;
                    }
                }

                pagePlay.setChangeTranslate(Translate());
                pagePlay.setChangeCorner(Angle());
                pagePlay.setChangeScale(Scale());

                break;
        }

        return true;
    }

    /// Return angle.
    public float Angle() {
        if (dAngle > 360)
            dAngle -= 360 * ((int)dAngle / 360);
        return dAngle;
    }

    /// Return scale between 0.1 and 11.0.
    public float Scale() {
        if (dScale < (float)0.1)
            dScale = (float)0.1;
        if (dScale >= (float)11.0)
            dScale = (float)11.0;

        return dScale;
    }

    /// Return vector of translate.
    public Point Translate() {
        return translate;
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

        return (float)Math.toDegrees(Math.acos(((x0[1] - x0[0]) * (x1[1] - x1[0])
            + (y0[1] - y0[0]) *  (y1[1] - y1[0])) / temp));
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
    private float dAngle = -90;

    private float[] beginX = new float[2];
    private float[] beginY = new float[2];
    private float[] endX = new float[2];
    private float[] endY = new float[2];

    // Coefficient to translate.
    private int coeffTranslate = 9;
    // Listener.
    private PagePlay pagePlay;
}