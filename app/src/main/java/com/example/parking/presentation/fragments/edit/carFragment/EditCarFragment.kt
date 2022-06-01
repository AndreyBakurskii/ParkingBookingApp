package com.example.parking.presentation.fragments.edit.carFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parking.R
import com.example.parking.data.models.Car
import com.example.parking.presentation.fragments.edit.carFragment.elm.Effect
import com.example.parking.presentation.fragments.edit.carFragment.elm.Event
import com.example.parking.presentation.fragments.edit.carFragment.elm.State
import com.example.parking.presentation.fragments.edit.carFragment.elm.storeFactory
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store

class EditCarFragment : ElmFragment<Event, Effect, State>() {

    private var progressBar : FrameLayout? = null
    private var btContinue : Button? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_edit_car, null)
        btContinue = rootView.findViewById<Button>(R.id.buttonContinue)
        progressBar = rootView.findViewById<FrameLayout>(R.id.progressBarContainer)

        val updatedModel = rootView.findViewById<EditText>(R.id.etCarModel)
        val updatedNum = rootView.findViewById<EditText>(R.id.etCarNum)

        // обновить старую инфу машины на новую
        btContinue!!.setOnClickListener {
//            store.accept(Event.Ui.EditClick)
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
        textOutput.text = "Car model: " + view.findViewById<EditText>(R.id.etCarModel).text +
                "\nRegistry number: " + view.findViewById<EditText>(R.id.etCarNum).text

        alertDialog.setPositiveButton("OK") { _, _ ->
            // выводим toast что всё ок и закрываем активность
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
}