package com.laursenjessen.bikeshare.components.authentication

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.laursenjessen.bikeshare.navigation.authenticationStateViewModel.AuthenticationViewModel
import com.laursenjessen.bikeshare.services.firestore.FireStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(service: FireStore, nav: NavController, authViewModel: AuthenticationViewModel) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val (isPasswordVisible, setPasswordVisible) = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 80.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Log in",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(value = email.value,
            onValueChange = { newText -> email.value = newText },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() })
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = password.value,
            onValueChange = { newText -> password.value = newText },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onDone = {
                scope.launch {
                    try {
                        service.login(email.value, password.value)
                        authViewModel.setAuthenticated(true)
                        nav.navigate("HomeScreen")
                    } catch (e: Exception) {
                        Log.e("Login", "Exception during login", e)
                        errorMessage = "Log in failed: ${e.localizedMessage}"
                    }
                }
                keyboardController?.hide()
            }),
            trailingIcon = {
                IconButton(onClick = { setPasswordVisible(!isPasswordVisible) }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        service.login(email.value, password.value)
                        authViewModel.setAuthenticated(true)
                        nav.navigate("HomeScreen")
                    } catch (e: Exception) {
                        Log.e("Login", "Exception during login", e)
                        errorMessage = "Log in failed: ${e.localizedMessage}"
                    }
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { nav.navigate("Signup") }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Don't have an account? Sign up!")
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage.toString(),
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(90.dp))
        Text(text = "BikeShare", style = MaterialTheme.typography.h5)
        Text(text = "The app for bike-lovers", style = MaterialTheme.typography.body1)
        Icon(
            imageVector = Icons.Filled.DirectionsBike,
            contentDescription = "LoginBikeIcon",
            modifier = Modifier.size(100.dp)
        )
    }
}

