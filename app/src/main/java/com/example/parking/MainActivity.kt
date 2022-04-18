package com.example.parking

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            this,
            R.array.roles, R.layout.titleitem_dropdown
        )
        adapter.setDropDownViewResource(R.layout.dropdown_item)
        spinner.adapter = adapter

        val buttonStart = findViewById<Button>(R.id.buttonStart)
        buttonStart.setOnClickListener {
            if (spinner.selectedItemPosition == 0) {
                val view = layoutInflater.inflate(R.layout.edittext_email, null)
                val alertDialog = AlertDialog.Builder(this, R.style.AlertDialog)
                alertDialog.setTitle("Enter e-mail")
                alertDialog.setCancelable(false)
                val editText = view.findViewById<EditText>(R.id.etEmail)
                alertDialog.setPositiveButton("OK") { dialog, _ ->
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches()) {
                        val intent = Intent(this, UserMainActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        val toast = Toast.makeText(applicationContext, "Invalid e-mail address", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
                alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                alertDialog.setView(view)
                alertDialog.show()
            }
            else {
                val view = layoutInflater.inflate(R.layout.edittext_password, null)
                val alertDialog = AlertDialog.Builder(this, R.style.AlertDialog)
                alertDialog.setTitle("Enter password")
                alertDialog.setCancelable(false)
                val editText = view.findViewById<EditText>(R.id.etPassword)
                alertDialog.setPositiveButton("OK") { dialog, _ ->
                    if (editText.text.toString() == "admin") {
                            val intent = Intent(this, AdminMainActivity::class.java)
                            startActivity(intent)
                        }
                    else {
                        val toast = Toast.makeText(applicationContext, "Wrong password!", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
                alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                alertDialog.setView(view)
                alertDialog.show()
            }
        }
    }
}
