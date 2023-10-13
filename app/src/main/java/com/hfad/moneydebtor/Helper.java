package com.hfad.moneydebtor;

import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Helper extends AppCompatActivity {
    MoneyDebtorDBHelper db = new MoneyDebtorDBHelper(this);

    public Toast toastEmptyEdit(String errorText) {
        return Toast.makeText(this, errorText, Toast.LENGTH_SHORT);
    }

    public boolean checkEmpty(EditText editText, int text) {
        if (editText.getText().toString().isEmpty()) {
            toastEmptyEdit(getResources().getText(text).toString()).show();
            return true;
        }
        return  false;
    }

    public boolean checkNames(EditText editText, int text) {
        if (checkEmpty(editText, text)) {
            return true;
        }
        if (db.getUniqueName(editText.getText().toString()) == 1) {
            toastEmptyEdit(getResources().getText(R.string.unique_name).toString()).show();
            return true;
        }
        return false;
    }

    public boolean checkNull(double value) {
        if (value == 0.0) {
            toastEmptyEdit(getResources().getText(R.string.zero_summa).toString()).show();
            return true;
        }
        return  false;
    }

}
