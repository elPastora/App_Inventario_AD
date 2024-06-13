package com.elpastora.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elpastora.app.R
import com.elpastora.app.databinding.ActivityContactBinding
import com.elpastora.app.ui.login.LoginActivity

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNavLogin.setSelectedItemId(R.id.imContact)

        binding.btnNavLogin.setOnItemSelectedListener {
            when(it.itemId){
                R.id.imHome -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.imLocation -> {
                    val intent = Intent(this, LocationActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.imContact -> true
                else -> false

            }
        }
    }
}