package com.example.parking.presentation.fragments.car.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.parking.R
import com.example.parking.data.mapper.CarMapper
import com.example.parking.data.models.Car
import com.example.parking.presentation.fragments.car.edit.elm.Effect
import com.example.parking.presentation.fragments.car.edit.elm.Event
import com.example.parking.presentation.fragments.car.edit.elm.State
import com.example.parking.presentation.fragments.car.edit.elm.storeFactory
import vivid.money.elmslie.android.base.ElmFragment
import java.util.*

class EditCarFragment : ElmFragment<Event, Effect, State>() {

    private var progressBar : FrameLayout? = null
    private var btContinue : Button? = null
    private lateinit var car: Car

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        car = CarMapper().fromHashMapToModel(activity?.intent?.extras?.get("car")!! as HashMap<String, Any>)

        val rootView: View = inflater.inflate(R.layout.fragment_edit_car, null)
        btContinue = rootView.findViewById(R.id.buttonContinue)
        progressBar = rootView.findViewById(R.id.progressBarContainer)

        val updatedModel = rootView.findViewById<EditText>(R.id.etCarModel)
        val updatedRegistryNumber = rootView.findViewById<EditText>(R.id.etCarNum)

        updatedModel.setText(car.model, TextView.BufferType.EDITABLE)
        updatedRegistryNumber.setText(car.registryNumber, TextView.BufferType.EDITABLE)

        // обновить старую инфу машины на новую
        btContinue!!.setOnClickListener {
            if (!isFieldEmpty(updatedModel) && (!isFieldEmpty(updatedRegistryNumber))){
                car.model = updatedModel.text.toString()
                car.registryNumber = updatedRegistryNumber.text.toString()
                store.accept(Event.Ui.EditClick(car))
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
    fun showConfirmDialog(car: Car) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)
        // сюда информацию (уже добавила)
        val textOutput = view.findViewById<TextView>(R.id.textView)
        textOutput.text = "Car model: ${car.model}\nRegistry number: ${car.registryNumber}"

        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickConfirmDialog(car))
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    fun toCarsFragment() {
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
        is Effect.ShowConfirmDialog -> showConfirmDialog(effect.car)
        is Effect.ToCarsFragment -> toCarsFragment()
        is Effect.ShowErrorEditCar -> Toast.makeText(
            activity,
            "Unable to edit a car, error on the server! Please check the entered data!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNetwork -> Toast.makeText(
            activity,
            "Problems with your connection! Check your internet connection!",
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