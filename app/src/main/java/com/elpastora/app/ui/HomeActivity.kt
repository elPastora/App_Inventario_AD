package com.elpastora.app.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.datastore.preferences.core.stringPreferencesKey
import com.elpastora.app.R
import com.elpastora.app.data.model.UserProfile
import com.elpastora.app.databinding.ActivityHomeBinding
import com.elpastora.app.ui.login.dataStore
import com.elpastora.app.ui.product.ProductActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        CoroutineScope(Dispatchers.IO).launch {

            getUserProfile().collect{
                val fullName:String = it.nombreCompleto
                val welcome = getString(R.string.tittle_welcome_home)

                val ss1 = SpannableStringBuilder()
                    .append("${welcome} ")
                    .bold{append(fullName)}

                runOnUiThread {
                    binding.tvWelcome.setText(ss1)
                }

            }

        }



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

    private fun getUserProfile() = dataStore.data.map { preferences ->
        UserProfile(
            nombreUsuario = preferences[stringPreferencesKey("nombreUsuario")].orEmpty(),
            email = preferences[stringPreferencesKey("email")].orEmpty(),
            nombreCompleto = preferences[stringPreferencesKey("nombreCompleto")].orEmpty(),
            token = preferences[stringPreferencesKey("token")].orEmpty()
        )
    }

}