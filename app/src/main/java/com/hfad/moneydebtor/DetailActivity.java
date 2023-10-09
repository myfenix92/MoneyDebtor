package com.hfad.moneydebtor;

import androidx.appcompat.app.ActionBar;
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
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    RecyclerView recyclerViewUsersDetail;
    List<UsersDetailDataset> usersDetailDatasetList = new ArrayList<>();
    MoneyDebtorDBHelper db;
    UsersDetailAdapter usersDetailAdapter;
    Cursor cursor;
    public static final String USER_ID = "user_id";
    private static final String ID_ACTIVITY = "DetailActivity";
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        userId = (int) getIntent().getExtras().get(USER_ID);
        Toast.makeText(this, String.valueOf(userId), Toast.LENGTH_SHORT).show();
        UsersDetailAdapter.Listener listener = new UsersDetailAdapter.Listener() {
            @Override
            public void onEditClick(UsersDetailDataset data, int position) {

            }
        };
        recyclerViewUsersDetail = findViewById(R.id.recycler_user_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewUsersDetail.setLayoutManager(linearLayoutManager);
        usersDetailAdapter = new UsersDetailAdapter(this,
                usersDetailDatasetList, listener);
        db = new MoneyDebtorDBHelper(this);
        recyclerViewUsersDetail.setAdapter(usersDetailAdapter);
        displayData();

    }

    private List<UsersDetailDataset> displayData() {

        cursor = db.getDataDetailUsers(userId);
        if (cursor.getCount() == 0) {
            usersDetailDatasetList.clear();
            Toast.makeText(this, "no entry exists", Toast.LENGTH_SHORT).show();
        } else {
            usersDetailDatasetList.clear();
            while (cursor.moveToNext()) {
                usersDetailDatasetList.add(
                        new UsersDetailDataset(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getLong(2),
                                cursor.getDouble(3),
                                cursor.getLong(4)
                        ));
            }
        }
        return usersDetailDatasetList;
    }

    public void addNewUser(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id_intent", ID_ACTIVITY);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}