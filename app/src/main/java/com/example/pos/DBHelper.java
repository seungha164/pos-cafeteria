package com.example.pos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper  extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "33eung.db";
    private static final String TABLE_NAME = "datas";
    private static final int DATABASE_VERSION = 1;
    public DBHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE datas("
                + "num INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER, isHot BOOLEAN, category TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS Person");
        onCreate(db);
    }
    void addData(Data d){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",d.getName());
        cv.put("price",d.getPrice());
        cv.put("isHot",d.isHot());
        cv.put("category",d.getCategory());
        long result = db.insert(TABLE_NAME,null,cv);
        if(result==-1)
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "데이터 추가 성공", Toast.LENGTH_SHORT).show();

    }
}
