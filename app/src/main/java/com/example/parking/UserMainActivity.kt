package com.example.parking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ExpandableListView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)
        val listView = findViewById<ExpandableListView>(R.id.expListView)

        //Создаем набор данных для адаптера
        val groups = ArrayList<Reservation>()
        val res1 = Reservation("01/02/2022", "11 am - 1 pm", "Nissan")
        groups.add(res1)
        val res2 = Reservation("02/02/2022", "11 am - 1 pm", "Lada")
        groups.add(res2)
        val res3 = Reservation("03/02/2022", "12 am - 1 pm", "Hondai")
        groups.add(res3)
        groups.add(res2)

        //Создаем адаптер и передаем context и список с данными
        val adapter = ExpListAdapter(applicationContext, groups)
        listView.setAdapter(adapter)

        val btAdd = findViewById<FloatingActionButton>(R.id.fab)
//        btAdd.setOnClickListener {
//            children2.add("New Child!")
//            adapter.notifyDataSetChanged()
//        }
    }
}
