package com.bozapro.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bozapro.circularsliderrange.CircularSliderRange;


public class MainActivity extends AppCompatActivity implements CircularSliderRange.OnSliderRangeMovedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CircularSliderRange sliderRange = (CircularSliderRange) findViewById(R.id.circular);
        sliderRange.setOnSliderRangeMovedListener(this);
    }

    @Override
    public void onStartSliderMoved(double pos) {
        Log.d(TAG, "onStartSliderMoved:" + pos);
    }

    @Override
    public void onEndSliderMoved(double pos) {
        Log.d(TAG, "onEndSliderMoved:" + pos);
    }
}
