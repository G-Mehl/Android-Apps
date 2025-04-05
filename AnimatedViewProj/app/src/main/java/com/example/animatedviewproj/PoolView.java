package com.example.animatedviewproj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;

public class PoolView extends SurfaceView implements SurfaceHolder.Callback {
    private AnimationThread thread;
    private float ballX;
    private float ballY;
    private float velocityX;
    private float velocityY;
    private final float radius = 20f;
    private final Paint paint = new Paint();

    public PoolView(Context context, AttributeSet attributes) {
        super(context, attributes);
        getHolder().addCallback(this);
        paint.setColor(Color.RED);
        setFocusable(true);
    }

    @Override

    public void surfaceCreated(SurfaceHolder holder) {
       Random rand = new Random();
       ballX = getWidth() / 2f;
       ballY = getHeight() / 2f;

       velocityX = rand.nextBoolean() ? 5 : -5;
       velocityY = rand.nextBoolean() ? 5 : -5;

       thread = new AnimationThread(getHolder());
       thread.setRunning(true);
       thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private class AnimationThread extends Thread {
        private final SurfaceHolder surfaceHolder;
        private boolean running;

        public AnimationThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while(running) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();

                    synchronized (surfaceHolder) {
                        updateBall();
                        drawCanvas(canvas);
                    }
                    Thread.sleep(16);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        private void updateBall() {
            ballX += velocityX;
            ballY += velocityY;

            if((ballX - radius) < 0 || (ballX + radius) > getWidth()) {
                velocityX = -velocityX;
            }
            if((ballY - radius) < 0 || (ballY + radius) > getHeight()) {
                velocityY = -velocityY;
            }
        }

        private void drawCanvas(Canvas canvas) {
            if(canvas == null) return;
            canvas.drawColor(Color.WHITE);
            canvas.drawCircle(ballX, ballY, radius, paint);
        }
    }
}
