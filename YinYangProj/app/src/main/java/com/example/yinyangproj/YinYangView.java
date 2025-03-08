package com.example.yinyangproj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class YinYangView extends View {
    public float x = 300;
    public float y = 300;
    public float radius = 100;
    private Paint blackPaint, whitePaint, borderPaint;

    public YinYangView(Context context, AttributeSet attributte) {
        super(context, attributte);
        init();
    }

    // initialized paints
    private void init() {
        blackPaint = new Paint();
        whitePaint = new Paint();
        borderPaint = new Paint();

        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(10);
    }

    // drawing the yin and yang circles and using initialized paints
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), borderPaint);

        canvas.drawCircle(x, y, radius, blackPaint);

        canvas.drawArc(x - radius, y - radius, x + radius, y + radius, -90, 180, true, whitePaint);

        float smallRadius = radius / 2;
        canvas.drawCircle(x, y - smallRadius, smallRadius, whitePaint);
        canvas.drawCircle(x, y + smallRadius, smallRadius, blackPaint);

        float dotRadius = radius / 6;
        canvas.drawCircle(x, y - smallRadius, dotRadius, blackPaint);
        canvas.drawCircle(x, y + smallRadius, dotRadius, whitePaint);
    }

    // Setting values for on touch or click to the
    // new x and y position that the user clicked
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            float newXposition = event.getX();
            float newYposition = event.getY();

            if (newXposition - radius >= 0 && newXposition + radius <= getWidth()) {
                x = newXposition;
            }
            if (newYposition - radius >= 0 && newYposition + radius <= getHeight()) {
                y = newYposition;
            }

            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

}