package com.simple.location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "longitude")
    public Double longitude;

    @ColumnInfo(name = "time")
    public Long time;

    @ColumnInfo(name = "address")
    public String address;

    public Item() {

    }

    public Item(long time, String address) {
        this.latitude = Double.parseDouble("0");
        this.longitude = Double.parseDouble("0");
        this.time = time;
        this.address = address;
    }
}
