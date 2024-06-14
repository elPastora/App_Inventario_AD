package com.elpastora.app.ui.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.elpastora.app.data.model.Producto
import com.elpastora.app.data.model.UserProfile
import com.elpastora.app.data.util.ApiClient
import com.elpastora.app.databinding.ActivityProductEditBinding
import com.elpastora.app.ui.catalog.adapter.BrandsCatalogAdapter
import com.elpastora.app.ui.catalog.adapter.CategoriesCatalogAdapter
import com.elpastora.app.ui.login.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductEditBinding

    private var isChangeButtom:Boolean = false

    private lateinit var catCatalogAdapter: CategoriesCatalogAdapter

    private lateinit var brandCatalogAdapter: BrandsCatalogAdapter

    private var codigoProducto: String = ""

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codigoProducto = intent.getStringExtra(EXTRA_ID).orEmpty()
        getProductById(codigoProducto)
        initListeners()
    }

    private fun getProductById(codigoProducto: String) {
        binding.pbProduct.isVisible = true

        CoroutineScope(Dispatchers.IO).launch {
            val service = ApiClient.getRetrofit().create(ProductService::class.java)

            try {
                getUserProfile().collect {
                    val token: String = it.token

                    val response: Response<ProductResponse> =
                        service.getProductById("Bearer $token",codigoProducto)

                    if (response.isSuccessful) {
                        val product = response.body()

                        if (product != null) {
                            runOnUiThread {
                                createUI(product.producto)
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
            }catch (exception: Exception) {
                Log.i("Product", exception.toString())
                binding.pbProduct.isVisible = false
            }
        }
    }

    private fun createUI(product: Producto){
        binding.tietProductId.setText(product.codigoProducto)
        binding.tietProductId.isEnabled = false
        binding.tietProductName.setText(product.nombre)
        binding.tietProductName.isEnabled = false
        val category = arrayOf(product.nombreCategoria)
        var adapter = ArrayAdapter(this, R.layout.item_drowdowm, category)
        binding.snCategory.adapter = adapter
        binding.snCategory.isEnabled = false
        val brand = arrayOf(product.nombreMarca)
        adapter = ArrayAdapter(this, R.layout.item_drowdowm, brand)
        binding.snBrand.adapter = adapter
        binding.snBrand.isEnabled = false
        binding.tietProductQuantity.setText(product.cantidad.toString())
        binding.tietProductQuantity.isEnabled = false
        binding.tietProductQuantityRes.setText(product.cantidadReserva.toString())
        binding.tietProductQuantityRes.isEnabled = false
        binding.tietProductPurchasePrice.setText(product.precioCompra.toString())
        binding.tietProductPurchasePrice.isEnabled = false
        binding.tietProductSalePrice.setText(product.precioVenta.toString())
        binding.tietProductSalePrice.isEnabled = false

    }

    private fun initListeners() {
        binding.btnProductEdit.setOnClickListener {
            if(isChangeButtom){
                submitForm()
            }else{
                changeFormToEdit()
            }

        }
    }

    private fun changeFormToEdit() {
        binding.pbProduct.isVisible = true
        isChangeButtom = true
        val helperRequired:String = getString(R.string.helper_required)

        binding.tietProductName.isEnabled = true
        //binding.tilProductName.helperText = helperRequired

        binding.snCategory.isEnabled = true
        binding.snBrand.isEnabled = true

        binding.tietProductQuantity.isEnabled = true
        //binding.tilProductQuantity.helperText = helperRequired

        binding.tietProductQuantityRes.isEnabled = true
        //binding.tilProductQuantityRes.helperText = helperRequired

        binding.tietProductPurchasePrice.isEnabled = true
        //binding.tilProductPurchasePrice.helperText = helperRequired

        binding.tietProductSalePrice.isEnabled = true
        //binding.tilProductSalePrice.helperText = helperRequired

        binding.btnProductEdit.setText(R.string.button_edit_product)
        binding.btnProductEdit.setBackgroundColor(getColor(R.color.button_success_app))

        productNameFocusListener()
        productQuantityFocusListener()
        productReserveQuantityFocusListener()
        productPurchasePriceFocusListener()
        productSalePriceFocusListener()
        getCatalogs()

        binding.pbProduct.isVisible = false
    }

    private fun getCatalogs() {
        getAllCategory()
        getAllBrand()
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

    private fun submitForm() {
        binding.pbProduct.isVisible = true

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
            editProduct()
        } else {
            invalidForm()
        }

        binding.pbProduct.isVisible = false
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
        val userNameRegex = "^[a-zA-Z0-9 /]*\$"

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

    private fun editProduct(){
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
                        service.updateProduct("Bearer $token",codigoProducto, product)

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

    private fun invalidFormLogin(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Forma Invalida")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->
                //no hacer nada
            }
            .show()
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

}