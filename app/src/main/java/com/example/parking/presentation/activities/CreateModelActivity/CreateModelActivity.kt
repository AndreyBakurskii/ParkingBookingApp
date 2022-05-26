package com.example.parking.presentation.activities.CreateModelActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.R
import com.example.parking.presentation.fragments.create.*
import com.example.parking.presentation.fragments.create.carFragment.CreateCarFragment

class CreateModelActivity : AppCompatActivity() {

    private val createUserReservationFragment = CreateUserReservationFragment()
    private val createCarFragment = CreateCarFragment()
    private val createUserFragment = CreateEmployeeFragment()
    private val createSpotsFragment = CreateSpotsFragment()
    private val createAdminReservationFragment = CreateAdminReservationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reservation)
        when(intent.extras!!.getString("fragment")) {
            "reservations" -> replaceFragment(createAdminReservationFragment)
            "user" -> {
                val userEmail = intent.extras?.getString("email")
                replaceFragment(createUserReservationFragment)
            }
            "cars" -> replaceFragment(createCarFragment)
            "employees" -> replaceFragment(createUserFragment)
            "spots" -> replaceFragment(createSpotsFragment)
        }
    }

    fun replaceFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}