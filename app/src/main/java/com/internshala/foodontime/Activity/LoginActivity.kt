package com.internshala.foodontime.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodontime.Connectivity
import com.internshala.foodontime.Fragment.Profile
import com.internshala.foodontime.R
import kotlinx.android.synthetic.main.header_drawer.view.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var mobile:EditText
    lateinit var password:EditText
    lateinit var btnLogin:Button
    lateinit var Register:TextView
    lateinit var Forgot:TextView
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences=getSharedPreferences("saurabh", Context.MODE_PRIVATE)
        mobile = findViewById(R.id.mobile1)
        password = findViewById(R.id.password1)
        btnLogin = findViewById(R.id.btnLogin)
        Register = findViewById(R.id.register)
        Forgot = findViewById(R.id.forgot)
        val i=Intent(this@LoginActivity,Main2Activity::class.java)
        if(sharedPreferences.getBoolean("Logged",false))
        {
            startActivity(i)
            finish()
        }



        Register.setOnClickListener {
            val j = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(j)
        }

        Forgot.setOnClickListener {
            val k = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(k)
        }




        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/login/fetch_result"

        var jparam = JSONObject()
        btnLogin.setOnClickListener {

            var mob=mobile.text.toString()
            var pw=password.text.toString()
            jparam.put("mobile_number", mob)
            jparam.put("password", pw)

            val jor = object : JsonObjectRequest(
                Request.Method.POST,
                url,
                jparam,
                Response.Listener {

                    val jobj = it.getJSONObject("data")
                    val success = jobj.getBoolean("success")



                    if (success) {
                        val obj=jobj.getJSONObject("data")
                        Toast.makeText(this@LoginActivity,"Name: ${obj.getString("name")} \naddress: ${obj.getString("address")} ",Toast.LENGTH_SHORT).show()
                        sharedPreferences.edit().putString("id",obj.getString("user_id")).apply()
                        sharedPreferences.edit().putString("name",obj.getString("name")).apply()
                        sharedPreferences.edit().putString("email",obj.getString("email")).apply()
                        sharedPreferences.edit().putString("mobile_number",obj.getString("mobile_number")).apply()
                        sharedPreferences.edit().putString("address",obj.getString("address")).apply()
                        sharedPreferences.edit().putBoolean("Logged",true).apply()
                        startActivity(i)
                        finish()

                    } else {
                        Toast.makeText(this@LoginActivity, "no registered user ", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {

                    Toast.makeText(this@LoginActivity, "Please your mobile to a proper internet connection", Toast.LENGTH_SHORT).show()

                    println("Error is $it")
                }
            ) {
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

        }



