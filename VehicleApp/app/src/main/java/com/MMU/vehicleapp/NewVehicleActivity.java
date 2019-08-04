package com.MMU.vehicleapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NewVehicleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vehicle);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Assign aspects as variables
        final EditText make = (EditText) findViewById(R.id.makeTxt);
        final EditText model = (EditText) findViewById(R.id.modelTxt);
        final EditText year = (EditText) findViewById(R.id.yearTxt);
        final EditText price = (EditText) findViewById(R.id.priceText);
        final EditText license_number = (EditText) findViewById(R.id.licenseTxt);
        final EditText colour = (EditText) findViewById(R.id.colourTxt);
        final EditText number_doors = (EditText) findViewById(R.id.numofdoorsTxt);
        final EditText transmission = (EditText) findViewById(R.id.transmissionTxt);
        final EditText mileage = (EditText) findViewById(R.id.mileageText);
        final EditText fuel_type = (EditText) findViewById(R.id.fuelTxt);
        final EditText engine_size = (EditText) findViewById(R.id.engineText);
        final EditText body_style = (EditText) findViewById(R.id.bodyTxt);
        final EditText condtion = (EditText) findViewById(R.id.condtitionTxt);
        final EditText notes = (EditText) findViewById(R.id.notesTxt);
        final Button addBtn = (Button) findViewById(R.id.addBtn);
        final HashMap<String, String> params = new HashMap<>();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                //Gets all user inputs and creates vehicle object
                int vehicleidI = 100000;
                String makeS = make.getText().toString();
                String modelS = model.getText().toString();
                int yearI = Integer.parseInt(year.getText().toString());
                int priceI = Integer.parseInt(price.getText().toString());
                String license_numberS = license_number.getText().toString();
                String colourS = colour.getText().toString();
                int number_doorsI = Integer.parseInt(number_doors.getText().toString());
                String transmissionS = transmission.getText().toString();
                int mileageI = Integer.parseInt(mileage.getText().toString());
                String fuel_typeS = fuel_type.getText().toString();
                int engine_sizeI = Integer.parseInt(engine_size.getText().toString());
                String body_styleS = body_style.getText().toString();
                String conditionS = condtion.getText().toString();
                String notesS = notes.getText().toString();
                Vehicle vehicle = new Vehicle(vehicleidI, makeS, modelS, yearI, priceI,
                                        license_numberS, colourS, number_doorsI, transmissionS,
                                        mileageI, fuel_typeS, engine_sizeI, body_styleS,
                                        conditionS, notesS);

                //Converts vehicle into json
                String vehicleJson = gson.toJson(vehicle);
                System.out.println(vehicleJson);
                //Creates hashmap with json of object
                params.put("json", vehicleJson);
                String url = "http://10.0.2.2:8005/api";
                performPostCall(url, params);   //Carries out post request

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Method to carry out the post call
    public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";

        try {
            url = new URL(requestURL);
            //Create Connection Obj
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            //Get server response to determine what to do next
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            //Checks whether carried out request
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Toast.makeText(this, "Contact saved to the database", Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                Toast.makeText(this, "Error failed to save content", Toast.LENGTH_LONG).show();
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Response  = " + response);
        return response;
    }

    //Converts hashmap to a URL query of key/value pair
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
