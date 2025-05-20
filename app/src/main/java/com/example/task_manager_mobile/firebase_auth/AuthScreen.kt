package com.example.task_manager_mobile.firebase_auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AuthScreen(viewModel: EmailPasswordViewModel = viewModel(), onAuthSuccess: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Entrar", "Registrar")
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is EmailPasswordViewModel.AuthResultState.Success) {
            if (Firebase.auth.currentUser != null) {
                onAuthSuccess()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.resetAuthState()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)) {

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (selectedTab) {
            0 -> SignInForm(viewModel)
            1 -> RegisterForm(viewModel)
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (authState) {
            is EmailPasswordViewModel.AuthResultState.Loading -> {
                CircularProgressIndicator()
            }
            is EmailPasswordViewModel.AuthResultState.Failure -> {
                Text(
                    text = (authState as EmailPasswordViewModel.AuthResultState.Failure).error,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is EmailPasswordViewModel.AuthResultState.Success -> {
                Text(
                    text = "Sucesso na autenticação!",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            else -> {}
        }
    }
}

@Composable
fun SignInForm(viewModel: EmailPasswordViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.signIn(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Entrar")
        }
    }
}

@Composable
fun RegisterForm(viewModel: EmailPasswordViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.register(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar")
        }
    }
}
