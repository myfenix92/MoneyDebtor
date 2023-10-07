package com.hfad.moneydebtor;

public class UsersDataset {
    private int id_user;
    private String name_user;

    public UsersDataset(int idUser, String nameUser) {
        id_user = idUser;
        name_user = nameUser;
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
}
