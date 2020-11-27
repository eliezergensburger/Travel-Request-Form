package com.example.travelrequestform.ui.addTravelActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation
import com.example.travelrequestform.R
import com.example.travelrequestform.data.models.Travel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.button.MaterialButton
import com.example.travelrequestform.data.models.Travel.UserLocation
import java.text.DateFormat
import java.util.*


const val ADDRESS_CODE = 1
const val LOCATION_CODE = 2

class AddTravelActivity : AppCompatActivity(), View.OnClickListener {

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

    private lateinit var travelDate: Date
    private lateinit var returnDate: Date
    private lateinit var addressPlace: Place
    private lateinit var destinationPlace: Place
    private lateinit var awesomeValidation: AwesomeValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_travel)

        Places.initialize(applicationContext, "AIzaSyD10IpMmv30oFrH1iwNvsXkjFx7ZuKCSck")
        initializeViews()
        setValidation()
        setOnClickListeners()
        initializeSpinner()


        travelViewModel = ViewModelProviders.of(this).get(TravelViewModel::class.java)
        travelViewModel.getIsSuccess().observe(this, { isSuccess ->
            if (isSuccess)
                setToast()
            else
                Toast.makeText(
                    applicationContext, "Failed to register please try again",
                    Toast.LENGTH_LONG
                ).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            when (requestCode) {
                ADDRESS_CODE -> {
                    // initialize place
                    addressPlace = Autocomplete.getPlaceFromIntent(data as Intent)
                    // set address on EditText
                    etAddress.setText(addressPlace.address)
                }
                LOCATION_CODE -> {
                    // initialize place
                    destinationPlace = Autocomplete.getPlaceFromIntent(data as Intent)
                    // set address on EditText
                    etDestination.setText(destinationPlace.address)
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
        etAddress.inputType = InputType.TYPE_NULL
        etDestination = findViewById(R.id.et_destination)
        etDestination.inputType = InputType.TYPE_NULL
        numOfTravelers = findViewById(R.id.num_of_travelers)
        etTravelDate = findViewById(R.id.et_travelDate)
        etTravelDate.inputType = InputType.TYPE_NULL
        etReturnDate = findViewById(R.id.et_returnDate)
        etReturnDate.inputType = InputType.TYPE_NULL
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
        etAddress.setOnClickListener(this)
        etDestination.setOnClickListener(this)
        etTravelDate.setOnClickListener(this)
        etReturnDate.setOnClickListener(this)
        btnSend.setOnClickListener(this)
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
                    this@AddTravelActivity, { view, year, monthOfYear, dayOfMonth ->
                        etTravelDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                        travelDate = Date(year, monthOfYear + 1, dayOfMonth)
                    },
                    year,
                    month,
                    day
                )
                picker.datePicker.minDate = System.currentTimeMillis() - 1000
                picker.show()
            }
            etReturnDate -> {
                val picker = DatePickerDialog(
                    this@AddTravelActivity, { view, year, monthOfYear, dayOfMonth ->
                        etReturnDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                        returnDate = Date(year, monthOfYear + 1, dayOfMonth)
                    },
                    year,
                    month,
                    day
                )
                picker.datePicker.minDate = System.currentTimeMillis() - 1000
                picker.show()
            }
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            etAddress -> onClickPlace(v)
            etDestination -> onClickPlace(v)
            etTravelDate -> onClickDate(v)
            etReturnDate -> onClickDate(v)
            btnSend -> clickSend()
        }
    }

    private fun setValidation() {
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)
        awesomeValidation.addValidation(
            this, R.id.et_name,
            RegexTemplate.NOT_EMPTY, R.string.invalid_name
        )
        awesomeValidation.addValidation(
            this, R.id.et_phone,
            "^\\+?(972|0)(\\-)?0?(([23489]{1}\\d{7})|[5]{1}\\d{8})\$", R.string.invalid_phone
        )
        awesomeValidation.addValidation(
            this, R.id.et_mail,
            Patterns.EMAIL_ADDRESS, R.string.invalid_email
        )
        awesomeValidation.addValidation(etAddress, SimpleCustomValidation {
            return@SimpleCustomValidation it.isNotEmpty()
        }, "אנא בחר כתובת יציאה וחזרה")
        awesomeValidation.addValidation(
            etDestination, SimpleCustomValidation {
                return@SimpleCustomValidation it.isNotEmpty()
            }, "אנא בחר כתובת יעד"
        )
        awesomeValidation.addValidation(etTravelDate, SimpleCustomValidation {

            if (etTravelDate.text.isNotEmpty() && etReturnDate.text.isNotEmpty()) {
                val travelDate = Date.parse(etTravelDate.text.toString())
                val returnDate = Date.parse(etReturnDate.text.toString())
                return@SimpleCustomValidation returnDate - travelDate > 0
            } else return@SimpleCustomValidation false
        }, "התאריך חזרה חייב להיות מאוחר מתאריך היציאה")

        /*awesomeValidation.addValidation(etReturnDate, SimpleCustomValidation {

            *//*if (etTravelDate.text.isNotEmpty() && etReturnDate.text.isNotEmpty()) {
                val flag : Boolean = if (returnDate.year > travelDate.year) {
                    true
                } else if (returnDate.year == travelDate.year && returnDate.month > travelDate.month) {
                    true
                } else returnDate.month == travelDate.month && returnDate.day > travelDate.day
                return@SimpleCustomValidation flag
            }

        } else return@SimpleCustomValidation false*//*
        }, "התאריך חזרה חייב להיות מאוחר מתאריך היציאה")*/

    }

    private fun clickSend() {
        awesomeValidation.clear()
        if (awesomeValidation.validate()) {
            val travel = Travel()
            travel.arrivalDate = returnDate
            travel.clientEmail = etMail.text.toString()
            travel.clientName = etName.text.toString()
            travel.clientPhone = etPhone.text.toString()
            travel.company = HashMap()
            travel.requestType = Travel.RequestType.SENT
            travel.travelDate = travelDate
            travel.numOfTravelers = numOfTravelers.selectedItem as Int
            travel.travelLocations.add(UserLocation(destinationPlace))
            travel.address = UserLocation(addressPlace)

            travelViewModel.addTravel(travel)
        }
    }

    private fun setToast() {
        val layout = layoutInflater.inflate(
            R.layout.custom_toast,
            findViewById(R.id.cl_customToastContainer)
        )

        Toast(this).apply {
            setGravity(Gravity.BOTTOM, 0, 40)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }
}

