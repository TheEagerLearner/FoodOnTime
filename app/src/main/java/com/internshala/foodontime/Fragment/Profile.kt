package com.internshala.foodontime.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.internshala.foodontime.R

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {

    lateinit var name:TextView
    lateinit var mobile:TextView
    lateinit var email:TextView
    lateinit var address:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        name=view.findViewById(R.id.name)
        mobile=view.findViewById(R.id.mobile)
        email=view.findViewById(R.id.email)
        address=view.findViewById(R.id.address)

        val themePrefs = activity!!.getSharedPreferences("saurabh", 0)

        name.setText(themePrefs.getString("name","don"))
        mobile.setText(themePrefs.getString("mobile_number","8329560411"))
        email.setText(themePrefs.getString("email","saurabhmul10@gmail.com"))
        address.setText(themePrefs.getString("address","home"))


        // Inflate the layout for this fragment
        return view
    }

}
