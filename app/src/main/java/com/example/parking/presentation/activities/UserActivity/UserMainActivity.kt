package com.example.parking.presentation.activities.UserActivity

import android.content.Intent
import android.os.Bundle
import android.widget.AbsListView
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import com.example.parking.R
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.presentation.adapters.ExpListAdapterUserReservations
import com.example.parking.data.models.Reservation
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)
        val email = intent.extras?.getString("email")
        val listView = findViewById<ExpandableListView>(R.id.expListView)

        //Создаем набор данных для адаптера
        val groups = ArrayList<Reservation>()
        val res1 = Reservation(
            "01/02/2022",
            "11 am - 1 pm",
            "Nissan",
            "ABC",
            email
        )
        groups.add(res1)
        val res2 = Reservation(
            "02/02/2022",
            "11 am - 1 pm",
            "Lada",
            "CDE",
            email
        )
        groups.add(res2)
        val res3 = Reservation(
            "03/02/2022",
            "12 am - 1 pm",
            "Hondai",
            "BCD",
            email
        )
        groups.add(res3)
        groups.add(res2)

        //Создаем адаптер и передаем context и список с данными
        val adapter =
            ExpListAdapterUserReservations(
                this,
                groups
            )
        listView.setAdapter(adapter)

        val btAdd = findViewById<FloatingActionButton>(R.id.fab)
//        btAdd.setOnClickListener {
//            val temp = Reservation(
//                "01/02/2022",
//                "11 am - 1 pm",
//                "Nissan",
//                "DEF"
//            )
//            groups.add(temp)
//            adapter.notifyDataSetChanged()
//        }
        btAdd.setOnClickListener {
            val data = "user"
            val intent = Intent(this, CreateModelActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("fragment", data)
            startActivity(intent)
        }
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}
            override fun onScroll(
                view: AbsListView?, firstVisibleItem: Int,
                visibleItemCount: Int, totalItemCount: Int
            ) {
                val lastItem = firstVisibleItem + visibleItemCount
                if ((lastItem == totalItemCount) && (firstVisibleItem > 0)) {
                    btAdd.hide()
                } else {
                    btAdd.show()
                }
            }
        })
    }
}
