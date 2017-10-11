package com.example.duxiji.ratingstar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.duxiji.ratingstar.view.Ratingbar;

public class MainActivity extends AppCompatActivity {

    private Ratingbar ratingbar1;
    private Ratingbar ratingbar2;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        ratingbar1 = (Ratingbar) findViewById(R.id.ratingbar1);
        ratingbar1.setOnStarChangeListener(new Ratingbar.OnStarChangeListener() {
            @Override
            public void OnStarChange(int currentStarNum) {
                tv.setText("回调的值："+currentStarNum);
            }
        });

        ratingbar2 = (Ratingbar) findViewById(R.id.ratingbar2);
        ratingbar2.setIsCanTouch(false);
    }
}
