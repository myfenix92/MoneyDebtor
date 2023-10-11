package com.hfad.moneydebtor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewUsers;
    List<UsersDataset> usersDatasetList = new ArrayList<>();
    MoneyDebtorDBHelper db;
    UsersAdapter usersAdapter;
    Cursor cursor;
    private final String ID_ACTIVITY = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UsersAdapter.Listener listener = new UsersAdapter.Listener() {
            @Override
            public void onViewClick(UsersDataset data, int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.USER_ID, usersDatasetList
                        .get(position).getId_user());
                intent.putExtra(DetailActivity.USER_NAME, usersDatasetList
                        .get(position).getName_user());
                intent.putExtra(DetailActivity.USER_ALL_SUMMA, usersDatasetList.get(position)
                        .getAll_summa());
                startActivity(intent);
            }
        };

        recyclerViewUsers = findViewById(R.id.recycler_users_list);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2, LinearLayoutManager.VERTICAL);
        recyclerViewUsers.setLayoutManager(staggeredGridLayoutManager);
        usersAdapter = new UsersAdapter(this, usersDatasetList, listener);
        db = new MoneyDebtorDBHelper(this);
        recyclerViewUsers.setAdapter(usersAdapter);
        displayData();
    }

    public void addNewUser(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id_intent", ID_ACTIVITY);
        startActivity(intent);
    }

    private void displayData() {
        cursor = db.getDataUsers();
        if (cursor.getCount() == 0) {
            usersDatasetList.clear();
            Toast.makeText(this, R.string.empty_db, Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}