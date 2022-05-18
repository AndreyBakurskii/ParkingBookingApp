package com.example.parking.fragments.edit

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.R

class EditEmployeeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_edit_employee, null)
        val btContinue = rootView.findViewById<Button>(R.id.buttonContinue)

        btContinue.setOnClickListener {
            // сюда вставить вызов функции редактирования на бэке

            // с полученной информацией выводим окно
            val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
            val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
            alertDialog.setTitle("Confirm")
            alertDialog.setCancelable(false)
            // сюда информацию
            val textOutput = view.findViewById<TextView>(R.id.textView)
            alertDialog.setPositiveButton("OK") { dialog, _ ->
                val toast = Toast.makeText(activity, "Done", Toast.LENGTH_SHORT)
                toast.show()
                activity?.finish()
            }
            alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            alertDialog.setView(view)
            alertDialog.show()
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}