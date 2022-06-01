package com.example.parking.presentation.fragments.carsFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ExpandableListView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.parking.presentation.activities.AdminActivity.AdminMainActivity
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.R
import com.example.parking.data.mapper.CarMapper
import com.example.parking.presentation.adapters.ExpListAdapterAdminCars
import com.example.parking.data.models.Car
import com.example.parking.presentation.activities.EditModelActivity.EditModelActivity
import com.example.parking.presentation.fragments.carsFragment.elm.Effect
import com.example.parking.presentation.fragments.carsFragment.elm.Event
import com.example.parking.presentation.fragments.carsFragment.elm.State
import com.example.parking.presentation.fragments.carsFragment.elm.storeFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import java.util.*
import kotlin.collections.ArrayList

class CarsFragment : ElmFragment<Event, Effect, State>() {

    private var carsInAdapter : ArrayList<Car>? = arrayListOf()
    private var carsAdapter: ExpListAdapterAdminCars? = null

    private var btAdd : FloatingActionButton? = null
    private var progressBar : FrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_cars, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)
        progressBar = rootView.findViewById(R.id.progressBarContainer)
        btAdd = rootView.findViewById(R.id.fab)

        progressBar = rootView.findViewById<FrameLayout>(R.id.progressBarContainer)

        carsAdapter = ExpListAdapterAdminCars(activity, carsInAdapter, store)
        listView.setAdapter(carsAdapter)


        btAdd?.setOnClickListener {
            store.accept(Event.Ui.ClickCreateCar)
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

    override fun onResume() {
        super.onResume()
        Log.i("ONRESUME", "on resume work!!!")
        // todo возможно вызывать LoadCars отсюда
    }

    override fun createStore(): Store<Event, Effect, State> = storeFactory()

    override val initEvent: Event = Event.Ui.LoadCars

    override fun render(state: State) {
        if (state.loading) {
            progressBar!!.visibility = View.VISIBLE
        } else {
            progressBar!!.visibility = View.INVISIBLE
        }

        if (state.doUpdate) {
            carsInAdapter!!.clear()
            carsInAdapter!!.addAll(state.cars as Collection<Car>)
            carsAdapter!!.notifyDataSetChanged()
        }
    }

    override fun handleEffect(effect: Effect) = when (effect) {
        is Effect.ShowErrorLoadCars -> Toast.makeText(
            activity,
            "Unexpected problems on the server! Try restarting the application!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNetwork -> Toast.makeText(
            activity,
            "Problems with your connection! Check your internet connection!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorDeleteCar -> Toast.makeText(
            activity,
            "Error during deletion, try again later!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ToEditCarFragment -> toEditCarFragment(effect.car, effect.positionInAdapter)
        is Effect.ToCreateCarFragment -> toCreateCarFragment()
    }

    private fun toCreateCarFragment() {
        val data = "cars"
        val intent = Intent((activity as AdminMainActivity), CreateModelActivity::class.java)
        intent.putExtra("fragment", data)
        (activity as AdminMainActivity).startActivity(intent)
    }

    private fun toEditCarFragment(car: Car, positionInAdapter: Int) {
        val data = "cars";
        val intent = Intent((activity as AdminMainActivity), EditModelActivity::class.java);
        intent.putExtra("fragment", data);
        intent.putExtra("car", car.toHashMap(withID = true))
        intent.putExtra("positionInAdapter", positionInAdapter)

        (activity as AdminMainActivity).startActivity(intent);
    }
}