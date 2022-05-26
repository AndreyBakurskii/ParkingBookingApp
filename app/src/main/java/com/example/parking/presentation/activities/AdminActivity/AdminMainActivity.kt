package com.example.parking.presentation.activities.AdminActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.parking.R
import com.example.parking.presentation.fragments.carsFragment.CarsFragment
import com.example.parking.presentation.fragments.ReservationsFragment
import com.example.parking.presentation.fragments.SpotsFragment
import com.example.parking.presentation.fragments.EmployeesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainActivity : AppCompatActivity() {

    private val employeesFragment = EmployeesFragment()
    private val carsFragment = CarsFragment()
    private val reservationsFragment = ReservationsFragment()
    private val spotsFragment = SpotsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        replaceFragment(employeesFragment)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_cars -> replaceFragment(carsFragment)
                R.id.menu_bookings -> replaceFragment(reservationsFragment)
                R.id.menu_employees -> replaceFragment(employeesFragment)
                R.id.menu_spot -> replaceFragment(spotsFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment){
        if(fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}