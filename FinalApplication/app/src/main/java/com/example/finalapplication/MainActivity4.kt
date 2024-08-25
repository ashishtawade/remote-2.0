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
import android.widget.Toast
import com.example.finalapplication.R
import androidx.appcompat.app.AlertDialog

class MainActivity4 : AppCompatActivity() {
    lateinit var submitButton: Button
    lateinit var logoutbutton: Button
    private var socketservice: Socket_service? = null
    private var isBound_socket = false
    lateinit var ip_text : EditText
    lateinit var Ip : String
    var port: Int = 0
    var flag = false
    private lateinit var firebaseHelper: FirebaseHelper
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder){
            val binder = service as Socket_service.LocalBinder
            socketservice = binder.getService()
            isBound_socket = true
            flag = true
        }
        override fun onServiceDisconnected(name: ComponentName){
            isBound_socket = false
            flag = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        ip_text = findViewById(R.id.editTextText)
        submitButton = findViewById(R.id.submitButton)
        firebaseHelper = FirebaseHelper()
        logoutbutton = findViewById<Button>(R.id.logout_button)
        val start_thread = Thread {
            while(!flag){
                Thread.sleep(2000)
            }
            Log.d("isBound_socket", isBound_socket.toString())
            if (isBound_socket) {
                val intent = Intent(this, MainActivity5::class.java)
                intent.putExtra("IP", Ip)
                intent.putExtra("Port", port)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Service Binding Failed.", Toast.LENGTH_LONG).show()
            }
        }
        start_thread.start()
        logoutbutton.setOnClickListener {
            // Navigate to the LogoutActivity
            firebaseHelper.logoutUser({

                showAlert("Logout Successful", "You have been logged out.")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, { errorMessage ->

                showAlert("Logout Failed", errorMessage)
            })
            startActivity(Intent(this, MainActivity::class.java))
        }

    }



    override fun onStart() {
        super.onStart()
        submitButton.setOnClickListener {
            //isBound_socket = false
            /*if(isBound_socket){
                Toast.makeText(this, "New Connection Unbinding Started.", Toast.LENGTH_LONG).show()
                val unbinding = Thread {
                    unbindService(connection)
                }
                unbinding.start()
                unbinding.join()
                val unbinding_check = Thread{
                    while(flag){
                        Thread.sleep(1000)
                    }
                }
                unbinding_check.start()
                val start_thread = Thread {
                    while(!flag){
                        Thread.sleep(1000)
                    }
                    Log.d("isBound_socket", isBound_socket.toString())
                    if (isBound_socket) {
                        val intent = Intent(this, SecondActivity::class.java)
                        intent.putExtra("IP", Ip)
                        intent.putExtra("Port", port)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Service Binding Failed.", Toast.LENGTH_LONG).show()
                    }
                }
                start_thread.start()
            }
             */
            Ip = ip_text.text.toString()
            port = 12345
            if (!isBound_socket) {
                try {
                    val intent =
                        Intent(this, Socket_service::class.java).apply { putExtra("IP", Ip) }
                    intent.putExtra("Port", port)
                    intent.putExtra("activity","Main")
                    bindService(intent, connection, Context.BIND_AUTO_CREATE)
                } catch (e: Exception) {
                    Toast.makeText(this, "Service Binding Failed.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //override fun onStop(){
    //    super.onStop()
    //    if(isBound_socket){
    //        unbindService(connection)
    //    }
    //    Log.d("Service","Service is unbinded.")
    //}
    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
