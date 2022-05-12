package com.example.parking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.fragments.AcceptReservationFragment
import com.example.parking.fragments.CreateReservationFragment

class CreateReservationActivity : AppCompatActivity() {

    private val createReservationFragment = CreateReservationFragment()
    private val acceptReservationFragment = AcceptReservationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reservation)
        when(intent.extras!!.getString("fragment")) {
            "user" -> replaceFragment(createReservationFragment)
            "test" -> replaceFragment(acceptReservationFragment)
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