package com.hfad.moneydebtor;

public class UsersDataset {
    private int id_user;
    private String name_user;
    private double all_summa;

    public UsersDataset(int idUser, String nameUser, double allSumma) {
        id_user = idUser;
        name_user = nameUser;
        all_summa = allSumma;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public double getAll_summa() {
        if (all_summa < 0) {
            return all_summa * -1;
        }
        return all_summa;
    }

    public void setAll_summa(double all_summa) {
        this.all_summa = all_summa;
    }
}
