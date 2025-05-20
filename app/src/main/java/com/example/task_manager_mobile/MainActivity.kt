package com.example.task_manager_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.task_manager_mobile.firebase_auth.AuthScreen
import com.example.task_manager_mobile.ui.theme.TaskmanagermobileTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.example.task_manager_mobile.screens.HomeScreen
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaskmanagermobileTheme {
                var isLoggedIn by remember { mutableStateOf(Firebase.auth.currentUser != null) }

                LaunchedEffect(Unit) {
                    Firebase.auth.addAuthStateListener { auth ->
                        val current = auth.currentUser
                        isLoggedIn = current != null
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        if (isLoggedIn) {
                            HomeScreen(onLogout = {
                                Firebase.auth.signOut()
                            })
                        } else {
                            AuthScreen(onAuthSuccess = {
                                isLoggedIn = true
                            })
                        }
                    }
                }
            }
        }
    }

}