package com.example.parking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.fragments.CreateCarFragment
import com.example.parking.fragments.CreateReservationFragment
import com.example.parking.fragments.CreateEmployeeFragment
import com.example.parking.fragments.CreateSpotsFragment

class CreateModelActivity : AppCompatActivity() {

    private val createReservationFragment = CreateReservationFragment()
    private val createCarFragment = CreateCarFragment()
    private val createUserFragment = CreateEmployeeFragment()
    private val createSpotsFragment = CreateSpotsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reservation)
        when(intent.extras!!.getString("fragment")) {
            "user" -> replaceFragment(createReservationFragment)
            "cars" -> replaceFragment(createCarFragment)
            "employees" -> replaceFragment(createUserFragment)
            "spots" -> replaceFragment(createSpotsFragment)
        }
    }

    fun replaceFragment(fragment : Fragment){
        if(fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}