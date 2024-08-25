package com.example.finalapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity3 : AppCompatActivity() {
    private lateinit var firebaseHelper: FirebaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        firebaseHelper = FirebaseHelper()
        val emailEditText = findViewById<EditText>(R.id.editTextText2)
        val passwordEditText = findViewById<EditText>(R.id.editTextText3)
        val repassword = findViewById<EditText>(R.id.editTextText4)
        val signUpButton = findViewById<Button>(R.id.button_submit)
        val nameEditText = findViewById<EditText>(R.id.editTextText1)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val password2 = repassword.text.toString()
            val name = nameEditText.text.toString()
            val deviceId = getDevice()
            if  (email==null||password==null||password2==null||name==null)
            {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            }
            else if (password != password2) {
                Toast.makeText(this, "Password must be same", Toast.LENGTH_SHORT).show()
            } else {
                firebaseHelper.signUpUser(deviceId, email, password, {
                    // Navigate to the login activity after successful sign up
                    val intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                    finish()
                }, { errorMessage ->
                    // Show error message to the user
                    showAlert("Sign Up Failed", errorMessage)
                })
            }
        }
    }
    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
    @SuppressLint("HardwareIds")
    fun getDevice(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }




    // Method to handle Submit button click
    fun submitForm(view: View) {
        // Navigate to MainActivity2
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }
}
