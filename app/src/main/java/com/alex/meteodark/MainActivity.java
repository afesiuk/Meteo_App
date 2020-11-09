package com.alex.meteodark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG_MAIN = "Main_Activity";
    private static final String DEFAULT_URL = "http://192.168.1.192:8000";

    private Calendar calendar;
    private ClientInterfaceAPI clientAPI;
    private TextView lastUpdateTextView;
    private TextView paragraphTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Retrofit retrofit;

    private ArrayList<ProgressBar> listOfProgressBars;
    private ArrayList<TextView> listOfTextViews;

    private String ipAddress;
    private String port;
    private String path;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkFirstStartUp();
        initListOfProgressBar();
        initListOfTextViews();
        initSwipeRefreshLayout();
        setHeaderText();
    }

    private void checkFirstStartUp() {
        boolean isFirstRun = getSharedPreferences(getString(R.string.startUpKey), MODE_PRIVATE)
                .getBoolean(getString(R.string.startUpValue), true);
        if (isFirstRun) {
            startActivity(new Intent(this, StartPageActivity.class));
            finish();
        }
        getSharedPreferences(getString(R.string.startUpKey), MODE_PRIVATE).edit()
                .putBoolean(getString(R.string.startUpValue), false).apply();
    }

    private void initListOfProgressBar() {
        if (Objects.isNull(this.listOfProgressBars)) {
            this.listOfProgressBars = new ArrayList<>();
        }
        this.listOfProgressBars.add(findViewById(R.id.card1TempBar1));
        this.listOfProgressBars.add(findViewById(R.id.card1TempBar2));
        this.listOfProgressBars.add(findViewById(R.id.card1TempBar3));
        this.listOfProgressBars.add(findViewById(R.id.card2TempBar1));
        this.listOfProgressBars.add(findViewById(R.id.card2TempBar2));
    }

    private void initListOfTextViews() {
        if (Objects.isNull(this.listOfTextViews)) {
            this.listOfTextViews = new ArrayList<>();
        }
        this.listOfTextViews.add(findViewById(R.id.card1Measure1));
        this.listOfTextViews.add(findViewById(R.id.card1Measure2));
        this.listOfTextViews.add(findViewById(R.id.card1Measure3));
        this.listOfTextViews.add(findViewById(R.id.card2Measure1));
        this.listOfTextViews.add(findViewById(R.id.card2Measure2));
    }

    private void initSwipeRefreshLayout() {
        if (Objects.isNull(this.swipeRefreshLayout)) {
            this.swipeRefreshLayout = findViewById(R.id.mainpage_swipe_layout);
        }
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void loadDataFields() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_data_fields), MODE_PRIVATE);
        this.ipAddress = sharedPreferences.getString(getString(R.string.shared_ip_key), "");
        this.port = sharedPreferences.getString(getString(R.string.shared_port_key), "");
        this.path = sharedPreferences.getString(getString(R.string.shared_path_key), "");
        this.userName = sharedPreferences.getString(getString(R.string.shared_name_key), "");
    }

    private void getDataFromServer() {
        if (Objects.isNull(this.clientAPI))
            this.clientAPI = getRetrofitClient().create(ClientInterfaceAPI.class);
        Call<List<SensorData>> call = this.clientAPI.getSensorData(this.path);
        call.enqueue(new Callback<List<SensorData>>() {
            @Override
            public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG_MAIN, "Error getting data from server.");
                    setDefaultValues();
                    return;
                }
                if (!Objects.isNull(response.body())) {
                    Log.e(TAG_MAIN, "Response body: " + response.body());
                    SensorData lastData = response.body().get(0);
                    if (lastData.getCo2_mhz() == 0 && lastData.getTemp_mhz() == 0) {
                        showToastMhZ19bError();
                    } else if (lastData.getPress_bme() == 0 && lastData.getHum_bme() == 0) {
                        showToastBme280Error();
                    }
                    updateUI(lastData);
                    return;
                }
                setDefaultValues();
                Log.e(TAG_MAIN, "Response body is null.");
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
                showToastConnectionError();
                setDefaultValues();
                Log.e(TAG_MAIN, "Error: " + t.getLocalizedMessage());
            }
        });
    }

    private Retrofit getRetrofitClient() {
        if (Objects.isNull(this.retrofit)) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(getUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return this.retrofit;
    }

    private String getUrl() {
        if (Objects.isNull(this.ipAddress) || Objects.isNull(this.port) ||
                this.ipAddress.equals("") || this.port.equals("")) {
            return DEFAULT_URL;
        }
        return "http://" + this.ipAddress + ":" + this.port;
    }

    private void setHeaderText() {
        if (Objects.isNull(this.calendar)) this.calendar = Calendar.getInstance();
        if (Objects.isNull(this.userName)) loadDataFields();

        int hours = this.calendar.get(Calendar.HOUR_OF_DAY);
        String greetingText = "Good ";

        if (hours >= 5 && hours <= 11) {
            greetingText += "morning, " + this.userName;
        } else if (hours > 11 && hours <= 17) {
            greetingText += "day, " + this.userName;
        } else if (hours > 17 && hours <= 22) {
            greetingText += "evening, " + this.userName;
        } else {
            greetingText += "night, " + this.userName;
        }

        TextView headerTextView = findViewById(R.id.mainpageHeaderText);
        headerTextView.setText(greetingText);
    }

    private void setProgressBars(SensorData sensorData) {
        if (Objects.isNull(this.listOfProgressBars)) return;
        setAnimProgress(sensorData.getPercentDataForProgressBar());
        Log.e(TAG_MAIN, "SetProgressBars method");
    }

    private void setAnimProgress(int[] progress) {
        AnimatorSet animatorSet = new AnimatorSet();
        List<ObjectAnimator> animatorList = new ArrayList<>();
        for (int i = 0; i < this.listOfProgressBars.size(); i++) {
            animatorList.add(ObjectAnimator.ofInt(this.listOfProgressBars.get(i),
                    "progress", progress[i]).setDuration(500));
        }
        /* TODO: Choose between playSequentially() and playTogether() */
        animatorSet.playTogether(animatorList.get(0), animatorList.get(1),
                animatorList.get(2), animatorList.get(3), animatorList.get(4));
        animatorSet.start();
    }

    private void setDefaultValues() {
        for (int i = 0; i < listOfTextViews.size(); i++) {
            listOfTextViews.get(i).setText("Value: 0");
            listOfProgressBars.get(i).setProgress(0);
        }
    }

    private void setCardTextViews(SensorData sensorData) {
        String[] preparedText = sensorData.getDataForTextView();
        for (String item : preparedText) {
            Log.e(TAG_MAIN, item);
        }
        for (int i = 0; i < this.listOfTextViews.size(); i++) {
            this.listOfTextViews.get(i).setText(preparedText[i]);
        }
    }

    private void setParagraphText(int co2) {
        if (Objects.isNull(this.paragraphTextView))
            this.paragraphTextView = findViewById(R.id.mainpageParagraphText);
        if (co2 <= 1400) {
            this.paragraphTextView.setText(R.string.co2_paragraph_text_ok);
            return;
        }
        this.paragraphTextView.setText(R.string.co2_paragraph_text_warning);
    }

    private void setLastUpdateText(String lastUpdate) {
        if (Objects.isNull(this.lastUpdateTextView))
            this.lastUpdateTextView = findViewById(R.id.lastUpdateText);
        String lastUpdateFull = "Last update: " + lastUpdate;
        this.lastUpdateTextView.setText(lastUpdateFull);
    }

    private void updateUI(SensorData sensorData) {
        setParagraphText(sensorData.getCo2_mhz());
        setProgressBars(sensorData);
        setCardTextViews(sensorData);
        setLastUpdateText(sensorData.getDate_time());
    }

    public void openConfigPage(View view) {
        Intent intent = new Intent(this, SettingsPageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void showToastConnectionError() {
        Toast.makeText(this, "Please check server settings.", Toast.LENGTH_LONG).show();
    }

    private void showToastMhZ19bError() {
        Toast.makeText(this, "Please check MH-Z19B connection.", Toast.LENGTH_LONG).show();
    }

    private void showToastBme280Error() {
        Toast.makeText(this, "Please check BME280 connection.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        getDataFromServer();
        this.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFields();
        onRefresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDefaultValues();
        loadDataFields();
        onRefresh();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Goodbye! See you later!", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}