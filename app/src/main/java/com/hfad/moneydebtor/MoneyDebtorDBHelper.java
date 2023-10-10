package com.hfad.moneydebtor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoneyDebtorDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "moneydebtorDB1";
    private static final int DB_VERSION = 1;

    public MoneyDebtorDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE USERS(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT UNIQUE," +
                    "ALL_SUMMA REAL DEFAULT 0);");
            db.execSQL("CREATE TABLE DETAIL_USERS(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ID_USER INTEGER NOT NULL," +
                    "DATE_TAKE INTEGER DEFAULT CURRENT_DATE," +
                    "SUMMA REAL NOT NULL," +
                    "DATE_GIVE INTEGER," +
                    "CONSTRAINT USERS_FK \n" +
                    "    FOREIGN KEY (ID_USER) REFERENCES USERS (_id) ON DELETE CASCADE);");
        }
        if (oldVersion == 2) {
        }
    }

    public long insertUsers(String user_name, double all_summa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues usersValues = new ContentValues();
        usersValues.put("NAME", user_name);
        usersValues.put("ALL_SUMMA", all_summa);
        long res = db.insert("USERS", null, usersValues);
        if (res == -1) {
            return 0;
        } else {
            return res;
        }
    }

    public Boolean insertUsersDetail(long id_user, long date_take, double summa, long date_give) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues usersDetailValues = new ContentValues();
        usersDetailValues.put("ID_USER", id_user);
        usersDetailValues.put("DATE_TAKE", date_take);
        usersDetailValues.put("SUMMA", summa);
        usersDetailValues.put("DATE_GIVE", date_give);
        long res = db.insert("DETAIL_USERS", null, usersDetailValues);
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateUsers(int _id, String name_user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues usersValues = new ContentValues();
        usersValues.put("NAME", name_user);
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE _id = ?",
                new String[]{String.valueOf(_id)});
        if (cursor.getCount() > 0) {
            long result = db.update("USERS", usersValues, "_id = ? ",
                    new String[]{String.valueOf(_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updateUsers(int _id, double all_summa)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues usersValues = new ContentValues();
        usersValues.put("ALL_SUMMA", all_summa);
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE _id = ?",
                new String[]{String.valueOf(_id)});
        if (cursor.getCount() > 0) {
            long result = db.update("USERS", usersValues, "_id = ? ",
                    new String[]{String.valueOf(_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updateUsersDetail(int _id, int id_user, long date_take, double summa, long date_give)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues usersDetailValues = new ContentValues();
        usersDetailValues.put("DATE_TAKE", date_take);
        usersDetailValues.put("SUMMA", summa);
        usersDetailValues.put("DATE_GIVE", date_give);
        Cursor cursor = db.rawQuery("SELECT * FROM DETAIL_USERS WHERE _id = ? AND ID_USER = ?",
                new String[]{String.valueOf(_id)});
        if (cursor.getCount() > 0) {
            long result = db.update("DETAIL_USERS", usersDetailValues, "_id = ? " +
                            "AND ID_USER = ?",
                    new String[]{String.valueOf(_id), String.valueOf(id_user)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean deleteData(int _id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE _id = ?",
                new String[]{String.valueOf(_id)});
        if (cursor.getCount() > 0) {
            long result = db.delete("USERS", "_id = ?",
                    new String[]{String.valueOf(_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Cursor getDataUsers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS", null);
        return  cursor;
    }

    public int getUniqueName(String userName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE NAME = ?;",
                new String[]{String.valueOf(userName)});
        return cursor.getCount();
    }

    public Cursor getDataUsers(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE _id = ?",
                new String[]{String.valueOf(id)});
        return  cursor;
    }

    public Cursor getDataDetailUsers(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DETAIL_USERS WHERE ID_USER = ?",
                new String[]{String.valueOf(id)});
        return  cursor;
    }
}
