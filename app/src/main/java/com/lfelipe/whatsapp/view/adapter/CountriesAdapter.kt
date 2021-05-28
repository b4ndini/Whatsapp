package com.lfelipe.whatsapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lfelipe.whatsapp.utils.loadSvg
import com.lfelipe.whatsapp.R
import com.lfelipe.whatsapp.model.CountryItem

class CountriesAdapter (
    private val countries: ArrayList<CountryItem>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:CountriesAdapter.ViewHolder, position: Int) {
        holder.bind(countries[position], onItemClicked)
    }

    override fun getItemCount(): Int {
        return countries.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(country: CountryItem, onItemClicked: (Int) -> Unit) = with(itemView){

            val flagImage = findViewById<ImageView>(R.id.ivNationFlag)

            Glide.with(this)
                .load(country.flag)
                .into(flagImage)

            flagImage.loadSvg(country.flag)

            val callingCode = "+${country.callingCodes[0]}"
            findViewById<TextView>(R.id.tvNationName).text = country.translations.br
            findViewById<TextView>(R.id.tvNationDdi).text = callingCode
            findViewById<TextView>(R.id.tvNativeNationName).text = country.nativeName
            findViewById<ConstraintLayout>(R.id.container).setOnClickListener {
                onItemClicked(this@ViewHolder.adapterPosition)
            }

        }

    }


}