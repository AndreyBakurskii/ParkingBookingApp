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
import com.example.parking.presentation.fragments.carsFragment.elm.Effect
import com.example.parking.presentation.fragments.carsFragment.elm.Event
import com.example.parking.presentation.fragments.carsFragment.elm.State
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vivid.money.elmslie.android.base.ElmFragment
import java.util.*
import kotlin.collections.ArrayList

class CarsFragment : ElmFragment<Event, Effect, State>() {

//    private var progressBar : FrameLayout? = null
    private var carsInAdapter : ArrayList<Car>? = null
    private var carsAdapter: ExpListAdapterAdminCars? = null

    private var btAdd : FloatingActionButton? = null
    private var progressBar : FrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_cars, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)
        progressBar = rootView.findViewById(R.id.progressBarContainer)
        btAdd = rootView.findViewById(R.id.fab)

        progressBar = rootView.findViewById<FrameLayout>(R.id.progressBarContainer)

//        val groups = ArrayList<Car>()

//        val car1 = Car(
//            id = UUID.fromString("0f29717c-37e4-4a93-9165-6baacae64e98"),
//            model = "Nissan",
//            length = 3000,
//            wight = 1800,
//            registryNumber = "E475CX152"
//        )

        carsAdapter = ExpListAdapterAdminCars(activity, carsInAdapter)
        listView.setAdapter(carsAdapter)


        btAdd?.setOnClickListener {
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
                    btAdd?.hide()
                } else {
                    btAdd?.show()
                }
            }
        })

        return rootView
    }

    override val initEvent: Event = Event.Ui.LoadCars

    override fun render(state: State) {
        if (state.loading) {
            progressBar!!.visibility = View.VISIBLE
        } else {
            progressBar!!.visibility = View.INVISIBLE
        }

//        if (state.doUpdate) {
//            carsInAdapter!!.clear()
//            carsInAdapter = carsInAdapter + state.cars
//        }
    }

    override fun handleEffect(effect: Effect): Unit? {
        return super.handleEffect(effect)
    }

}