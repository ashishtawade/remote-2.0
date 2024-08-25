package com.example.finalapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.finalapplication.R
import kotlin.properties.Delegates

class MainActivity5 : AppCompatActivity() {
    lateinit var button5 : Button
    lateinit var button6 : Button
    lateinit var button7 : Button
    lateinit var button8 : Button
    lateinit var button3 : Button
    lateinit var Ip:String
    var port : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        button5 = findViewById<Button>(R.id.button5)
        button6 = findViewById<Button>(R.id.button6)
        button7 = findViewById<Button>(R.id.button7)
        button8 = findViewById<Button>(R.id.button8)
        button3 = findViewById<Button>(R.id.button3)
    }

    override fun onStart() {
        super.onStart()
        port = intent.getIntExtra("Port",12345)
        Ip = intent.getStringExtra("IP").toString()
        button5.setOnClickListener {
            val intent = Intent(this, MainActivity7::class.java)
            intent.putExtra("IP",Ip)
            intent.putExtra("Port",port)
            startActivity(intent)
        }

        button6.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            intent.putExtra("IP",Ip)
            intent.putExtra("Port",port)
            startActivity(intent)
        }

        button7.setOnClickListener {
            val intent = Intent(this, MainActivity8::class.java)
            intent.putExtra("IP",Ip)
            intent.putExtra("Port",port)
            startActivity(intent)
        }

        button8.setOnClickListener {
            val intent = Intent(this, MainActivity9::class.java)
            intent.putExtra("IP",Ip)
            intent.putExtra("Port",port)
            startActivity(intent)
        }

        button3.setOnClickListener {
            val intent = Intent(this, MainActivity10::class.java)
            intent.putExtra("IP",Ip)
            intent.putExtra("Port",port)
            startActivity(intent)
        }
    }
}
