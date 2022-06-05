package com.example.parking.presentation.fragments.employee.list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ExpandableListView
import android.widget.FrameLayout
import com.example.parking.presentation.activities.AdminActivity.AdminMainActivity
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.R
import com.example.parking.presentation.adapters.ExpListAdapterAdminEmployees
import com.example.parking.data.models.Employee
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EmployeesFragment : Fragment() {

    var btAdd : FloatingActionButton? = null
    var progressBar : FrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_employees, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)
        progressBar = rootView.findViewById(R.id.progressBarContainer)
        btAdd = rootView.findViewById(R.id.fab)

        // нужно отключить кнопку btAdd на время загрузки
//        progressBar?.visibility = View.VISIBLE
//        btAdd?.isClickable = false
        // а потом не забыть включить !!!!!!!!
//        progressBar?.visibility = View.INVISIBLE
//        btAdd?.isClickable = true

        val groups = ArrayList<Employee>()
        val emp1 =
            Employee("test")
        groups.add(emp1)
        val emp2 =
            Employee("test1")
        groups.add(emp2)
        val emp3 = Employee("test3")
        groups.add(emp3)
        groups.add(emp2)

        val adapter =
            ExpListAdapterAdminEmployees(
                activity,
                groups
            )
        listView.setAdapter(adapter)

        btAdd?.setOnClickListener {
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
                    btAdd?.hide()
                } else {
                    btAdd?.show()
                }
            }
        })

        return rootView
    }

}