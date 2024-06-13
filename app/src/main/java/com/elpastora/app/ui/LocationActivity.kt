package com.elpastora.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elpastora.app.R
import com.elpastora.app.databinding.ActivityLocationBinding
import com.elpastora.app.ui.login.LoginActivity

class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNavLogin.setSelectedItemId(R.id.imLocation)

        binding.btnNavLogin.setOnItemSelectedListener {
            when(it.itemId){
                R.id.imHome -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.imLocation -> true
                R.id.imContact -> {
                    val intent = Intent(this, ContactActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false

            }
        }
    }
}