package com.example.travelrequestform.data.repositories

import androidx.lifecycle.MutableLiveData
import com.example.travelrequestform.data.models.Travel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class TravelDataSource private constructor() {

    val isSuccess = MutableLiveData<Boolean>()

    /*interface ChangedListener {
        fun change()
    }

    private var listener: ChangedListener? = null
    fun setChangedListener(l: ChangedListener?) {
        listener = l
    }*/


    val travelsList: List<Travel>? = null


    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val travelsRef: DatabaseReference = firebaseDatabase.getReference("ExistingTravels")

    fun addTravel(travel: Travel) {
        val id: String = travelsRef.push().key as String
        travel.travelId = id
        val task = travelsRef.child(id).setValue(travel)

        task.addOnCompleteListener(object : OnCompleteListener<Void> {

            override fun onComplete(p0: Task<Void>) {
                isSuccess.value = task.isSuccessful
            }
        })

        /*travelsRef.child(id).setValue(travel)
            .addOnSuccessListener(object : OnSuccessListener<Void?> {
                override fun onSuccess(aVoid: Void?) {
                    isSuccess.value = true
                    isSuccess.value = null
                }
            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    isSuccess.value = false
                    isSuccess.value = null
                }
            })*/
    }

    companion object {
        var instance: TravelDataSource? = null
            get() {
                if (field == null) field = TravelDataSource()
                return field
            }
            private set
    }
}
