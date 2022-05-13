package com.example.parking.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ExpandableListView
import com.example.parking.AdminMainActivity
import com.example.parking.CreateModelActivity
import com.example.parking.R
import com.example.parking.adapters.ExpListAdapterAdminCars
import com.example.parking.adapters.ExpListAdapterAdminUsers
import com.example.parking.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UsersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_users, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)

        val groups = ArrayList<User>()
        val car1 = User("Khoroshavina Ekaterina", "test")
        groups.add(car1)
        val car2 = User("Bakurskii Andrei", "test1")
        groups.add(car2)
        val car3 = User("Name Surname", "test3")
        groups.add(car3)
        groups.add(car2)

        val adapter =
            ExpListAdapterAdminUsers(
                activity,
                groups
            )
        listView.setAdapter(adapter)

        val btAdd = rootView.findViewById<FloatingActionButton>(R.id.fab)
        btAdd.setOnClickListener {
            val data = "employees"
            val intent = Intent((activity as AdminMainActivity), CreateModelActivity::class.java)
            intent.putExtra("fragment", data)
            (activity as AdminMainActivity).startActivity(intent)
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

        return rootView
    }

}