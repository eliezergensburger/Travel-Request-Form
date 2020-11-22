package com.example.travelrequestform.ui.addTravelActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.example.travelrequestform.R
import com.example.travelrequestform.data.models.Travel
import com.example.travelrequestform.data.models.Travel.RequestType
import com.example.travelrequestform.data.models.Travel.UserLocation
import java.util.*
import kotlin.collections.HashMap
import androidx.lifecycle.Observer
import com.example.travelrequestform.ui.mainActivity.MainActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.button.MaterialButton
import java.sql.Array
import java.util.Arrays.asList

class AddTravelActivity : AppCompatActivity() {

    private lateinit var travel : Travel
    private lateinit var travelViewModel: TravelViewModel

    private lateinit var etName : EditText
    private lateinit var etPhone : EditText
    private lateinit var etMail : EditText
    private lateinit var etAddress : EditText
    private lateinit var etDestination : EditText
    private lateinit var numOfTravelers : Spinner
    private lateinit var etTravelDate : EditText
    private lateinit var etReturnDate : EditText
    private lateinit var btnSend : MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_travel)

        initializeViews()
        travel = Travel()
        initializeAddress()

        travelViewModel = ViewModelProviders.of(this).get(TravelViewModel::class.java)
        travelViewModel.getIsSuccess().observe(this,{ isSuccess ->
            if (isSuccess)
                Toast.makeText(applicationContext, "We made it!", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(applicationContext, "Failed to register please try again",
                    Toast.LENGTH_LONG).show()
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

    private fun initializeAddress(){

        Places.initialize(applicationContext, "AIzaSyD10IpMmv30oFrH1iwNvsXkjFx7ZuKCSck")

        etAddress.isFocusable = false
        etAddress.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                // initialize place filed list
                val fieldList : List<Place.Field> = arrayListOf(Place.Field.ADDRESS,
                Place.Field.LAT_LNG, Place.Field.NAME)

                // Create intent
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                , fieldList).build(this@AddTravelActivity)

                //start activity result
                startActivityForResult(intent, 100)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            // initialize place
            val place = Autocomplete.getPlaceFromIntent(data as Intent)
            // set address on EditText
            etAddress.setText(place.address)
            //travel.address?.convertFromPlace(place)
            Toast.makeText(applicationContext, place.latLng?.latitude.toString(),
                Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, place.latLng?.longitude.toString(),
                Toast.LENGTH_LONG).show()

        }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            // initialize status
            val status = Autocomplete.getStatusFromIntent(data as Intent)
            // Display toast
            Toast.makeText(applicationContext, status.statusMessage,
            Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeViews(){
        etName = findViewById(R.id.et_name)
        etPhone = findViewById(R.id.et_phone)
        etMail = findViewById(R.id.et_mail)
        etAddress = findViewById(R.id.et_address)
        etDestination = findViewById(R.id.et_destination)
        numOfTravelers = findViewById(R.id.num_of_travelers)
        etTravelDate = findViewById(R.id.et_travelDate)
        etReturnDate = findViewById(R.id.et_returnDate)
        btnSend = findViewById(R.id.btn_send)
    }

}