package com.internshala.foodontime.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodontime.Adapter.FoodAdapter
import com.internshala.foodontime.R
import com.internshala.foodontime.database.RestDatabase
import com.internshala.foodontime.database.RestaurantEntity
import com.internshala.foodontime.models.Foods
import org.json.JSONObject

class FoodList : AppCompatActivity() {

    lateinit var Tool:Toolbar
    lateinit var Rec:RecyclerView
    lateinit var Lman:RecyclerView.LayoutManager
    lateinit var adp:FoodAdapter
    lateinit var Cart:Button
lateinit var sharedPreferences: SharedPreferences
    lateinit var iden:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        sharedPreferences=getSharedPreferences("saurabh", Context.MODE_PRIVATE)
iden= sharedPreferences.getString("id","100").toString()

        Cart=findViewById(R.id.btnCart)
        Cart.visibility=View.GONE
        var food= arrayListOf<Foods>()

        Tool=findViewById(R.id.FoodTool)
        setSupportActionBar(Tool)
        var name=intent.getStringExtra("name")
        var id=intent.getStringExtra("id")
        supportActionBar?.title=name

        Rec=findViewById(R.id.FoodRec)
        Lman=LinearLayoutManager(this)

        var queue=Volley.newRequestQueue(this)
        var url="http://13.235.250.119/v2/restaurants/fetch_result/$id"

        var jor=object:JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {

                var jobj=it.getJSONObject("data")
                var success=jobj.getBoolean("success")
                if(success)
                {

                    var data=jobj.getJSONArray("data")
                    for(i in 0 until data.length())
                    {
                        var item=data.getJSONObject(i)
                        var fooditem=Foods(
                            item.getString("id"),
                            item.getString("name"),
                            item.getString("cost_for_one")
                        )

                        food.add(fooditem)
                    }

                }
                else
                {
                    Toast.makeText(this,"Unable to fetch data",Toast.LENGTH_SHORT).show()

                }

                adp=FoodAdapter(this@FoodList,food,Cart,iden,id,0,name)
                Rec.layoutManager=Lman
                Rec.adapter=adp


            },
            Response.ErrorListener {
                Toast.makeText(this,"Some thing went wrong",Toast.LENGTH_SHORT).show()

            }
        ){

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "2d9b1a32eecbea"
                return headers
            }
        }

        queue.add(jor)






    }


}
