package com.internshala.foodontime.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import com.internshala.foodontime.Fragment.*
import com.internshala.foodontime.R
import com.internshala.foodontime.database.RestDatabase
import com.internshala.foodontime.database.RestaurantEntity

class Main2Activity : AppCompatActivity() {

    lateinit var Tool:Toolbar
    lateinit var tog:ActionBarDrawerToggle
    lateinit var Draw:DrawerLayout
    lateinit var Nav:NavigationView
    lateinit var Frame:FrameLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var username:TextView
    lateinit var number:TextView
    var rest= listOf<RestaurantEntity>()


    var prev:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        Tool=findViewById(R.id.Tool)
        Draw=findViewById(R.id.draw)
        Nav=findViewById(R.id.Nav)
        Frame=findViewById(R.id.Nav)

        sharedPreferences=getSharedPreferences("saurabh", Context.MODE_PRIVATE)
//        username=findViewById(R.id.txtName)
//        number=findViewById(R.id.txtNumber)
//        username.setText(sharedPreferences.getString("name","Saurabh"))
//        number.setText(sharedPreferences.getString("mobile_number","8329560411"))

        setSupportActionBar(Tool)
        tog= ActionBarDrawerToggle(this@Main2Activity,Draw,R.string.open,R.string.close)
        Draw.addDrawerListener(tog)
        supportActionBar?.title="FoodOnTime"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tog.syncState()

        OpenHome()

       // Nav.setNavigationItemSelectedListener {  }

        Nav.setNavigationItemSelectedListener {


            if(prev!=null)
            {
                prev?.isChecked=false
            }

            val select=it.itemId
            when(select)
            {
                R.id.home->{

                    it.isCheckable=true
                    it.isChecked=true
                    prev=it
                    OpenHome()

                }

                R.id.profile->{
                    it.isCheckable=true
                    it.isChecked=true
                    prev=it
                    supportFragmentManager.beginTransaction().replace(R.id.Frame, Profile()).addToBackStack("profile").commit()
                    Draw.closeDrawers()
                    supportActionBar?.title="Profile"

                }

                R.id.fav->{
                    it.isCheckable=true
                    it.isChecked=true
                    prev=it
                    supportFragmentManager.beginTransaction().replace(R.id.Frame, Fav()).addToBackStack("fav").commit()
                    supportActionBar?.title="Favorite Restaurants"
                    Draw.closeDrawers()

                }

                R.id.history->{
                    it.isCheckable=true
                    it.isChecked=true
                    prev=it
                    supportFragmentManager.beginTransaction().replace(R.id.Frame, History()).addToBackStack("history").commit()
                    supportActionBar?.title="Order History"
                    Draw.closeDrawers()

                }

                R.id.faq->{

                    supportFragmentManager.beginTransaction().replace(R.id.Frame,Faq()).addToBackStack("faq").commit()
                    supportActionBar?.title="Frequently Asked Questions"
                    Draw.closeDrawers()

                }

                R.id.logout->{
                    rest=RemoAsync(applicationContext).execute().get()
                    val ask=AlertDialog.Builder(this@Main2Activity)
                    ask.setTitle("Log out")
                    ask.setMessage("Are you sure you want to log out ?")
                    ask.setPositiveButton("Ok"){text,listener->

                        for(i in 0 until rest.size)
                        {
                            ReAsync(applicationContext,rest[i]).execute().get()
                        }


                        sharedPreferences.edit().putBoolean("Logged",false).apply()
                        val i=Intent(this@Main2Activity,LoginActivity::class.java)
                        startActivity(i)
                    }
                    ask.setNegativeButton("Cancel"){text,listener->
                    }
                    ask.create()
                    ask.show()

                }


            }
            return@setNavigationItemSelectedListener true
        }


    }

    fun OpenHome(){


        supportFragmentManager.beginTransaction().replace(R.id.Frame,Home()).addToBackStack("home").commit()
        supportActionBar?.title="Home"
        Draw.closeDrawers()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val c=item.itemId
        if(c==android.R.id.home)
        {
            Draw.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val sel=supportFragmentManager.findFragmentById(R.id.Frame)
        when(sel)
        {
            is Home->{
                finish()
                super.onPause()
            }

            !is Home->{
            OpenHome()

                }

            else->
            {
                super.onBackPressed()}
        }


    }

}
class RemoAsync(val context:Context): AsyncTask<Void, Void, List<RestaurantEntity>>()
{
    override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {

        val db= Room.databaseBuilder(context, RestDatabase::class.java,"sam").build()

        return db.restDao().getAllRest()
    }


}
class ReAsync(val context:Context,val restaurantEntity: RestaurantEntity):AsyncTask<Void,Void,Boolean>()
{

    val db=Room.databaseBuilder(context,RestDatabase::class.java,"sam").build()
    override fun doInBackground(vararg params: Void?): Boolean {

        db.restDao().Delete(restaurantEntity)
        return true
    }


}
