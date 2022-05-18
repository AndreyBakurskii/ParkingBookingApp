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
import com.example.parking.adapters.ExpListAdapterAdminSpots
import com.example.parking.models.ParkingSpot
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SpotsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_spots, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)

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

        val btAdd = rootView.findViewById<FloatingActionButton>(R.id.fab)
        btAdd.setOnClickListener {
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
                    btAdd.hide()
                } else {
                    btAdd.show()
                }
            }
        })

        return rootView
    }

}