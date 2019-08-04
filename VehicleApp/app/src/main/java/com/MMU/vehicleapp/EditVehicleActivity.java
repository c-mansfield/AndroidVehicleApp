package com.MMU.vehicleapp;

import android.content.Intent;
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

public class EditVehicleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        Bundle extras = getIntent().getExtras();
        final Vehicle vehicle = (Vehicle) extras.get("vehicle");

        final EditText makeText = (EditText) findViewById(R.id.makeTxt);
        final EditText modelText = (EditText) findViewById(R.id.modelTxt);
        final EditText yearText = (EditText) findViewById(R.id.yearText);
        final EditText priceText = (EditText) findViewById(R.id.priceText);
        final EditText license_numberText = (EditText) findViewById(R.id.licenseTxt);
        final EditText colourText = (EditText) findViewById(R.id.colourTxt);
        final EditText number_doorsText = (EditText) findViewById(R.id.numofdoorsText);
        final EditText transmissionText = (EditText) findViewById(R.id.transmissionTxt);
        final EditText mileageText = (EditText) findViewById(R.id.mileageText);
        final EditText fuel_typeText = (EditText) findViewById(R.id.fuelTxt);
        final EditText engine_sizeText = (EditText) findViewById(R.id.engineText);
        final EditText body_styleText = (EditText) findViewById(R.id.bodyTxt);
        final EditText conditionText = (EditText) findViewById(R.id.conditionTxt);
        final EditText notesText = (EditText) findViewById(R.id.notesTxt);
        final Button editSaveBtn = (Button) findViewById(R.id.editVehicleBtn);
        final HashMap<String, String> params = new HashMap<>();

        makeText.setText(vehicle.get_make());
        modelText.setText(vehicle.get_model());
        yearText.setText(Integer.toString(vehicle.get_year()));
        priceText.setText(Integer.toString(vehicle.get_price()));
        license_numberText.setText(vehicle.get_license_number());
        colourText.setText(vehicle.get_colour());
        number_doorsText.setText(Integer.toString(vehicle.get_number_doors()));
        transmissionText.setText(vehicle.get_transmission());
        mileageText.setText(Integer.toString(vehicle.get_mileage()));
        fuel_typeText.setText(vehicle.get_fuel_type());
        engine_sizeText.setText(Integer.toString(vehicle.get_engine_size()));
        body_styleText.setText(vehicle.get_body_style());
        conditionText.setText(vehicle.get_condition());
        notesText.setText(vehicle.get_notes());

        editSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();

                String makeS = makeText.getText().toString();
                String modelS = modelText.getText().toString();
                int yearI = Integer.parseInt(yearText.getText().toString());
                int priceI = Integer.parseInt(priceText.getText().toString());
                String license_numberS = license_numberText.getText().toString();
                String colourS = colourText.getText().toString();
                int number_doorsI = Integer.parseInt(number_doorsText.getText().toString());
                String transmissionS = transmissionText.getText().toString();
                int mileageI = Integer.parseInt(mileageText.getText().toString());
                String fuel_typeS = fuel_typeText.getText().toString();
                int engine_sizeI = Integer.parseInt(engine_sizeText.getText().toString());
                String body_styleS = body_styleText.getText().toString();
                String conditionS = conditionText.getText().toString();
                String notesS = notesText.getText().toString();
                Vehicle vehicleIn = new Vehicle(vehicle.get_vehicle_ID(), makeS, modelS, yearI, priceI,
                        license_numberS, colourS, number_doorsI, transmissionS,
                        mileageI, fuel_typeS, engine_sizeI, body_styleS,
                        conditionS, notesS);

                String vehicleJson = gson.toJson(vehicleIn);
                System.out.println(vehicleJson);
                params.put("json", vehicleJson);
                String url = "http://10.0.2.2:8005/api";
                performPutCall(url, params);

                finish();
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("vehicle", vehicleIn);

                startActivity(intent);
            }
        });
    }

    //Method to carry out the put call
    public String performPutCall(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";

        try {
            url = new URL(requestURL);

            //Create Connection Obj
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPutDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            //Get server response to determine what to do next
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Toast.makeText(this, "Vehicle Updated", Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                Toast.makeText(this, "Error failed to update content", Toast.LENGTH_LONG).show();
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Response  = " + response);
        return response;
    }

    //Converts hashmap to a URL query of key/value pair
    private String getPutDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
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
