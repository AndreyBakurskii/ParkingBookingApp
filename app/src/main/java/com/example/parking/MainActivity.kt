package com.example.parking

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.parking.ELMMainActivity.Effect
import com.example.parking.ELMMainActivity.Event
import com.example.parking.ELMMainActivity.State
import com.example.parking.ELMMainActivity.storeFactory
import vivid.money.elmslie.android.base.ElmActivity


class MainActivity : ElmActivity<Event, Effect, State>(R.layout.activity_main) {

    override val initEvent: Event = Event.Ui.Init

    override fun createStore() = storeFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            this,
            R.array.roles, R.layout.titleitem_dropdown
        )
        adapter.setDropDownViewResource(R.layout.dropdown_item)
        spinner.adapter = adapter

        val buttonStart = findViewById<Button>(R.id.buttonStart)
        buttonStart.setOnClickListener {
            store.accept(Event.Ui.StartClick(spinner.selectedItemPosition))
        }
    }

    override fun render(state: State) {
        Log.i("STATE", "render state")
    }

    private fun showAlertDialogEmail() {
        val view = layoutInflater.inflate(R.layout.edittext_email, null)

        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialog)
        alertDialog.setTitle("Enter e-mail")
        alertDialog.setCancelable(false)

        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickAlertDialogEmail(
                view.findViewById<EditText>(R.id.etEmail)
                    .text
                    .toString()
                )
            )
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun showAlertDialogPassword() {
        val view = layoutInflater.inflate(R.layout.edittext_password, null)

        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialog)
        alertDialog.setTitle("Enter password")
        alertDialog.setCancelable(false)

        alertDialog.setPositiveButton("OK") { _, _ ->
            store.accept(Event.Ui.OkClickAlertDialogPassword(
                view.findViewById<EditText>(R.id.etPassword)
                    .text
                    .toString()
            )
            )
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    override fun handleEffect(effect: Effect) = when (effect) {
        is Effect.ShowAlertDialogEmail -> showAlertDialogEmail()
        is Effect.ShowAlertDialogPassword -> showAlertDialogPassword()
        is Effect.ShowErrorInvalidPassword -> Toast.makeText(applicationContext, "Wrong password!", Toast.LENGTH_SHORT).show()
        is Effect.ShowErrorInvalidEmail -> Toast.makeText(applicationContext, "Invalid e-mail address", Toast.LENGTH_SHORT).show()
        is Effect.ToUserMainActivity -> startActivity(Intent(this, UserMainActivity::class.java))
        is Effect.ToAdminMainActivity -> startActivity(Intent(this, AdminMainActivity::class.java))
        is Effect.ShowCars -> Toast.makeText(applicationContext, effect.cars[0].toString(), Toast.LENGTH_SHORT).show()
    }
}
