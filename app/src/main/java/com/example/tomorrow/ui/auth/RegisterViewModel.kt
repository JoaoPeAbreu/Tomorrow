package com.example.tomorrow.ui.auth

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tomorrow.AppContextHolder
import com.example.tomorrow.data.UserEntity
import com.example.tomorrow.data.UserRepository
import com.example.tomorrow.data.UserRepositoryProvider
import kotlinx.coroutines.launch

class RegisterViewModel() : ViewModel() {

    private val userRepository = UserRepositoryProvider.getRepository(AppContextHolder.appContext)
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmpassword by mutableStateOf("")
/*
    val keygen = KeyGenerator.getInstance("AES")
    keygen.init(128) //Key size must be either 128, 192, or 256 bits
    val key: SecretKey = keygen.generateKey()

    val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key)

    val iv: ByteArray = cipher.iv
    val ivSpec = IvParameterSpec(iv)

    val encryted_bytes = cipher.doFinal(password.toByteArray())
    val encode_bytes: ByteArray = Base64.encode(encryted_bytes, Base64.DEFAULT)

    val encryptedPassword = String(encode_bytes, Charset.forName("UTF-8"))
*/
    fun insert(user: UserEntity) {
        viewModelScope.launch { userRepository.addUser(user) }
    }

    fun showUser(name: String) {
        viewModelScope.launch { userRepository.getUserByName(name) }
    }

    val nameHasErrors by derivedStateOf {
        val bool = !name.isNotBlank() && !(name.length >= 3)
        bool
    }

    val emailHasErrors by derivedStateOf {
        if (email.isNotBlank()) {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    val passwordHasErrors by derivedStateOf {
        val bool = !password.isNotBlank() && !(password.length >= 6)
        bool
    }

    val confirmPasswordHasErrors by derivedStateOf {
        val bool = !confirmpassword.isNotBlank() && !(password.equals(confirmpassword))
        bool
    }

}

