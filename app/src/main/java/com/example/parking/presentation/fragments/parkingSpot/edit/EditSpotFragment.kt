package com.example.parking.presentation.fragments.parkingSpot.edit

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
import com.example.parking.data.mapper.ParkingSpotMapper
import com.example.parking.data.models.ParkingSpot
import com.example.parking.presentation.fragments.parkingSpot.edit.elm.Effect
import com.example.parking.presentation.fragments.parkingSpot.edit.elm.Event
import com.example.parking.presentation.fragments.parkingSpot.edit.elm.State
import com.example.parking.presentation.fragments.parkingSpot.edit.elm.storeFactory
import vivid.money.elmslie.android.base.ElmFragment

class EditSpotFragment : ElmFragment<Event, Effect, State>() {

    private var progressBar : FrameLayout? = null
    private var btContinue : Button? = null
    private lateinit var parkingSpot: ParkingSpot

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        parkingSpot = ParkingSpotMapper().fromHashMapToModel(activity?.intent?.extras?.get("parkingSpot")!! as HashMap<String, Any>)

        val rootView: View = inflater.inflate(R.layout.fragment_edit_spot, null)

        btContinue = rootView.findViewById<Button>(R.id.buttonContinue)
        progressBar = rootView.findViewById(R.id.progressBarContainer)

        val etNumSpot = rootView.findViewById<EditText>(R.id.etNumSpot)
        etNumSpot.setText(parkingSpot.parkingNumber.toString(), TextView.BufferType.EDITABLE)

        btContinue?.setOnClickListener {
            if (!isFieldEmpty(etNumSpot)) {
                parkingSpot.parkingNumber = etNumSpot.text.toString().toInt()
                store.accept(Event.Ui.EditClick(parkingSpot))
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
    fun showConfirmDialog(parkingSpot: ParkingSpot) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
        
        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)
        
        val textOutput = view.findViewById<TextView>(R.id.textView)
        textOutput.text = "Parking spot number: ${parkingSpot.parkingNumber}"

        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickConfirmDialog(parkingSpot))
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    fun toParkingSpotsFragment() {
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
        is Effect.ShowConfirmDialog -> showConfirmDialog(effect.parkingSpot)
        is Effect.ToParkingSpotsFragment -> toParkingSpotsFragment()
        is Effect.ShowErrorEditParkingSpot -> Toast.makeText(
            activity,
            "Unable to edit a parking spot, error on the server! Please check the entered data!",
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