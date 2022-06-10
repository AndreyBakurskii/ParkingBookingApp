package com.example.parking.presentation.activities.UserActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ExpandableListView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parking.R
import com.example.parking.data.models.Employee
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.presentation.adapters.ExpListAdapterUserReservations
import com.example.parking.data.models.Reservation
import com.example.parking.presentation.activities.UserActivity.elm.Effect
import com.example.parking.presentation.activities.UserActivity.elm.Event
import com.example.parking.presentation.activities.UserActivity.elm.State
import com.example.parking.presentation.activities.UserActivity.elm.storeFactory
import com.example.parking.presentation.adapters.ExpListAdapterAdminReservations
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.core.store.Store
import java.util.*

class UserMainActivity : ElmActivity<Event, Effect, State>(R.layout.activity_user_main) {
    private var reservationsInAdapter: ArrayList<Reservation>? = arrayListOf()
    private var reservationAdapter: ExpListAdapterUserReservations? = null

    private var btAdd : FloatingActionButton? = null
    private var progressBar : FrameLayout? = null

    var employee: Employee = Employee(
        id = UUID.fromString("5a1874b2-4d30-3af0-ad60-1daf278ba512"),
        name = "bakurskii2001@gmail.com"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        progressBar = findViewById(R.id.progressBarContainer)
        btAdd = findViewById(R.id.fab)

        val listView = findViewById<ExpandableListView>(R.id.expListView)
        reservationAdapter =
            ExpListAdapterUserReservations(
                this,
                reservationsInAdapter,
                store
            )
        listView.setAdapter(reservationAdapter)

        val btAdd = findViewById<FloatingActionButton>(R.id.fab)

        btAdd.setOnClickListener {
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
                    btAdd.hide()
                } else {
                    btAdd.show()
                }
            }
        })
        store.accept(Event.Ui.LoadReservations(
            employee = employee
        ))
    }

    override fun onStart() {
        super.onStart()
    }

    override fun createStore(): Store<Event, Effect, State> = storeFactory()

//    override val initEvent: Event = Event.Ui.Init

    override val initEvent: Event = Event.Ui.Init

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
            this,
            "Unexpected problems on the server! Try restarting the application!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNetwork -> Toast.makeText(
            this,
            "Problems with your connection! Check your internet connection!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ToCreateReservationFragment -> toCreateReservationFragment()
    }

    private fun toCreateReservationFragment() {
        val data = "user"
        val intent = Intent(this, CreateModelActivity::class.java)
        intent.putExtra("fragment", data)
        startActivityForResult(intent, 200);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            store.accept(Event.Ui.LoadReservations(
                employee = employee
            ))
        }
    }
}
