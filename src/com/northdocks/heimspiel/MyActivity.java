package com.northdocks.heimspiel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity {
    private BarView barView;
    private DonutView donutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        barView = (BarView) findViewById(R.id.barView);
        barView.setData(80, 80);
        barView.setSecondaryData(40, 60);
        barView.setAnimationLength(1000);

        donutView = (DonutView) findViewById(R.id.donutView);
        donutView.setData(60, 100);
        donutView.setAnimationLength(1500);
    }

    public void onResetBarView(View view) {
        barView.resetAnimationState(false);
    }

    public void onStartBarView(View view) {
        barView.startAnimation();
    }

    public void onEndBarView(View view) {
        barView.resetAnimationState(true);
    }

    public void onResetDonutView(View view) {
        donutView.resetAnimationState(false);
    }

    public void onStartDonutView(View view) {
        donutView.startAnimation();
    }

    public void onEndDonutView(View view) {
        donutView.resetAnimationState(true);
    }
}
