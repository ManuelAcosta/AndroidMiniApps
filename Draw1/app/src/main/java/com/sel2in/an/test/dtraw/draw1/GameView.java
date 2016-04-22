package com.sel2in.an.test.dtraw.draw1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by t on 22-04-2016.
 */
public class GameView extends View {
    // setup initial color
    private final int paintColor = Color.WHITE;
    // defines paint and canvas
    private Paint drawPaint;
    Context context;
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(3);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(50, 50, 20, drawPaint);
        drawPaint.setColor(Color.GREEN);
        canvas.drawCircle(50, 150, 20, drawPaint);
        drawPaint.setColor(Color.BLUE);
        canvas.drawCircle(50, 250, 20, drawPaint);
        Resources res = context.getResources();
        Drawable img = res.getDrawable(R.drawable.back1);

        float dx = 10,dy=10;
        //canvas.drawBitmap(img,dx, dy,paintColor);
        img.draw(canvas);
        /*
        *  @param bitmap The bitmap to be drawn
     * @param src    May be null. The subset of the bitmap to be drawn
     * @param dst    The rectangle that the bitmap will be scaled/translated
     *               to fit into
     * @param paint  May be null. The paint used to draw the bitmap

        public void drawBitmap(@NonNull Bitmap bitmap, @Nullable Rect src, @NonNull RectF dst,
                @Nullable Paint paint) */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //circlePoints.add(new Point(Math.round(touchX), Math.round(touchY)));
        // indicate view should be redrawn
        postInvalidate();
        return true;
    }
}
