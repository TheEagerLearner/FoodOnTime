package com.internshala.foodontime.Adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodontime.Activity.FoodList
import com.internshala.foodontime.R
import com.internshala.foodontime.database.RestDatabase
import com.internshala.foodontime.database.RestaurantEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaraunt_row.view.*
import org.w3c.dom.Text

class FavAdapter(val context:Context,val data:List<RestaurantEntity>):RecyclerView.Adapter<FavAdapter.favViewGroup>() {

    class favViewGroup(view: View):RecyclerView.ViewHolder(view){

        var pro:ImageView=view.findViewById(R.id.imgRes)
        var nam:TextView=view.findViewById(R.id.txtResName)
        var cost:TextView=view.findViewById(R.id.txtResCost)
        var rating:TextView=view.findViewById(R.id.txtResRating)

        var like:ImageView=view.findViewById(R.id.imgLike)
        var row:RelativeLayout=view.findViewById(R.id.relRow)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favViewGroup {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.restaraunt_row,parent,false)
        return favViewGroup(view)

    }

    override fun getItemCount(): Int {

        return data.size

    }

    override fun onBindViewHolder(holder: favViewGroup, position: Int) {

        val z=data[position]
        holder.nam.text=z.name
        holder.cost.text=z.cost_for_one
        holder.rating.text=z.rating
        Picasso.get().load(z.image).into(holder.pro)
        holder.like.setImageResource(R.drawable.ic_like)


        holder.row.setOnClickListener {

            val i = Intent(context,FoodList::class.java)
            i.putExtra("id",z.id)
            i.putExtra("name",z.name)
            context.startActivity(i)

        }
        holder.like.setOnClickListener {

            val rest=RestaurantEntity(
                z.id,
                z.name,
                z.cost_for_one,
                z.rating,
                z.image
            )

            RemAsync(context,rest).execute().get()
            holder.like.setImageResource(R.drawable.ic_unlike)

        }


    }
}

class RemAsync(val context:Context,val restaurantEntity: RestaurantEntity):AsyncTask<Void,Void,Boolean>()
{
    val db= Room.databaseBuilder(context,RestDatabase::class.java,"sam").build()
    override fun doInBackground(vararg params: Void?): Boolean {

        db.restDao().Delete(restaurantEntity)
        db.close()
        return true

    }


}