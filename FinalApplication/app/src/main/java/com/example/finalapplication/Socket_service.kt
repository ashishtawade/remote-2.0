package com.example.finalapplication

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Callable


class Socket_service : Service() {

    // private val serverIp = "192.168.0.55"
    private val serverPort = 12345
    private lateinit var socket: Socket
    private lateinit var outputstream: OutputStreamWriter
    private lateinit var inputstream: BufferedReader
    private lateinit var outputcommand : String
    override fun onCreate() {
        super.onCreate()
        Log.d("Service","Service is created")
        //startSocket()
        //Log.d("Service","Socket is created")

    }



    fun isInitializedSocket(): Boolean {
        return ::socket.isInitialized
    }


    // Binder given to clients
    val binder = LocalBinder()

    // Class used for the client Binder
    inner class LocalBinder : Binder() {
        fun getService(): Socket_service = this@Socket_service
    }


    private fun startSocket(Ip:String,port:Int): Thread {
        val thread =Thread {
            try {
                socket = Socket()
                //socket = Socket(serverIp, serverPort)
                socket.connect(InetSocketAddress(Ip, port), 10000) // 10-second timeout
                outputstream = OutputStreamWriter(socket.getOutputStream())
                inputstream = BufferedReader(InputStreamReader(socket.getInputStream()))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        return thread
    }

    override fun onBind(intent: Intent): IBinder? {
        //Log.d("Service","Service Bound")
        val activity = intent?.getStringExtra("activity").toString()
        if(!isInitializedSocket()) {
            val IP = intent.getStringExtra("IP")
            try {
                val port = intent.getIntExtra("Port", 12345)
                val thread = startSocket(IP.toString(), port)
                thread.join()
                Log.d("Service", "Socket is created")
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Log.d("Service", "Socket creation failed")
            }
        }
        return if(socket.isConnected) {
            binder
        } else{
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun commandExec(cmd : String) : String{
        val command_thread = Thread {
            outputcommand=command(cmd,outputstream,inputstream)
        }
        command_thread.start()
        command_thread.join()
        return outputcommand
    }


    fun read_from_server(inputstream: BufferedReader) : String{
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = inputstream.readLine()
            if (line != "NULL_MARKER" && line != null) {
                //println(line)
                sb.append(line+"\n")
            }
            else if (line == null){
                //println("Server exited.")
                sb.append("Server Exited.")
                break
            }
            else {
                break
            }
        }
        val substringtoreplace = "aXNsaXZlY29ubmVjdA=="
        val stringtoreturn = sb.toString().replace(substringtoreplace,"")
        return stringtoreturn
    }

    fun command(cmd : String,outputStream: OutputStreamWriter,inputStream: BufferedReader) : String {
        outputStream.write(cmd+"\n")
        outputStream.flush()
        val output = read_from_server(inputStream)
        //println(output)
        return output
    }

}