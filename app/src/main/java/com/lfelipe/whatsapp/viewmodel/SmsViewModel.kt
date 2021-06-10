package com.lfelipe.whatsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SmsViewModel() : ViewModel() {


    var signIn: MutableLiveData<FirebaseUser> = MutableLiveData()
    var invalidCredential: MutableLiveData<String> = MutableLiveData()
    private val firebaseAuth by lazy{
        Firebase.auth
    }


    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){


        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signIn.postValue(task.result?.user)


                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        invalidCredential.postValue("Invalid code")
                    }
                    else{
                        invalidCredential.postValue("Something wrong")
                    }

                }
            }
    }



}