@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.project.puppyplace.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.puppyplace.R

@Composable
fun LogiScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(R.drawable.login_background),
            contentDescription = "Login background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment =  Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            WelcomeMessageText()
            LoginCard()
        }
        
    }
}
@Composable
fun WelcomeMessageText(){
    Text(
        text = "Welcome!",
        style = MaterialTheme.typography.displayLarge
    )
}
@ExperimentalMaterial3Api
@Composable
fun LoginCard(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Card {
            LoginTitle()
            EmailTextField()
            PasswordTextField()
            LoginButton()
            SignUpButton()
        }
    }
}

@Composable
fun LoginTitle(){
    Text(
        text = "LogIn",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(16.dp)
    )
}
@ExperimentalMaterial3Api
@Composable
fun EmailTextField(){
    OutlinedTextField(
        value = "",
        onValueChange = { TODO() },
        label = { Text(text = "Email") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.AlternateEmail,
                contentDescription = "At icon"
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(){
    OutlinedTextField(
        value = "",
        onValueChange = { TODO() },
        label = { Text(text = "Password") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Password icon"
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        maxLines = 1
    )
}

@Composable
fun LoginButton(){
    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        Button(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = "LogIn",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun SignUpButton(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Row(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { /*TODO*/ }
            )
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun PreviewLogin(){
    LogiScreen()
}