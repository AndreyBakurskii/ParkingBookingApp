package com.example.parking.presentation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ExpandableListView
import com.example.parking.presentation.activities.AdminActivity.AdminMainActivity
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.R
import com.example.parking.presentation.adapters.ExpListAdapterAdminReservations
import com.example.parking.data.models.Reservation
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReservationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_reservations, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)

        val groups = ArrayList<Reservation>()
        val res1 = Reservation("01/02/2022", "11 am - 1 pm", "Nissan",
            "ABC", "test")
        groups.add(res1)
        val res2 = Reservation("02/02/2022", "11 am - 1 pm", "Lada",
            "CDE", "test")
        groups.add(res2)
        val res3 = Reservation("03/02/2022", "12 am - 1 pm", "Hondai",
            "BCD", "test")
        groups.add(res3)
        groups.add(res2)

        val adapter =
            ExpListAdapterAdminReservations(
                activity,
                groups
            )
        listView.setAdapter(adapter)

        val btAdd = rootView.findViewById<FloatingActionButton>(R.id.fab)
        btAdd.setOnClickListener {
            val data = "reservations"
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