package com.northdocks.heimspiel;

import android.animation.Animator;
import android.animation.TimeAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public abstract class AnimatedStatsView extends View {

    protected int primaryColor = Color.WHITE;
    protected int secondaryColor = Color.RED;
    protected float animationProgress = 0;
    private ValueAnimator animator;
    private long animationLength = 500;
    private static final String TAG = AnimatedStatsView.class.getCanonicalName();
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

    //primaryAmount ist das, was mit dem weißen Teil der Grafik dargestellt wird
    public abstract void setData(int primaryAmount, int totalAmount);

    //setzt die Animation zurück, falls sie läuft. Der View zeigt danach entweder
    //den Zustand vor der Animation (showAnimationEnd==false) oder nach der Animation
    //(showAnimationEnd==true).
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

    //animation startet und bleibt danach im Endzustand.
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

    //setzt Dauer der Animation in Millisekunden, default ist 500
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

    public static float clamp(float min, float max, float t) {
        if (max < min) {
            float tmp = max;
            max = min;
            min = max;
        }

        return Math.max(min, Math.min(max, t));
    }

    public static float lerp(float from, float to, float t) {
        t = clamp(0, 1, t);
        return from + t * (to - from);
    }
}

