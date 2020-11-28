package com.alex.meteodark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingspage);
        getWindow().setNavigationBarColor(getColor(R.color.navbar));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void openConfigPage(View view) {
        Intent intent = new Intent(this, ServerPageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void showDevelopmentMessage(View view) {
        Toast.makeText(this, "Sorry, this page is not available. The application is under development.", Toast.LENGTH_LONG).show();
    }
}