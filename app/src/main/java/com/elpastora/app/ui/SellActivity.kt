package com.elpastora.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elpastora.app.R
import com.elpastora.app.databinding.ActivitySellBinding
import com.elpastora.app.ui.product.ProductActivity

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