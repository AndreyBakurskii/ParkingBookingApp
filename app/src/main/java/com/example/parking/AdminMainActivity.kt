package com.example.parking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.parking.fragments.CarsFragment
import com.example.parking.fragments.ReservationsFragment
import com.example.parking.fragments.SpotsFragment
import com.example.parking.fragments.UsersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminMainActivity : AppCompatActivity() {

    private val usersFragment = UsersFragment()
    private val carsFragment = CarsFragment()
    private val reservationsFragment = ReservationsFragment()
    private val spotsFragment = SpotsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        replaceFragment(usersFragment)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_cars -> replaceFragment(carsFragment)
                R.id.menu_bookings -> replaceFragment(reservationsFragment)
                R.id.menu_users -> replaceFragment(usersFragment)
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