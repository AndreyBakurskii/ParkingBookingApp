package com.example.parking.presentation.fragments.employee.list

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.parking.presentation.activities.AdminActivity.AdminMainActivity
import com.example.parking.presentation.activities.CreateModelActivity.CreateModelActivity
import com.example.parking.R
import com.example.parking.presentation.adapters.ExpListAdapterAdminEmployees
import com.example.parking.data.models.Employee
import com.example.parking.presentation.fragments.employee.list.elm.Effect
import com.example.parking.presentation.fragments.employee.list.elm.Event
import com.example.parking.presentation.fragments.employee.list.elm.State
import com.example.parking.presentation.fragments.employee.list.elm.storeFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store

class EmployeesFragment : ElmFragment<Event, Effect, State>() {
    
    private var employeesInAdapter: ArrayList<Employee>? = arrayListOf()
    private var employeesAdapter: ExpListAdapterAdminEmployees? = null
    
    var btAdd : FloatingActionButton? = null
    var progressBar : FrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_employees, null)
        val listView = rootView.findViewById<ExpandableListView>(R.id.expListView)
        
        progressBar = rootView.findViewById(R.id.progressBarContainer)
        btAdd = rootView.findViewById(R.id.fab)
        
        employeesAdapter =
            ExpListAdapterAdminEmployees(
                activity,
                employeesInAdapter,
                store
            )
        listView.setAdapter(employeesAdapter)

        btAdd?.setOnClickListener {
            store.accept(Event.Ui.ClickCreateEmployee)
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

    override val initEvent: Event = Event.Ui.LoadEmployees

    override fun render(state: State) {
        if (state.loading) {
            progressBar!!.visibility = View.VISIBLE
        } else {
            progressBar!!.visibility = View.INVISIBLE
        }

        if (state.doUpdate) {
            employeesInAdapter!!.clear()
            employeesInAdapter!!.addAll(state.employees as Collection<Employee>)
            employeesAdapter!!.notifyDataSetChanged()
        }
    }

    override fun handleEffect(effect: Effect) = when (effect) {
        is Effect.ShowErrorLoadEmployees -> Toast.makeText(
            activity,
            "Unexpected problems on the server! Try restarting the application!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNetwork -> Toast.makeText(
            activity,
            "Problems with your connection! Check your internet connection!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorDeleteEmployee -> Toast.makeText(
            activity,
            "Error during deletion, try again later!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ToCreateEmployeeFragment -> toCreateEmployeeFragment()
        is Effect.ShowDeleteDialog -> showDeleteDialog(effect.employee, effect.positionInAdapter)
    }
    
    @SuppressLint("SetTextI18n")
    private fun showDeleteDialog(employee: Employee, positionInAdapter: Int) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)

        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)

        val text = view.findViewById<TextView>(R.id.textView)
        text.text = "Are you sure you\nwant to delete?"

        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickDeleteDialog(employee, positionInAdapter))
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    fun toCreateEmployeeFragment() {
        val data = "employees"
        val intent = Intent((activity as AdminMainActivity), CreateModelActivity::class.java)
        intent.putExtra("fragment", data)

        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            store.accept(Event.Ui.LoadEmployees)
        }
    }
}