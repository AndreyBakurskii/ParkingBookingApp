package com.example.parking.presentation.fragments.reservation.edit.admin

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
import com.example.parking.data.mapper.CarMapper
import com.example.parking.data.mapper.EmployeeMapper
import com.example.parking.data.mapper.ParkingSpotMapper
import com.example.parking.data.models.*
import com.example.parking.presentation.fragments.reservation.edit.admin.elm.Effect
import com.example.parking.presentation.fragments.reservation.edit.admin.elm.Event
import com.example.parking.presentation.fragments.reservation.edit.admin.elm.State
import com.example.parking.presentation.fragments.reservation.edit.admin.elm.storeFactory
import com.example.parking.utils.toDate
import com.example.parking.utils.toStr
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vivid.money.elmslie.android.base.ElmFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EditAdminReservationFragment : ElmFragment<Event, Effect, State>() {

    private lateinit var parkingSpot: ParkingSpot
    private lateinit var employee: Employee
    private lateinit var car: Car
    private lateinit var startTime: Date
    private lateinit var endTime: Date
    private lateinit var reservationId: UUID
    private lateinit var reservation: Reservation

    private var dateTimeFormatForServer: String = "yyyy-MM-dd'T'HH:mm:ss"

    private var btnContinue: Button? = null
    private var progressBar : FrameLayout? = null


    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_edit_admin_reservation, null)

        btnContinue = rootView.findViewById<Button>(R.id.buttonContinue)
        progressBar = rootView.findViewById(R.id.progressBarContainer)

        reservationId = UUID.fromString((activity?.intent?.extras?.get("reservationId")!! as String))
        parkingSpot = ParkingSpotMapper().fromHashMapToModel(activity?.intent?.extras?.get("parkingSpot")!! as HashMap<String, Any>)
        employee = EmployeeMapper().fromHashMapToModel(activity?.intent?.extras?.get("employee")!! as HashMap<String, String>)
        car = CarMapper().fromHashMapToModel(activity?.intent?.extras?.get("car")!! as HashMap<String, Any>)
        startTime = (activity?.intent?.extras?.get("startTime")!! as String).toDate(dateTimeFormatForServer)
        endTime = (activity?.intent?.extras?.get("endTime")!! as String).toDate(dateTimeFormatForServer)

        reservation = Reservation(
            id = reservationId,
            employee = employee,
            car = car,
            parkingSpot = parkingSpot,
            startTime = startTime,
            endTime = endTime
        )

        val dateChips = rootView.findViewById<ChipGroup>(R.id.DateChips)

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
        selectChip(dateChips, startTime.toStr("EEE dd/MM", TimeZone.getDefault(), Locale.getDefault()))

        val startTimeChips = rootView.findViewById<ChipGroup>(R.id.StartTimeChips)
        selectChip(startTimeChips, startTime.toStr("HH:mm", TimeZone.getDefault(), Locale.getDefault()))

        val endTimeChips = rootView.findViewById<ChipGroup>(R.id.EndTimeChips)
        selectChip(endTimeChips, endTime.toStr("HH:mm", TimeZone.getDefault(), Locale.getDefault()))

        val etModel = rootView.findViewById<EditText>(R.id.etCarModel)
        etModel.setText(car.model)

        val etNum = rootView.findViewById<EditText>(R.id.etCarNum)
        etNum.setText(car.registryNumber)

        val etEmail = rootView.findViewById<EditText>(R.id.etEmployeeEmail)
        etEmail.setText(employee.name)

        btnContinue?.setOnClickListener {
            if (!isFieldEmpty(etEmail) && !isFieldEmpty(etModel) && !isFieldEmpty(etNum)
                && isChipSelected(dateChips) && isChipSelected(startTimeChips) && isChipSelected(endTimeChips)
            ) {
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

                store.accept(Event.Ui.UpdateClick(
                    reservation = reservation,
                    updatedEmployee = employee,
                    updatedCarModel = carModel, updatedCarRegistryNumber = carRegistryNumber,
                    updatedStartTime = startTime, updatedEndTime = endTime
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

    override fun createStore() = storeFactory()

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
            reservation = effect.reservation,
            employee = effect.updatedEmployee,
            carModel = effect.updatedCarModel,
            carRegistryNumber = effect.updatedCarRegistryNumber,
            startTime = effect.updatedStartTime,
            endTime = effect.updatedEndTime
        )
        is Effect.ToReservationsFragment -> toReservationsFragment()
        is Effect.ShowErrorUpdateReservation -> Toast.makeText(
            activity,
            "Unable to edit a reservation! Please check the entered data!",
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
        is Effect.ShowErrorNotFreeParkingSpots -> Toast.makeText(
            activity,
            "There are no free parking spots!",
            Toast.LENGTH_SHORT
        ).show()
        is Effect.ShowErrorNetwork -> Toast.makeText(
            activity,
            "Problems with your connection! Check your internet connection!",
            Toast.LENGTH_SHORT
        ).show()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun showConfirmDialog(
        reservation: Reservation,
        employee: Employee,
        carModel: String, carRegistryNumber: String,
        startTime: Date, endTime: Date,
    ) {
        val view = layoutInflater.inflate(R.layout.alertdialog_model, null)
        val alertDialog = AlertDialog.Builder(activity, R.style.AlertDialog)
        alertDialog.setTitle("Confirm")
        alertDialog.setCancelable(false)

        val textOutput = view.findViewById<TextView>(R.id.textView)
        textOutput.text =
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
        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickConfirmDialog(
                reservation = reservation,
                updatedCarModel = carModel,
                updatedCarRegistryNumber = carRegistryNumber,
                updatedEmployee = employee,
                updatedStartTime = startTime,
                updatedEndTime = endTime
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

    private fun selectChip(chips : ChipGroup, msg : String) {
        val chipsCount: Int = chips.childCount
        if (chipsCount != 0) {
            var i = 0
            while (i < chipsCount) {
                val chip = chips.getChildAt(i) as Chip
                if (chip.text == msg) {
                    chip.isChecked = true
                }
                i++
            }
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