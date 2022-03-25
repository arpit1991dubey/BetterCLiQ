package com.example.bettercliq

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.grpc.NameResolver
import java.util.jar.Attributes

class SignUpScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen)
        auth= FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()

        val NameRegister = findViewById<EditText>(R.id.RegisterName)
        val PhoneRegister = findViewById<EditText>(R.id.RegisterPhone)
        val EmailRegister = findViewById<EditText>(R.id.RegisterEmail)
        val PasswordRegister = findViewById<EditText>(R.id.RegisterPassword)
        val SignUp = findViewById<Button>(R.id.SignUpButton)

        fun checking():Boolean{
            if(NameRegister.text.toString().trim{it<=' '}.isNotEmpty()
                && PhoneRegister.text.toString().trim{it<=' '}.isNotEmpty()
                && EmailRegister.text.toString().trim{it<=' '}.isNotEmpty()
                && PasswordRegister.text.toString().trim{it<=' '}.isNotEmpty()
            )
            {
                return true
            }
            return false
        }

        SignUp.setOnClickListener {
            if(checking())
            {
                var email=EmailRegister.text.toString()
                var password= PasswordRegister.text.toString()
                var name= NameRegister.text.toString()
                var phone= PhoneRegister.text.toString()
                val user= hashMapOf(
                    "Name" to name,
                    "Phone" to phone,
                    "email" to email
                )
                val Users=db.collection("USERS")
                val query =Users.whereEqualTo("email",email).get()
                    .addOnSuccessListener {
                            tasks->
                        if(tasks.isEmpty)
                        {
                            auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(this){
                                        task->
                                    if(task.isSuccessful)
                                    {
                                        Users.document(email).set(user)
                                        val intent=Intent(this,HomeScreeen::class.java)
                                        intent.putExtra("email",email)
                                        startActivity(intent)
                                        finish()
                                    }
                                    else
                                    {
                                        Toast.makeText(this,"Authentication Failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        else
                        {
                            Toast.makeText(this,"User Already Registered", Toast.LENGTH_LONG).show()
                            val intent= Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
            else{
                Toast.makeText(this,"Enter the Details", Toast.LENGTH_LONG).show()
            }
        }
    }
}