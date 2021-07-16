package com.internshala.foodontime.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodontime.R
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    lateinit var etUsername:EditText
    lateinit var etEmail:EditText
    lateinit var etMobile:EditText
    lateinit var etAddress:EditText
    lateinit var etPassword:EditText
    lateinit var etPasswordConfirm:EditText
    lateinit var btnRegister:Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        sharedPreferences=getSharedPreferences("saurabh", Context.MODE_PRIVATE)

        etUsername=findViewById(R.id.etUsername)
        etEmail=findViewById(R.id.etEmail)
        etMobile=findViewById(R.id.etMobile)
        etAddress=findViewById(R.id.etAddress)
        etPassword=findViewById(R.id.etPassword)
        etPasswordConfirm=findViewById(R.id.etPasswordConfirm)
        btnRegister=findViewById(R.id.btnRegister)

        var queue=Volley.newRequestQueue(this)

        val url="http://13.235.250.119/v2/register/fetch_result"


        btnRegister.setOnClickListener {
            val jparam=JSONObject()
            val name=etUsername.text.toString()
            val email=etEmail.text.toString()
            val password=etPassword.text.toString()
            val password2=etPasswordConfirm.text.toString()
            val mobile=etMobile.text.toString()
            val address=etAddress.text.toString()
            val compare=password.compareTo(password2)

            if(compare==0)
            {

                jparam.put("name",name)
                jparam.put("mobile_number",mobile)
                jparam.put("password",password)
                jparam.put("address",address)
                jparam.put("email",email)

                val jor=object:JsonObjectRequest(Request.Method.POST,url,jparam,
                    Response.Listener {

                        val data=it.getJSONObject("data")
                        var success=data.getBoolean("success")
                        if(success)
                        {
                            val obj=data.getJSONObject("data")
                            sharedPreferences.edit().putString("id",obj.getString("user_id")).apply()
                            sharedPreferences.edit().putString("name",obj.getString("name")).apply()
                            sharedPreferences.edit().putString("email",obj.getString("email")).apply()
                            sharedPreferences.edit().putString("mobile_number",obj.getString("mobile_number")).apply()
                            sharedPreferences.edit().putString("address",obj.getString("address")).apply()
                            sharedPreferences.edit().putBoolean("Logged",true).apply()
                            val i =Intent(this@RegisterActivity,Main2Activity::class.java)
                            startActivity(i)
                        }
                        else
                        {
                            Toast.makeText(this@RegisterActivity,"${data.getString("errorMessage")}",Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()


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
                Toast.makeText(this,"different password",Toast.LENGTH_SHORT).show()

            }




        }




    }
}
