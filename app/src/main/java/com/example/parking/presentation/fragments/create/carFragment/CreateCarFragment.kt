package com.example.parking.presentation.fragments.create.carFragment

import android.annotation.SuppressLint
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
import com.example.parking.data.models.Car

import com.example.parking.presentation.fragments.create.carFragment.elm.*
import com.example.parking.presentation.fragments.create.carFragment.elm.storeFactory
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class CreateCarFragment : ElmFragment<Event, Effect, State>() {

    private var progressBar : FrameLayout? = null
    private var btContinue : Button? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_create_car, null)
        btContinue = rootView.findViewById(R.id.buttonContinue)
        progressBar = rootView.findViewById(R.id.progressBarContainer)
        val etModel = rootView.findViewById<EditText>(R.id.etCarModel)
        val etNum = rootView.findViewById<EditText>(R.id.etCarNum)

        btContinue?.setOnClickListener {
            if (!isFieldEmpty(etModel) && !(isFieldEmpty(etNum))) {
                // сюда вставить вызов функции создания в бэке
                val car: Car = Car(
                    model = etModel.text.toString(),
                    registryNumber = etNum.text.toString()
                )
                store.accept(Event.Ui.CreateClick(car))
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
    private fun showConfirmDialog(car: Car) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)
        // сюда информацию (уже добавила)
        val textOutput = view.findViewById<TextView>(R.id.textView)
        textOutput.text = "Car model: " + car.model + "\nRegistry number: " + car.registryNumber
        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickConfirmDialog(car))
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun toCarsFragment() {
        val toast = Toast.makeText(activity, "Done", Toast.LENGTH_SHORT)
        toast.show()
        activity?.finish()
    }

    override val initEvent: Event = Event.Ui.Init

    override fun createStore() = storeFactory()

    override fun render(state: State) {
        // todo loader!!

        // вот так вызывается загрузочная крутяшка (отключаем кнопку ещё на всякий)
//        progressBar?.visibility = View.VISIBLE
//        btContinue.isClickable = false
        // вот так она скрывается
//        progressBar?.visibility = View.INVISIBLE
    }

    override fun handleEffect(effect: Effect) = when (effect) {
        is Effect.ShowConfirmDialog -> showConfirmDialog(effect.car)
        is Effect.ToCarsFragment -> toCarsFragment()
        is Effect.ShowErrorCreateCar -> Toast.makeText(
            activity,
            "Unable to create a machine, error on the server! Please check the entered data!",
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