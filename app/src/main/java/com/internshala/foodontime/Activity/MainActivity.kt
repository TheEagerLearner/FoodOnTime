package com.internshala.foodontime.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.internshala.foodontime.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Handler().postDelayed(
            {
                val i=Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(i)
                finish()
            },2000

        )
    }
}
