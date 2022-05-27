package com.example.parking.presentation.fragments.carsFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ExpandableListView
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.parking.presentation.activities.AdminActivity.AdminMainActivity
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.R
import com.example.parking.presentation.adapters.ExpListAdapterAdminCars
import com.example.parking.data.models.Car
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

class CarsFragment : Fragment() {

    var btAdd : FloatingActionButton? = null
    var progressBar : FrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_cars, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)
        progressBar = rootView.findViewById(R.id.progressBarContainer)
        btAdd = rootView.findViewById(R.id.fab)

        val groups = ArrayList<Car>()

        val car1 = Car(
            id = UUID.fromString("0f29717c-37e4-4a93-9165-6baacae64e98"),
            model = "Nissan",
            length = 3000,
            wight = 1800,
            registryNumber = "E475CX152"
        )
        groups.add(car1)

        val adapter =
            ExpListAdapterAdminCars(
                activity,
                groups
            )
        listView.setAdapter(adapter)

        btAdd?.setOnClickListener {
//            val temp = Car("123", "Nissan", "ADC")
//            groups.add(temp)
//            adapter.notifyDataSetChanged()

            val data = "cars"
            val intent = Intent((activity as AdminMainActivity), CreateModelActivity::class.java)
            intent.putExtra("fragment", data)
            (activity as AdminMainActivity).startActivity(intent)
        }

        // нужно отключить кнопку btAdd на время загрузки
//        progressBar?.visibility = View.VISIBLE
//        btAdd?.isClickable = false
        // а потом не забыть включить !!!!!!!!
//        progressBar?.visibility = View.INVISIBLE
//        btAdd?.isClickable = true

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