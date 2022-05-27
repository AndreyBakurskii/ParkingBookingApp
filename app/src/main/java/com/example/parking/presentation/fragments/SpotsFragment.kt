package com.example.parking.presentation.fragments

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
import com.example.parking.presentation.adapters.ExpListAdapterAdminSpots
import com.example.parking.data.models.ParkingSpot
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SpotsFragment : Fragment() {

    var btAdd : FloatingActionButton? = null
    var progressBar : FrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_spots, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)
        progressBar = rootView.findViewById(R.id.progressBarContainer)
        btAdd = rootView.findViewById(R.id.fab)

        // нужно отключить кнопку btAdd на время загрузки
//        progressBar?.visibility = View.VISIBLE
//        btAdd?.isClickable = false
        // а потом не забыть включить !!!!!!!!
//        progressBar?.visibility = View.INVISIBLE
//        btAdd?.isClickable = true

        val groups = ArrayList<ParkingSpot>()
        val spot1 = ParkingSpot("123", "A123")
        groups.add(spot1)
        val spot2 = ParkingSpot("345", "B123")
        groups.add(spot2)
        val spot3 = ParkingSpot("567", "C123")
        groups.add(spot3)
        groups.add(spot2)

        val adapter =
            ExpListAdapterAdminSpots(
                activity,
                groups
            )
        listView.setAdapter(adapter)

        btAdd?.setOnClickListener {
//            val temp = ParkingSpot("123", "A123")
//            groups.add(temp)
//            adapter.notifyDataSetChanged()

            val data = "spots"
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