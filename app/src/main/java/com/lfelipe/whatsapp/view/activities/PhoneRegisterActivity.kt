package com.lfelipe.whatsapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lfelipe.whatsapp.R
import com.lfelipe.whatsapp.databinding.ActivityPhoneRegisterBinding
import com.lfelipe.whatsapp.viewmodel.PhoneRegisterViewModel

class PhoneRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneRegisterBinding
    private lateinit var viewModel: PhoneRegisterViewModel
    private lateinit var phoneFormatting: PhoneNumberFormattingTextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =  ViewModelProvider(this).get(PhoneRegisterViewModel::class.java)

        signUp()
        observes()
        ddiListener()


    }

    private fun ddiListener() {
        binding.etDdiEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(code: CharSequence?, start: Int, before: Int, count: Int) {
                if (code != null) {
                    if(code.isNotEmpty())
                        viewModel.getCountryByCode(code.toString())

                }

            }
        })
    }

    private fun observes() {
        viewModel.countryLiveData.observe(this,{
            it?.let{



                when {
                    it[0].callingCodes[0] == "1" -> {
                        binding.dropDownText.text = Editable.Factory.getInstance().newEditable(it[1].translations.br)
                        binding.etNumberEdit.removeTextChangedListener(phoneFormatting)
                        phoneFormatting = PhoneNumberFormattingTextWatcher(it[1].alpha2Code)
                        binding.etNumberEdit.addTextChangedListener(
                           phoneFormatting
                        )

                    }
                    it[0].callingCodes[0] == "44" -> {
                        binding.dropDownText.text = Editable.Factory.getInstance().newEditable(it[3].translations.br)
                        binding.etNumberEdit.removeTextChangedListener(phoneFormatting)
                        phoneFormatting = PhoneNumberFormattingTextWatcher(it[3].alpha2Code)
                        binding.etNumberEdit.addTextChangedListener(
                            phoneFormatting
                        )
                    }
                    else -> {
                        binding.dropDownText.text = Editable.Factory.getInstance().newEditable(it[0].translations.br)
                        binding.etNumberEdit.removeTextChangedListener(phoneFormatting)
                        phoneFormatting = PhoneNumberFormattingTextWatcher(it[0].alpha2Code)
                        binding.etNumberEdit.addTextChangedListener(
                            phoneFormatting
                        )
                    }
                }
            }
        })

        viewModel.errorMsgLiveData.observe(this,{
            it?.let{
                binding.dropDownText.text = Editable.Factory.getInstance().newEditable(it)
            }
        })
    }


    private fun signUp() {
        val countryName: String? = intent.getStringExtra("country")
        var phoneNumber: String
        var ddi: String? = intent.getStringExtra("ddi_code")
        val countryCode= intent.getStringExtra("country_code")

        phoneFormatting = PhoneNumberFormattingTextWatcher(countryCode ?: "BR")
        binding.etNumberEdit.addTextChangedListener(
            phoneFormatting
        )

        binding.etDdiEdit.text = Editable.Factory.getInstance().newEditable(ddi ?: "55")
        binding.dropDownText.text = Editable.Factory.getInstance().newEditable(countryName ?: "Brasil")

        binding.btnContinue.setOnClickListener {

            ddi = binding.etDdiEdit.text.toString()
            phoneNumber = binding.etNumberEdit.text.toString()


            if (phoneNumber.length < 12 || phoneNumber.length > 15) {
                MaterialAlertDialogBuilder(
                    this,
                    R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                    .setMessage("+$ddi $phoneNumber \nnão é um número válido para o país Brasil")
                    .setPositiveButton("Ok") { dialog, which ->
                        closeContextMenu()
                    }
                    .show()
            } else {
                MaterialAlertDialogBuilder(
                    this,
                    R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                    .setMessage("Nós confirmaremos o número: + $ddi $phoneNumber Este número está correto ou deseja editá-lo?")
                    .setNegativeButton("Editar") { dialog, which ->
                        closeContextMenu()
                    }
                    .setPositiveButton("Ok") { dialog, which ->
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .show()

            }

        }
    }


}