package sn.maliki.weatherapi_tp_tuto.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import sn.maliki.weatherapi_tp_tuto.Models.WeatherReportModel;
import sn.maliki.weatherapi_tp_tuto.R;
import sn.maliki.weatherapi_tp_tuto.Utils.WeatherDataService;

public class MainActivity extends AppCompatActivity {

    Button btn_cityID, btn_getWeatherByID, btn_geWeatherByName;
    EditText edit_dataInput;
    ListView tv_weatherReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the elements
        btn_cityID          = findViewById(R.id.btn_getCityID);
        btn_geWeatherByName = findViewById(R.id.btn_getWeatherByCityName);
        btn_getWeatherByID  = findViewById(R.id.btn_getWeatherByCityID);

        edit_dataInput      = findViewById(R.id.edit_dataInput);

        tv_weatherReports   = findViewById(R.id.tv_weatherReports);

        WeatherDataService weatherDataService = new WeatherDataService(this);

        btn_getWeatherByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "You clicked the button Weather By ID",
//                        Toast.LENGTH_SHORT).show();
                weatherDataService.getCityForecastByID(edit_dataInput.getText().toString(), new WeatherDataService.ForecastByIDResponseListener() {
                    @Override
                    public void onResponse(List<WeatherReportModel> reports) {
//                        Toast.makeText(MainActivity.this, wrModel.toString(), Toast.LENGTH_LONG).show();
//                        Log.w("MODEL", wrModel.toString());
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
                                android.R.layout.simple_list_item_1, reports);

                        tv_weatherReports.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onError(String messageError) {
                        Toast.makeText(MainActivity.this, messageError.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btn_geWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.w("WARN_BTN", "You clicked the button Get Weather By Name");
//                Toast.makeText(MainActivity.this, "You typed " + edit_dataInput.getText().toString(),
//                        Toast.LENGTH_SHORT).show();
                weatherDataService.getCityForecastByName(edit_dataInput.getText().toString(), new WeatherDataService.ForecastByNameResponseListener() {
                    @Override
                    public void onResponse(List<WeatherReportModel> reports) {
//                        Toast.makeText(MainActivity.this, wrModel.toString(), Toast.LENGTH_LONG).show();
//                        Log.w("MODEL", wrModel.toString());
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,
                                android.R.layout.simple_list_item_1, reports);

                        tv_weatherReports.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onError(String messageError) {
                        Toast.makeText(MainActivity.this, messageError.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btn_cityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get a RequestQueue
//                RequestQueue queue = MySingleton.getInstance(MainActivity.this).
//                        getRequestQueue();

//                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//                String cityID = weatherDataService.getCityID(edit_dataInput.getText().toString())
                weatherDataService.getCityID(edit_dataInput.getText().toString(),
                        new WeatherDataService.VolleyResponseListener() {
                            @Override
                            public void onResponse(String cityID) {
                                Toast.makeText(MainActivity.this, "The city ID is : " +cityID,
                                        Toast.LENGTH_LONG).show();
                                edit_dataInput.setText(cityID);

                            }

                            @Override
                            public void onError(String messageError) {
                                Log.e("ERROR", messageError);
                                Toast.makeText(MainActivity.this, "Error occured " +messageError,
                                        Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
    }
}