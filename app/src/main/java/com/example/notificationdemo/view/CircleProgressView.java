package com.example.notificationdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by boby on 2016/12/3.
 */

public class CircleProgressView extends View {

    private Paint backgroundPaint;
    private Paint barPaint;
    private Paint textPaint;

    private int centerX;
    private int centerY;
    private int radius;

    private RectF barRectF;
    private Paint.FontMetrics textFont;

    private int progress;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setDither(true);
        backgroundPaint.setColor(Color.parseColor("#4363ae"));
        backgroundPaint.setStyle(Paint.Style.FILL);

        barPaint = new Paint();
        barPaint.setAntiAlias(true);
        barPaint.setDither(true);
        barPaint.setColor(Color.parseColor("#47eaf4"));
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeJoin(Paint.Join.ROUND);
        barPaint.setStrokeCap(Paint.Cap.BUTT);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(Color.parseColor("#4294EF"));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(centerX, centerY) * 3 / 4;
        barRectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY +
                radius);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawCircle(centerX, centerY, radius, backgroundPaint);

        barPaint.setStrokeWidth(radius / 4);
        canvas.drawArc(barRectF, 0, progress * ((float) 360 / 100), false, barPaint);

        textPaint.setTextSize(radius / 3);
        textFont = textPaint.getFontMetrics();
        canvas.drawText(progress == 0 ? "准备更新" : progress + "", centerX,
                centerY + (textFont.bottom - textFont.top) / 2 - textFont.bottom, textPaint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }
}
