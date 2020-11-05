package com.alex.meteodark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class ConfigPageActivity extends AppCompatActivity {
    private String [] fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configpage);
    }

    private boolean readFields() {
        ConstraintLayout layout = findViewById(R.id.configpage);
        ArrayList<EditText> editTextList = new ArrayList<>();

        for(int i = 0; i < layout.getChildCount(); i++) {
            if(layout.getChildAt(i) instanceof EditText) {
                editTextList.add((EditText) layout.getChildAt(i));
            }
        }

        if(Objects.isNull(fields)) fields = new String[editTextList.size()];

        for(int i = 0; i < fields.length; i++) {
            fields[i] = editTextList.get(i).getText().toString();
        }

        for(String field : fields) {
            if (field.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    public void openMainPage(View view) {
        if(!readFields()) return;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.IP_EXTRA, fields[0]);
        intent.putExtra(MainActivity.PORT_EXTRA, fields[1]);
        intent.putExtra(MainActivity.PATH_EXTRA, fields[2]);
        intent.putExtra(MainActivity.NAME_EXTRA, fields[3]);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}