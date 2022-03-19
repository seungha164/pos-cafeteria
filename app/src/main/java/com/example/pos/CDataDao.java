package com.example.pos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import static androidx.room.OnConflictStrategy.REPLACE;
import java.util.List;
@Dao
public interface CDataDao{
    @Insert(onConflict = REPLACE)
    void insert(CData cData);
    @Delete
    void delete(CData cdata);
    @Query("SELECT * FROM CTable")
    List<CData> CgetAll();
    @Query("SELECT cname FROM CTable")
    List<String> CgetName();
}
