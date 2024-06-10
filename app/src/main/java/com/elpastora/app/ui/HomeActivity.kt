package com.elpastora.app.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import com.elpastora.app.R
import com.elpastora.app.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FULLNAME = "EXTRA_FULLNAME"
    }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullName:String = intent.getStringExtra(EXTRA_FULLNAME).orEmpty()
        val welcome = getString(R.string.tittle_welcome_home)

        val ss1 = SpannableStringBuilder()
            .append("${welcome} ")
            .bold{append(fullName)}


        binding.tvWelcome.setText(ss1)

        binding.btnNavHome.setSelectedItemId(R.id.imHomeApp)

        binding.btnNavHome.setOnItemSelectedListener {
            when(it.itemId){
                R.id.imHomeApp -> true
                R.id.imProduct -> {
                    val intent = Intent(this, ProductActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.imSell -> {
                    val intent = Intent(this, SellActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false

            }
        }
    }
}