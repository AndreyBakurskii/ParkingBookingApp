package com.example.parking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.parking.fragments.edit.*

class EditModelActivity : AppCompatActivity() {

    private val editEmployeeFragment = EditEmployeeFragment()
    private val editCarFragment = EditCarFragment()
    private val editSpotsFragment = EditSpotFragment()
    private val editUserReservationFragment = EditUserReservationFragment()
    private val editAdminReservationFragment = EditAdminReservationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_model)
        when(intent.extras!!.getString("fragment")) {
            "reservations" -> replaceFragment(editAdminReservationFragment)
            "user" -> replaceFragment(editUserReservationFragment)
            "cars" -> replaceFragment(editCarFragment)
            "employees" -> replaceFragment(editEmployeeFragment)
            "spots" -> replaceFragment(editSpotsFragment)
        }
    }

    private fun replaceFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}