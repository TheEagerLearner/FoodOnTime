package com.internshala.foodontime.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestDao {

    @Insert
    fun Insert(restaurantEntity: RestaurantEntity)

    @Delete
    fun Delete(restaurantEntity: RestaurantEntity)

    @Query(value = "SELECT * FROM rentity WHERE id=:restid")
    fun findRestId(restid:String):RestaurantEntity

    @Query(value = "SELECT * FROM rentity")
    fun getAllRest():List<RestaurantEntity>


}