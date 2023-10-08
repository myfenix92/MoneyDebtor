package com.hfad.moneydebtor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final String MAIN_ACTIVITY = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addNewUser(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id_intent", MAIN_ACTIVITY);
        startActivity(intent);
    }
}