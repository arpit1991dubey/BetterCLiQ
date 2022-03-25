package com.example.bettercliq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        var email = findViewById<EditText>(R.id.LoginEmail)
        var password = findViewById<EditText>(R.id.LoginPassword)
        val register = findViewById<Button>(R.id.RegisterButton)
        val login = findViewById<Button>(R.id.LoginButton)

        register.setOnClickListener {
            var intent = Intent(this, SignUpScreen::class.java)
            startActivity(intent)
            finish()
        }

        fun checking(): Boolean {
            if (email.text.toString().trim { it <= ' ' }.isNotEmpty()
                && password.text.toString().trim { it <= ' ' }.isNotEmpty()
            ) {
                return true
            }
            return false
        }

        login.setOnClickListener {
            if (checking()) {
                val email = email.text.toString()
                val password = password.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            var intent = Intent(this, HomeScreeen::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Enter Complete Details", Toast.LENGTH_LONG).show()
            }
        }
    }
}