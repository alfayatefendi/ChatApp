package com.app.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class SignInActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signIn: Button
    private lateinit var signUp: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        signIn = findViewById(R.id.login)
        signUp = findViewById(R.id.signup)
        auth = FirebaseAuth.getInstance()

        signUp.setOnClickListener{
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        signIn.setOnClickListener {
            if (email.text.isEmpty() || email.text.isBlank() || password.text.isEmpty() || password.text.isBlank()) {
                Toast.makeText(this, "Please enter email dan password", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) {task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "Authentication Success", Toast.LENGTH_SHORT).show()
                            Log.d("SigninActivity", "SigninWithEmail: Success")
                            val intent = Intent(this@SignInActivity, MainActivity::class.java)
                            finish()
                            startActivity(intent)
                        } else {
                            Log.w("SigninFails", "SigninWithEmail: Failure", task.exception)
                            Toast.makeText(baseContext, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }
    }
}