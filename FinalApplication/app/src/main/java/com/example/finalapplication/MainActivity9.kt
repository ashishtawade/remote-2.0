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
import android.widget.EditText
import android.widget.TextView
import com.example.finalapplication.R

class MainActivity9 : AppCompatActivity() {

    private var socketservice: Socket_service? = null
    private var isBound_socket = false
    lateinit var cmd : EditText
    lateinit var exec_cmd : Button
    lateinit var output_text : TextView



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
        setContentView(R.layout.activity_main9)
        cmd = findViewById(R.id.editTextText9)
        exec_cmd = findViewById(R.id.button9)
        output_text = findViewById(R.id.textView9)
    }

    override fun onStart() {
        super.onStart()
        if (!isBound_socket) {
            val intent =
                Intent(this, Socket_service::class.java).apply { putExtra("IP", "192.168.0.55") }
            intent.putExtra("Port", 12345)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        exec_cmd.setOnClickListener(){
            val command = cmd.text.toString()
            output_text.setText(socketservice?.commandExec(command))
        }
    }

}
