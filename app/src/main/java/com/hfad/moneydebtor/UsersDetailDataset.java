package com.hfad.moneydebtor;

public class UsersDetailDataset {
    private int id_record;
    private int id_user;
    private int date_take;
    private double summa;
    private int date_give;

    public UsersDetailDataset(int idRecord, int idUser, int dateTake, double summa, int dateGive) {
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

    public int getDate_take() {
        return date_take;
    }

    public void setDate_take(int date_take) {
        this.date_take = date_take;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public int getDate_give() {
        return date_give;
    }

    public void setDate_give(int date_give) {
        this.date_give = date_give;
    }
}
