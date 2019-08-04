package com.MMU.vehicleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private Vehicle theVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView nameTxt = (TextView) findViewById(R.id.vehicleNameTxt);
        TextView priceTxt = (TextView) findViewById(R.id.priceText);
        TextView colourTxt = (TextView) findViewById(R.id.colourTxt);
        TextView transmissionTxt = (TextView) findViewById(R.id.transmissionTxt);
        TextView mileageTxt = (TextView) findViewById(R.id.mileageText);
        TextView fuelTxt = (TextView) findViewById(R.id.fuelTxt);
        TextView engineTxt = (TextView) findViewById(R.id.engineText);
        TextView numberofdoorsTxt = (TextView) findViewById(R.id.numberofdoorsTxt);
        TextView conditionTxt = (TextView) findViewById(R.id.conditionTxt);
        TextView notesTxt = (TextView) findViewById(R.id.notesTxt);
        //Gets vehicle obj which was passed through the intent
        Bundle extras = getIntent().getExtras();
        theVehicle = (Vehicle) extras.get("vehicle");

        System.out.println("Recieved from the intent: " + theVehicle.get_vehicle_ID());

        //Sets text of all the text views to the vehicle attributes
        nameTxt.setText(theVehicle.get_make() + " " + theVehicle.get_model() + " (" + theVehicle.get_year() + ")");
        priceTxt.setText(Integer.toString(theVehicle.get_price()));
        colourTxt.setText(theVehicle.get_colour());
        transmissionTxt.setText(theVehicle.get_transmission());
        mileageTxt.setText(Integer.toString(theVehicle.get_mileage()));
        fuelTxt.setText(theVehicle.get_fuel_type());
        engineTxt.setText(Integer.toString(theVehicle.get_engine_size()));
        numberofdoorsTxt.setText(Integer.toString(theVehicle.get_number_doors()));
        conditionTxt.setText(theVehicle.get_condition());
        notesTxt.setText(theVehicle.get_notes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbardetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            finish();
            Intent intent = new Intent(getApplicationContext(), EditVehicleActivity.class);
            intent.putExtra("vehicle", theVehicle);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        theVehicle = (Vehicle) extras.get("vehicle");
    }
}
