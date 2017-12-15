package com.bozapro.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CircularSliderRange sliderRange1 = findViewById(R.id.circular1);
        sliderRange1.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override
            public void onStartSliderMoved(double pos) {
                Log.d(TAG, "onStartSliderMoved:" + pos);
            }

            @Override
            public void onEndSliderMoved(double pos) {
                Log.d(TAG, "onEndSliderMoved:" + pos);
            }

            @Override
            public void onStartSliderEvent(ThumbEvent event) {
                Log.d(TAG, "onStartSliderEvent:" + event);
            }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                Log.d(TAG, "onEndSliderEvent:" + event);
            }
        });


        CircularSliderRange sliderRange2 = findViewById(R.id.circular2);
        sliderRange2.setOnSliderRangeMovedListener(new CircularSliderRange.OnSliderRangeMovedListener() {
            @Override
            public void onStartSliderMoved(double pos) {
                Log.d(TAG, "onStartSliderMoved:" + pos);
            }

            @Override
            public void onEndSliderMoved(double pos) {
                Log.d(TAG, "onEndSliderMoved:" + pos);
            }

            @Override
            public void onStartSliderEvent(ThumbEvent event) {
                Log.d(TAG, "onStartSliderEvent:" + event);
            }

            @Override
            public void onEndSliderEvent(ThumbEvent event) {
                Log.d(TAG, "onEndSliderEvent:" + event);
            }
        });
    }


}
