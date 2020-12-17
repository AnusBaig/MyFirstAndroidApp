package com.test.myfirstandroidapp;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;

import java.net.HttpURLConnection;
import java.util.EventListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class First2Fragment extends Fragment {

    private static HttpURLConnection httpClient;
    public static String responseText;
    public static String Apikey="be2c512ead7e6ce4590bae4b7c4e0947";
    public static String baseUrl="https://api.openweathermap.org/data/2.5/weather";
    public static String temprature="";
    public static String location="";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first2, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText countryInput = (EditText) view.findViewById(R.id.txt_input_country);
        TextView locationText = (TextView) view.findViewById(R.id.txt_loc);
        TextView tempratureText = (TextView) view.findViewById(R.id.txt_temp);
        TextView weatherText = (TextView) view.findViewById(R.id.txt_weather);
        ImageView bgImage = (ImageView) view.findViewById(R.id.img_bg);

        if(location.length()>3)
            countryInput.setText(location);
        Weather(countryInput.getText().toString(),locationText,tempratureText,weatherText,bgImage);

        countryInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                    Weather(countryInput.getText().toString(),locationText,tempratureText,weatherText,bgImage);
                    return true;
            }
        });


            view.findViewById(R.id.btn_weather_report).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (responseText.length() > 70) {
                            //Create bundler
                            Bundle args = new Bundle();
                            // Add properties in bundler from fetched user from database
                            String[] stats = new String[9];
                            stats[0] = GetPropertyValue("temp");
                            stats[1] = GetPropertyValue("humidity");
                            stats[2] = GetPropertyValue("pressure");
                            stats[3] = GetPropertyValue("feels_like");
                            stats[4] = GetPropertyValue("temp_min");
                            stats[5] = GetPropertyValue("temp_max");
                            stats[6] = GetPropertyValue("speed");
                            stats[7] = GetPropertyValue("sunrise");
                            stats[8] = GetPropertyValue("sunset");

                            // Pass bunddler as argument
                            args.putStringArray("Stats", stats);

                            NavHostFragment.findNavController(First2Fragment.this)
                                    .navigate(R.id.action_First2Fragment_to_Second2Fragment, args);
                        } else {
                            Toast.makeText(First2Fragment.super.getContext(), "Weather stats not available", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){
                    }
                }
            });
    }

    public void Weather(String city,TextView location,TextView temprature,TextView weather, ImageView image) {
        if (city.equals("") || city.equals("Enter Country..") || city.length() < 4) city = "London";

        if (city.length() > 3) {
            String url = baseUrl + "?q=" + city + "&appid=" + Apikey + "&units=imperial";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        responseText = response.toString();

                        if (responseText=="") {
                            Toast.makeText(First2Fragment.super.getContext(), "City not found", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String weatherClass = GetPropertyValue("description");

                        location.setText(GetPropertyValue("name") + " , " + GetPropertyValue("country"));
                        temprature.setText(GetPropertyValue("temp")+" F");
                        weather.setText(weatherClass);

                        First2Fragment.location=location.getText().toString();
                        First2Fragment.temprature=temprature.getText().toString();

                        if(weatherClass.contains("cloud")){
                            image.setImageResource(R.drawable.weather);
                        }
                        else if(weatherClass.contains("clear")){
                            image.setImageResource(R.drawable.clear);
                        }
                        else if(weatherClass.contains("rain")){
                            image.setImageResource(R.drawable.rain);
                        }
                        else {
                            image.setImageResource(R.drawable.weather);
                        }

                    } catch (Exception e) {
//                        Toast.makeText(First2Fragment.super.getContext(), "Invalid response", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error
//                    Toast.makeText(First2Fragment.super.getContext(), "Failed getting weather updates", Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue queue = Volley.newRequestQueue(First2Fragment.super.getContext());
            queue.add(jsonObjectRequest);

        }
    }

    public String  GetPropertyValue(String key) {
        if (!this.responseText.equals("") && this.responseText.contains(key)) {
                String filteredText = this.responseText.substring(this.responseText.indexOf(key));
                int lastIndex = this.responseText.length() - 1;
                int lastIndexFirstChocice = filteredText.indexOf(","), lastIndexSecondChocice = filteredText.indexOf("}");
                if (lastIndexFirstChocice > lastIndexSecondChocice)
                    lastIndex = filteredText.indexOf("}");
                else
                    lastIndex = filteredText.indexOf(",");

                filteredText = filteredText.substring(filteredText.indexOf(":") + 1, lastIndex);

                if (filteredText.toCharArray()[0] == '"')
                    filteredText = filteredText.substring(1);
                if (filteredText.toCharArray()[filteredText.length() - 1] == '"')
                    filteredText = filteredText.substring(0, filteredText.length() - 1);
                return filteredText;
        }
        return "";
    }
}
