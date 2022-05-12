package com.example.parking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.CreateReservationActivity
import com.example.parking.R

class CreateReservationFragment : Fragment() {

    private val acceptReservationFragment = AcceptReservationFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_create_reservation, null)
        val btContinue = rootView.findViewById<Button>(R.id.buttonContinue)

        btContinue.setOnClickListener {
            (activity as CreateReservationActivity).replaceFragment(acceptReservationFragment)
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