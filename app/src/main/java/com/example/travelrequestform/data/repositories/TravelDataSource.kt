package com.example.travelrequestform.data.repositories

import androidx.lifecycle.MutableLiveData
import com.example.travelrequestform.data.models.Travel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener


class TravelDataSource private constructor() {

    val isSuccess = MutableLiveData<Boolean?>()


    interface ChangedListener {
        fun change()
    }

    private var listener: ChangedListener? = null
    fun setChangedListener(l: ChangedListener?) {
        listener = l
    }


    var travelsList: List<Travel>? = null
        private set


    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var travelsRef: DatabaseReference = firebaseDatabase.getReference("ExistingTravels")

    fun addTravel(travel: Travel) {
        val id: String = travelsRef.push().getKey()
        travel.setTravelId(id)
        travelsRef.child(id).setValue(travel).addOnSuccessListener(object : OnSuccessListener<Void?> {
            override fun onSuccess(aVoid: Void?) {
                isSuccess.setValue(true)
                isSuccess.setValue(null)
            }
        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                isSuccess.setValue(false)
                isSuccess.setValue(null)
            }
        })
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
