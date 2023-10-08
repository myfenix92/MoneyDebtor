package com.hfad.moneydebtor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DetailActivity extends AppCompatActivity {
    private static final String ID_ACTIVITY = "DetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    public void addNewUser(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id_intent", ID_ACTIVITY);
        startActivity(intent);
    }
}