package com.example.finalapplication


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun loginUser(deviceId: String, email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        database.child("users").child(it.uid).get().addOnSuccessListener { snapshot ->
                            val storedDeviceId = snapshot.child("device_id").value as? String

                            if (storedDeviceId != null && storedDeviceId != deviceId) {
                                onFailure("Another device is using this account.")
                                auth.signOut()
                            } else {
                                database.child("users").child(it.uid).child("device_id").setValue(deviceId)
                                    .addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            onSuccess()
                                        } else {
                                            onFailure("Failed to update device ID: ${updateTask.exception?.message}")
                                        }
                                    }
                            }
                        }.addOnFailureListener { exception ->
                            onFailure("Failed to retrieve device ID: ${exception.message}")
                        }
                    }
                } else {
                    onFailure("Login failed: ${task.exception?.message}")
                }
            }
    }

    fun signUpUser(deviceId: String, email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        database.child("users").child(it.uid).child("device_id").setValue(deviceId)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    onSuccess()
                                } else {
                                    onFailure("Failed to store device ID: ${dbTask.exception?.message}")
                                }
                            }
                    }
                } else {
                    onFailure("Sign Up failed: ${task.exception?.message}")
                }
            }
    }
    fun logoutUser(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val user = auth.currentUser
        user?.let {
            database.child("users").child(it.uid).child("device_id").setValue(null)
                .addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) {
                        auth.signOut()
                        onSuccess()
                    } else {
                        onFailure("Failed to clear device ID: ${updateTask.exception?.message}")
                    }
                }
        } ?: run {
            onFailure("No user is currently logged in.")
        }
    }

}