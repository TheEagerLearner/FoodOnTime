package com.internshala.foodontime.Activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodontime.R
import com.internshala.foodontime.models.Foods
import kotlinx.android.synthetic.main.activity_place_order.*

class PlaceOrder : AppCompatActivity() {

    lateinit var Tool:Toolbar
    lateinit var sharedPreferences: SharedPreferences
    lateinit var ResName:TextView
    lateinit var ResFoodCost:TextView
    var food= listOf<Foods>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        Tool=findViewById(R.id.PlaceTool)
        setSupportActionBar(Tool)
        supportActionBar?.title="Place Order"

        ResName=findViewById(R.id.txtResName)
        ResFoodCost=findViewById(R.id.txtCost)

        sharedPreferences=getSharedPreferences("saurabh", Context.MODE_PRIVATE)
        var id=sharedPreferences.getString("id","0")

        val queue=Volley.newRequestQueue(this)
        val url="http://13.235.250.119/v2/orders/fetch_result/$id"

        val jor=object :JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener {

                val obj = it.getJSONObject("data")
                val success = obj.getBoolean("success")

                if (success)
                {
                    Toast.makeText(this,"True",Toast.LENGTH_SHORT).show()

                       val data=obj.getJSONArray("data")

                    val size=data.length()
                         val d=data.getJSONObject(1)
                         ResName.text=d.getString("restaurant_name")
                         ResFoodCost.text=d.getString("total_cost")


                }
                else
                {
                    Toast.makeText(this,"False",Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this,"Some unexpected error occurred",Toast.LENGTH_SHORT).show()

            }
            )
        {
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
