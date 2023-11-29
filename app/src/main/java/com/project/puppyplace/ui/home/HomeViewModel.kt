package com.project.puppyplace.ui.home

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.project.puppyplace.R
import com.project.puppyplace.data.remote.dto.DogDto
import com.project.puppyplace.data.repository.HomeRepository
import com.project.puppyplace.di.AppModule.sharedDog
import com.project.puppyplace.di.AppModule.userLoged
import com.project.puppyplace.navigation.Destination
import com.project.puppyplace.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel(){
    private var _state = MutableStateFlow(HomeListState())
    val state: StateFlow<HomeListState> = _state.asStateFlow()

    private var _dogInFavorite by mutableStateOf(false)
    var dogInFavorite = _dogInFavorite

    var searchItem by mutableStateOf("")

    var isLoadig by mutableStateOf(false)

    fun isMale(dog:DogDto): Boolean{
        return dog.gender == "Male"
    }

    fun onLikedClicked(dog: DogDto) {
        viewModelScope.launch {
            homeRepository.updateUser(userLoged!!, dog.id)
            homeRepository.updateDog(dog.copy(isLiked = !dog.isLiked))
        }
    }
    fun isInFavorite(dogId: Int): Boolean{
        return userLoged!!.favoriteDogs.any{
            it.dogId == dogId
        }
    }



    //    fun onLikedClicked(dog: DogDto, isLiked: Boolean){
//        viewModelScope.launch {
//            homeRepository.updateDog(
//                DogDto(
//                    id = dog.id,
//                    name = dog.name,
//                    breed = dog.breed,
//                    size = dog.size,
//                    weight = dog.weight,
//                    gender = dog.gender,
//                    birthDate = dog.birthDate,
//                    hairColor = dog.hairColor,
//                    isSterilized = dog.isSterilized,
//                    behaviour = dog.behaviour,
//                    activityLevel = dog.activityLevel,
//                    origin = dog.origin,
//                    image = dog.image,
//                    age = dog.age,
//                    isLiked = isLiked,
//                    status = dog.status,
//                    description = dog.description
//                )
//            )
//        }
//    }
    fun onPetsPressed(context: Context){
        val effectsList = listOf(
            R.raw.dog_bark,
            R.raw.dog_bark2,
            R.raw.dog_bark3
        )
        val randomIndex = Random.nextInt(effectsList.size)
        val effectResource = effectsList[randomIndex]
        val mp: MediaPlayer = MediaPlayer.create(
            context,
            effectResource
        )
        mp.start()
    }
    fun onDogSelected(navController: NavController, dog: DogDto){
        getDogs()
        sharedDog = dog
        navController.navigate(Destination.dogDetail.route)
    }
    fun onLikeIconPressed(navController: NavController){
        navController.navigate(Destination.like.route)
    }
    fun goToUser(navController: NavController){
        navController.navigate(Destination.user.route)
    }
    fun onSearchItemChanged(searchItem: String){
        this.searchItem = searchItem
    }
    fun getDogsBySize(size: String){
        viewModelScope.launch {
            homeRepository.getDogsBySize(size).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = HomeListState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _state.value = HomeListState(dogsList = result.data ?: emptyList())
                    }

                    is Resource.Error -> {
                        _state.value = HomeListState(error = result.message ?: "Unknown error")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    fun getDogsByBreed(breed: String){
        viewModelScope.launch {
            if(breed.isBlank()) {
                getDogs()
                return@launch
            }
            homeRepository.getDogsByBreed(breed).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = HomeListState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _state.value = HomeListState(dogsList = result.data ?: emptyList())
                    }

                    is Resource.Error -> {
                        _state.value = HomeListState(error = result.message ?: "Unknown error")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    fun getDogsBySex(sex: String){
        viewModelScope.launch {
            homeRepository.getDogsBySex(sex).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = HomeListState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _state.value = HomeListState(dogsList = result.data ?: emptyList())
                    }

                    is Resource.Error -> {
                        _state.value = HomeListState(error = result.message ?: "Unknown error")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    fun getDogs(){
        viewModelScope.launch {
            homeRepository.getDogs().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = HomeListState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _state.value = HomeListState(dogsList = result.data ?: emptyList())
                    }

                    is Resource.Error -> {
                        _state.value = HomeListState(error = result.message ?: "Unknown error")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    init {
        getDogs()
    }
}