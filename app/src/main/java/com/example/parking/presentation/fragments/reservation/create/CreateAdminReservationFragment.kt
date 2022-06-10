package com.example.parking.presentation.fragments.reservation.create

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
import com.example.parking.R
import com.example.parking.data.models.Employee
import com.example.parking.data.models.createEmployee
import com.example.parking.presentation.fragments.reservation.create.elm.Effect
import com.example.parking.presentation.fragments.reservation.create.elm.Event
import com.example.parking.presentation.fragments.reservation.create.elm.State
import com.example.parking.presentation.fragments.reservation.create.elm.storeFactory
import com.example.parking.utils.toDate
import com.example.parking.utils.toStr
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateAdminReservationFragment : ElmFragment<Event, Effect, State>() {

    private var progressBar : FrameLayout? = null
    private var btnContinue : Button? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_create_admin_reservation, null)
        btnContinue = rootView.findViewById<Button>(R.id.buttonContinue)
        progressBar = rootView.findViewById<FrameLayout>(R.id.progressBarContainer)

        val dateChips = rootView.findViewById<ChipGroup>(R.id.DateChips)
        val startTimeChips = rootView.findViewById<ChipGroup>(R.id.StartTimeChips)
        val endTimeChips = rootView.findViewById<ChipGroup>(R.id.EndTimeChips)
        val etModel = rootView.findViewById<EditText>(R.id.etCarModel)
        val etNum = rootView.findViewById<EditText>(R.id.etCarNum)
        val etEmail = rootView.findViewById<EditText>(R.id.etEmployeeEmail)

        val c = Calendar.getInstance()
        val df: DateFormat = SimpleDateFormat("EEE dd/MM")
        var i = 0
        while (i < 7) {
            if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                i++
                val chip = inflater.inflate(R.layout.layout_chip_choice, dateChips, false) as Chip
                chip.text=df.format(c.time)
                dateChips.addView(chip)
            }
            c.add(Calendar.DATE, 1)
        }

        btnContinue?.setOnClickListener {
            if (!isFieldEmpty(etEmail) && !isFieldEmpty(etModel) && !isFieldEmpty(etNum)
                && isChipSelected(dateChips) && isChipSelected(startTimeChips) && isChipSelected(endTimeChips)) {

                val employee: Employee = createEmployee(etEmail.text.toString())

                val carModel: String = etModel.text.toString()
                val carRegistryNumber: String = etNum.text.toString()

                val startTime: Date =
                    ("${Calendar.getInstance().get(Calendar.YEAR)}" +
                            " ${getSelectedChip(dateChips)} " +
                            "${getSelectedChip(startTimeChips)}")
                        .toDate("yyyy EEE dd/MM HH:mm", locale = Locale.getDefault())
                val endTime: Date =
                    ("${Calendar.getInstance().get(Calendar.YEAR)}" +
                            " ${getSelectedChip(dateChips)} " +
                            "${getSelectedChip(endTimeChips)}")
                        .toDate("yyyy EEE dd/MM HH:mm", locale = Locale.getDefault())

                store.accept(Event.Ui.CreateClick(
                    employee = employee,
                    carModel = carModel, carRegistryNumber = carRegistryNumber,
                    startTime = startTime, endTime = endTime
                ))
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

    override val initEvent: Event = Event.Ui.Init

    override fun createStore(): Store<Event, Effect, State> = storeFactory()

    override fun render(state: State) {
        if (state.loading) {
            progressBar!!.visibility = View.VISIBLE
            btnContinue!!.isClickable = false
        } else {
            progressBar!!.visibility = View.INVISIBLE
            btnContinue!!.isClickable = true
        }
    }

    override fun handleEffect(effect: Effect) = when (effect) {
        is Effect.ShowConfirmDialog -> showConfirmDialog(
            employee = effect.employee,
            carModel = effect.carModel, carRegistryNumber = effect.carRegistryNumber,
            startTime = effect.startTime, endTime = effect.endTime
        )
        is Effect.ToReservationsFragment -> toReservationsFragment()
        is Effect.ShowErrorCreateReservation -> Toast.makeText(
            activity,
            "Unable to create a reservation, error on the server! Please check the entered data!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNotFreeParkingSpots -> Toast.makeText(
            activity,
            "There are no free parking spots!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNotFoundCar -> Toast.makeText(
            activity,
            "This car doesn't exist in the database!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNotFoundEmployee -> Toast.makeText(
            activity,
            "This employee doesn't exist in the database!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNetwork -> Toast.makeText(
            activity,
            "Problems with your connection! Check your internet connection!",
            Toast.LENGTH_SHORT
        ).show()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun showConfirmDialog(
        employee: Employee,
        carModel: String, carRegistryNumber: String,
        startTime: Date, endTime: Date,
    ) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)

        val textOutput = view.findViewById<TextView>(R.id.textView)
        textOutput.setText(
            "Employee: ${employee.name}\n" +
            "Car model: $carModel\n" +
            "Car registry number: $carRegistryNumber\n" +
            "Date: ${startTime.toStr(
                pattern = "EEE dd/MM",
                timeZone = TimeZone.getDefault(),
                locale = Locale.getDefault()
            )}\n" +
            "Time: ${startTime.toStr(
                pattern = "HH:mm",
                timeZone = TimeZone.getTimeZone("GMT"),
                locale = Locale.getDefault()
            )} - ${endTime.toStr(
                pattern = "HH:mm",
                timeZone = TimeZone.getTimeZone("GMT"),
                locale = Locale.getDefault())}"
        )
        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickConfirmDialog(
                employee = employee,
                carModel = carModel, carRegistryNumber = carRegistryNumber,
                startTime = startTime, endTime = endTime
            ))
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun toReservationsFragment() {
        val toast = Toast.makeText(activity, "Done", Toast.LENGTH_SHORT)
        toast.show()
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun getSelectedChip(chips : ChipGroup) : String {
        val chipsCount: Int = chips.childCount
        var msg = ""
        if (chipsCount != 0) {
            var i = 0
            while (i < chipsCount) {
                val chip = chips.getChildAt(i) as Chip
                if (chip.isChecked) {
                    msg += chip.getText().toString()
                }
                i++
            }
        }
        return msg
    }

    private fun isChipSelected(chips : ChipGroup) : Boolean {
        return if (getSelectedChip(chips) == "") {
            val toast = Toast.makeText(activity, "Select date and time", Toast.LENGTH_SHORT)
            toast.show()
            false
        } else {
            true
        }
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