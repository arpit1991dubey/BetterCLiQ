package com.example.bettercliq.formActicvity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.bettercliq.HomeScreeen
import com.example.bettercliq.R
import com.example.bettercliq.data_class.FeatureSuggestion
import com.example.bettercliq.databinding.ActivityFeatureSuggestionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class FeatureSuggestion : AppCompatActivity() {
    private lateinit var binding: ActivityFeatureSuggestionBinding
    private lateinit var database: DatabaseReference
    private lateinit var os: String
    private lateinit var item: String
    private  var imageURI: Uri?=null
    private var feature_count: Int = 1
    var ref:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeatureSuggestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding the spinner items with xml file...
        val platform = resources.getStringArray(R.array.Platform)
        val arrayAdapter = ArrayAdapter(this@FeatureSuggestion, R.layout.dropdown_items, platform)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        //spinners............
        AdapterView.OnItemClickListener { parent, _, position, _ ->
            item = parent?.getItemAtPosition(position).toString()
            Toast.makeText(this@FeatureSuggestion, item, Toast.LENGTH_SHORT).show()
        }.also { binding.autoCompleteTextView.onItemClickListener = it }

        //RadioButton....
        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb1 -> {
                    Toast.makeText(this@FeatureSuggestion, "first", Toast.LENGTH_SHORT).show()
                    os = "Apple"
                }
                R.id.rb3 -> {
                    Toast.makeText(this@FeatureSuggestion, "Second", Toast.LENGTH_SHORT).show()
                    os = "Android"
                }
                R.id.rb3 -> {
                    Toast.makeText(this@FeatureSuggestion, "third", Toast.LENGTH_SHORT).show()
                    os = "Web"
                }
                R.id.rb4 -> {
                    Toast.makeText(this@FeatureSuggestion, "forth", Toast.LENGTH_SHORT).show()
                    os = "All device"
                }
            }
        }
        binding.upload.setOnClickListener {
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.submitBut.setOnClickListener {

            ref=(0..1000000).random()

            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val now = Date()
            val filename = formatter.format(now)
            val storageReference =
                FirebaseStorage.getInstance().getReference("images/${"$filename-$ref"}")
            if(binding.txt1.text.toString().isEmpty()
                || binding.bugd.text.toString().isEmpty()
                || binding.stepR.text.toString().isEmpty()
                || item.isEmpty()
                || os.isEmpty()
                || imageURI==null){
                MaterialAlertDialogBuilder(this@FeatureSuggestion)
                    .setTitle("Alert")
                    .setMessage("Please type your queries...")
                    .setPositiveButton("Ok"){ dialog ,which ->
                        Toast.makeText(this@FeatureSuggestion, "Try Again", Toast.LENGTH_SHORT).show()
                    }.show()
            }else {

                val progressDialog = ProgressDialog(this@FeatureSuggestion)
                progressDialog.setMessage("Uploading to Cloud...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                storageReference.putFile(imageURI!!).addOnSuccessListener {
                    addData(filename)
                    feature_count++
                    Toast.makeText(
                        this@FeatureSuggestion,
                        "Successfully...uploaded",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    var intent =Intent(this, HomeScreeen::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    Toast.makeText(
                        this@FeatureSuggestion,
                        "failed...uploaded images / data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun addData(filename:String) {
        val title = binding.txt1.text.toString()
        val use = binding.bugd.text.toString()
        val problem = binding.stepR.text.toString()
        val status = "Submitted"

        database = FirebaseDatabase.getInstance().getReference("Feature Suggestion")
        val user = FeatureSuggestion(title,item,os,use,problem,ref.toString(),filename,status)
        database.child(feature_count.toString()).setValue(user).addOnSuccessListener {
            binding.txt1.text?.clear()
            binding.bugd.text?.clear()
            binding.stepR.text?.clear()
            binding.radioGroup.clearCheck()
            binding.autoCompleteTextView.setText("")
        }.addOnFailureListener {
            Toast.makeText(this@FeatureSuggestion, "failed uploading data", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK)
            data?.data!!.also { imageURI = it }
        if (imageURI != null)
            Toast.makeText(this, "added", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show()
    }
}