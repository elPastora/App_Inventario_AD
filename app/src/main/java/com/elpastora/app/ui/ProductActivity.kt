package com.elpastora.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.elpastora.app.R
import com.elpastora.app.databinding.ActivityProductBinding

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNavHome.setSelectedItemId(R.id.imProduct)

        binding.btnNavHome.setOnItemSelectedListener {
            when(it.itemId){
                R.id.imHomeApp -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.imProduct -> true
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