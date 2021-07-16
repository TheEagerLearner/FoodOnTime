package com.internshala.foodontime.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import java.lang.Exception

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var etmobile:EditText
    lateinit var etemail:EditText
    lateinit var btnNext:Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etmobile=findViewById(R.id.etMobile1)
        etemail=findViewById(R.id.etEmailId)
        btnNext=findViewById(R.id.btnNext)

        sharedPreferences=getSharedPreferences("forgot", Context.MODE_PRIVATE)

        var queue=Volley.newRequestQueue(this)
        val url="http://13.235.250.119/v2/forgot_password/fetch_result"

        btnNext.setOnClickListener {

            var jparam=JSONObject()
            val mob=etmobile.text.toString()
            val em=etemail.text.toString()

            jparam.put("mobile_number",mob)
            jparam.put("email",em)

            sharedPreferences.edit().putString("mobile",mob).apply()

            val jor=object:JsonObjectRequest(Request.Method.POST,url,jparam,Response.Listener {


                val obj=it.getJSONObject("data")
                val success=obj.getBoolean("success")


                if(success)
                {
                    val first=obj.getBoolean("first_try")

                        if (first) {

                            val alert=AlertDialog.Builder(this)
                            alert.setTitle("Done")
                            alert.setMessage("OTP will be sent to you on your registered Email")
                            alert.setNeutralButton("Ok"){text,listener->
                                val i =Intent(this@ForgotPasswordActivity,OtpEnter::class.java)
                                startActivity(i)
                            }
                            alert.create()
                            alert.show()
                        }
                        else {
                            val alert=AlertDialog.Builder(this)
                            alert.setTitle("Done")
                            alert.setMessage("Please use the OTP that was sent before on your Registered Email")
                            alert.setNeutralButton("Ok"){text,listener->
                                val i =Intent(this@ForgotPasswordActivity,OtpEnter::class.java)
                                startActivity(i)

                            }
                            alert.create()
                            alert.show()

                        }


                }
                else{
                    Toast.makeText(this@ForgotPasswordActivity,"Please Enter the registered email and mobile number \n" +
                            "$mob \n" +
                            "$em",Toast.LENGTH_SHORT).show()
                }

            },Response.ErrorListener {



                Toast.makeText(this@ForgotPasswordActivity,"Error",Toast.LENGTH_SHORT).show()

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

    }
}
