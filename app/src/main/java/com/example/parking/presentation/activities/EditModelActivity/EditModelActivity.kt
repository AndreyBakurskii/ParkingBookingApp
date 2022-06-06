package com.example.parking.presentation.activities.EditModelActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.parking.R
import com.example.parking.presentation.fragments.edit.*
import com.example.parking.presentation.fragments.car.edit.EditCarFragment
import com.example.parking.presentation.fragments.parkingSpot.edit.EditSpotFragment

class EditModelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_model)
        when(intent.extras!!.getString("fragment")) {
            "reservations" -> replaceFragment(EditAdminReservationFragment())
            "user" -> replaceFragment(EditUserReservationFragment())
            "cars" -> replaceFragment(EditCarFragment())
            "spots" -> replaceFragment(EditSpotFragment())
        }
    }

    private fun replaceFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}