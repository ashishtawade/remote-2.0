package com.example.finalapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginButton = findViewById<Button>(R.id.button)
        val signinbutton = findViewById<Button>(R.id.button2)
        loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        signinbutton.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

    }
//
//
//
//    // Method to handle login button click
//
//    fun openActivity_1(view: View) {
//        val intent = Intent(this, MainActivity2::class.java)
//        startActivity(intent)
//    }
//
//    // Method to handle signup button click
//    fun openActivity_2(view: View) {
//        val intent = Intent(this, MainActivity3::class.java)
//        startActivity(intent)
//    }
}
