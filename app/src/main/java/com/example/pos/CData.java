package com.example.pos;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "CTable")
public class CData implements Serializable {
    @PrimaryKey(autoGenerate = true) public int cid;
    @ColumnInfo(name = "cname") public String cname;
}
