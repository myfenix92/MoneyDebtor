package com.hfad.moneydebtor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class EditActivity extends Helper {
    final Calendar myCalendar = Calendar.getInstance();
    EditText dateTake;
    EditText dateGive;
    EditText nameUser;
    EditText summa;
    ToggleButton switchDebtor;
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

        dateTake = findViewById(R.id.edit_date_take);
        dateGive = findViewById(R.id.edit_date_give);
        switchDebtor = findViewById(R.id.switch_btn);
        TextView textDateGive = findViewById(R.id.dateGiveText);
        TextView textDateTake = findViewById(R.id.dateTakeText);
        summa = findViewById(R.id.edit_summa);

        String value = getIntent().getStringExtra("id_intent");
        setEditText(value);

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

        switchDebtor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    textDateTake.setText(R.string.date_give_text);
                    textDateGive.setVisibility(View.INVISIBLE);
                    dateGive.setVisibility(View.INVISIBLE);
                    dateGive.setText("");
                } else {
                    textDateTake.setText(R.string.date_take_text);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setDate(EditText dateText){
        String myFormat="dd.MM.yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateText.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void setEditText(String value) {
        String nameUserText = getIntent().getStringExtra(DetailActivity.USER_NAME);
        nameUser = findViewById(R.id.edit_name);
        if (Objects.equals(value, "MainActivity")) {
            nameUser.setFocusable(true);
            EditActivity.this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            nameUser.requestFocus();
            setDate(dateTake);
            dateTakeNumber = myCalendar.getTime().getTime();
        } else if (Objects.equals(value, "DetailActivity")) {
            nameUser.setText(nameUserText);
            nameUser.setEnabled(false);
            summa.setFocusable(true);
            EditActivity.this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            summa.requestFocus();
            setDate(dateTake);
            summa.setSelection(summa.getText().length());
            dateTakeNumber = myCalendar.getTime().getTime();
        } else if (Objects.equals(value, "DetailActivityEdit")) {
            nameUser.setEnabled(false);
            summa.setFocusable(true);
            dateTakeNumber = getIntent().getLongExtra("date_take_long", 0);
            dateGiveNumber = getIntent().getLongExtra("date_give_long", 0);
            EditActivity.this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            summa.requestFocus();
            summa.post(new Runnable() {
                @Override
                public void run() {
                    summa.setSelection(summa.length());
                }
            });
            EditRecord();
        }
    }

    private void EditRecord() {
        TextView textDateGive = findViewById(R.id.dateGiveText);
        TextView textDateTake = findViewById(R.id.dateTakeText);
        boolean color = getIntent().getBooleanExtra("color", true);
        switchDebtor.setChecked(color);
        if (!color) {
            textDateTake.setText(R.string.date_give_text);
            textDateGive.setVisibility(View.INVISIBLE);
            dateGive.setVisibility(View.INVISIBLE);
            dateGive.setText("");
        } else {
            textDateTake.setText(R.string.date_take_text);
            textDateGive.setVisibility(View.VISIBLE);
            dateGive.setVisibility(View.VISIBLE);
        }

        String userName = getIntent().getStringExtra(DetailActivity.USER_NAME);
        nameUser.setText(userName);

        String date_take = getIntent().getStringExtra("date_take");
        dateTake.setText(date_take);

        String date_give = getIntent().getStringExtra("date_give");
        dateGive.setText(date_give);

        double summaRecord = getIntent().getDoubleExtra("summa", 0);
        summa.setText(String.valueOf(summaRecord));
    }

    public void addNewRecord(View view) {
        nameUser = findViewById(R.id.edit_name);
        String nameText = nameUser.getText().toString().trim();
        String value = getIntent().getStringExtra("id_intent");
        if (Objects.equals(value, "MainActivity") && checkNames(nameUser, R.string.empty_name)) {
            return;
        }

        summa = findViewById(R.id.edit_summa);
        double summaText;
        if (!checkEmpty(summa, R.string.empty_summa)) {
            summaText = Double.parseDouble(summa.getText().toString());
        } else {
            return;
        }
        if (checkNull(summaText)) {
            return;
        }

        switchDebtor = findViewById(R.id.switch_btn);
        double startSumma = getIntent().getDoubleExtra("summa", 0);
        boolean color = getIntent().getBooleanExtra("color", true);
        if (!switchDebtor.isChecked()) {
            summaText *= -1;
            dateGive.setText("");
            dateGiveNumber = 0;
        }
        switch (value) {
            case "MainActivity": {
                    long idNewUser = db.insertUsers(nameText, summaText);
                    db.insertUsersDetail(idNewUser,
                            dateTakeNumber,
                            summaText,
                            dateGiveNumber);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                break;
            }

            case "DetailActivity": {
                int idUser = getIntent().getIntExtra(DetailActivity.USER_ID, 0);
                String nameUser = getIntent().getStringExtra(DetailActivity.USER_NAME);
                double allSummaUser = getIntent().getDoubleExtra(DetailActivity.USER_ALL_SUMMA, 0);
                db.insertUsersDetail(idUser, dateTakeNumber, summaText, dateGiveNumber);
                db.updateUsers(idUser, (allSummaUser + summaText));
                String summaCut = String.format(Locale.US, "%.2f", (allSummaUser + summaText));
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.USER_ID, idUser);
                intent.putExtra(DetailActivity.USER_NAME, nameUser);
                intent.putExtra(DetailActivity.USER_ALL_SUMMA, Double.parseDouble(summaCut));
                startActivity(intent);
                break;
            }

            case "DetailActivityEdit": {
                int idUser = getIntent().getIntExtra(DetailActivity.USER_ID, 0);
                int idRecord = getIntent().getIntExtra("id_record", 0);
                String nameUser = getIntent().getStringExtra(DetailActivity.USER_NAME);
                double allSummaUser = getIntent().getDoubleExtra(DetailActivity.USER_ALL_SUMMA, 0);
                double tempSumma;
                if (color) {
                    tempSumma = allSummaUser - startSumma + summaText;
                } else {
                    tempSumma = allSummaUser + startSumma + summaText;
                }
                db.updateUsers(idUser, tempSumma);
                db.updateUsersDetail(idRecord, dateTakeNumber, summaText, dateGiveNumber);
                String summaCut = String.format(Locale.US, "%.2f", tempSumma);
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
                case "DetailActivity":
                case "DetailActivityEdit": {
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra(DetailActivity.USER_ID, idUser);
                    intent.putExtra(DetailActivity.USER_NAME, nameUser);
                    intent.putExtra(DetailActivity.USER_ALL_SUMMA, allSummaUser);
                    startActivity(intent);
                    break;
                }
            }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        String value = getIntent().getStringExtra("id_intent");
        int idUser = getIntent().getIntExtra(DetailActivity.USER_ID, 0);
        String nameUser = getIntent().getStringExtra(DetailActivity.USER_NAME);
        double allSummaUser = getIntent().getDoubleExtra(DetailActivity.USER_ALL_SUMMA, 0);
        switch (value) {
            case "MainActivity": {
                finish();
                break;
            }
            case "DetailActivity":
            case "DetailActivityEdit": {
                finish();
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.USER_ID, idUser);
                intent.putExtra(DetailActivity.USER_NAME, nameUser);
                intent.putExtra(DetailActivity.USER_ALL_SUMMA, allSummaUser);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}