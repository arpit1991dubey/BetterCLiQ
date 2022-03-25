package com.example.bettercliq.formActicvity

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
import com.example.bettercliq.data_class.FeatureEnhancement
import com.example.bettercliq.databinding.ActivityFeatureEnhancmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class FeatureEnhancment : AppCompatActivity() {
    private lateinit var binding: ActivityFeatureEnhancmentBinding
    private lateinit var database: DatabaseReference
    private lateinit var os: String
    private lateinit var item: String
    private var imageURI: Uri?=null
    private var feature_count: Int = 1
    var ref:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeatureEnhancmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding the spinner items with xml file...
        val platform = resources.getStringArray(R.array.Platform)
        val arrayAdapter = ArrayAdapter(this@FeatureEnhancment, R.layout.dropdown_items, platform)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        //spinners............
        AdapterView.OnItemClickListener { parent, _, position, _ ->
            item = parent?.getItemAtPosition(position).toString()
            Toast.makeText(this@FeatureEnhancment, item, Toast.LENGTH_SHORT).show()
        }.also { binding.autoCompleteTextView.onItemClickListener = it }

        //RadioButton....
        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb1 -> {
                    Toast.makeText(this@FeatureEnhancment, "first", Toast.LENGTH_SHORT).show()
                    os = "Apple"
                    i==-1
                }
                R.id.rb2 -> {
                    Toast.makeText(this@FeatureEnhancment, "Second", Toast.LENGTH_SHORT).show()
                    os = "Android"
                    i==-1
                }
                R.id.rb3 -> {
                    Toast.makeText(this@FeatureEnhancment, "third", Toast.LENGTH_SHORT).show()
                    os = "Web"
                    i==-1
                }
                R.id.rb4 -> {
                    Toast.makeText(this@FeatureEnhancment, "forth", Toast.LENGTH_SHORT).show()
                    os = "All device"
                    i==-1
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
                MaterialAlertDialogBuilder(this@FeatureEnhancment)
                    .setTitle("Alert")
                    .setMessage("Please type your queries...")
                    .setPositiveButton("Ok"){ dialog ,which ->
                        Toast.makeText(this@FeatureEnhancment, "Try Again", Toast.LENGTH_SHORT).show()
                    }.show()
            }else {
                val progressDialog = ProgressDialog(this@FeatureEnhancment)
                progressDialog.setMessage("Uploading to Cloud...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                storageReference.putFile(imageURI!!).addOnSuccessListener {
                    addData(filename)
                    feature_count++
                    Toast.makeText(
                        this@FeatureEnhancment,
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
                        this@FeatureEnhancment,
                        "failed...uploaded images / data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun addData(filename:String) {
        val title = binding.txt1.text.toString()
        val advice = binding.bugd.text.toString()
        val desc = binding.stepR.text.toString()
        val status = "Submitted"
        database = FirebaseDatabase.getInstance().getReference("Feature Enhancement")
        val user = FeatureEnhancement(title,item,os,advice,desc,ref.toString(),status,filename)
        database.child(feature_count.toString()).setValue(user).addOnSuccessListener {
            binding.txt1.text?.clear()
            binding.bugd.text?.clear()
            binding.stepR.text?.clear()
            binding.radioGroup.clearCheck()
            binding.autoCompleteTextView.setText("")
        }.addOnFailureListener {
            Toast.makeText(this@FeatureEnhancment, "failed uploading data", Toast.LENGTH_LONG).show()
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