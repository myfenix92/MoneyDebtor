package com.hfad.moneydebtor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
        ed.apply();
    }


    private void loadText() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        int savedIdMenu = sPref.getInt("saved_id_menu", 0);
        isView = sPref.getBoolean("save_isView", true);
        sortMenu(savedIdMenu);
    }

    private void viewMenu(boolean isViewBool) {
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
        viewMenu(isView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.view_choose) {
            if (isView) {
                menuItem.setIcon(R.drawable.grid);
                isView = false;
                viewMenu(false);
            } else {
                menuItem.setIcon(R.drawable.list);
                isView = true;
                viewMenu(true);
            }
        } else {
            if (menuItem.getGroupId() == R.id.group_sort) {
                sortMenu(menuItem.getItemId());
            }
        }
        if (menuItem.getItemId() == R.id.menu_about_app) {
            CoordinatorLayout coordinatorLayoutDetail = findViewById(R.id.coordinator_main);
            aboutHelper(coordinatorLayoutDetail);
        }
        if (menuItem.getItemId() == R.id.menu_all_summa) {
            CoordinatorLayout coordinatorLayoutDetail = findViewById(R.id.coordinator_main);
            countAllSummaDialog(coordinatorLayoutDetail);
        }
        saveText(menuItem.getItemId(), isView);
        return super.onOptionsItemSelected(menuItem);
    }

    public void aboutHelper(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promtView = layoutInflater.inflate(R.layout.alert_about, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.sym_action_chat);
        builder.setTitle(R.string.about_title);
        builder.setView(promtView);
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void countAllSumma(View view) {
        double allSummaRed = 0.0;
        double allSummaGreen = 0.0;
        for (int i = 0; i < usersDatasetList.size(); i++) {
            if (usersDatasetList.get(i).getAll_summa() > 0) {
                allSummaRed += usersDatasetList.get(i).getAll_summa();
            } else {
                allSummaGreen += usersDatasetList.get(i).getAll_summa();
            }
        }
        TextView allRedText = view.findViewById(R.id.all_summa_red);
        allRedText.setText(String.valueOf(allSummaRed));
        TextView allGreenText = view.findViewById(R.id.all_summa_green);
        allGreenText.setText(String.valueOf(allSummaGreen * -1));
    }

    public void countAllSummaDialog(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promtView = layoutInflater.inflate(R.layout.alert_all_summa, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.stat_notify_more);
        builder.setTitle(R.string.menu_all_summa);
        countAllSumma(promtView);
        builder.setView(promtView);
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void addNewUser(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        String ID_ACTIVITY = "MainActivity";
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
        cursor.close();
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