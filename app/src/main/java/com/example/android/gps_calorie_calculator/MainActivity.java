package com.example.android.gps_calorie_calculator;

import android.support.v7.app.AppCompatActivity;


        import android.os.Bundle;
        import android.app.Activity;
        import android.content.Intent;
        import android.view.Menu;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void RunningForm(View v)
    {
        Intent intent = new Intent(this,Run.class);
        startActivity(intent);
    }
    public void CyclingForm(View v)
    {
        Intent intent = new Intent(this, Cycle.class);
        startActivity(intent);
    }

    public void WalkingForm(View v)
    {
        Intent intent = new Intent(this, Walk.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

