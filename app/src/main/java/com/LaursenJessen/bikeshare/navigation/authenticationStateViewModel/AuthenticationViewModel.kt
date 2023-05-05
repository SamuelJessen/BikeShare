package com.LaursenJessen.bikeshare.navigation.authenticationStateViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthenticationViewModel : ViewModel() {
    private val auth = Firebase.auth
    private var _isAuthenticated by mutableStateOf(auth.currentUser != null)
    val isAuthenticated: Boolean
        get() = _isAuthenticated

    fun setAuthenticated(isAuth: Boolean) {
        _isAuthenticated = isAuth
    }
}


