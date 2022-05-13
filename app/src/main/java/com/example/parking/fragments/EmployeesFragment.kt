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
import com.example.parking.adapters.ExpListAdapterAdminUsers
import com.example.parking.models.Employee
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EmployeesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_employees, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)

        val groups = ArrayList<Employee>()
        val emp1 =
            Employee("Khoroshavina Ekaterina", "test")
        groups.add(emp1)
        val emp2 =
            Employee("Bakurskii Andrei", "test1")
        groups.add(emp2)
        val emp3 = Employee("Name Surname", "test3")
        groups.add(emp3)
        groups.add(emp2)

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