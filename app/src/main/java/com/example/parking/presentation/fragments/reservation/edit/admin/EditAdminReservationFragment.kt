package com.example.parking.presentation.fragments.reservation.edit.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EditAdminReservationFragment : Fragment() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_edit_admin_reservation, null)
        val btContinue = rootView.findViewById<Button>(R.id.buttonContinue)
        val dateChips = rootView.findViewById<ChipGroup>(R.id.DateChips)
        val startTimeChips = rootView.findViewById<ChipGroup>(R.id.StartTimeChips)
        val endTimeChips = rootView.findViewById<ChipGroup>(R.id.EndTimeChips)
        val etModel = rootView.findViewById<EditText>(R.id.etCarModel)
        val etNum = rootView.findViewById<EditText>(R.id.etCarNum)
        val etEmail = rootView.findViewById<EditText>(R.id.etEmployeeEmail)

        val c = Calendar.getInstance()
        val df: DateFormat = SimpleDateFormat("EEE dd/MM")
        var i = 0
        while (i < 7) {
            if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                i++
                val chip = inflater.inflate(R.layout.layout_chip_choice, dateChips, false) as Chip
                chip.text=df.format(c.time)
                dateChips.addView(chip)
            }
            c.add(Calendar.DATE, 1)
        }

        btContinue.setOnClickListener {
            // сюда вставить вызов функции редактирования на бэке

            // с полученной информацией выводим окно
            val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
            val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
            alertDialog.setTitle("Confirm")
            alertDialog.setCancelable(false)
            // сюда информацию
            val textOutput = view.findViewById<TextView>(R.id.textView)
            alertDialog.setPositiveButton("OK") { _, _ ->
                // вот так вызывается загрузочная крутяшка (отключаем кнопку ещё на всякий)
                rootView.findViewById<FrameLayout>(R.id.progressBarContainer).visibility = View.VISIBLE
                btContinue.isClickable = false
                // вот так она скрывается
                rootView.findViewById<FrameLayout>(R.id.progressBarContainer).visibility = View.INVISIBLE
                // выводим toast что всё ок и закрываем активность
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

    private fun getSelectedChip(chips : ChipGroup) : String {
        val chipsCount: Int = chips.childCount
        var msg = ""
        if (chipsCount != 0) {
            var i = 0
            while (i < chipsCount) {
                val chip = chips.getChildAt(i) as Chip
                if (chip.isChecked) {
                    msg += chip.getText().toString()
                }
                i++
            }
        }
        return msg
    }

    private fun isChipSelected(chips : ChipGroup) : Boolean {
        return if (getSelectedChip(chips) == "") {
            val toast = Toast.makeText(activity, "Select date and time", Toast.LENGTH_SHORT)
            toast.show()
            false
        } else {
            true
        }
    }

    private fun isFieldEmpty(etField : EditText) : Boolean {
        return if (TextUtils.isEmpty(etField.text.toString())) {
            etField.error = "This field cannot be empty"
            true
        } else {
            false
        }
    }
}