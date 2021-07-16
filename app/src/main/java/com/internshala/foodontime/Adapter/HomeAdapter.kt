package com.internshala.foodontime.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodontime.Activity.FoodList

import com.internshala.foodontime.R
import com.internshala.foodontime.database.RestDatabase
import com.internshala.foodontime.database.RestaurantEntity
import com.internshala.foodontime.models.Restaurants
import com.squareup.picasso.Picasso



class HomeAdapter(var context:Context,var data:ArrayList<Restaurants>):RecyclerView.Adapter<HomeAdapter.HomeViewGroup>() {


    class HomeViewGroup(view: View) : RecyclerView.ViewHolder(view) {

        var imgRes: ImageView = view.findViewById(R.id.imgRes)
        var txtResName: TextView = view.findViewById(R.id.txtResName)
        var txtResCost: TextView = view.findViewById(R.id.txtResCost)
        var txtResRating: TextView = view.findViewById(R.id.txtResRating)
        var row:RelativeLayout = view.findViewById(R.id.relRow)
        var like:ImageView=view.findViewById(R.id.imgLike)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewGroup {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaraunt_row, parent, false)
        return HomeViewGroup(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HomeViewGroup, position: Int) {


        val z = data[position]
        holder.txtResName.text = z.name
        holder.txtResCost.text = "${z.cost_for_one}/person"
        holder.txtResRating.text = z.rating
        Picasso.get().load(z.image_url).into(holder.imgRes)



        var rest=RestaurantEntity(
            z.Id,
            z.name,
            z.cost_for_one,
            z.rating,
            z.image_url

        )



        val checkFav= HomeAsync(context,rest,1).execute()
        val s=checkFav.get()
        if(s)
        {
            holder.like.setBackgroundResource(R.drawable.ic_like)

        }
        else
        {
            holder.like.setBackgroundResource(R.drawable.ic_unlike)
        }

        holder.like.setOnClickListener {

            if (!(HomeAsync(context,rest,1).execute().get())){
                if (HomeAsync(context, rest, 2).execute().get()) {
                    Toast.makeText(context,"the restaurant ${z.name} was added to favorites",Toast.LENGTH_SHORT).show()
                    holder.like.setBackgroundResource(R.drawable.ic_like)

                }
                else
                {
                    Toast.makeText(context,"some error occurred",Toast.LENGTH_SHORT).show()


                }


            }
            else
            {
                if (HomeAsync(context, rest, 3).execute().get()) {
                    Toast.makeText(context,"the restaurant ${z.name} was removed from favorites",Toast.LENGTH_SHORT).show()
                    holder.like.setBackgroundResource(R.drawable.ic_unlike)

                }
                else
                {
                    Toast.makeText(context,"some error occurred",Toast.LENGTH_SHORT).show()


                }
            }


        }

        holder.row.setOnClickListener {

            val i = Intent(context, FoodList::class.java)
            i.putExtra("id", z.Id)
            i.putExtra("name",z.name)
            context.startActivity(i)
        }




}

}
class HomeAsync(val context: Context, val restaurantEntity: RestaurantEntity, val mode:Int):
    AsyncTask<Void, Void, Boolean>(){
    override fun doInBackground(vararg params: Void?): Boolean {

        val db= Room.databaseBuilder(context, RestDatabase::class.java,"sam").build()

        when(mode)
        {
            1->{

                val rest: RestaurantEntity?=db.restDao().findRestId(restaurantEntity.id)
                db.close()
                return rest!=null
            }
            2->{

                db.restDao().Insert(restaurantEntity)
                db.close()
                return true

            }
            3->{

                db.restDao().Delete(restaurantEntity)
                db.close()
                return true

            }


        }
        return false
    }

}





