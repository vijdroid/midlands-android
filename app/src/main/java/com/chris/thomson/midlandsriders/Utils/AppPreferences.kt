package com.chris.thomson.midlandsriders.Utils

import android.content.Context
import android.content.SharedPreferences

class AppPreferences {
    companion object {
        var ISLOGIN="islogin"
        var BASEURL="http://www.assurelive.com"
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
    }
}