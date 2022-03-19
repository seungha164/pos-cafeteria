package com.example.pos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Data.class,CData.class},version = 1)
public abstract class DataDB extends RoomDatabase { // Entity 기반Room.databaseBuilder()로 DB를 생성해 가져온다.
    static DataDB database;
    static String DATABASE_NAME = "33eung";

    public static DataDB getInstance(Context context){
        if(database==null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    DataDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract DataDao dataDao();
    public abstract CDataDao cdataDao();
}
