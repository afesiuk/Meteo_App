package com.alex.meteodark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class ServerPageActivity extends AppCompatActivity {
    private ArrayList<EditText> fields;
    private String[] dataFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serverpage);
        initListOfEditTexts();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initListOfEditTexts() {
        if (Objects.isNull(this.fields)) this.fields = new ArrayList<>();
        this.fields.add(findViewById(R.id.serverpageEt1));
        this.fields.add(findViewById(R.id.serverpageEt2));
        this.fields.add(findViewById(R.id.serverpageEt3));
    }

    private boolean checkFields() {
        this.dataFields = new String[this.fields.size()];
        for (int i = 0; i < dataFields.length; i++) {
            this.dataFields[i] = this.fields.get(i).getText().toString();
            if (this.dataFields[i].isEmpty()) {
                return false;
            }
        }
        int port = Integer.parseInt(this.dataFields[1]);
        if (port < 0 || port > 65535) {
            Toast.makeText(this, "Range of available ports: 0 - 65535", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveData(String[] dataFields) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.shared_data_fields), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.shared_ip_key), dataFields[0]);
        editor.putString(getString(R.string.shared_port_key), dataFields[1]);
        editor.putString(getString(R.string.shared_path_key), dataFields[2]);
        editor.apply();
    }

    public void applyChanges(View view) {
        if (!checkFields()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        saveData(this.dataFields);
        Toast.makeText(this, "Changes have been applied", Toast.LENGTH_SHORT).show();
    }
}