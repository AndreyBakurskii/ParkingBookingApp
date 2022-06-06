package com.example.parking.presentation.fragments.car.list

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.parking.presentation.activities.AdminActivity.AdminMainActivity
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.R
import com.example.parking.presentation.adapters.ExpListAdapterAdminCars
import com.example.parking.data.models.Car
import com.example.parking.presentation.activities.EditModelActivity.EditModelActivity
import com.example.parking.presentation.fragments.car.list.elm.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
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
        Log.i("CAR FRAGMENT", "OnCreateView work")
        val rootView: View = inflater.inflate(R.layout.fragment_cars, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)
        progressBar = rootView.findViewById(R.id.progressBarContainer)
        btAdd = rootView.findViewById(R.id.fab)

        progressBar = rootView.findViewById(R.id.progressBarContainer)

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

    @SuppressLint("SetTextI18n")
    fun showDeleteDialog(car: Car, positionInAdapter: Int) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)
        val text = view.findViewById<TextView>(R.id.textView)
        text.text = "Are you sure you\nwant to delete?"
        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickDeleteDialog(car, positionInAdapter))
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        Log.i("CAR FRAGMENT", "OnStart work\n" +
                "${carsAdapter} - ${carsInAdapter}")
    }
    override fun onResume() {
        super.onResume()
        Log.i("CAR FRAGMENT", "OnResume work\n${carsAdapter} - ${carsInAdapter}")
        store.accept(Event.Ui.LoadCars)

    }

    override fun onPause() {
        super.onPause()
        Log.i("CAR FRAGMENT", "OnPause work")
    }
    override fun onStop() {
        super.onStop()
        Log.i("CAR FRAGMENT", "OnStop work")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("CAR FRAGMENT", "OnDestroy work")
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

    override fun handleEffect(effect: Effect) {
        Log.i("HANDLE_EFFECT_CAR", "work handle effect")
        when (effect) {
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
            is Effect.ToEditCarFragment -> toEditCarFragment(effect.car)
            is Effect.ToCreateCarFragment -> toCreateCarFragment()
            is Effect.ShowDeleteDialog -> showDeleteDialog(effect.car, effect.positionInAdapter)
        }
    }

    private fun toCreateCarFragment() {
        Log.i("LIST_CARS", "TO CREATE FRAGMENT")
        val data = "cars"
        val intent = Intent((activity as AdminMainActivity), CreateModelActivity::class.java)
        intent.putExtra("fragment", data)

        startActivityForResult(intent, 200);
    }

    private fun toEditCarFragment(car: Car) {
        Log.i("LIST_CARS", "TO EDIT FRAGMENT")

        val data = "cars";
        val intent = Intent((activity as AdminMainActivity), EditModelActivity::class.java);
        intent.putExtra("fragment", data);

        intent.putExtra("car", car.toHashMap(withID = true))

        startActivityForResult(intent, 200);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            store.accept(Event.Ui.LoadCars)
        }
    }
}