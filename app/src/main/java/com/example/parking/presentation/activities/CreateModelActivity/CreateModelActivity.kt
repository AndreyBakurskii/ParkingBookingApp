package com.example.parking.presentation.activities.CreateModelActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.R
import com.example.parking.presentation.fragments.create.*
import com.example.parking.presentation.fragments.car.create.CreateCarFragment
import com.example.parking.presentation.fragments.employee.create.CreateEmployeeFragment
import com.example.parking.presentation.fragments.parkingSpot.create.CreateSpotsFragment

class CreateModelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reservation)
        when(intent.extras!!.getString("fragment")) {
            "reservations" -> replaceFragment(CreateAdminReservationFragment())
            "user" -> {
                val userEmail = intent.extras?.getString("email")
                replaceFragment(CreateUserReservationFragment())
            }
            "cars" -> replaceFragment(CreateCarFragment())
            "employees" -> replaceFragment(CreateEmployeeFragment())
            "spots" -> replaceFragment(CreateSpotsFragment())
        }
    }

    fun replaceFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}