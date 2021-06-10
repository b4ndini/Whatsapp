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
import com.lfelipe.whatsapp.viewmodel.PhoneRegisterViewModel
import com.lfelipe.whatsapp.viewmodel.SmsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SmsVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySmsVerificationBinding
    //private var noInstantVerification = true
    private val viewModel: SmsViewModel by viewModel()
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

        val num = intent.getStringExtra("phone_number")

        smsEditTextListener()
        observes()
        setupSmsCodeService(num)


        val sentSmsText = resources.getString(R.string.sms_msg_confirmation, num)
        val confirmText = resources.getString(R.string.confirm_number_sms, num)
        binding.tvConfirmPhone.text = confirmText
        binding.tvSentSms.text = sentSmsText





    }

    //service to send sms code
    private fun setupSmsCodeService(num: String?) {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {


                //binding.etInputSms.text =  Editable.Factory.getInstance().newEditable(credential.smsCode)
                viewModel.signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

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

        if (options != null) PhoneAuthProvider.verifyPhoneNumber(options)

    }


    private fun observes() {
        viewModel.signIn.observe(this,{
        it?.let{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }


        })

        viewModel.invalidCredential.observe(this,{
            it?.let{
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }

        })

    }



    private fun smsEditTextListener() {
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
        viewModel.signInWithPhoneAuthCredential(credential)

    }

}