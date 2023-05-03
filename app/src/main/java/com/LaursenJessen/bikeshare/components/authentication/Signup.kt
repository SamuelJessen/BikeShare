package com.LaursenJessen.bikeshare.components.authentication

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.authenticationStateViewModel.AuthenticationViewModel
import kotlinx.coroutines.launch

@Composable
fun Signup(service: FireStore, nav: NavController, authViewModel: AuthenticationViewModel) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign up",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email.value,
            onValueChange = { newText -> email.value = newText },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { newText -> password.value = newText },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                scope.launch {
                    try {
                        val user = service.signup(email.value, password.value)
                        authViewModel.setAuthenticated(true)
                        nav.navigate("HomeScreen") {
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        Log.e("Signup", "Exception during signup", e)
                        errorMessage = "Sign up failed: ${e.localizedMessage}"
                    }
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = { nav.navigate("Login") }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? Log in")
        }
    }
}

