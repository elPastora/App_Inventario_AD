package com.elpastora.app.ui.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.recyclerview.widget.LinearLayoutManager
import com.elpastora.app.R
import com.elpastora.app.data.api.ProductService
import com.elpastora.app.data.model.ErrorResponse
import com.elpastora.app.data.model.ProductResponse
import com.elpastora.app.data.model.ProductsResponse
import com.elpastora.app.data.model.UserProfile
import com.elpastora.app.data.util.ApiClient
import com.elpastora.app.databinding.ActivityProductBinding
import com.elpastora.app.ui.HomeActivity
import com.elpastora.app.ui.SellActivity
import com.elpastora.app.ui.login.dataStore
import com.elpastora.app.ui.product.adapter.ProductAdapter
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding

    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNav()
        initUI()
        initListeners()
    }

    private fun initNav() {
        binding.btnNavHome.setSelectedItemId(R.id.imProduct)

        binding.btnNavHome.setOnItemSelectedListener {
            when (it.itemId) {
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

    private fun initUI() {
        getAllProducts()

        productAdapter = ProductAdapter {
            navToProductById(it)
        }

        binding.rvProduct.layoutManager = LinearLayoutManager(this)
        binding.rvProduct.adapter = productAdapter
    }

    private fun getAllProducts() {
        binding.pbProduct.isVisible = true

        CoroutineScope(Dispatchers.IO).launch {
            val service = ApiClient.getRetrofit().create(ProductService::class.java)
            try {

                getUserProfile().collect {
                    val token: String = it.token

                    val response: Response<ProductsResponse> =
                        service.getAllProducts("Bearer $token")

                    if (response.isSuccessful) {
                        val product = response.body()

                        if (product != null) {
                            runOnUiThread {
                                productAdapter.updateList(product.productos)
                                binding.pbProduct.isVisible = false
                            }
                        }
                    } else {

                        val errorResponse =
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )

                        var message = ""

                        errorResponse.errors.map {
                            message = it.message
                        }

                        runOnUiThread {
                            invalidFormLogin(message)
                            binding.pbProduct.isVisible = false
                        }

                    }
                }


            } catch (exception: Exception) {
                Log.i("Product", exception.toString())
                binding.pbProduct.isVisible = false
            }
        }
    }

    private fun invalidFormLogin(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Forma Invalida")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->
                //no hacer nada
            }
            .show()
    }

    private fun navToProductById(id: String) {
        val intent = Intent(this, SellActivity::class.java)
        //intent.putExtra(EXTRA_ID, id)
        startActivity(intent)
    }

    private fun getUserProfile() = dataStore.data.map { preferences ->
        UserProfile(
            nombreUsuario = preferences[stringPreferencesKey("nombreUsuario")].orEmpty(),
            email = preferences[stringPreferencesKey("email")].orEmpty(),
            nombreCompleto = preferences[stringPreferencesKey("nombreCompleto")].orEmpty(),
            token = preferences[stringPreferencesKey("token")].orEmpty()
        )
    }

    private fun initListeners() {
        binding.fabAddProduct.setOnClickListener {
            val intent = Intent(this, ProductAddActivity::class.java)
            startActivity(intent)
        }
    }
}