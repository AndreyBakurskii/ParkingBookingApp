package com.example.parking.fragments.create

import android.annotation.SuppressLint
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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateAdminReservationFragment : Fragment() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_create_admin_reservation, null)
        val btContinue = rootView.findViewById<Button>(R.id.buttonContinue)

        val c = Calendar.getInstance()
        val df: DateFormat = SimpleDateFormat("EEE dd/MM")
        val dateChips = rootView.findViewById<ChipGroup>(R.id.DateChips)
        for (i in 0..6) {
            val chip = inflater.inflate(R.layout.layout_chip_choice, dateChips, false) as Chip
            chip.text=df.format(c.time)
            dateChips.addView(chip)
            c.add(Calendar.DATE, 1)
        }

        btContinue.setOnClickListener {
            // вот так можно посчитать, какая дата выбрана
//            val chipsCount: Int = dateChips.childCount
//            var msg = ""
//            if (chipsCount != 0) {
//                var i = 0
//                while (i < chipsCount) {
//                    val chip = dateChips.getChildAt(i) as Chip
//                    if (chip.isChecked) {
//                        msg += chip.getText().toString()
//                    }
//                    i++
//                }
//            }
            // сюда вставить вызов функции создания в бэке

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