package com.internshala.foodontime.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodontime.Adapter.HomeAdapter
import com.internshala.foodontime.Connectivity

import com.internshala.foodontime.R
import com.internshala.foodontime.database.RestDatabase
import com.internshala.foodontime.database.RestaurantEntity
import com.internshala.foodontime.models.Restaurants
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {

    lateinit var Rec:RecyclerView
    lateinit var Lman:RecyclerView.LayoutManager
    lateinit var adp:HomeAdapter
    var restaurant= arrayListOf<Restaurants>()

    lateinit var prog:RelativeLayout

    val rating= Comparator<Restaurants>{rest1, rest2 ->
        if (    rest1.rating.compareTo(rest2.rating,true)==0)
        {
            rest1.name.compareTo(rest2.name,true)
        }
        else
        {
            rest1.rating.compareTo(rest2.rating,true)
        }

    }

    val cost= Comparator<Restaurants> { rest1, rest2 ->
        if (    rest1.cost_for_one.compareTo(rest2.cost_for_one,true)==0)
        {
            rest1.name.compareTo(rest2.name,true)
        }
        else
        {
            rest1.cost_for_one.compareTo(rest2.cost_for_one,true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)


        val c=Connectivity().connect(activity as Context)

        if (c)
        {


            Rec=view.findViewById(R.id.Rec)
            Lman=LinearLayoutManager(activity as Context)

            val queue= Volley.newRequestQueue(activity as Context)

            val url="http://13.235.250.119/v2/restaurants/fetch_result/"


            val jor=object:JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener {

                    var data=it.getJSONObject("data")
                    var success=data.getBoolean("success")
                    if(success)
                    {

                        var set=data.getJSONArray("data")

                        for(i in 0 until set.length())
                        {
                            val resti=set.getJSONObject(i)
                            val rest=Restaurants(
                                resti.getString("id"),
                                resti.getString("name"),
                                resti.getString("rating"),
                                resti.getString("cost_for_one"),
                                resti.getString("image_url")
                            )
                            restaurant.add(rest)
                        }


                        if (activity != null) {
                            adp = HomeAdapter(activity as Context,restaurant)
                            Rec.adapter = adp
                            Rec.layoutManager = Lman
                        }

                    }
                    else
                    {

                        Toast.makeText(activity,"false",Toast.LENGTH_SHORT).show()

                    }


                },
                Response.ErrorListener {

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
        }
        else
        {
            Toast.makeText(activity,"not connected to the internet",Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.sort,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i =item.itemId
        if(i==R.id.sort)
        {

            val ans= arrayOf("rating","Cost(Low to High)","Cost(High to Low)")
            val what=AlertDialog.Builder(activity)
            what.setTitle("Sort By?")
            what.setSingleChoiceItems(ans,-1) { dialog: DialogInterface?, which: Int ->
                when (which)
                {
                    0->{
                        Collections.sort(restaurant,rating)
                        restaurant.reverse()

                    }
                    1->{
                        Collections.sort(restaurant,cost)

                    }
                    2->{
                        Collections.sort(restaurant,cost)
                        restaurant.reverse()
                    }
                }


            }
            adp.notifyDataSetChanged()
            what.setNegativeButton("Cancel"){text,listerner->
            }
            what.setPositiveButton("Ok"){text,listener->
                adp.notifyDataSetChanged()
            }

            what.create()
            what.show()
        }


        return super.onOptionsItemSelected(item)
    }

}


