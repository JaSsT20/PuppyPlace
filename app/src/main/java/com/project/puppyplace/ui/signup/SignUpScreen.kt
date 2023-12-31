@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.project.puppyplace.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.project.puppyplace.R
import com.project.puppyplace.navigation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController
){
    Scaffold(
        snackbarHost = { viewModel.snackbarHostState },
    ) {paddingValues ->
        if(viewModel.isLoading){
            LoadingScreen(viewModel = viewModel)
        }
        else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(R.drawable.signup_background),
                    contentDescription = "Login background image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment =  Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    SignUpCard(viewModel, navController)
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun SignUpCard(
    viewModel: SignUpViewModel,
    navController: NavController
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = CardDefaults.outlinedCardBorder(enabled = true)

        ){
            SignUpTitle()
            Row {
                Column(
                    modifier = Modifier.weight(1f)
                ){
                    NameTextField(viewModel = viewModel)
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    SurnameTextField(viewModel = viewModel)
                }
            }
            EmailTextField(viewModel = viewModel)
            PasswordTextField(viewModel = viewModel)
            SignUpButton(viewModel = viewModel, navController = navController)
            AlreadyAnAccountTextField(navController)
        }
    }
}
@Composable
fun SignUpTitle(){
    Text(
        text = "Sign Up",
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameTextField(viewModel: SignUpViewModel) {
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next),
        value = viewModel.name,
        onValueChange = { viewModel.onNameChange(it) },
        label = { Text(text = "First Name") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "At icon"
            )
        },
        maxLines = 1,
        singleLine = true,
        isError = viewModel.nameError.isNotEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    Text(
        text = viewModel.nameError,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(start = 8.dp)
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurnameTextField(viewModel: SignUpViewModel){
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next),
        value = viewModel.surname,
        onValueChange = { viewModel.onSurnameChange(it) },
        label = { Text(text = "Surname") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "At icon"
            )
        },
        maxLines = 1,
        singleLine = true,
        isError = viewModel.surnameError.isNotEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    Text(text = viewModel.surnameError,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(start = 8.dp)
    )
}
@ExperimentalMaterial3Api
@Composable
fun EmailTextField(viewModel: SignUpViewModel){
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next),
        value = viewModel.email,
        onValueChange = { viewModel.onEmailChange(it) },
        label = { Text(text = "Email") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.AlternateEmail,
                contentDescription = "At icon"
            )
        },
        maxLines = 1,
        singleLine = true,
        isError = viewModel.emailError.isNotEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    Text(
        text = viewModel.emailError,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(start = 8.dp)
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(viewModel: SignUpViewModel){
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next),
        value = viewModel.password,
        onValueChange = { viewModel.onPasswordChange(it) },
        label = { Text(text = "Password") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Password icon"
            )
        },
        trailingIcon = {
            IconButton(onClick = { viewModel.onHidePasswordPressed() }) {
                Icon(
                    imageVector =  if(viewModel.hidePassword) Icons.Filled.Remove
                    else Icons.Filled.RemoveRedEye,
                    contentDescription = "Password icon"
                )
            }
        },
        isError = viewModel.passwordError.isNotEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        maxLines = 1,
        singleLine = true,
        visualTransformation =
        if(viewModel.hidePassword) PasswordVisualTransformation()
        else VisualTransformation.None
    )
    Text(
        text = viewModel.passwordError,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(start = 8.dp)
    )
}
@Composable
fun SignUpButton(viewModel: SignUpViewModel, navController: NavController){
    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        Button(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
            onClick = { viewModel.onSignUpClick(navController = navController) }
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun LoadingScreen(viewModel: SignUpViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    
    ) {
        CircularProgressIndicator()
        Text(text = viewModel.loadingMessage)
    }
}

@Composable
fun AlreadyAnAccountTextField(
    navController: NavController
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Row(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(
                text = "Do you have an account? ",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "LogIn",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { navController.navigate(Destination.login.route)}
            )
        }
    }
}