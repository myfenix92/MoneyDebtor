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
    private boolean color;

    public UsersDetailDataset(int idRecord, int idUser, long dateTake, double summa, long dateGive, boolean color) {
        id_record = idRecord;
        id_user = idUser;
        date_take = dateTake;
        this.summa = summa;
        date_give = dateGive;
        this.color = color;
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

    public void setDate_take(long date_take) {
        this.date_take = date_take;
    }

    public double getSumma() {
        if (summa < 0) {
            return summa * -1;
        }
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public String getDateString(String nameDate) {
        Calendar myCalendar = Calendar.getInstance();
        String myFormat="dd.MM.yyyy";
        String dateText = "";
        switch (nameDate) {
            case "date_take": {
                myCalendar.setTimeInMillis(date_take);
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                dateText = dateFormat.format(myCalendar.getTime());
                break;
            }
            case "date_give": {
                if (date_give == 0) {
                    dateText = "";
                } else {
                    myCalendar.setTimeInMillis(date_give);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                    dateText = dateFormat.format(myCalendar.getTime());
                }
                break;
            }
        }

        return dateText;
    }
    public long getDate_take() {
        return date_take;
    }
    public long getDate_give() {
        return date_give;
    }

    public void setDate_give(long date_give) {
        this.date_give = date_give;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }
}
