package com.example.tomorrow.ui.auth

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.tomorrow.data.UserEntity
import com.example.tomorrow.data.UserRepository
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import kotlin.coroutines.coroutineContext

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

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
        user.password
        viewModelScope.launch { repository.addUser(user) }
    }

    val nameHasErrors by derivedStateOf {
        if(name.isNotBlank() && name.length >= 3) true else false
    }

    val emailHasErrors by derivedStateOf {
        if (email.isNotBlank()) {
            // Email is considered erroneous until it completely matches EMAIL_ADDRESS.
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    val passwordHasErrors by derivedStateOf {
        if(password.isNotBlank() && password.length >= 6) true else false
    }

    val confirmPasswordHasErrors by derivedStateOf {
        if(password.equals(confirmpassword)) true else false
    }

    fun updateName(input: String) {
        name = input
    }

    fun updateEmail(input: String) {
        email = input
    }
    fun updatePassword(input: String) {
        password = input
    }
    fun updateConfirmPassword(input: String) {
        confirmpassword = input
    }


}

