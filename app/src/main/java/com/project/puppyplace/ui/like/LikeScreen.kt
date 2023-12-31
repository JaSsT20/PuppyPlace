package com.project.puppyplace.ui.like

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.project.puppyplace.R
import com.project.puppyplace.data.remote.dto.DogDto
import com.project.puppyplace.util.appBottomBar.AppBottomBar
import com.project.puppyplace.util.appTopBar.AppTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeScreen(
    navController: NavController,
    viewModel: LikeViewModel = hiltViewModel()
) {
    val dogs = viewModel.state.collectAsStateWithLifecycle().value.favoriteDogs
    Scaffold(
        topBar = {
            AppTopBar(
                navIcon = Icons.Filled.ArrowBack,
                title = "Favorite dogs",
                navController = navController,
            )
        },
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) {paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.like_background),
                contentDescription = "User background image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
            LikeList(
                dogs = dogs, navController = navController,
                viewModel = viewModel,
                paddingValues
            )
        }
    }
}


@Composable
fun LikeList(
    dogs: List<DogDto>,
    navController: NavController,
    viewModel: LikeViewModel,
    paddingValues: PaddingValues
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(dogs.size){ index ->
            DogItem(
                dog = dogs[index],
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DogItem(
    dog: DogDto,
    navController: NavController,
    viewModel: LikeViewModel
){
    val isAvailable by remember { mutableStateOf(viewModel.checkIfDogIsAvailable(dog)) }
    var isLiked by remember { mutableStateOf(viewModel.dogIsLiked(dog)) }

    Row(
        modifier = Modifier
            .width(300.dp)
            .height(400.dp)
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            if(isAvailable){
                Modifier.clickable {
                    viewModel.onDogSelected(dog = dog, navController = navController)
                }
            } else{
                Modifier
            }
        ){
            AsyncImage(
                model = dog.image,
                contentDescription = dog.name,
                modifier = Modifier.fillMaxHeight(),
                contentScale = ContentScale.FillBounds,
                colorFilter =
                if(isAvailable) null
                else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
            )
            if(!isAvailable){
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Not available :(",
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Column {
                    Text(
                        text = dog.name,
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Row {
                    Column {
                        Text(
                            text = "${dog.age} years",
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    Column(
                        modifier = Modifier.weight(2f)
                    ) {
                        Icon(
                            imageVector =
                            if(viewModel.isMale(dog)) Icons.Filled.Male
                            else Icons.Filled.Female,
                            contentDescription = "Male gender",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(35.dp)
                        )

                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(onClick = {
                            viewModel.onLikedClicked(dog = dog)
                            isLiked = viewModel.isLiked
                        }) {
                            Icon(
                                imageVector =
                                if(isLiked) Icons.Filled.Favorite
                                else Icons.Filled.HeartBroken,
                                contentDescription = "Like icon",
                                tint =
                                if(isLiked) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSecondary,
                            )
                        }
                    }
                }
            }
        }
    }
}