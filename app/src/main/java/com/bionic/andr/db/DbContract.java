package com.bionic.andr.db;

import android.provider.BaseColumns;

/**  */

public class DbContract {


    public static class Person implements BaseColumns {
        public static final String TABLE = "person";
        public static final String NAME = "name";
        public static final String AGE = "age";

        public static final String CREATE =
                "CREATE TABLE " + TABLE + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        NAME + " TEXT," +
                        AGE + " INT" +
                        " )";


    }

}
