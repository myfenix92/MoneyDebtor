package com.hfad.moneydebtor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UsersDetailDataset {
    private int id_record;
    private int id_user;
    private long date_take;
    private double summa;
    private long date_give;

    public UsersDetailDataset(int idRecord, int idUser, long dateTake, double summa, long dateGive) {
        id_record = idRecord;
        id_user = idUser;
        date_take = dateTake;
        this.summa = summa;
        date_give = dateGive;
    }

    public int getId_record() {
        return id_record;
    }

    public void setId_record(int id_record) {
        this.id_record = id_record;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getDate_take() {
        Calendar myCalendar = Calendar.getInstance();
        String myFormat="dd.MM.yyyy";
        String dateText;
        myCalendar.setTimeInMillis(date_take);
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateText = dateFormat.format(myCalendar.getTime());
        return dateText;
    }

    public void setDate_take(long date_take) {
        this.date_take = date_take;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public String getDate_give() {
        Calendar myCalendar = Calendar.getInstance();
        String myFormat="dd.MM.yyyy";
        String dateText;
        myCalendar.setTimeInMillis(date_give);
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dateText = dateFormat.format(myCalendar.getTime());
        return dateText;
    }

    public void setDate_give(long date_give) {
        this.date_give = date_give;
    }
}
