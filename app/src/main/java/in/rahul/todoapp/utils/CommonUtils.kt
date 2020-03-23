package `in`.rahul.todoapp.utils

import `in`.rahul.todoapp.BuildConfig
import `in`.rahul.todoapp.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast

object CommonUtils {
    fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun LogMessage(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }

//    fun internetDialog(context: Context) {
//        val builder = AlertDialog.Builder(context)
//        builder.setMessage("Please Connect to Internet")
//        builder.setCancelable(false).setIcon(R.drawable.ic_internet)
//            .setTitle("No Internet Connection")
//        builder.setPositiveButton(
//            "OK"
//        ) { dialog, _ ->
//            dialog.cancel()
//            (context as Activity).finish()
//        }.setNegativeButton("Retry") { dialog, _ ->
//            if (!isOnline(context)) {
//                internetDialog(context)
//            } else {
//                dialog.dismiss()
//            }
//        }
//        val alert = builder.create()
//        alert.show()
//    }

    fun isOnline(context: Context): Boolean {
        val result: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
        }
        return result
    }
}