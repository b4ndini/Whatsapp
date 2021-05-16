package com.lfelipe.whatsapp.view.activities

import android.content.Intent

import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lfelipe.whatsapp.databinding.ActivityWelcomeBinding


class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAgree.setOnClickListener {
            val intent = Intent(this, PhoneRegisterActivity::class.java)
            startActivity(intent)

        }

        settingLinks()

    }


    fun makeLink(url: String): ClickableSpan {

        val clickSpan: ClickableSpan = object : ClickableSpan() {

            override fun onClick(widget: View) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

        }
        return clickSpan
    }

    fun settingLinks(){

        val linkOne = "https://www.whatsapp.com/legal/privacy-policy?lg=pt&lc=BR&eea=0"
        val linkTwo = "https://www.whatsapp.com/legal/terms-of-service?lg=pt&lc=BR&eea=0"
        val spannebleString = SpannableString(binding.tvMainText.text)

        spannebleString.setSpan(makeLink(linkOne), 11, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannebleString.setSpan(makeLink(linkTwo), 83, 100, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        binding.tvMainText.text = spannebleString
        binding.tvMainText.movementMethod = LinkMovementMethod.getInstance()


    }


}