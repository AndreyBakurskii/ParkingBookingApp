package com.example.parking.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.CreateModelActivity
import com.example.parking.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateReservationFragment : Fragment() {

    private val acceptReservationFragment = AcceptReservationFragment()

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_create_reservation, null)
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
            val chipsCount: Int = dateChips.getChildCount()
            var msg = ""
            if (chipsCount != 0) {
                var i = 0
                var chip = dateChips.getChildAt(0) as Chip
                while ((!chip.isChecked) && (i < chipsCount)) {
                    chip = dateChips.getChildAt(i) as Chip
                    i++
                }
                if(i != chipsCount) {
                    msg += chip.text.toString()
                }
            }
            val toast = Toast.makeText( rootView.context, msg, Toast.LENGTH_LONG )
            toast.show()
            (activity as CreateModelActivity).replaceFragment(acceptReservationFragment)
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