package com.lfelipe.whatsapp.view.activities

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lfelipe.whatsapp.R
import com.lfelipe.whatsapp.databinding.ActivityPhoneRegisterBinding
import com.lfelipe.whatsapp.viewmodel.PhoneRegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PhoneRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneRegisterBinding
    private val viewModel: PhoneRegisterViewModel by viewModel()
    private lateinit var phoneFormatting: PhoneNumberFormattingTextWatcher
    private val SECOND_ACTIVITY_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        signUp()
        observes()
        ddiListener()

        binding.dropDownText.setOnClickListener {
            val intent = Intent(this, CountriesActivity::class.java)
            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                val countryName: String? = intent?.getStringExtra("country")
                val ddi: String? = intent?.getStringExtra("ddi_code")
                val countryCode: String? = intent?.getStringExtra("country_code")

                binding.etNumberEdit.removeTextChangedListener(phoneFormatting)
                phoneFormatting = PhoneNumberFormattingTextWatcher(countryCode ?: "BR")
                binding.etNumberEdit.addTextChangedListener(
                    phoneFormatting
                )
                binding.etDdiEdit.text = Editable.Factory.getInstance().newEditable(ddi ?: "55")
                binding.dropDownText.text = Editable.Factory.getInstance().newEditable(countryName ?: "Brasil")
            }
        }

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

            //to do validacao
            if (phoneNumber.length < 12 || phoneNumber.length > 15) {
                MaterialAlertDialogBuilder(
                    this,
                    R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                    .setMessage("+$ddi $phoneNumber \nnão é um número válido para o país Brasil.")
                    .setPositiveButton("Ok") { dialog, which ->
                        closeContextMenu()
                    }
                    .show()
            } else {
                MaterialAlertDialogBuilder(
                    this,
                    R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                    .setMessage("Nós confirmaremos o número:\n\n+$ddi $phoneNumber \n\nEste número está correto ou deseja editá-lo?")
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