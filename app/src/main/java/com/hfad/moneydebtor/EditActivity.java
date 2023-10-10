package com.hfad.moneydebtor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText dateTake;
    EditText dateGive;
    EditText nameUser;
    EditText summa;
    ToggleButton switchDebtor;
    MoneyDebtorDBHelper db;

    long dateTakeNumber;
    long dateGiveNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dateTake = (EditText) findViewById(R.id.edit_date_take);
        dateGive = (EditText) findViewById(R.id.edit_date_give);
        switchDebtor = findViewById(R.id.switch_btn);
        TextView textDateGive = findViewById(R.id.dateGiveText);
        TextView textDateTake = findViewById(R.id.dateTakeText);
        summa = findViewById(R.id.edit_summa);
        setDate(dateTake);
        summa.setText("0");
        dateTakeNumber = myCalendar.getTime().getTime();
        String value = getIntent().getStringExtra("id_intent");
        String nameUserText = getIntent().getStringExtra(DetailActivity.USER_NAME);
        nameUser = findViewById(R.id.edit_name);
        nameUser.post(new Runnable() {
            @Override
            public void run() {
                EditActivity.this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                nameUser.requestFocus();
            }
        });

        if (Objects.equals(value, "DetailActivity")) {
            Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
            nameUser.setText(nameUserText);
            nameUser.setEnabled(false);
        }

        DatePickerDialog.OnDateSetListener dateTakeDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                setDate(dateTake);
                dateTakeNumber = myCalendar.getTime().getTime();
            }
        };

        DatePickerDialog.OnDateSetListener dateGiveDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                setDate(dateGive);
                dateGiveNumber = myCalendar.getTime().getTime();

            }
        };

        dateTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditActivity.this, dateTakeDialog, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dateGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditActivity.this, dateGiveDialog, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        db = new MoneyDebtorDBHelper(this);
        switchDebtor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {

                    textDateTake.setText("Когда отдали:");
                    textDateGive.setVisibility(View.INVISIBLE);
                    dateGive.setVisibility(View.INVISIBLE);
                    dateGive.setText("");
                } else {
                    textDateTake.setText("Когда взяли:");
                    textDateGive.setVisibility(View.VISIBLE);
                    dateGive.setVisibility(View.VISIBLE);
                }
            }
        });

        summa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Считываем вводимый текст
                String str = editable.toString();
                //Узнаем позицию
                int position = str.indexOf(".");
                //Если точка есть делаем проверки
                if (position != -1) {
                    //Отрезаем кусок строки начиная с точки и до конца строки
                    String subStr = str.substring(position);
                    //Отрезаем строку с начала и до точки
                    String subStrStart = str.substring(0, position);
                    //Если символов после точки больше чем 3 или если точка первая в строке - удаляем последний
                    if (subStr.length() > 3 || subStrStart.length() == 0) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }
            }
        });

    }

    private void setDate(EditText dateText){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateText.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void addNewRecord(View view) {
        nameUser = findViewById(R.id.edit_name);
        String nameText = nameUser.getText().toString();
        summa = findViewById(R.id.edit_summa);
        double summaText = Double.parseDouble(summa.getText().toString());
        String value = getIntent().getStringExtra("id_intent");
        switchDebtor = findViewById(R.id.switch_btn);
        if (!switchDebtor.isChecked()) {
            summaText *= -1;
        }
        switch (value) {
            case "MainActivity": {
                int checkName = db.getUniqueName(nameText);
                if (nameText.isEmpty()) {
                    Toast.makeText(this, "Имя не может быть пустым", Toast.LENGTH_SHORT).show();
                } else if (checkName == 1) {
                    Toast.makeText(this, "Такое имя уже существует", Toast.LENGTH_SHORT).show();
                } else {
                    long idNewUser = db.insertUsers(nameText, summaText);
                    boolean insertDetail = db.insertUsersDetail(idNewUser,
                            dateTakeNumber,
                            summaText,
                            dateGiveNumber);
                    if (insertDetail) {
                        Toast.makeText(this, "insert detail", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "not insert detail", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            }

            case "DetailActivity": {
                int idUser = getIntent().getIntExtra(DetailActivity.USER_ID, 0);
                String nameUser = getIntent().getStringExtra(DetailActivity.USER_NAME);
                double allSummaUser = getIntent().getDoubleExtra(DetailActivity.USER_ALL_SUMMA, 0);

                db.insertUsersDetail(idUser,
                        dateTakeNumber,
                        summaText,
                        dateGiveNumber);
                db.updateUsers(idUser, (allSummaUser + summaText));
                String summaCut = String.format("%.2f", (allSummaUser + summaText));
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.USER_ID, idUser);
                intent.putExtra(DetailActivity.USER_NAME, nameUser);
                intent.putExtra(DetailActivity.USER_ALL_SUMMA, Double.parseDouble(summaCut));
                startActivity(intent);
                break;
            }
        }
    }

    public void cancelRecord(View view) {
        nameUser = findViewById(R.id.edit_name);
        summa = findViewById(R.id.edit_summa);
        switchDebtor = findViewById(R.id.switch_btn);
        nameUser.setText("");
        dateTake.setText("");
        dateGive.setText("");
        summa.setText("");
        switchDebtor.setChecked(true);
     //   Bundle extras = getIntent().getExtras();
     //   if (extras != null) {
            String value = getIntent().getStringExtra("id_intent");
            int idUser = getIntent().getIntExtra(DetailActivity.USER_ID, 0);
            String nameUser = getIntent().getStringExtra(DetailActivity.USER_NAME);
            double allSummaUser = getIntent().getDoubleExtra(DetailActivity.USER_ALL_SUMMA, 0);
            switch (value) {
                case "MainActivity": {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
                case "DetailActivity": {
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra(DetailActivity.USER_ID, idUser);
                    intent.putExtra(DetailActivity.USER_NAME, nameUser);
                    intent.putExtra(DetailActivity.USER_ALL_SUMMA, allSummaUser);
                    startActivity(intent);
                    break;
                }
            }
    //    }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}