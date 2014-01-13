package com.northdocks.heimspiel;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public abstract class AnimatedStatsView extends View {

    private static final String TAG = AnimatedStatsView.class.getCanonicalName();
    protected int primaryColor = Color.WHITE;
    protected int secondaryColor = Color.RED;
    protected float animationProgress = 0;
    private ValueAnimator animator;
    private long animationLength = 500;
    private TimeInterpolator interpolator = new DecelerateInterpolator();

    public AnimatedStatsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AnimatedStatsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedStatsView(Context context) {
        super(context);
    }

    /**
     * Clamp a value between a minimum and a maximum.
     * If min > max, min and max are swapped.
     *
     * @param min The minimum value of the result
     * @param max The maximum value of the result
     * @param t   The value to be clamped
     * @return The clamped value
     */
    public static float clamp(float min, float max, float t) {
        if (max < min) {
            float tmp = max;
            max = min;
            min = max;
        }

        return Math.max(min, Math.min(max, t));
    }

    /**
     * Linear interpolation (also known as "lerp") between two values
     *
     * @param from the start value (for t = 0)
     * @param to   the end value (for t = 1)
     * @param t    value in the range between [0,1]
     * @return The interpolated result
     */
    public static float lerp(float from, float to, float t) {
        t = clamp(0, 1, t);
        return from + t * (to - from);
    }

    /**
     * @param primaryAmount Das, was normalerweise weiß (ergo mit der Primärfarbe dargestellt wird)
     * @param totalAmount
     */
    public abstract void setData(int primaryAmount, int totalAmount);

    /**
     * Setzt die Animation zurück, falls sie läuft.
     *
     * @param showAnimationEnd Wenn true, wird die Animation auf den Endzustand gesetzt.
     */
    public void resetAnimationState(boolean showAnimationEnd) {
        cancelAnimation();
        animationProgress = (showAnimationEnd ? 1 : 0);
        invalidate();
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        assert interpolator != null;
        this.interpolator = interpolator;
    }

    /**
     * Startet die Animation und bleibt danach im Endzustand.
     */
    public void startAnimation() {
        Log.d(TAG, "Starting animation...");

        resetAnimationState(false);
        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(interpolator);
        animator.setDuration(animationLength);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = animation.getAnimatedFraction();

                if (progress >= 1) {
                    resetAnimationState(true);
                } else {
                    onProgress(progress);
                }
            }
        });
        animator.start();
    }

    /**
     * Setzt die Dauer der Animation
     *
     * @param millis Dauer der Animation in Millisekunden (default: 500ms)
     */
    public void setAnimationLength(int millis) {
        assert millis > 0;
        cancelAnimation();
        animationLength = millis;
    }

    protected void cancelAnimation() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    protected void onProgress(float progress) {
        this.animationProgress = progress;
        invalidate();
    }
}

