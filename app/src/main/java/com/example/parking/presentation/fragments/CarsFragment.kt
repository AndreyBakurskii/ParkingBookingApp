package com.example.parking.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.example.parking.presentation.activities.AdminActivity.AdminMainActivity
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.R
import com.example.parking.presentation.adapters.ExpListAdapterAdminCars
import com.example.parking.data.models.Car
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CarsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_cars, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)

        val groups = ArrayList<Car>()
        val car1 = Car("123", "Nissan", "ADC")
        groups.add(car1)
        val car2 = Car("345", "Lada", "BCD")
        groups.add(car2)
        val car3 = Car("567", "Honda", "CDE")
        groups.add(car3)
        groups.add(car2)

        val adapter =
            ExpListAdapterAdminCars(
                activity,
                groups
            )
        listView.setAdapter(adapter)

        val btAdd = rootView.findViewById<FloatingActionButton>(R.id.fab)
        btAdd.setOnClickListener {
//            val temp = Car("123", "Nissan", "ADC")
//            groups.add(temp)
//            adapter.notifyDataSetChanged()

            val data = "cars"
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