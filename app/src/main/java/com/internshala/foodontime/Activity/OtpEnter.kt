package com.internshala.foodontime.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SharedMemory
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodontime.R
import org.json.JSONObject

class OtpEnter : AppCompatActivity() {

    lateinit var etOtp:EditText
    lateinit var etPassword:EditText
    lateinit var etConfPassword:EditText
    lateinit var btnSubmit:Button
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_enter)

        sharedPreferences=getSharedPreferences("forgot", Context.MODE_PRIVATE)
        etOtp=findViewById(R.id.etOtp)
        etPassword=findViewById(R.id.etPassword)
        etConfPassword=findViewById(R.id.etConfPassword)
        btnSubmit=findViewById(R.id.btnSubmit)

        val queue=Volley.newRequestQueue(this)
        val url="http://13.235.250.119/v2/reset_password/fetch_result"

        val jparam=JSONObject()

        btnSubmit.setOnClickListener {

            val password=etPassword.text.toString()
            val passwordc=etConfPassword.text.toString()
            val otp=etOtp.text.toString()
            val v=password.compareTo(passwordc)
            val mob=sharedPreferences.getString("mobile","012")

            jparam.put("mobile_number",mob)
            jparam.put("password",password)
            jparam.put("otp",otp)
            val jor=object:JsonObjectRequest(Request.Method.POST,url,jparam,
                Response.Listener {

                    if(v==0)
                    {
                        val data=it.getJSONObject("data")
                        val success=data.getBoolean("success")

                        if(success)
                        {
                            Toast.makeText(this,"True\n$mob",Toast.LENGTH_SHORT).show()
                            val alert=AlertDialog.Builder(this)
                            alert.setTitle("Done!")
                            alert.setMessage(data.getString("successMessage"))
                            alert.setNeutralButton("OK"){text,listener->
                                val i =Intent(this,LoginActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                            alert.create()
                            alert.show()

                        }
                        if(!success)
                        {
                            val error=data.getString("errorMessage")
                            Toast.makeText(this, error,Toast.LENGTH_SHORT).show()
                        }

                    }
                    else
                    {

                        Toast.makeText(this,"Password are not matching",Toast.LENGTH_SHORT).show()
                    }


                },
                Response.ErrorListener {
                    Toast.makeText(this,"Please connect to a stable Internt Connection",Toast.LENGTH_SHORT).show()
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
}
