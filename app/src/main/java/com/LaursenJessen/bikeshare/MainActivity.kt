package com.LaursenJessen.bikeshare


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.LaursenJessen.bikeshare.components.authentication.Login
import com.LaursenJessen.bikeshare.components.authentication.Signup
import com.LaursenJessen.bikeshare.components.drawermenu.DrawerMenuHeader
import com.LaursenJessen.bikeshare.components.drawermenu.menuitems.getMenuItems
import com.LaursenJessen.bikeshare.components.drawermenu.models.DrawerMenuItem
import com.LaursenJessen.bikeshare.components.home.HomeScreen
import com.LaursenJessen.bikeshare.components.rentbike.RentBikeView
import com.LaursenJessen.bikeshare.components.rentoutbike.RentOutBikeView
import com.LaursenJessen.bikeshare.components.rentoutbike.addbikes.AddBikeView
import com.LaursenJessen.bikeshare.components.rentoutbike.addbikestrava.AddBikeFromStravaView
import com.LaursenJessen.bikeshare.components.rentoutbike.mybikes.MyBikesMain
import com.LaursenJessen.bikeshare.firestore.FireStore
import com.LaursenJessen.bikeshare.ui.theme.BikeShareTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var isLoggedIn by mutableStateOf(false)

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth
        auth.signOut()
        FirebaseApp.initializeApp(this)
        val service = FireStore(auth) { isLoggedIn = it }
        val currentUser = auth.currentUser
        isLoggedIn = currentUser != null
        setContent {
            BikeShareTheme() {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                val menuItems = getMenuItems(navController)

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "BikeShare") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { scaffoldState.drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = null)
                                }
                            }
                        )
                    },
                    drawerContent = {
                        DrawerMenuHeader()
                        DrawerMenuItem(menuItems)
                    },
                    content = {
                        NavHost(
                            navController = navController,
                            startDestination = if (isLoggedIn) "HomeScreen" else "Login"
                        ) {
                            composable("HomeScreen") { HomeScreen(nav = navController) }
                            composable("Login") { Login(service, nav = navController) }
                            composable("Signup") { Signup(service, nav = navController) }
                            composable("RentBikeView") { RentBikeView(nav = navController) }
                            composable("RentOutBikeView") { RentOutBikeView(nav = navController) }
                            composable("MyBikesView") { MyBikesMain(nav = navController) }
                            composable("AddBike") { AddBikeView(nav = navController) }
                            composable("AddBikeStrava") { AddBikeFromStravaView(nav = navController) }
                        }
                    }
                )
            }
        }
    }
}


