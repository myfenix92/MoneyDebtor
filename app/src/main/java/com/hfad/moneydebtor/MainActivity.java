package com.hfad.moneydebtor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewUsers;
    List<UsersDataset> usersDatasetList = new ArrayList<>();
    MoneyDebtorDBHelper db;
    UsersAdapter usersAdapter;
    Cursor cursor;
    private final String ID_ACTIVITY = "MainActivity";
    private boolean isView = true;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    boolean isSort = false;
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.view_choose && isView) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            recyclerViewUsers.setLayoutManager(layoutManager);
            menuItem.setIcon(R.drawable.grid);
            isView = false;
        } else if (menuItem.getItemId() == R.id.view_choose && !isView) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                    2, LinearLayoutManager.VERTICAL);
            recyclerViewUsers.setLayoutManager(staggeredGridLayoutManager);
            menuItem.setIcon(R.drawable.list);
            isView = true;
        }

        if (menuItem.getItemId() == R.id.sort_name_asc) {
            Collections.sort(usersDatasetList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((UsersDataset) o1).getName_user()
                            .compareTo(((UsersDataset) o2).getName_user());
                }
            });
            usersAdapter.notifyDataSetChanged();
        } else if (menuItem.getItemId() == R.id.sort_name_desc) {
            Collections.sort(usersDatasetList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((UsersDataset) o2).getName_user()
                            .compareTo(((UsersDataset) o1).getName_user());
                }
            });
            usersAdapter.notifyDataSetChanged();
        } else if (menuItem.getItemId() == R.id.sort_summa_asc) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                usersDatasetList.sort(new Comparator<UsersDataset>() {
                    @Override
                    public int compare(UsersDataset o1, UsersDataset o2) {
                        if (Double.compare(o1.getAll_summa(), o2.getAll_summa()) == -1) {
                            return -1;
                        }
                        return 0;
                    }
                });
            }
            usersAdapter.notifyDataSetChanged();
        } else if (menuItem.getItemId() == R.id.sort_summa_desc) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                usersDatasetList.sort(new Comparator<UsersDataset>() {
                    @Override
                    public int compare(UsersDataset o1, UsersDataset o2) {
                        if (Double.compare(o1.getAll_summa(), o2.getAll_summa()) == 1) {
                            return -1;
                        }
                        return 0;
                    }
                });
            }
            usersAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(menuItem);
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