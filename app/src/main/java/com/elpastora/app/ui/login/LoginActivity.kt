package com.elpastora.app.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.elpastora.app.R
import com.elpastora.app.data.api.LoginService
import com.elpastora.app.data.model.ErrorResponse
import com.elpastora.app.data.model.LoginRequest
import com.elpastora.app.data.model.LoginResponse
import com.elpastora.app.data.util.ApiClient
import com.elpastora.app.databinding.ActivityLoginBinding
import com.elpastora.app.ui.ContactActivity
import com.elpastora.app.ui.HomeActivity
import com.elpastora.app.ui.LocationActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

val Context.dataStore by preferencesDataStore(name = "USER_PREFERENCES")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usernameFocusListener()
        passwordFocusListener()

        binding.btnLogin.setOnClickListener {
            submitForm()
        }

        binding.btnNavLogin.setSelectedItemId(R.id.imHome)

        binding.btnNavLogin.setOnItemSelectedListener {
            when(it.itemId){
                R.id.imHome -> true
                R.id.imLocation -> {
                    val intent = Intent(this, LocationActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.imContact -> {
                    val intent = Intent(this, ContactActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false

            }
        }

    }

    private fun submitForm() {

        binding.tilUserName.helperText = validUserName()
        binding.tilPassword.helperText = validPassword()

        val validUserName = binding.tilUserName.helperText == null
        val validPassword = binding.tilPassword.helperText == null

        if (validUserName && validPassword) {
            login()
        } else {
            invalidForm()
        }
    }

    private fun login() {

        var loginRequest = LoginRequest(
            binding.tietUserName.text.toString(),
            binding.tietPassword.text.toString()
        )

        binding.pbLogin.isVisible = true

        CoroutineScope(Dispatchers.IO).launch {

            val service = ApiClient.getRetrofit().create(LoginService::class.java)

            try {
                val response: Response<LoginResponse> = service.login(loginRequest)

                if (response.isSuccessful) {
                    val login = response.body()
                    if (login != null) {
                        val fullName = "${login.usuario.nombre} ${login.usuario.apellido}"
                        saveValues(login.usuario.nombreUsuario,login.usuario.email, fullName,login.token)
                        runOnUiThread {
                            binding.pbLogin.isVisible = false
                            goToMenu()
                        }
                    }
                } else {

                    val errorResponse =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)

                    var message = ""

                    errorResponse.errors.map {
                        message = it.message
                    }

                    runOnUiThread {
                        invalidFormLogin(message)
                        binding.pbLogin.isVisible = false
                    }

                }
            } catch (exception: Exception) {
                Log.i("User", exception.toString())
                binding.pbLogin.isVisible = false
            }

        }
    }

    private fun goToMenu() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
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

    private fun invalidForm() {
        var message = ""
        if (binding.tilUserName.helperText != null)
            message += "\n\nNombre Usuario: " + binding.tilUserName.helperText
        if (binding.tilPassword.helperText != null)
            message += "\n\nContraseña: " + binding.tilPassword.helperText

        AlertDialog.Builder(this)
            .setTitle("Forma Invalida")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->
                //no hacer nada
            }
            .show()
    }

    private fun usernameFocusListener() {
        binding.tietUserName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilUserName.helperText = validUserName()
            }

        }
    }

    private fun validUserName(): String? {

        val userName = binding.tietUserName.text.toString()
        val userNameRegex = "^[a-zA-Z0-9]*\$"

        if (userName.length < 6) {
            return getString(R.string.error_length_username)
        } else if (!Regex(userNameRegex).matches(userName)) {
            return getString(R.string.error_character_username)
        }

        return null
    }

    private fun passwordFocusListener() {
        binding.tietPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilPassword.helperText = validPassword()
            }

        }
    }

    private fun validPassword(): String? {

        val password = binding.tietPassword.text.toString()

        if (password.length < 8) {
            return getString(R.string.error_length_password)
        } else if (!password.matches(".*[A-Z]*.".toRegex())) {
            return getString(R.string.error_uppercase_password)
        } else if (!password.matches(".*[a-z]*.".toRegex())) {
            return getString(R.string.error_lowercase_password)
        } else if (!password.matches(".*[@#\$%^&+=]*.".toRegex())) {
            return getString(R.string.error_especialchar_password)
        }

        return null
    }

    private suspend fun saveValues(nombreUsuario:String, email:String, nombreCompleto:String, token:String ){
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("nombreUsuario")] = nombreUsuario
            preferences[stringPreferencesKey("email")] = email
            preferences[stringPreferencesKey("nombreCompleto")] = nombreCompleto
            preferences[stringPreferencesKey("token")] = token
        }
    }

}