package com.example.android.gps_calorie_calculator;

import android.support.v7.app.AppCompatActivity;


        import android.location.LocationManager;
        import android.os.Bundle;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.view.Menu;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Spinner;

public class Walk extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
    }

    public void onRun(View v)
    {

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return;
        }
        Intent intent = new Intent(this, CalorieCount.class);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        EditText edittext=(EditText) findViewById(R.id.editText1);
        Bundle bundle=new Bundle();
        String s=spinner1.getSelectedItem().toString();
        bundle.putString("scale", s);
        bundle.putString("level", spinner2.getSelectedItem().toString());
        bundle.putString("weight", edittext.getText().toString());
        bundle.putString("mode", "walk");
        intent.putExtra("data", bundle);

        startActivity(intent);
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.walk, menu);
        return true;
    }

}
