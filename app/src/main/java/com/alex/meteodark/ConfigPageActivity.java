package com.alex.meteodark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class ConfigPageActivity extends AppCompatActivity {
    private String[] dataFields;
    private ArrayList<EditText> listOfFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configpage);
        getWindow().setNavigationBarColor(getColor(R.color.navbar));
        initListOfFields();
    }

    private void initListOfFields() {
        if (Objects.isNull(this.listOfFields)) this.listOfFields = new ArrayList<>();
        listOfFields.add(findViewById(R.id.configpageField1Et));
        listOfFields.add(findViewById(R.id.configpageField2Et));
        listOfFields.add(findViewById(R.id.configpageField3Et));
        listOfFields.add(findViewById(R.id.configpageField4Et));
    }

    private boolean checkEditTextViews() {
        this.dataFields = new String[this.listOfFields.size()];
        int portNum = Integer.parseInt(String.valueOf(this.listOfFields.get(1).getText()));
        if (portNum < 0 || portNum > 65535) {
            Toast.makeText(this, "Range of available ports: 0 - 65535", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < this.listOfFields.size(); i++) {
            if (this.listOfFields.get(i).getText().toString().isEmpty()) {
                Toast.makeText(this, "Please, fill in all required fields.", Toast.LENGTH_SHORT).show();
                return false;
            }
            this.dataFields[i] = this.listOfFields.get(i).getText().toString();
        }
        return true;
    }

    private void saveDataToSharedPref(String[] dataFields) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.shared_data_fields), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.shared_ip_key), dataFields[0]);
        editor.putString(getString(R.string.shared_port_key), dataFields[1]);
        editor.putString(getString(R.string.shared_path_key), dataFields[2]);
        editor.putString(getString(R.string.shared_name_key), dataFields[3]);
        editor.apply();
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