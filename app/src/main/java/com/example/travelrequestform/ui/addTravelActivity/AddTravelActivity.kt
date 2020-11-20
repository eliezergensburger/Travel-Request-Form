package com.example.travelrequestform.ui.addTravelActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.travelrequestform.R
import com.example.travelrequestform.data.models.Travel
import com.example.travelrequestform.data.models.Travel.RequestType
import com.example.travelrequestform.data.models.Travel.UserLocation
import java.util.*
import kotlin.collections.HashMap
import androidx.lifecycle.Observer

class AddTravelActivity : AppCompatActivity() {

    private lateinit var travelViewModel: TravelViewModel
    private lateinit var tvSuccessText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_travel)

        //tvSuccessText = findViewById(R.id.tv_isSuccess)

        travelViewModel = ViewModelProviders.of(this).get(TravelViewModel::class.java)
        travelViewModel.getIsSuccess().observe(this,{ isSuccess ->
            if (isSuccess)
                Toast.makeText(applicationContext, "We made it!", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(applicationContext, "Failed to register please try again",
                    Toast.LENGTH_LONG).show()
        })

        /*val travel = Travel()
        travel.arrivalDate = Date(2020, 12, 4)
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
}