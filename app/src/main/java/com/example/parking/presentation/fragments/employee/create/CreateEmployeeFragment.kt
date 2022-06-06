package com.example.parking.presentation.fragments.employee.create

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.R
import com.example.parking.data.models.Employee
import com.example.parking.data.models.createEmployee
import com.example.parking.presentation.fragments.employee.create.elm.Effect
import com.example.parking.presentation.fragments.employee.create.elm.Event
import com.example.parking.presentation.fragments.employee.create.elm.State
import com.example.parking.presentation.fragments.employee.create.elm.storeFactory
import vivid.money.elmslie.android.base.ElmFragment

class CreateEmployeeFragment : ElmFragment<Event, Effect, State>() {

    private var progressBar : FrameLayout? = null
    private var btContinue : Button? = null

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_create_employee, null)

        btContinue = rootView.findViewById<Button>(R.id.buttonContinue)
        progressBar = rootView.findViewById(R.id.progressBarContainer)

        val etEmployeeEmail = rootView.findViewById<EditText>(R.id.etEmployeeEmail)

        btContinue?.setOnClickListener {
            if (!isFieldEmpty(etEmployeeEmail)) {
                val employee: Employee = createEmployee(etEmployeeEmail.text.toString())
                store.accept(Event.Ui.CreateClick(employee))
            }
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    @SuppressLint("SetTextI18n")
    fun showConfirmDialog(employee: Employee) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)
        // сюда информацию (уже добавила)
        val textOutput = view.findViewById<TextView>(R.id.textView)
        textOutput.text = "E-mail: ${employee.name}"

        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickConfirmDialog(employee))
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    fun toEmployeesFragment() {
        val toast = Toast.makeText(activity, "Done", Toast.LENGTH_SHORT)
        toast.show()
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }
    
    override val initEvent: Event = Event.Ui.Init

    override fun createStore() = storeFactory()

    override fun render(state: State) {
        if (state.loading) {
            progressBar!!.visibility = View.VISIBLE
            btContinue!!.isClickable = false
        } else {
            progressBar!!.visibility = View.INVISIBLE
            btContinue!!.isClickable = true
        }
    }

    override fun handleEffect(effect: Effect) = when (effect) {
        is Effect.ShowConfirmDialog -> showConfirmDialog(effect.employee)
        is Effect.ToEmployeesFragment -> toEmployeesFragment()
        is Effect.ShowErrorCreateEmployee -> Toast.makeText(
            activity,
            "Unable to create a employee, error on the server! Please check the entered data!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNetwork -> Toast.makeText(
            activity,
            "Problems with your connection! Check your internet connection!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorEmployeeExist -> Toast.makeText(
            activity,
            "Employee with this name already exist! Change name!",
            Toast.LENGTH_SHORT
        ).show()
    }
    
    private fun isFieldEmpty(etField : EditText) : Boolean {
        return if (TextUtils.isEmpty(etField.text.toString())) {
            etField.error = "This field cannot be empty"
            true
        } else {
            false
        }
    }

}