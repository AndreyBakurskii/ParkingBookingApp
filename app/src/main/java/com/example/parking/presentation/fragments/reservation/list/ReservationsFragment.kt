package com.example.parking.presentation.fragments.reservation.list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.parking.R
import com.example.parking.data.models.Reservation
import com.example.parking.presentation.activities.AdminActivity.AdminMainActivity
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.presentation.activities.EditModelActivity.EditModelActivity
import com.example.parking.presentation.adapters.ExpListAdapterAdminReservations
import com.example.parking.presentation.fragments.reservation.list.elm.Effect
import com.example.parking.presentation.fragments.reservation.list.elm.Event
import com.example.parking.presentation.fragments.reservation.list.elm.State
import com.example.parking.presentation.fragments.reservation.list.elm.storeFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import java.util.*

class ReservationsFragment : ElmFragment<Event, Effect, State>() {

    private var reservationsInAdapter: ArrayList<Reservation>? = arrayListOf()
    private var reservationAdapter: ExpListAdapterAdminReservations? = null

    private var btAdd : FloatingActionButton? = null
    private var progressBar : FrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_reservations, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)

        progressBar = rootView.findViewById(R.id.progressBarContainer)
        btAdd = rootView.findViewById(R.id.fab)

        reservationAdapter =
            ExpListAdapterAdminReservations(
                activity,
                reservationsInAdapter,
                store
            )
        listView.setAdapter(reservationAdapter)


        btAdd?.setOnClickListener {
            store.accept(Event.Ui.ClickCreateReservation)
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
    override val initEvent: Event = Event.Ui.LoadReservations

    override fun render(state: State) {
        if (state.loading) {
            progressBar!!.visibility = View.VISIBLE
        } else {
            progressBar!!.visibility = View.INVISIBLE
        }

        if (state.doUpdate) {
            reservationsInAdapter!!.clear()
            reservationsInAdapter!!.addAll(state.reservations as Collection<Reservation>)
            reservationAdapter!!.notifyDataSetChanged()
        }
    }

    override fun handleEffect(effect: Effect) = when (effect) {
        is Effect.ShowErrorLoadReservations -> Toast.makeText(
            activity,
            "Unexpected problems on the server! Try restarting the application!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNetwork -> Toast.makeText(
            activity,
            "Problems with your connection! Check your internet connection!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorDeleteReservation -> Toast.makeText(
            activity,
            "Error during deletion, try again later!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ToEditReservationFragment -> toEditReservationFragment(effect.reservation)
        is Effect.ToCreateReservationFragment -> toCreateReservationFragment()
        is Effect.ShowDeleteDialog -> showDeleteDialog(effect.reservation, effect.positionInAdapter)
    }

    @SuppressLint("SetTextI18n")
    private fun showDeleteDialog(reservation: Reservation, positionInAdapter: Int) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)

        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)

        val text = view.findViewById<TextView>(R.id.textView)
        text.text = "Are you sure you\nwant to delete?"

        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickDeleteDialog(reservation, positionInAdapter))
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun toCreateReservationFragment() {
        val data = "reservations"
        val intent = Intent((activity as AdminMainActivity), CreateModelActivity::class.java)
        intent.putExtra("fragment", data)
        (activity as AdminMainActivity).startActivity(intent)
    }

    private fun toEditReservationFragment(reservation: Reservation) {
        val data = "reservations"
        val intent = Intent((activity as AdminMainActivity), EditModelActivity::class.java)
        intent.putExtra("fragment", data)
        activity?.startActivity(intent)
    }
}