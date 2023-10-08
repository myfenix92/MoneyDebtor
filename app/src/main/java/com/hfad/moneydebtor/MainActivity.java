package com.hfad.moneydebtor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewUsers;
    List<UsersDataset> usersDatasetList = new ArrayList<>();
    MoneyDebtorDBHelper db;
    UsersAdapter usersAdapter;
    Cursor cursor;

    private final String MAIN_ACTIVITY = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewUsers = findViewById(R.id.recycler_users_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewUsers.setLayoutManager(linearLayoutManager);
        usersAdapter = new UsersAdapter(this, usersDatasetList);
        db = new MoneyDebtorDBHelper(this);
        recyclerViewUsers.setAdapter(usersAdapter);
        displayData();
    }

    public void addNewUser(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id_intent", MAIN_ACTIVITY);
        startActivity(intent);
    }

    private List<UsersDataset> displayData() {
        cursor = db.getDataUsers();
        if (cursor.getCount() == 0) {
            usersDatasetList.clear();
            Toast.makeText(this, "no entry exists", Toast.LENGTH_SHORT).show();
        } else {
            usersDatasetList.clear();
            while (cursor.moveToNext()) {
                usersDatasetList.add(
                        new UsersDataset(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getDouble(2)
                ));
            }
        }
        return usersDatasetList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}