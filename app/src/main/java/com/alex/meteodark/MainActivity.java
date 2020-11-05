package com.alex.meteodark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_MAIN = "Main_Activity";

    public static final String IP_EXTRA = "ip-address-extra";
    public static final String PORT_EXTRA = "port-extra";
    public static final String PATH_EXTRA = "post-path-extra";
    public static final String NAME_EXTRA = "name-exta";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = findViewById(R.id.card1TempBar1);
        progressBar.setProgress(10);
    }
}