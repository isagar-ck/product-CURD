package com.example.myapplication.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.myapplication.util.Constants.Companion.IS_LOGIN
import com.example.myapplication.util.Constants.Companion.USERDATA
import com.example.myapplication.util.Constants.Companion.USERID

class SharedPref(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(USERDATA, 0)
    var editor: SharedPreferences.Editor = sharedPreferences.edit()

    var userID: Int
        get() = sharedPreferences.getInt(USERID, 0)
        set(value) = sharedPreferences.edit { putInt(USERID, value) }

    var isLogin: Boolean
        get() = sharedPreferences.getBoolean(IS_LOGIN, false)
        set(value) = sharedPreferences.edit { putBoolean(IS_LOGIN, value) }

    fun clearPreferences() {
        editor.clear()
        editor.apply()
    }
}