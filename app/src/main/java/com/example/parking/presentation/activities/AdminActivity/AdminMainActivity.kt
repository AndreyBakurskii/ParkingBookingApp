package com.example.parking.presentation.activities.AdminActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.parking.R
import com.example.parking.presentation.fragments.car.list.CarsFragment
import com.example.parking.presentation.fragments.reservation.list.ReservationsFragment
import com.example.parking.presentation.fragments.parkingSpot.list.SpotsFragment
import com.example.parking.presentation.fragments.employee.list.EmployeesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class AdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        replaceFragment(EmployeesFragment())

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_cars -> replaceFragment(CarsFragment())
                R.id.menu_bookings -> replaceFragment(ReservationsFragment())
                R.id.menu_employees -> replaceFragment(EmployeesFragment())
                R.id.menu_spot -> replaceFragment(SpotsFragment())
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