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
    @Query("SELECT * FROM CTable where cname=:c")
    CData Cget(String c);
}
@Dao
interface ODataDao{
    @Insert(onConflict = REPLACE)
    void insert(Order order);
    @Query("SELECT * FROM OTable")
    List<Order> getAll();
    @Delete
    void delete(Order oData);

    @Query("DELETE FROM OTable")
    void deleteAll();

    @Query("SELECT * FROM OTable WHERE oDate like :d")
    List<Order> getOrderbyDate(String d);
}
