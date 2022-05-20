package com.example.parking.presentation.activities.SplashScreenActivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.parking.presentation.activities.MainActivity.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash_screen)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}