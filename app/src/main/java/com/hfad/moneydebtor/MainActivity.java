package com.hfad.moneydebtor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewUsers;
    List<UsersDataset> usersDatasetList = new ArrayList<>();
    MoneyDebtorDBHelper db;
    UsersAdapter usersAdapter;
    Cursor cursor;
    private final String ID_ACTIVITY = "MainActivity";
    private boolean isView;
    SharedPreferences sPref;

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

        usersAdapter = new UsersAdapter(this, usersDatasetList, listener);
        db = new MoneyDebtorDBHelper(this);
        recyclerViewUsers.setAdapter(usersAdapter);
        displayData();
        loadText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!isView) {
            menu.findItem(R.id.view_choose).setIcon(R.drawable.grid);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void saveText(int menuItem, boolean viewBool) {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("saved_id_menu", menuItem);
        ed.putBoolean("save_isView", viewBool);
        ed.commit();
    }


    private void loadText() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        int savedIdMenu = sPref.getInt("saved_id_menu", 0);
        isView = sPref.getBoolean("save_isView", true);
    //    viewMenu(savedIdMenu, isView);
        sortMenu(savedIdMenu);
    }

    private void viewMenu(int idMenuItem, boolean isViewBool) {
        if (isViewBool) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                    2, LinearLayoutManager.VERTICAL);
            recyclerViewUsers.setLayoutManager(staggeredGridLayoutManager);
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            recyclerViewUsers.setLayoutManager(layoutManager);
        }
    }

    private void sortMenu(int idItemMenu) {
        if (idItemMenu == R.id.sort_name_asc) {
            Collections.sort(usersDatasetList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((UsersDataset) o1).getName_user()
                            .compareTo(((UsersDataset) o2).getName_user());
                }
            });
            usersAdapter.notifyDataSetChanged();
        } else if (idItemMenu == R.id.sort_name_desc) {
            Collections.sort(usersDatasetList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((UsersDataset) o2).getName_user()
                            .compareTo(((UsersDataset) o1).getName_user());
                }
            });
            usersAdapter.notifyDataSetChanged();
        } else if (idItemMenu == R.id.sort_summa_asc) {
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
        } else if (idItemMenu == R.id.sort_summa_desc) {
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
        viewMenu(idItemMenu, isView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.view_choose) {
            if (isView) {
                menuItem.setIcon(R.drawable.grid);
                isView = false;
                viewMenu(menuItem.getItemId(), isView);
            } else {
                menuItem.setIcon(R.drawable.list);
                isView = true;
                viewMenu(menuItem.getItemId(), isView);
            }
        } else {
            if (menuItem.getGroupId() == R.id.group_sort) {
                sortMenu(menuItem.getItemId());
            }
        }
        saveText(menuItem.getItemId(), isView);
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
    public void onBackPressed(){
        finishAffinity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}