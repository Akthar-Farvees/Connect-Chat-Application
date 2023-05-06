package com.connect.connectproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.DecimalFormat;

public class WeatherActivity extends AppCompatActivity implements SensorEventListener {

    EditText etCity, etCountry;
    TextView tvResult, textView;
    private final String url = "https://api.openweathermap.org/data/3.0/onecall?lat=";
    private final String appId = "4feeb75048565654f758f82f2e691def";
    DecimalFormat df = new DecimalFormat("#.##");


    SensorManager sensorManager;
    Sensor tempSensor;
    Boolean isTemperatureSensorAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        getSupportActionBar().hide();
//
//        etCity = findViewById(R.id.txtCityName);
//        etCountry = findViewById(R.id.txtCountryCode);
//        tvResult = findViewById(R.id.result);



        //SensorEventListener New Implementation
        textView = findViewById(R.id.sens);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTemperatureSensorAvailable = true;
        }else{
            textView.setText("Temperature Sensor is not Available");
            isTemperatureSensorAvailable = false;
        }

    }

//    public void getWeatherDetails(View view) {
//        String tempUrl = "";
//        String city = etCity.getText().toString().trim();
//        String country = etCountry.getText().toString().trim();
//        if(city.equals(""))
//        {
//            tvResult.setText("City Filed Cannot Empty");
//        }else{
//            if(!country.equals("")){
//                tempUrl = url + "?q=" + city + "," + country + "&appId=" + appId;
//            }else{
//                tempUrl = url + "?q=" + city + "&appId=" + appId;
//
//            }
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.d("response",response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//    }





//    Sensor Event Lisitner - Next

    @Override
    public void onSensorChanged(SensorEvent event) {
        textView.setText((event.values[0]+" °C"));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isTemperatureSensorAvailable){
            sensorManager.registerListener(this,tempSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTemperatureSensorAvailable){
            sensorManager.unregisterListener(this);
        }
    }
}