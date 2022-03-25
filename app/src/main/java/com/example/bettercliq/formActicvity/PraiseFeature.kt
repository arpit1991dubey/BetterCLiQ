package com.example.bettercliq.formActicvity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.bettercliq.HomeScreeen
import com.example.bettercliq.R
import com.example.bettercliq.data_class.PraiseFeature
import com.example.bettercliq.databinding.ActivityPraiseFeatureBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class PraiseFeature : AppCompatActivity() {
    private lateinit var binding: ActivityPraiseFeatureBinding
    private lateinit var database: DatabaseReference
    private lateinit var os: String
    private lateinit var choose: String
    private lateinit var item: String
    private var imageURI: Uri?=null
    private var feature_count: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPraiseFeatureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding the spinner items with xml file...
        val platform = resources.getStringArray(R.array.Platform)
        val arrayAdapter = ArrayAdapter(this@PraiseFeature, R.layout.dropdown_items, platform)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        //spinners............
        AdapterView.OnItemClickListener { parent, _, position, _ ->
            item = parent?.getItemAtPosition(position).toString()
            Toast.makeText(this@PraiseFeature, item, Toast.LENGTH_SHORT).show()
        }.also { binding.autoCompleteTextView.onItemClickListener = it }

        //RadioButton....
        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb1 -> {
                    Toast.makeText(this@PraiseFeature, "first", Toast.LENGTH_SHORT).show()
                    os = "Apple"
                }
                R.id.rb4 -> {
                    Toast.makeText(this@PraiseFeature, "Second", Toast.LENGTH_SHORT).show()
                    os = "Android"
                }
                R.id.rb4 -> {
                    Toast.makeText(this@PraiseFeature, "third", Toast.LENGTH_SHORT).show()
                    os = "Web"
                }
                R.id.rb4 -> {
                    Toast.makeText(this@PraiseFeature, "forth", Toast.LENGTH_SHORT).show()
                    os = "All device"
                }
            }
        }
        binding.radioGroup2.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.yes -> {
                    choose = "Yes"
                }
                R.id.no -> {
                    choose = "No"
                }
            }
        }


        binding.submitBut.setOnClickListener {
            Log.i("clicked button","problem...")
            val title = binding.txt1.text.toString()
            val excites = binding.bugd.text.toString()
            val review = binding.stepR.text.toString()
            val status = "Submitted"
            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val now = Date()
            val filename = formatter.format(now)
            if(title.isEmpty() || excites.isEmpty() || review.isEmpty() || item.isEmpty() || os.isEmpty() || choose.isEmpty()){
                MaterialAlertDialogBuilder(this@PraiseFeature)
                    .setTitle("Alert")
                    .setMessage("Please type your queries...")
                    .setPositiveButton("Ok"){ dialog ,which ->
                        Toast.makeText(this@PraiseFeature, "Try Again", Toast.LENGTH_SHORT).show()
                    }.show()
            }else {

                val progress= ProgressDialog(this@PraiseFeature)

                progress.setMessage("Uploading Data...")
                progress.setCancelable(false)
                progress.show()

                database = FirebaseDatabase.getInstance().getReference("Praise Feature")
                val user = PraiseFeature(title, item, os, excites, review, choose,filename,status)
                database.child(feature_count.toString()).setValue(user).addOnSuccessListener {
                    Toast.makeText(this@PraiseFeature, "Successful", Toast.LENGTH_LONG)
                        .show()

                    binding.txt1.text?.clear()
                    binding.bugd.text?.clear()
                    binding.stepR.text?.clear()
                    binding.radioGroup.clearCheck()
                    binding.radioGroup2.clearCheck()
                    binding.autoCompleteTextView.setText("")

                    item = ""
                    os = ""
                    choose = ""

                    if (progress.isShowing) progress.dismiss()
                    feature_count++
                    var intent = Intent(this, HomeScreeen::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {

                    if (progress.isShowing) progress.dismiss()
                    Toast.makeText(
                        this@PraiseFeature,
                        "failed uploading data",
                        Toast.LENGTH_LONG
                    )
                        .show()

                }
            }
        }
    }
}