package com.example.pos;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.*;
import java.util.List;

@Dao
public interface DataDao {  // DB에 접근해 질의를 수행. - 데이터 접근 객체
    @Insert(onConflict = REPLACE)
    void insert(Data data);
    @Delete
    void delete(Data data);
    @Query("SELECT * FROM datatable ORDER BY category")
    List<Data> getAll();

    @Query("SELECT Distinct category FROM datatable")
    List<String> getCategory();

    @Query("SELECT * FROM datatable WHERE category=:c")
    List<Data> getOrderbyCategory(String c);
}
