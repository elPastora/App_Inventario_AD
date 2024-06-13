package com.elpastora.app.ui.product


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.stringPreferencesKey
import com.elpastora.app.R
import com.elpastora.app.data.api.BrandService
import com.elpastora.app.data.api.CategoryService
import com.elpastora.app.data.api.ProductService
import com.elpastora.app.data.model.BrandResponse
import com.elpastora.app.data.model.Categoria
import com.elpastora.app.data.model.CategoryResponse
import com.elpastora.app.data.model.ErrorResponse
import com.elpastora.app.data.model.Marca
import com.elpastora.app.data.model.ProductRequest
import com.elpastora.app.data.model.ProductResponse
import com.elpastora.app.data.model.UserProfile
import com.elpastora.app.data.util.ApiClient
import com.elpastora.app.databinding.ActivityProductAddBinding
import com.elpastora.app.ui.SellActivity
import com.elpastora.app.ui.catalog.adapter.BrandsCatalogAdapter
import com.elpastora.app.ui.catalog.adapter.CategoriesCatalogAdapter
import com.elpastora.app.ui.login.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductAddBinding

    private lateinit var catCatalogAdapter: CategoriesCatalogAdapter

    private lateinit var brandCatalogAdapter: BrandsCatalogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initListeners()
    }

    private fun initUI() {
        productIdFocusListener()
        productNameFocusListener()
        productQuantityFocusListener()
        productReserveQuantityFocusListener()
        productPurchasePriceFocusListener()
        productSalePriceFocusListener()
        getCatalogs()
    }

    private fun productIdFocusListener() {
        binding.tietProductId.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilProductId.helperText = validProductId()
            }

        }
    }

    private fun validProductId(): String? {

        val productId = binding.tietProductId.text.toString()
        val userNameRegex = "^[a-zA-Z0-9]*\$"

        if (!Regex(userNameRegex).matches(productId)) {
            return getString(R.string.error_character_product_id)
        } else if (productId.isEmpty()) {
            return getString(R.string.helper_required)
        }

        return null
    }

    private fun productNameFocusListener() {
        binding.tietProductName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilProductName.helperText = validProductName()
            }

        }
    }

    private fun validProductName(): String? {

        val productName = binding.tietProductName.text.toString()
        val userNameRegex = "^[a-zA-Z0-9]*\$"

        if (!Regex(userNameRegex).matches(productName)) {
            return getString(R.string.error_character_product_name)
        } else if (productName.isEmpty()) {
            return getString(R.string.helper_required)
        }

        return null
    }

    private fun productQuantityFocusListener() {
        binding.tietProductQuantity.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilProductQuantity.helperText = validProductQuantity()
            }

        }
    }

    private fun validProductQuantity(): String? {

        val productQuantity = binding.tietProductQuantity.text.toString()

        if (productQuantity.isEmpty()) {
            return getString(R.string.helper_required)
        }

        return null
    }

    private fun productReserveQuantityFocusListener() {
        binding.tietProductQuantityRes.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilProductQuantityRes.helperText = validProductReserveQuantity()
            }

        }
    }

    private fun validProductReserveQuantity(): String? {

        val productQuantityRes = binding.tietProductQuantityRes.text.toString()

        if (productQuantityRes.isEmpty()) {
            return getString(R.string.helper_required)
        }

        return null
    }

    private fun productPurchasePriceFocusListener() {
        binding.tietProductPurchasePrice.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilProductPurchasePrice.helperText = validProductPurchasePrice()
            }

        }
    }

    private fun validProductPurchasePrice(): String? {

        val productPurchasePrice = binding.tietProductPurchasePrice.text.toString()

        if (productPurchasePrice.isEmpty()) {
            return getString(R.string.helper_required)
        }

        return null
    }

    private fun productSalePriceFocusListener() {
        binding.tietProductSalePrice.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilProductSalePrice.helperText = validProductSalePrice()
            }

        }
    }

    private fun validProductSalePrice(): String? {

        val productSalePrice = binding.tietProductSalePrice.text.toString()

        if (productSalePrice.isEmpty()) {
            return getString(R.string.helper_required)
        }

        return null
    }

    private fun getCatalogs() {
        binding.pbProduct.isVisible = true
        getAllCategory()
        getAllBrand()
        binding.pbProduct.isVisible = false
    }

    private fun getAllCategory() {

        CoroutineScope(Dispatchers.IO).launch {
            val service = ApiClient.getRetrofit().create(CategoryService::class.java)
            try {
                getUserProfile().collect {
                    val token: String = it.token

                    val response: Response<CategoryResponse> =
                        service.getAllCategories("Bearer $token")

                    if (response.isSuccessful) {
                        val category = response.body()

                        if (category != null) {
                            runOnUiThread {
                                goToCategoriesCatalog(category.categorias)
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
                            invalidFormService(message)
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

    private fun goToCategoriesCatalog(categories: List<Categoria>) {

        catCatalogAdapter = CategoriesCatalogAdapter(categories, this)

        binding.snCategory.adapter = catCatalogAdapter

        binding.snCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = categories[position].idCategoria
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun getAllBrand() {

        CoroutineScope(Dispatchers.IO).launch {
            val service = ApiClient.getRetrofit().create(BrandService::class.java)
            try {
                getUserProfile().collect {
                    val token: String = it.token

                    val response: Response<BrandResponse> =
                        service.getAllBrands("Bearer $token")

                    if (response.isSuccessful) {
                        val brand = response.body()

                        if (brand != null) {
                            runOnUiThread {
                                goToBrandsCatalog(brand.marcas)
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
                            invalidFormService(message)
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

    private fun goToBrandsCatalog(brands: List<Marca>) {

        brandCatalogAdapter = BrandsCatalogAdapter(brands, this)

        binding.snBrand.adapter = brandCatalogAdapter

        binding.snBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = brands[position].idMarca
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun invalidFormService(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Forma Invalida")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->
                //no hacer nada
            }
            .show()
    }

    private fun initListeners() {

        binding.btnProductCreate.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        binding.pbProduct.isVisible = true

        binding.tilProductId.helperText = validProductId()
        binding.tilProductName.helperText = validProductName()
        binding.tilProductQuantity.helperText = validProductQuantity()
        binding.tilProductQuantityRes.helperText = validProductReserveQuantity()
        binding.tilProductPurchasePrice.helperText = validProductPurchasePrice()
        binding.tilProductSalePrice.helperText = validProductSalePrice()

        val isValidProductId = binding.tilProductId.helperText == null
        val isValidProductName = binding.tilProductName.helperText == null
        val isValidProductQuantity = binding.tilProductQuantity.helperText == null
        val isValidProductQuantityRes = binding.tilProductQuantityRes.helperText == null
        val isValidProductPurchasePrice = binding.tilProductPurchasePrice.helperText == null
        val isValidProductSalePrice = binding.tilProductSalePrice.helperText == null

        if (isValidProductId && isValidProductName && isValidProductQuantity && isValidProductQuantityRes && isValidProductPurchasePrice
            && isValidProductSalePrice
        ) {
            addProduct()
        } else {
            invalidForm()
        }

        binding.pbProduct.isVisible = false
    }

    private fun invalidForm() {
        var message = ""
        if (binding.tilProductId.helperText != null)
            message += "\n\nCodigo Producto: " + binding.tilProductId.helperText
        if (binding.tilProductName.helperText != null)
            message += "\n\nNombre Producto: " + binding.tilProductName.helperText
        if (binding.tilProductName.helperText != null)
            message += "\n\nCantidad: " + binding.tilProductQuantity.helperText
        if (binding.tilProductName.helperText != null)
            message += "\n\nCantidad Reserva: " + binding.tilProductQuantityRes.helperText
        if (binding.tilProductName.helperText != null)
            message += "\n\nPrecio Compra: " + binding.tilProductPurchasePrice.helperText
        if (binding.tilProductName.helperText != null)
            message += "\n\nPrecio Venta: " + binding.tilProductSalePrice.helperText

        AlertDialog.Builder(this)
            .setTitle("Forma Invalida")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->
                //no hacer nada
            }
            .show()
    }

    private fun addProduct() {
        val selectedCategory = binding.snCategory.selectedItem as Categoria
        val selectedBrand = binding.snBrand.selectedItem as Marca

        val product: ProductRequest = ProductRequest(
            binding.tietProductId.text.toString(),
            binding.tietProductName.text.toString(),
            selectedCategory.idCategoria,
            selectedBrand.idMarca,
            binding.tietProductQuantity.text.toString().toInt(),
            binding.tietProductQuantityRes.text.toString().toInt(),
            binding.tietProductPurchasePrice.text.toString().toDouble(),
            binding.tietProductSalePrice.text.toString().toDouble()
        )

        CoroutineScope(Dispatchers.IO).launch {
            val service = ApiClient.getRetrofit().create(ProductService::class.java)

            try {
                getUserProfile().collect {
                    val token: String = it.token

                    val response: Response<ProductResponse> =
                        service.addProducts("Bearer $token",product)

                    if (response.isSuccessful) {
                        val prod = response.body()

                        if (prod != null) {
                            runOnUiThread {
                                navToProduct()
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
                            invalidFormService(message)
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

    private fun navToProduct() {
        val intent = Intent(this, ProductActivity::class.java)
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


}