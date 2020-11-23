package com.example.travelrequestform.ui.addTravelActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.travelrequestform.R
import com.example.travelrequestform.data.models.Travel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.button.MaterialButton
import java.util.*


const val ADDRESS_CODE = 1
const val LOCATION_CODE = 2

class AddTravelActivity : AppCompatActivity() {

    private lateinit var travel: Travel
    private lateinit var travelViewModel: TravelViewModel

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etMail: EditText
    private lateinit var etAddress: EditText
    private lateinit var etDestination: EditText
    private lateinit var numOfTravelers: Spinner
    private lateinit var etTravelDate: EditText
    private lateinit var etReturnDate: EditText
    private lateinit var btnSend: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_travel)

        Places.initialize(applicationContext, "AIzaSyD10IpMmv30oFrH1iwNvsXkjFx7ZuKCSck")
        travel = Travel()
        initializeViews()
        setOnClickListeners()
        initializeSpinner()


        travelViewModel = ViewModelProviders.of(this).get(TravelViewModel::class.java)
        travelViewModel.getIsSuccess().observe(this, { isSuccess ->
            if (isSuccess)
                Toast.makeText(applicationContext, "We made it!", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(
                    applicationContext, "Failed to register please try again",
                    Toast.LENGTH_LONG
                ).show()
        })


        /*travel.arrivalDate = Date(2020, 12, 4)
        travel.clientEmail = "yehudajka@gmail.com"
        travel.clientName = "Yehuda"
        travel.clientPhone = "055093239"
        travel.company = HashMap()
        travel.company["gogo"] = true
        travel.requestType = RequestType.SENT
        travel.travelDate = Date(2020, 11, 20)
        travel.travelLocations.add(UserLocation(1.44, 2.33))

        travelViewModel.addTravel(travel)*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            // initialize place
            val place = Autocomplete.getPlaceFromIntent(data as Intent)

            when (requestCode) {
                ADDRESS_CODE -> {
                    // set address on EditText
                    etAddress.setText(place.address)
                    //travel.address?.convertFromPlace(place)
                }
                LOCATION_CODE -> {
                    // set address on EditText
                    etDestination.setText(place.address)
                    //travel.address?.convertFromPlace(place)
                }
            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // initialize status
            val status = Autocomplete.getStatusFromIntent(data as Intent)
            // Display toast
            Toast.makeText(
                applicationContext, status.statusMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun initializeViews() {
        etName = findViewById(R.id.et_name)
        etPhone = findViewById(R.id.et_phone)
        etMail = findViewById(R.id.et_mail)
        etAddress = findViewById(R.id.et_address)
        etAddress.isFocusable = false
        etDestination = findViewById(R.id.et_destination)
        etDestination.isFocusable = false
        numOfTravelers = findViewById(R.id.num_of_travelers)
        etTravelDate = findViewById(R.id.et_travelDate)
        etTravelDate.inputType = InputType.TYPE_NULL
        etReturnDate = findViewById(R.id.et_returnDate)
        btnSend = findViewById(R.id.btn_send)
    }

    private fun onClickPlace(v: View) {

        // initialize place filed list
        val fieldList: List<Place.Field> = arrayListOf(
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG, Place.Field.NAME
        )
        // Create intent
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fieldList
        ).build(this@AddTravelActivity)
        //start activity result
        when (v) {
            etAddress -> startActivityForResult(intent, ADDRESS_CODE)
            etDestination -> startActivityForResult(intent, LOCATION_CODE)
        }
    }


    private fun setOnClickListeners() {
        etAddress.setOnClickListener { view -> onClickPlace(view) }
        etDestination.setOnClickListener { view -> onClickPlace(view) }
        etTravelDate.setOnClickListener { view -> onClickDate(view) }
        etReturnDate.setOnClickListener { view -> onClickDate(view) }
    }

    private fun initializeSpinner() {
        val items: MutableList<Int> = arrayListOf()
        for (i in 1..50) {
            items.add(i)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        numOfTravelers.adapter = adapter
    }

    private fun onClickDate(view: View) {

        val cldr: Calendar = Calendar.getInstance()
        val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
        val month: Int = cldr.get(Calendar.MONTH)
        val year: Int = cldr.get(Calendar.YEAR)

        when (view) {
            // date picker dialog
            etTravelDate -> {
                val picker = DatePickerDialog(
                    this@AddTravelActivity,
                    { view, year, monthOfYear, dayOfMonth -> etTravelDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                    year,
                    month,
                    day
                )
                picker.show()
            }
            etReturnDate -> {
                val picker = DatePickerDialog(
                    this@AddTravelActivity,
                    { view, year, monthOfYear, dayOfMonth -> etReturnDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                    year,
                    month,
                    day
                )
                picker.show()
            }
        }

    }
}

