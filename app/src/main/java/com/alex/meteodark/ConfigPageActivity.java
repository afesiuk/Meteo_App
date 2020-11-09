package com.alex.meteodark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ConfigPageActivity extends AppCompatActivity {
    private String[] dataFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configpage);
    }

    private ArrayList<EditText> getListOfEditText() {
        ConstraintLayout layout = findViewById(R.id.configpage);
        ArrayList<EditText> listOfEditText = new ArrayList<>();

        for (int i = 0; i < layout.getChildCount(); i++) {
            if (layout.getChildAt(i) instanceof EditText) {
                listOfEditText.add((EditText) layout.getChildAt(i));
            }
        }
        return listOfEditText;
    }

    private boolean checkEditTextViews() {
        ArrayList<EditText> list = getListOfEditText();
        this.dataFields = new String[list.size()];
        int portNum = Integer.parseInt(String.valueOf(list.get(1).getText()));
        if (portNum < 0 || portNum > 65535) {
            Toast.makeText(this, "Range of available ports: 0 - 65535", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().toString().isEmpty()) {
                Toast.makeText(this, "Please, fill in all required fields.", Toast.LENGTH_SHORT).show();
                return false;
            }
            this.dataFields[i] = list.get(i).getText().toString();
        }
        return true;
    }

    private void saveDataToSharedPref(String[] dataFields) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.shared_data_fields), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.shared_ip_key), dataFields[0]);
        Log.d(getString(R.string.tag_configpage), "ip field: " + dataFields[0]);

        editor.putString(getString(R.string.shared_port_key), dataFields[1]);
        Log.d(getString(R.string.tag_configpage), "port field: " + dataFields[1]);

        editor.putString(getString(R.string.shared_path_key), dataFields[2]);
        Log.d(getString(R.string.tag_configpage), "path field: " + dataFields[2]);

        editor.putString(getString(R.string.shared_name_key), dataFields[3]);
        editor.apply();
        Log.d(getString(R.string.tag_configpage), "name field: " + dataFields[3]);
    }

    public void openMainPage(View view) {
        if (!checkEditTextViews()) return;
        saveDataToSharedPref(this.dataFields);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}