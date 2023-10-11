package com.hfad.moneydebtor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
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
    public static final String USER_NAME = "user_name";
    public static final String USER_ALL_SUMMA = "user_all_summa";
    private static final String ID_ACTIVITY = "DetailActivity";
    private static final String ID_ACTIVITY_EDIT = "DetailActivityEdit";
    int userId;
    String userName;
    double userAllSumma;
    TextView nameUser;
    TextView allSummaUser;
    private String m_Text;
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
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("id_intent", ID_ACTIVITY_EDIT);
                intent.putExtra(DetailActivity.USER_ID, userId);
                intent.putExtra(DetailActivity.USER_NAME, userName);
                intent.putExtra(DetailActivity.USER_ALL_SUMMA, userAllSumma);
                intent.putExtra("id_record", usersDetailDatasetList
                        .get(position).getId_record());
                intent.putExtra("date_take", usersDetailDatasetList
                        .get(position).getDate_take());
                intent.putExtra("date_give", usersDetailDatasetList
                        .get(position).getDate_give());
                intent.putExtra("summa", usersDetailDatasetList.get(position).getSumma());
                intent.putExtra("color", usersDetailDatasetList.get(position).getColor());
                startActivity(intent);
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
        displayDataDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void dialogDeleteUser(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promtView = layoutInflater.inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promtView);
        builder.setIcon(android.R.drawable.ic_delete);
        final TextView textTitle = promtView.findViewById(R.id.title_alert);
        textTitle.setText(R.string.title_delete_alert);
        final TextView textDescription = promtView.findViewById(R.id.text_alert);
        textDescription.setText(R.string.text_delete_alert);
        final EditText changeName = promtView.findViewById(R.id.input_change_name);
        changeName.setVisibility(View.GONE);
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteUser(userId);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void dialogChangeNameUser(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promtView = layoutInflater.inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promtView);
        builder.setIcon(android.R.drawable.ic_menu_edit);
        final TextView textTitle = promtView.findViewById(R.id.title_alert);
        textTitle.setText(R.string.title_change_name_alert);
        final TextView textDescription = promtView.findViewById(R.id.text_alert);
        textDescription.setVisibility(View.GONE);
        final EditText changeName = promtView.findViewById(R.id.input_change_name);
        nameUser = findViewById(R.id.user_name_detail);
        changeName.setText(nameUser.getText().toString());
        changeName.setSelection(changeName.getText().length());
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeName.clearFocus();
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (changeName.length() > 0) {
                    m_Text = changeName.getText().toString();
                    db.updateUsers(userId, m_Text);
                    nameUser.setText(m_Text);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(DetailActivity.this, R.string.empty_error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.delete_user) {
            CoordinatorLayout coordinatorLayoutDetail = findViewById(R.id.coordinator_detail);
            dialogDeleteUser(coordinatorLayoutDetail);
        }
        if (menuItem.getItemId() == R.id.change_name_user) {
            CoordinatorLayout coordinatorLayoutDetail = findViewById(R.id.coordinator_detail);
            dialogChangeNameUser(coordinatorLayoutDetail);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private List<UsersDetailDataset> displayDataDetail() {
        cursor = db.getDataDetailUsers(userId);
        if (cursor.getCount() == 0) {
            usersDetailDatasetList.clear();
            Toast.makeText(this, R.string.empty_db, Toast.LENGTH_SHORT).show();
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