package com.example.android.gps_calorie_calculator;

import java.text.DecimalFormat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.Time;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;


public class CalorieCount extends AppCompatActivity {

    public int level, weight, age, rate;
    public String scale;
    public float distance;
    public Location l;
    public Time t1, t2;
    public LocationManager locationManager;
    public String mode;
    public PowerManager.WakeLock wl;
    AnimationDrawable runAnimation;

    //  @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_count);

        // Show the Up button in the action bar.
        Intent intent = getIntent();
        l = null;
        t1 = new Time();
        t2 = new Time();
        Bundle bundle = intent.getBundleExtra("data");
        mode = bundle.getString("mode");
        scale = bundle.getString("scale");
        if (bundle.getString("weight").equalsIgnoreCase(""))
            weight = 0;
        else
            weight = Integer.parseInt(bundle.getString("weight"));
        level = Integer.parseInt(bundle.getString("level"));
        if (mode.equalsIgnoreCase("run")) {
            if (bundle.getString("age").equalsIgnoreCase(""))
                age = 0;
            else

                age = Integer.parseInt(bundle.getString("age"));
            if (bundle.getString("rate").equalsIgnoreCase(""))
                rate = 0;
            else
                rate = Integer.parseInt(bundle.getString("rate"));
        }
        if (scale.equals("lb"))
            weight = (int) (weight * 0.453592);
        setupActionBar();

        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(svcName);

        ImageView rocketImage = (ImageView) findViewById(R.id.imageView1);
        rocketImage.setImageBitmap(null);
        rocketImage.setBackgroundResource(R.drawable.running);
        runAnimation = (AnimationDrawable) rocketImage.getBackground();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();
        Toast.makeText(this, "Finding Location....", Toast.LENGTH_LONG).show();

    }

    public void onResume() {
        super.onResume();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
      //  String provider = locationManager.getBestProvider(criteria, true);
        //  Log.i("provider is" ,provider);
           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION};
               ActivityCompat.requestPermissions(this, permissions, 1);
            }
         locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, locationListener);
        //  Toast.makeText(this, "onResume()", Toast.LENGTH_LONG).show();
    }
    public void onPause() {
        super.onPause();
        // Toast.makeText(this, "onPause()", Toast.LENGTH_LONG).show();
        wl.release();


    }
    protected void onDestroy() {
        //The activity about to be destroyed.
        super.onDestroy();

        locationManager.removeUpdates(locationListener);
        // Toast.makeText(this, "onDestroy()", Toast.LENGTH_LONG).show();
    }
    protected void onStop() {
        //the activity is no longer visible.
        super.onStop();

    }
    private void updateWithNewLocation(Location location) {
        TextView myLocationText,distance_text,speed_text,status;

        myLocationText = (TextView)findViewById(R.id.textView4);
        distance_text = (TextView)findViewById(R.id.textView2);
        speed_text = (TextView)findViewById(R.id.textView6);

        if(l== null)
        {
            runAnimation.start();
            l=location;
            t1.setToNow();

            Toast.makeText(this, "Started...Dont Lock the Phone", Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            float  speed= 0;
            distance+=l.distanceTo(location);
            t2.setToNow();
            long timeElapsed=t2.toMillis(false)-t1.toMillis(false);
            float hr=(((float)timeElapsed / (1000*60*60))) ;
            l=location;
            float distance_in_km=distance/1000;
            if(hr!=0.0)
                speed=distance_in_km/hr;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            myLocationText.setText(getCalorieCount(hr,speed));

            distance_text.setText(decimalFormat.format(distance));
            speed_text.setText(decimalFormat.format(speed));
        }





    }
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {}
    };

    public String getCalorieCount(float hr,float speed)
    {

        float  CB=0.0f;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if(mode.equalsIgnoreCase("run"))
        {
            float TF=0.84f,distance=speed*hr;
            if(level<=-10)
                CB = (((-0.02f * level) + 0.35f) * weight + TF) * distance * getCFF();
            if(level<=0&&level>(-10))
                CB = (((0.04f * level) + 0.95f) * weight + TF) * distance * getCFF();
            if(level>0&&level<=10)
                CB = (((0.05f * level) + 0.95f) * weight + TF) * distance * getCFF();
            if(level>10)
                CB = (((0.07f * level) + 0.75f) * weight + TF) * distance * getCFF();
        }
        else
        {
            if(level == 0)
                CB =(float) (0.0215 * (speed * speed * speed) - 0.1765 * (speed * speed) + 0.8710 * speed + 1.4577) * weight * hr;
            else if(level == 1)
                CB =(float) (0.0171 * (speed * speed * speed) - 0.1062 * (speed * speed) + 0.6080 * speed + 1.8600) * weight * hr;
            else if(level == 2)
                CB =(float) (0.01841 * (speed * speed * speed) - 0.1134 * (speed * speed) + 0.6566 * speed + 1.9200) * weight * hr;
            else if(level == 3)
                CB =(float) (0.0196 * (speed * speed * speed) - 0.1205 * (speed * speed) +  0.7053 * speed + 1.9800) * weight * hr;
            else if(level == 4)
                CB =(float) (0.0208 * (speed * speed * speed) - 0.1277 * (speed * speed) + 0.7539 * speed + 2.0400) * weight * hr;
            else if(level == 5)
                CB =(float) (0.0221 * (speed * speed * speed) - 0.1349 * (speed * speed) + 0.8025 * speed + 2.1000) * weight * hr;

            else if(level == -1)
                CB =(float) (0.0222 * (speed * speed * speed) - 0.1844 * (speed * speed) +  0.8546 * speed + 1.4253) * weight * hr;
            else if(level == -2)
                CB =(float) (0.0230 * (speed * speed * speed) - 0.1922 * (speed * speed) + 0.8382 * speed + 1.3929) * weight * hr;
            else if(level == -3)
                CB =(float) (0.0237 * (speed * speed * speed) - 0.2000 * (speed * speed) + 0.8217* speed + 1.3605) * weight * hr;
            else if(level == -4)
                CB =(float) (0.0244 * (speed * speed * speed) - 0.2079 * (speed * speed) + 0.8053 * speed + 1.3281) * weight * hr;
            else if(level == -5)
                CB =(float) (0.0251 * (speed * speed * speed) - 0.2157 * (speed * speed) + 0.7888 * speed + 1.2957) * weight * hr;
        }
        return decimalFormat.format(CB);

    }
    public float getCFF()
    {
        float MHR=208 - (0.7f * age),VO2max = 15.3f * (MHR/(rate*3)),CFF;
        if(VO2max>56)
            CFF=1.0f;
        if(VO2max>=54&&VO2max<56);
        CFF=1.01f;
        if(VO2max>=52&&VO2max<54);
        CFF=1.02f;
        if(VO2max>=50&&VO2max<52);
        CFF=1.03f;
        if(VO2max>=48&&VO2max<50);
        CFF=1.04f;
        if(VO2max>=46&&VO2max<48);
        CFF=1.05f;
        if(VO2max>=44&&VO2max<46);
        CFF=1.06f;
        if(VO2max<44);
        CFF=1.07f;
        return CFF;
    }
    public void closeWalk(View v)
    {
        l=null;

        locationManager.removeUpdates(locationListener);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}

