package com.internshala.foodontime.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.FtsOptions
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.internshala.foodontime.Activity.FoodList
import com.internshala.foodontime.Activity.PlaceOrder

import com.internshala.foodontime.R
import com.internshala.foodontime.models.Foods
import com.internshala.foodontime.models.food
import com.internshala.foodontime.models.fooditems
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*
import kotlin.collections.ArrayList

val items= arrayListOf<food>()



class FoodAdapter(var context:Context,var food:ArrayList<Foods>,var cart:Button,var iden:String,var ResId:String,var total_cost:Int,val ResName:String):RecyclerView.Adapter<FoodAdapter.FoodViewGroup>() {


    class FoodViewGroup(view: View) : RecyclerView.ViewHolder(view) {

        var name: TextView = view.findViewById(R.id.txtFoodName)
        var cost: TextView = view.findViewById(R.id.txtFoodPrice)
        var button: Button = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewGroup {

        val view = LayoutInflater.from(context).inflate(R.layout.foodrow, parent, false)
        return FoodViewGroup(view)
    }

    override fun getItemCount(): Int {
        return food.size
    }



    override fun onBindViewHolder(holder: FoodViewGroup, position: Int) {
        val z = food[position]

        holder.name.text = z.name
        holder.cost.text = "Rs.${z.cost}"



        val food = Gson()
        holder.button.setOnClickListener {
            val order = food(
                z.id
            )


            var p = false
            for (i in 0 until items.size) {
                if (order == items[i]) {
                    p = true
                    break
                } else {
                    continue
                }

            }

            if (!p) {
                items.add(order)
                total_cost= total_cost + z.cost.toInt()
                holder.button.setBackgroundResource(R.color.remove)
                holder.button.text = "Remove"

            } else {
                items.remove(order)
                total_cost= total_cost - z.cost.toInt()
                holder.button.setBackgroundResource(R.color.add)
                holder.button.text = "Add"

            }

            if (items.isEmpty()) {
                cart.visibility = View.GONE
            } else {
                cart.visibility = View.VISIBLE
            }
        }


        val queue=Volley.newRequestQueue(context)
        val url="http://13.235.250.119/v2/place_order/fetch_result/"
        var jparam=JSONObject()
        jparam.put("user_id",iden)
        jparam.put("restaurant_id",ResId)


        cart.setOnClickListener {
            var tot= total_cost

            jparam.put("total_cost",tot.toString())
            val s=food.toJson(items)
            jparam.put("food",s)


            val jor=object:JsonObjectRequest(Request.Method.POST,url,jparam,
                Response.Listener {

                    val success=it.getJSONObject("data").getBoolean("success")
                    if(success)
                    {
                        Toast.makeText(context,"id:$iden\ntext:$s",Toast.LENGTH_SHORT).show()


                    }
                    else
                    {
                        Toast.makeText(context,"False",Toast.LENGTH_SHORT).show()

                    }


                },
                Response.ErrorListener {

                    Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()

                })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "2d9b1a32eecbea"
                    return headers
                }
            }



            queue.add(jor)

            total_cost=0
        }
    }
}


