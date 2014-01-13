package com.northdocks.heimspiel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

public class DonutView extends AnimatedStatsView {

    // angle=0 is 3 o'clock so we need to rotate counterclockwise by 90 degrees
    private int OFFSET = -90;
    private int amount = 0;
    private int totalAmount = 0;
    private float strokeSize = 20f;
    private final Paint paint = new Paint();
    private final Paint textPaint = new Paint();
    private final RectF rect = new RectF();

    public DonutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DonutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DonutView(Context context) {
        super(context);
    }

    @Override
    public void setData(int amount, int totalAmount) {
        assert amount <= totalAmount;
        this.amount = amount;
        this.totalAmount = totalAmount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect.set(strokeSize, strokeSize, getWidth() - strokeSize, getHeight() - strokeSize);

        float total = totalAmount;
        float current = amount;

        if (isInEditMode()) {
            total = 1;
            current = 0.6f;
            animationProgress = 0.8f;
        } else if (total <= 0) {
            return;
        }

        float sweep1 = (current / total) * 360f * animationProgress;
        float sweep2 = (1f - (current / total)) * 360f * animationProgress;

        paint.setStrokeWidth(strokeSize);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(primaryColor);
        canvas.drawArc(rect, OFFSET - sweep1, sweep1, false, paint);

        paint.setColor(secondaryColor);
        canvas.drawArc(rect, OFFSET, sweep2, false, paint);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setLinearText(true);
        textPaint.setSubpixelText(true);
        textPaint.setTextSize(AnimatedStatsView.lerp((getWidth() / 6), (getWidth() / 4), animationProgress));
        textPaint.setColor(primaryColor);
        textPaint.setAlpha(Math.min((int)(animationProgress * 255 * 2), 255));

        float textX = getWidth() / 2;
        float textY = ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText("%", textX, textY, textPaint);
    }

    public void setStrokeSize(float strokeSize) {
        this.strokeSize = strokeSize;
    }
}
