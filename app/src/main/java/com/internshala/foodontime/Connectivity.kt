package com.internshala.foodontime
import android.content.Context
import android.net.ConnectivityManager

class Connectivity {
    fun connect(context: Context):Boolean{

        var con=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var check=con.activeNetworkInfo
        if(check?.isConnected!=null)
        {
            return check.isConnected
        }
        else
        {
            return false
        }

    }

}