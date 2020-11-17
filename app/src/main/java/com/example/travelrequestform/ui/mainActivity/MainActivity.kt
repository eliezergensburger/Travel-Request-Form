package com.example.travelrequestform.ui.mainActivity
import com.google.android.libraries.places.api.Places;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelrequestform.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Places.initialize(applicationContext,"AIzaSyD10IpMmv30oFrH1iwNvsXkjFx7ZuKCSck")

    }
}