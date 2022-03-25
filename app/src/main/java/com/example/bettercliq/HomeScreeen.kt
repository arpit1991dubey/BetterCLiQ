package com.example.bettercliq

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bettercliq.formActicvity.BugReport
import com.example.bettercliq.formActicvity.FeatureEnhancment
import com.example.bettercliq.formActicvity.FeatureSuggestion
import com.example.bettercliq.formActicvity.PraiseFeature
import com.google.firebase.firestore.FirebaseFirestore


class HomeScreeen : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screeen)
        init()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val isLogin = sharedPref.getString("Email", "1")
        val logout = findViewById<ImageButton>(R.id.logout)
        val name = findViewById<TextView>(R.id.name)
        val bugReportButton = findViewById<Button>(R.id.Report_bug)
        val praiseFeatureButton = findViewById<Button>(R.id.Praise_a_feature)
        val suggestFeature = findViewById<Button>(R.id.Suggest_a_feature)
        val enhanceFeature = findViewById<Button>(R.id.Feature_enhancment)
        val profileButton =findViewById<ImageButton>(R.id.profile)



        profileButton.setOnClickListener {
            val intent =Intent(this,ProfileScreen::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            sharedPref.edit().remove("Email").apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        bugReportButton.setOnClickListener {
            val intent =Intent(this,BugReport::class.java)
            startActivity(intent)
        }

        praiseFeatureButton.setOnClickListener {
            val intent =Intent(this,PraiseFeature::class.java)
            startActivity(intent)
        }

        suggestFeature.setOnClickListener {
            val intent =Intent(this,FeatureSuggestion::class.java)
            startActivity(intent)
        }

        enhanceFeature.setOnClickListener {
            val intent =Intent(this,FeatureEnhancment::class.java)
            startActivity(intent)
        }





        fun setText(email:String?)
        {
            db= FirebaseFirestore.getInstance()
            if (email != null) {
                db.collection("USERS").document(email).get()
                    .addOnSuccessListener {
                            tasks->
                        name.text=tasks.get("Name").toString()
                       // phone.text=tasks.get("Phone").toString()
                       // emailLog.text=tasks.get("email").toString()

                    }
            }

        }


        if(isLogin=="1")
        {
            val email=intent.getStringExtra("email")
            if(email!=null)
            {
                setText(email)
                with(sharedPref.edit())
                {
                    putString("Email",email)
                    apply()
                }
            }
            else{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        else
        {
            setText(isLogin)
        }

    }

        fun init() {
            val recylView = findViewById<RecyclerView>(R.id.recyclerView)
            recylView.layoutManager = LinearLayoutManager(this)
            recylView.adapter = RecycleAdapter()
        }

}