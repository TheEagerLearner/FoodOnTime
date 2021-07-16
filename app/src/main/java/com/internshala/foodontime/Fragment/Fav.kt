package com.internshala.foodontime.Fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import com.internshala.foodontime.Adapter.FavAdapter

import com.internshala.foodontime.R
import com.internshala.foodontime.database.RestDatabase
import com.internshala.foodontime.database.RestaurantEntity

/**
 * A simple [Fragment] subclass.
 */
class Fav : Fragment() {

    lateinit var Rec:RecyclerView
    lateinit var Lman:RecyclerView.LayoutManager
    lateinit var adp:FavAdapter
    var dbRestList=listOf<RestaurantEntity>()
    lateinit var NoFav: RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_fav, container, false)

        NoFav=view.findViewById(R.id.NoFav)
        NoFav.visibility=View.VISIBLE

        Rec=view.findViewById(R.id.Rec)
        Lman=LinearLayoutManager(activity as Context)
        dbRestList=FavAsync(activity as Context).execute().get()

        if(!dbRestList.isEmpty())
        {
            NoFav.visibility=View.GONE
        }



        if(activity!=null) {
            adp = FavAdapter(activity as Context, dbRestList)

            Rec.layoutManager = Lman
            Rec.adapter = adp
        }


        return view




    }

}

class FavAsync(val context:Context):AsyncTask<Void,Void,List<RestaurantEntity>>()
{
    override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {

        val db= Room.databaseBuilder(context, RestDatabase::class.java,"sam").build()

        return db.restDao().getAllRest()
    }


}
