package com.hfad.moneydebtor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText dateTake;
    EditText dateGive;
    EditText nameUser;
    EditText summa;
    ToggleButton switchDebtor;
    MoneyDebtorDBHelper db;
    String idIntent;
    long dateTakeNumber;
    long dateGiveNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dateTake = (EditText) findViewById(R.id.edit_date_take);
        dateGive = (EditText) findViewById(R.id.edit_date_give);
        DatePickerDialog.OnDateSetListener dateTakeDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                setDateTake();
                dateTakeNumber = myCalendar.getTime().getTime();
            }
        };

        DatePickerDialog.OnDateSetListener dateGiveDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                setDateGive();
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

    }

    private void setDateTake(){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateTake.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void setDateGive(){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateGive.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void addNewRecord(View view) {
        nameUser = findViewById(R.id.edit_name);
        String nameText = nameUser.getText().toString();
        summa = findViewById(R.id.edit_summa);
        double summaText = Double.parseDouble(summa.getText().toString());

        long idNewUser = db.insertUsers(nameText, summaText);
        boolean insertDetail = db.insertUsersDetail(idNewUser,
                dateTakeNumber,
                Double.parseDouble(summa.getText().toString()),
                dateGiveNumber);
        if (insertDetail) {
            Toast.makeText(this, "insert detail", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "not insert detail", Toast.LENGTH_SHORT).show();
        }

        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("id_intent");
            switch (value) {
                case "MainActivity": {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
                case "DetailActivity": {
                    Intent intent = new Intent(this, DetailActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }

    }
}