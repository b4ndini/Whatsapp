package com.lfelipe.whatsapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lfelipe.whatsapp.R
import com.lfelipe.whatsapp.databinding.ActivityCountriesBinding
import com.lfelipe.whatsapp.view.adapter.CountriesAdapter
import com.lfelipe.whatsapp.viewmodel.CountriesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountriesBinding
    private val viewModel: CountriesViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.search -> {
                    true
                }
                else -> false
            }
        }

        observer()
        viewModel.getCountries()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }


    private fun observer() {
        viewModel.countryLiveData.observe(this, {
            it?.let {

                binding.rvCountriesList.apply {
                    val itemDecoration =
                        DividerItemDecoration(
                            this@CountriesActivity,
                            DividerItemDecoration.VERTICAL
                        )

                    itemDecoration.setDrawable(resources.getDrawable(R.drawable.layer, null))
                    addItemDecoration(itemDecoration)
                    layoutManager = LinearLayoutManager(this@CountriesActivity)
                    adapter = CountriesAdapter(it) { position ->
                        val intent = Intent(
                            this@CountriesActivity,
                            PhoneRegisterActivity::class.java
                        ).apply {
                            putExtra("country", it[position].translations.br)
                            putExtra("ddi_code", it[position].callingCodes[0])
                            putExtra("country_code", it[position].alpha2Code)

                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                }
            }
        })

        viewModel.errorMsgLiveData.observe(this, {
            it?.let {
                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show();
            }
        })
    }

}