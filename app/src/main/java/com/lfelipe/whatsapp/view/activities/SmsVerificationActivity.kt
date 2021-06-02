package com.lfelipe.whatsapp.view.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lfelipe.whatsapp.R
import com.lfelipe.whatsapp.databinding.ActivitySmsVerificationBinding
import java.util.concurrent.TimeUnit

class SmsVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySmsVerificationBinding
    private val firebaseAuth by lazy{
        Firebase.auth
    }
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        smsFieldListener()
        val num = intent.getStringExtra("phone_number")

        val text = resources.getString(R.string.sms_msg_confirmation, num)
        val text2 = resources.getString(R.string.confirm_number_sms, num)
        binding.textView2.text = text
        binding.textView.text = text2

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("firebasee", "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("firebase", "onVerificationFailed" + e.localizedMessage, e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }

        }

        firebaseAuth.setLanguageCode("pt")

        val options = num?.let {
            PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(it)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()

        }

        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this,"Código inválido!" , Toast.LENGTH_LONG).show()
                    }
                    // Update UI
                }
            }
    }

    private fun smsFieldListener() {
        binding.etInputSms.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(sms: CharSequence?, start: Int, before: Int, count: Int) {
                if (sms != null) {
                    if(sms.length == 6){
                        verifyPhoneNumberWithCode(storedVerificationId, sms.toString())
                    }


                }

            }
        })
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String){
        val credential = PhoneAuthProvider.getCredential(verificationId ?: "empty", code)
        signInWithPhoneAuthCredential(credential)

    }

}