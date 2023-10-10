package com.hfad.moneydebtor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    RecyclerView recyclerViewUsersDetail;
    List<UsersDetailDataset> usersDetailDatasetList = new ArrayList<>();
    MoneyDebtorDBHelper db;
    UsersDetailAdapter usersDetailAdapter;
    Cursor cursor;
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_ALL_SUMMA = "user_all_summa";
    private static final String ID_ACTIVITY = "DetailActivity";
    int userId;
    String userName;
    double userAllSumma;
    TextView nameUser;
    TextView allSummaUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        userId = (int) getIntent().getExtras().get(USER_ID);
        userName = (String) getIntent().getExtras().get(USER_NAME);
        userAllSumma = (double) getIntent().getExtras().get(USER_ALL_SUMMA);

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
        nameUser = findViewById(R.id.user_name_detail);
        allSummaUser = findViewById(R.id.user_all_summa);
        nameUser.setText(userName);
        allSummaUser.setText(String.valueOf(userAllSumma));
        EditText nameEditText = findViewById(R.id.edit_name_detail);

        displayDataDetail();
        nameUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEditText.post(new Runnable() {
                    @Override
                    public void run() {
                        DetailActivity.this.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        nameEditText.requestFocus();
                    }
                });
                TextViewClicked();

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        //...
        return false;
    }

    public void TextViewClicked() {
        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.switch_name);
        switcher.showNext();
        EditText nameEditText = findViewById(R.id.edit_name_detail);
        nameEditText.setText(nameUser.getText().toString());
//        nameEditText.post(new Runnable() {
//            @Override
//            public void run() {
//                DetailActivity.this.getWindow().setSoftInputMode(
//                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                nameEditText.requestFocus();
//            }
//        });

    }

    private List<UsersDetailDataset> displayDataDetail() {
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
                                cursor.getLong(4),
                                cursor.getDouble(3) > 0
                        ));
            }
        }
        return usersDetailDatasetList;
    }

    public void addNewUser(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id_intent", ID_ACTIVITY);
        intent.putExtra(DetailActivity.USER_ID, userId);
        intent.putExtra(DetailActivity.USER_NAME, userName);
        intent.putExtra(DetailActivity.USER_ALL_SUMMA, userAllSumma);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}