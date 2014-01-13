package com.northdocks.heimspiel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class BarView extends AnimatedStatsView {

    private int secondaryAmount;
    private int secondaryTotalAmount;
    private int primaryBackgroundColor = Color.argb(127, 255, 255, 255);
    private int secondaryBackgroundColor = Color.argb(127, 255, 0, 0);
    private int primaryAmount;
    private int primaryTotalAmount;
    private Paint paint = new Paint();

    public BarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public BarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BarView(Context context) {
        super(context);
    }

    public void setPrimaryBackgroundColor(int color) {
        this.primaryBackgroundColor = color;
    }

    public void setSecondaryBackgroundColor(int color) {
        this.secondaryBackgroundColor = color;
    }

    @Override
    public void setData(int primaryAmount, int primaryTotalAmount) {
        assert primaryAmount <= primaryTotalAmount;
        this.primaryAmount = primaryAmount;
        this.primaryTotalAmount = primaryTotalAmount;
    }

    public void setSecondaryData(int secondaryAmount, int secondaryTotalAmount) {
        assert secondaryAmount <= secondaryTotalAmount;
        this.secondaryAmount = secondaryAmount;
        this.secondaryTotalAmount = secondaryTotalAmount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = getWidth() / 5;
        float h = getHeight();

        float total = Math.max(primaryTotalAmount, secondaryTotalAmount);
        float progress1 = Math.min(animationProgress * 2, 1);
        float progress2 = Math.max((animationProgress - 0.5f) * 2, 0);

        float amount1 = (primaryAmount / total) * progress1;
        float total1 = (primaryTotalAmount / total) * progress1;
        float amount2 = (secondaryAmount / total) * progress2;
        float total2 = (secondaryTotalAmount / total) * progress2;

        if (isInEditMode()) {
            amount1 = 1;
            total1 = 1;
            amount2 = 0.5f;
            total2 = 0.75f;
        }

        paint.setColor(primaryBackgroundColor);
        canvas.drawRect(w, h - total1 * h, w * 2, h, paint);

        paint.setColor(primaryColor);
        canvas.drawRect(w, h - amount1 * h, w * 2, h, paint);

        paint.setColor(secondaryBackgroundColor);
        canvas.drawRect(w * 3, h - total2 * h, w * 4, h, paint);

        paint.setColor(secondaryColor);
        canvas.drawRect(w * 3, h - amount2 * h, w * 4, h, paint);
    }
}
