package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.db.DatabaseHelper
import com.example.myapplication.util.SharedPref
import com.example.myapplication.util.adjustFullScreen

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    val dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adjustFullScreen(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (SharedPref(this).isLogin) {
                val getUser = dbHelper.getUser(SharedPref(this).userID)
                getUser?.let {
                    if (it.userType == "admin") {
                        startActivity(Intent(this, AdminDashboardActivity::class.java))
                        finishAffinity()
                    } else {
                        startActivity(Intent(this, UserActivity::class.java))
                        finishAffinity()
                    }
                }
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
                finishAffinity()
            }
        }, 1500)
    }
}