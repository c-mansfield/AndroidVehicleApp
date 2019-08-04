package com.MMU.vehicleapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] vehicleNames;
    private ArrayList<Vehicle> vehicleArrayList;
    private ListView vehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.drawable.toolbar_add_btn);
        vehicleList = (ListView) findViewById(R.id.vehicleList);

        //Get request, getting vehicles from server storing in array
        HttpURLConnection urlConnection;
        InputStream in = null;
        String response= "";
        try {
            //Connect to array and get response
            URL url = new URL("http://10.0.2.2:8005/api");
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            response = convertStreamToString(in);
            System.out.println("Server response = " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //Gets reponse and creates array for vehicles
            JSONArray jsonArray = new JSONArray(response);
            vehicleNames = new String[jsonArray.length()];
            vehicleArrayList = new ArrayList<>(jsonArray.length());

            //Loops through every vehicle and stores as object
            for (int i = 0; i < jsonArray.length(); i++) {
                String vehicle_idStr = jsonArray.getJSONObject(i).get("vehicle_id").toString();
                int vehicle_id = Integer.parseInt(jsonArray.getJSONObject(i).get("vehicle_id").toString());
                String make = jsonArray.getJSONObject(i).get("make").toString();
                String model = jsonArray.getJSONObject(i).get("model").toString();
                int year = Integer.parseInt(jsonArray.getJSONObject(i).get("year").toString());
                int price = Integer.parseInt(jsonArray.getJSONObject(i).get("price").toString());
                String license_number = jsonArray.getJSONObject(i).get("license_number").toString();
                String colour = jsonArray.getJSONObject(i).get("colour").toString();
                int number_doors = Integer.parseInt(jsonArray.getJSONObject(i).get("number_doors").toString());
                String transmission = jsonArray.getJSONObject(i).get("transmission").toString();
                int mileage = Integer.parseInt(jsonArray.getJSONObject(i).get("mileage").toString());
                String fuel_type = jsonArray.getJSONObject(i).get("fuel_type").toString();
                int engine_size = Integer.parseInt(jsonArray.getJSONObject(i).get("engine_size").toString());
                String body_style = jsonArray.getJSONObject(i).get("body_style").toString();
                String condition = jsonArray.getJSONObject(i).get("condition").toString();
                String notes = jsonArray.getJSONObject(i).get("notes").toString();
                Vehicle vehicle = new Vehicle(vehicle_id, make, model, year, price,
                                        license_number, colour, number_doors, transmission,
                                        mileage, fuel_type, engine_size, body_style, condition, notes);
                vehicleNames[i] = make + " " + model + " (" + year + ")";
                vehicleArrayList.add(vehicle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Creates array apapter with vehicles and adds to vehicle list
        ArrayAdapter vehicleArrayA = new ArrayAdapter(this, android.R.layout.simple_list_item_1, vehicleNames);
        vehicleList.setAdapter(vehicleArrayA);

        //Goes to detail view of chosen vehicle
        vehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("vehicle", vehicleArrayList.get(i));

                startActivity(intent);
            }
        });

        //When long clicked deletes vehicle
        vehicleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                System.out.println("Long Clicked!");
                //Creates pop up box to confirm delete
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you wish to delete " + vehicleNames[position] + "?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            System.out.println(vehicleArrayList.get(position).get_vehicle_ID());
                            //Connects to api and sends the vehicle id of chosen vehicle wish to delete
                            URL url2 = new URL("http://10.0.2.2:8005/api?id=" + vehicleArrayList.get(position).get_vehicle_ID());
                            HttpURLConnection httpCon = (HttpURLConnection) url2.openConnection();
                            httpCon.setDoOutput(true);
                            httpCon.setRequestMethod("DELETE");
                            httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            httpCon.setRequestProperty("charset", "utf-8");
                            httpCon.connect();          //Deletes vehicle

                            System.out.println("Response code: " + httpCon.getResponseCode());

                            BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                            String line, responseText = "";
                            while ((line = br.readLine()) != null) {
                                System.out.println("LINE: "+line);
                                responseText += line;
                            }
                            br.close();
                            httpCon.disconnect();

                            vehicleList.invalidateViews();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }

                        //Refreshes array
                        finish();
                        startActivity(getIntent());
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    }

    //Converts get request to string
    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    //Sets up action bar at the top
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbarmain, menu);
        return true;
    }

    //When button on action bar clicks goes to insert vehicle
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            Intent intent = new Intent(getApplicationContext(), NewVehicleActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //When activity loaded back up does get request again to refresh data
    @Override
    protected void onResume() {
        super.onResume();

        HttpURLConnection urlConnection;
        InputStream in = null;
        String response= "";
        try {
            URL url = new URL("http://10.0.2.2:8005/api");
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            response = convertStreamToString(in);
            System.out.println("Server response = " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(response);
            vehicleNames = new String[jsonArray.length()];
            vehicleArrayList = new ArrayList<>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                String vehicle_idStr = jsonArray.getJSONObject(i).get("vehicle_id").toString();
                int vehicle_id = Integer.parseInt(jsonArray.getJSONObject(i).get("vehicle_id").toString());
                String make = jsonArray.getJSONObject(i).get("make").toString();
                String model = jsonArray.getJSONObject(i).get("model").toString();
                int year = Integer.parseInt(jsonArray.getJSONObject(i).get("year").toString());
                int price = Integer.parseInt(jsonArray.getJSONObject(i).get("price").toString());
                String license_number = jsonArray.getJSONObject(i).get("license_number").toString();
                String colour = jsonArray.getJSONObject(i).get("colour").toString();
                int number_doors = Integer.parseInt(jsonArray.getJSONObject(i).get("number_doors").toString());
                String transmission = jsonArray.getJSONObject(i).get("transmission").toString();
                int mileage = Integer.parseInt(jsonArray.getJSONObject(i).get("mileage").toString());
                String fuel_type = jsonArray.getJSONObject(i).get("fuel_type").toString();
                int engine_size = Integer.parseInt(jsonArray.getJSONObject(i).get("engine_size").toString());
                String body_style = jsonArray.getJSONObject(i).get("body_style").toString();
                String condition = jsonArray.getJSONObject(i).get("condition").toString();
                String notes = jsonArray.getJSONObject(i).get("notes").toString();
                Vehicle vehicle = new Vehicle(vehicle_id, make, model, year, price,
                        license_number, colour, number_doors, transmission,
                        mileage, fuel_type, engine_size, body_style, condition, notes);
                vehicleNames[i] = make + " " + model + " (" + year + ")";
                vehicleArrayList.add(vehicle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vehicleList = (ListView) findViewById(R.id.vehicleList);
        ArrayAdapter vehicleArrayA = new ArrayAdapter(this, android.R.layout.simple_list_item_1, vehicleNames);
        vehicleList.setAdapter(vehicleArrayA);
    }

}

