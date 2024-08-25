package com.example.finalapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.finalapplication.R

class MainActivity7 : AppCompatActivity() {
    lateinit var ls_exec : Button
    lateinit var ls_output : TextView
    lateinit var Ip : String
    var port : Int = 0
    private var socketservice: Socket_service? = null
    private var isBound_socket = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder){
            val binder = service as Socket_service.LocalBinder
            socketservice = binder.getService()
            isBound_socket = true
        }
        override fun onServiceDisconnected(name: ComponentName){
            isBound_socket = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main7)
        ls_exec = findViewById(R.id.button6)
        ls_output = findViewById(R.id.textView7)
    }

    override fun onStart() {
        super.onStart()
        Ip = intent.getStringExtra("IP").toString()
        port = intent.getIntExtra("Port",12345)
        if(!isBound_socket){
            val intent = Intent(this, Socket_service::class.java).apply { putExtra("IP",Ip)}
            intent.putExtra("Port",port)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        //if(isBound_socket){
        ls_exec.setOnClickListener(){
            Log.d("Button6","Button6 is pressed.")
            ls_output.setText(socketservice?.commandExec("ls"))
        }
        //}
    }

}
