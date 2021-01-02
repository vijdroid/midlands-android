package com.chris.thomson.midlandsriders.Utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.DialogInterface
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.chris.thomson.midlandsriders.R


class AppPreferences {
    companion object {
        val AUTH = "midlandsriders123!@#"
        var ISLOGIN="islogin"
        var BASEURL="http://www.assurelive.com/midlands/v1/"
        private val NAME = "MidlandsRiders"
        private val MODE = Context.MODE_PRIVATE
        private lateinit var preferences: SharedPreferences

        // list of app specific preferences
        fun init(context: Context) {
            preferences = context.getSharedPreferences(NAME, MODE)
        }

        /**
         * SharedPreferences extension function, so we won't need to call edit() and apply()
         * ourselves on every SharedPreferences operation.
         */
        private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
            val editor = edit()
            operation(editor)
            editor.apply()
        }


        fun setBool(key: String, value: Boolean) {
            preferences.edit {
                it.putBoolean(key, value)
            }
        }
        fun getBool(key: String): Boolean {
            return preferences.getBoolean(key, false)
        }
        fun showCstomDailog(context: Context, message: String, iSNegativeButton: Boolean, positiveStr:String="", negativeStr:String="", positiveClick:()->Unit, nagativeClick:()->Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.app_name)
            builder.setMessage(message)
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->                                 // User clicked the Yes button
                    {
                        positiveClick()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        nagativeClick()
                    }
                }
            }
            builder.setPositiveButton(positiveStr, dialogClickListener)
            if (iSNegativeButton) {
                builder.setNegativeButton(negativeStr, dialogClickListener)
            }
            val dialog = builder.create()
            dialog.show()
        }
        fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }
    }
}