package com.project.puppyplace.ui.appointment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.project.puppyplace.data.remote.dto.AppointmentDto
import com.project.puppyplace.data.remote.dto.DogDto
import com.project.puppyplace.data.remote.dto.UserDto
import com.project.puppyplace.data.repository.AdoptionRepository
import com.project.puppyplace.data.repository.HomeRepository
import com.project.puppyplace.data.repository.UserRepository
import com.project.puppyplace.di.AppModule.sharedAppointment
import com.project.puppyplace.di.AppModule.sharedDog
import com.project.puppyplace.di.AppModule.userLoged
import com.project.puppyplace.navigation.Destination
import com.project.puppyplace.ui.adoption.AdoptionListState
import com.project.puppyplace.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val adoptionRepository: AdoptionRepository,
    private val homeRepository: HomeRepository
): ViewModel() {
    private var _state = MutableStateFlow(AppointmentListState())
    val state: StateFlow<AppointmentListState> = _state.asStateFlow()

    private var _stateAdoption = MutableStateFlow(AdoptionListState())
    val stateAdoption: StateFlow<AdoptionListState> = _stateAdoption.asStateFlow()

    var user by mutableStateOf(UserDto())
    var adoption by mutableStateOf(AppointmentDto())

    var dog by mutableStateOf(DogDto())

    fun onModifyPressed(navController: NavController, appointmentDto: AppointmentDto){
        viewModelScope.launch {
            val deferred = async {
                dog = homeRepository.getDogById(appointmentDto.dogId)
            }
            deferred.await()
            sharedDog = dog
            sharedAppointment = appointmentDto
            navController.navigate(Destination.adoption.route)
        }
    }

    fun deleteAppointment(id: Int) {
        viewModelScope.launch {1
            val appointment = userRepository.getAppointmentById(id)
            dog = homeRepository.getDogById(appointment.dogId)
            homeRepository.updateDog(
                DogDto(
                    id = dog.id,
                    name = dog.name,
                    breed = dog.breed,
                    size = dog.size,
                    weight = dog.weight,
                    status = true,
                    gender = dog.gender,
                    birthDate = dog.birthDate,
                    hairColor = dog.hairColor,
                    isSterilized = dog.isSterilized,
                    behaviour = dog.behaviour,
                    activityLevel = dog.activityLevel,
                    origin = dog.origin,
                    age = dog.age,
                    description = dog.description,
                    image = dog.image,
                    isLiked = dog.isLiked,
                )
            )
            val deferred = async {
                userRepository.deleteAppointment(id)
            }
            deferred.await()
            getUserAppointments()
        }
    }
    init {
        user = userLoged!!
        getUserAppointments()
    }
    fun BackHome(navController: NavController){
        navController.navigate(Destination.home.route)
    }

    fun logOut(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        userLoged = null
        navController.navigate(Destination.login.route) {
            popUpTo(Destination.home.route) {
                inclusive = true
            }
        }
    }
    private fun getUserAppointments(){
        viewModelScope.launch {
            adoptionRepository.getUserAppointments(userLoged!!.email).onEach { result ->
                when(result){
                    is Resource.Loading ->{
                        _stateAdoption.value = AdoptionListState(isLoading = true)
                    }
                    is Resource.Success ->{
                        _stateAdoption.value = AdoptionListState(adoptionList = result.data ?: emptyList())
                    }
                    is Resource.Error ->{
                        _stateAdoption.value = AdoptionListState(error = result.message ?: "Unknown error")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}