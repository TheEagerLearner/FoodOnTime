package com.internshala.foodontime.database

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rentity")
data class RestaurantEntity(
    @PrimaryKey  var id:String,
    @ColumnInfo(name = "name") var name:String,
    @ColumnInfo(name = "cost_for_one") var cost_for_one:String,
    @ColumnInfo(name="rating") var rating:String,
    @ColumnInfo(name = "image") var image:String
)