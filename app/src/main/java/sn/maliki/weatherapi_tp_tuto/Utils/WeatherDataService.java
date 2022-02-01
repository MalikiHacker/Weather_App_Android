package sn.maliki.weatherapi_tp_tuto.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sn.maliki.weatherapi_tp_tuto.Models.WeatherReportModel;

public class WeatherDataService {
    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";

    private Context context;
    private String cityID;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{

        public void onResponse(String cityID);
        public void onError(String messageError);

    }

    public void getCityID(String cityName, VolleyResponseListener volleyResponseListener) {
        String url = QUERY_FOR_CITY_ID + cityName;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        cityID = "";
                        try {
                            JSONObject cityInfos = response.getJSONObject(0);
                            cityID = cityInfos.getString("woeid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        volleyResponseListener.onResponse(cityID);
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        volleyResponseListener.onError(error.toString());

                    }
                });

        // Access the RequestQueue through your singleton class.
//                queue.add(jsonArrayRequest);
        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

//        return cityID;
    }

    public interface ForecastByIDResponseListener{

        public void onResponse(List<WeatherReportModel> reports);
        public void onError(String messageError);

    }

    public void getCityForecastByID(String cityID, ForecastByIDResponseListener fbIDRListener) {
        List<WeatherReportModel> reports = new ArrayList<>();

        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;

        // get the Json Object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // Get the property "consolidated_weather" which is an array
                            JSONArray consolidated_weather_list = response
                                    .getJSONArray("consolidated_weather");

                            // Get each item in the array and assign it to a new WeatherReportModel object
                            for (int i = 0; i<consolidated_weather_list.length(); i++){
                                WeatherReportModel one_day = new WeatherReportModel();

                                JSONObject one_day_from_api = consolidated_weather_list.getJSONObject(i);
                                one_day.setId(one_day_from_api.getLong("id"));
                                one_day.setWeatherStateName(one_day_from_api.getString("weather_state_name"));
                                one_day.setWeatherStateAbbr(one_day_from_api.getString("weather_state_abbr"));
                                one_day.setWindDirectionCompass(one_day_from_api.getString("wind_direction_compass"));
                                one_day.setCreated(one_day_from_api.getString("created"));
                                one_day.setApplicableDate(one_day_from_api.getString("applicable_date"));
                                one_day.setMinTemp(one_day_from_api.getDouble("min_temp"));
                                one_day.setMaxTemp(one_day_from_api.getDouble("max_temp"));
                                one_day.setTheTemp(one_day_from_api.getDouble("the_temp"));
                                one_day.setWindSpeed(one_day_from_api.getDouble("wind_speed"));
                                one_day.setWindDirection(one_day_from_api.getDouble("wind_direction"));
                                one_day.setAirPressure(one_day_from_api.getInt("air_pressure"));
                                one_day.setHumidity(one_day_from_api.getInt("humidity"));
                                one_day.setVisibility(one_day_from_api.getDouble("visibility"));
                                one_day.setPredictability(one_day_from_api.getInt("predictability"));

                                reports.add(one_day);
                            }



                            fbIDRListener.onResponse(reports);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
//                        Log.e("ERROR", error.toString());
                        fbIDRListener.onError(error.toString());

                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public interface ForecastByNameResponseListener{

        public void onResponse(List<WeatherReportModel> reports);
        public void onError(String messageError);

    }

    public void getCityForecastByName(String cityName, ForecastByNameResponseListener fcBNRListener) {
        // Get the city ID
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onResponse(String cityID) {
                getCityForecastByID(cityID, new ForecastByIDResponseListener() {
                    @Override
                    public void onResponse(List<WeatherReportModel> reports) {
                        fcBNRListener.onResponse(reports);
                    }

                    @Override
                    public void onError(String messageError) {
                        fcBNRListener.onError(messageError);
                    }
                });
            }

            @Override
            public void onError(String messageError) {
                fcBNRListener.onError(messageError);
            }
        });
    }
}
