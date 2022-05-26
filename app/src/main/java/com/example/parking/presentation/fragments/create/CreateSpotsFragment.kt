package com.example.parking.presentation.fragments.create

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.parking.R

class CreateSpotsFragment : Fragment() {

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_create_spots, null)
        val btContinue = rootView.findViewById<Button>(R.id.buttonContinue)

        btContinue.setOnClickListener {
            // сюда вставить вызов функции создания в бэке

            // с полученной информацией выводим окно
            val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
            val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
            alertDialog.setTitle("Confirm")
            alertDialog.setCancelable(false)
            // сюда информацию (уже добавила)
            val textOutput = view.findViewById<TextView>(R.id.textView)
            textOutput.text = "Car model: " + rootView.findViewById<EditText>(R.id.etNumSpot).text
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
}