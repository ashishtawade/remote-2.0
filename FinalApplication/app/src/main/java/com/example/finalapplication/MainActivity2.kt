package com.example.finalapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity2 : AppCompatActivity() {
    private lateinit var firebaseHelper: FirebaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        firebaseHelper = FirebaseHelper()
        val emailEditText = findViewById<EditText>(R.id.editTextText1)
        val passwordEditText = findViewById<EditText>(R.id.editTextText2)
        val loginButton = findViewById<Button>(R.id.button_login)
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val deviceId = getDevice()
            firebaseHelper.loginUser(deviceId, email, password, {
                // Navigate to the main activity after successful login
                startActivity(Intent(this, MainActivity4::class.java))
                finish()
            }, { errorMessage ->
                // Show error message to the user
                showAlert("Login Failed", errorMessage)
            })
        }



    }
    @SuppressLint("HardwareIds")
    fun getDevice(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }
    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
    // Method to handle Login button click
//    fun loginUser(view: View) {
//        // Navigate to MainActivity4
//        val intent = Intent(this, MainActivity4::class.java)
//        startActivity(intent)
//    }
}
