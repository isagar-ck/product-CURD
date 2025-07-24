package com.example.myapplication.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPref(context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(USERDATA, 0)
    var editor: SharedPreferences.Editor = sharedPreferences.edit()

    var userID: Int
        get() = sharedPreferences.getInt(USERID, 0)
        set(value) = sharedPreferences.edit { putInt(USERID, value) }
}