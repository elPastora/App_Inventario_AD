package com.elpastora.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elpastora.app.R
import com.elpastora.app.databinding.ActivitySellBinding

class SellActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNavHome.setSelectedItemId(R.id.imSell)

        binding.btnNavHome.setOnItemSelectedListener {
            when(it.itemId){
                R.id.imHomeApp -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.imProduct -> {
                    val intent = Intent(this, ProductActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.imSell -> true
                else -> false

            }
        }

    }
}