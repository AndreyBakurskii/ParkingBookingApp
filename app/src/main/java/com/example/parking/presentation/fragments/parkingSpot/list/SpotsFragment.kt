package com.example.parking.presentation.fragments.parkingSpot.list

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.parking.presentation.activities.AdminActivity.AdminMainActivity
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.R
import com.example.parking.presentation.adapters.ExpListAdapterAdminSpots
import com.example.parking.data.models.ParkingSpot
import com.example.parking.presentation.activities.EditModelActivity.EditModelActivity
import com.example.parking.presentation.fragments.parkingSpot.list.elm.Effect
import com.example.parking.presentation.fragments.parkingSpot.list.elm.Event
import com.example.parking.presentation.fragments.parkingSpot.list.elm.State
import com.example.parking.presentation.fragments.parkingSpot.list.elm.storeFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store

class SpotsFragment : ElmFragment<Event, Effect, State>() {

    private var parkingSpotsInAdapter : ArrayList<ParkingSpot>? = arrayListOf()
    private var parkingSpotsAdapter: ExpListAdapterAdminSpots? = null

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


        parkingSpotsAdapter =
            ExpListAdapterAdminSpots(
                activity,
                parkingSpotsInAdapter,
                store
            )
        listView.setAdapter(parkingSpotsAdapter)

        btAdd?.setOnClickListener {
            store.accept(Event.Ui.ClickCreateParkingSpot)
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

    override fun createStore(): Store<Event, Effect, State> = storeFactory()

    override val initEvent: Event = Event.Ui.LoadParkingSpots

    override fun render(state: State) {
        if (state.loading) {
            progressBar!!.visibility = View.VISIBLE
        } else {
            progressBar!!.visibility = View.INVISIBLE
        }

        if (state.doUpdate) {
            parkingSpotsInAdapter!!.clear()
            parkingSpotsInAdapter!!.addAll(state.parkingSpots as Collection<ParkingSpot>)
            parkingSpotsAdapter!!.notifyDataSetChanged()
        }
    }

    override fun handleEffect(effect: Effect) = when (effect) {
        is Effect.ShowErrorLoadParkingSpots -> Toast.makeText(
            activity,
            "Unexpected problems on the server! Try restarting the application!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNetwork -> Toast.makeText(
            activity,
            "Problems with your connection! Check your internet connection!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorDeleteParkingSpot -> Toast.makeText(
            activity,
            "Error during deletion, try again later!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ToEditParkingSpotFragment -> toEditParkingSpotFragment(effect.parkingSpot)
        is Effect.ToCreateParkingSpotFragment -> toCreateParkingSpotFragment()
        is Effect.ShowDeleteDialog -> showDeleteDialog(effect.parkingSpot, effect.positionInAdapter)
    }

    @SuppressLint("SetTextI18n")
    fun showDeleteDialog(parkingSpot: ParkingSpot, positionInAdapter: Int) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)

        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)

        val text = view.findViewById<TextView>(R.id.textView)
        text.text = "Are you sure you\nwant to delete?"

        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickDeleteDialog(parkingSpot, positionInAdapter))
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun toCreateParkingSpotFragment() {
        val data = "spots"
        val intent = Intent((activity as AdminMainActivity), CreateModelActivity::class.java)
        intent.putExtra("fragment", data)

        startActivityForResult(intent, 200);
    }

    private fun toEditParkingSpotFragment(parkingSpot: ParkingSpot) {
        val data = "spots"
        val intent = Intent((activity as AdminMainActivity), EditModelActivity::class.java)
        intent.putExtra("fragment", data)

        intent.putExtra("parkingSpot", parkingSpot.toHashMap(withID = true))

        startActivityForResult(intent, 200);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            store.accept(Event.Ui.LoadParkingSpots)
        }
    }
}