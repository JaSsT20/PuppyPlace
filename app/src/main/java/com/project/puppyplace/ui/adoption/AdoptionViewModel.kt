package com.project.puppyplace.ui.adoption

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.project.puppyplace.data.remote.dto.AppointmentDto
import com.project.puppyplace.data.remote.dto.DogDto
import com.project.puppyplace.data.remote.dto.UserDto
import com.project.puppyplace.data.repository.AdoptionRepository
import com.project.puppyplace.data.repository.HomeRepository
import com.project.puppyplace.data.repository.LoginRepository
import com.project.puppyplace.data.repository.SignUpRepository
import com.project.puppyplace.di.AppModule.sharedAppointment
import com.project.puppyplace.di.AppModule.sharedDog
import com.project.puppyplace.di.AppModule.userLoged
import com.project.puppyplace.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class AdoptionViewModel @Inject constructor(
    private val adoptionRepository: AdoptionRepository,
    private val signUpRepository: SignUpRepository,
    private val loginRepository: LoginRepository,
    private val homeRepository: HomeRepository
): ViewModel(){
    var editable by mutableStateOf(false)

    var dog by mutableStateOf(DogDto())
    var date by mutableStateOf("")
    var userName by mutableStateOf("")
    var userSurname by mutableStateOf("")
    var identificationNumber by mutableStateOf("")
    var telephone by mutableStateOf("")
    var cellphone by mutableStateOf("")
    var email by mutableStateOf("")
    var address by mutableStateOf("")

    //Error messages
    var dateError by mutableStateOf("")
    var userNameError by mutableStateOf("")
    var userSurnameError by mutableStateOf("")
    var identificationNumberError by mutableStateOf("")
    var telephoneError by mutableStateOf("")
    var cellphoneError by mutableStateOf("")
    var emailError by mutableStateOf("")
    var addressError by mutableStateOf("")

    var showDialog by mutableStateOf(false)

    fun onBackPressed(navController: NavController){
        navController.popBackStack()
        sharedAppointment = null
    }
    fun onAdoptClick(navController: NavController){
        viewModelScope.launch {
            val deferred = async{
                if(isValid()){
                    if(sharedAppointment != null){
                        adoptionRepository.updateAppointment(
                            AppointmentDto(
                                id = sharedAppointment!!.id,
                                dogId = sharedDog!!.id,
                                date = date,
                                userName = sharedAppointment!!.userName,
                                userSurname = sharedAppointment!!.userSurname,
                                identificationNumber = sharedAppointment!!.identificationNumber,
                                telephone = sharedAppointment!!.telephone,
                                cellphone = sharedAppointment!!.cellphone,
                                email = sharedAppointment!!.email,
                                address = sharedAppointment!!.address
                            )
                        )
                    }
                    else{
                        adoptionRepository.createAppointment(
                            AppointmentDto(
                                dogId = dog.id,
                                date = date,
                                userName = userName,
                                userSurname = userSurname,
                                identificationNumber = identificationNumber,
                                telephone = telephone,
                                cellphone = cellphone,
                                email = email,
                                address = address
                            )
                        )
                        if(!editable){
                            signUpRepository.updateSimpleUser(userLoged!!.id,
                                UserDto(
                                    id = userLoged!!.id,
                                    name = userLoged!!.name,
                                    surname = userLoged!!.surname,
                                    identificationNumber = identificationNumber,
                                    telephone = telephone,
                                    cellphone = cellphone,
                                    email = userLoged!!.email,
                                    password = userLoged!!.password,
                                    address = address,
                                    favoriteDogs = userLoged!!.favoriteDogs
                                )
                            )
                        }
                    }
                }
            }
            deferred.await()
            sharedAppointment = null
            showDialog = false
            userLoged = loginRepository.getUserByEmail(userLoged!!.email)
            homeRepository.updateDog(
                DogDto(
                    id = sharedDog!!.id,
                    name = sharedDog!!.name,
                    breed = sharedDog!!.breed,
                    size = sharedDog!!.size,
                    weight = sharedDog!!.weight,
                    status = false,
                    gender = sharedDog!!.gender,
                    birthDate = sharedDog!!.birthDate,
                    hairColor = sharedDog!!.hairColor,
                    isSterilized = sharedDog!!.isSterilized,
                    behaviour = sharedDog!!.behaviour,
                    activityLevel = sharedDog!!.activityLevel,
                    origin = sharedDog!!.origin,
                    age = sharedDog!!.age,
                    description = sharedDog!!.description,
                    image = sharedDog!!.image,
                    isLiked = sharedDog!!.isLiked,
                )
            )
            navController.navigate(Destination.appointment.route)
        }
    }
    fun onShowDialog(){
        if(isValid())
            showDialog = true
    }
    fun onDismissDialog(){
        showDialog = false
    }
    private fun isValid(): Boolean{
        onDateChange(date)
        onUserNameChange(userName)
        onUserSurnameChange(userSurname)
        onIdentificationNumberChange(identificationNumber)
        onTelephoneChange(telephone)
        onCellphoneChange(cellphone)
        onEmailChange(email)
        onAddressChange(address)
        return dateError.isEmpty() &&
                userNameError.isEmpty() &&
                userSurnameError.isEmpty() &&
                identificationNumberError.isEmpty() &&
                telephoneError.isEmpty() &&
                cellphoneError.isEmpty() &&
                emailError.isEmpty() &&
                addressError.isEmpty()
    }

    fun onUserNameChange(userName: String){
        this.userName = userName
        userNameError = if(userName.isEmpty()){
            "Name required."
        }else{
            ""
        }
    }
    fun onUserSurnameChange(userSurname: String){
        this.userSurname = userSurname
        userSurnameError = if(userSurname.isEmpty()){
            "Surname required."
        }else{
            ""
        }
    }
    fun onIdentificationNumberChange(identificationNumber: String){
        this.identificationNumber = identificationNumber
        identificationNumberError = if(identificationNumber.isEmpty()){
            "Identification Number required."
        } else if(identificationNumber.length < 11){
            "Identification Number must be ${identificationNumber.length}/11 digits."
        } else if(identificationNumber.length > 11){
            "Identification Number must be ${identificationNumber.length}/11 digits."
        }
        else{
            ""
        }
    }
    fun onTelephoneChange(telephone: String){
        this.telephone = telephone
        telephoneError = if(telephone.isEmpty()){
            "Telephone required."
        } else if(telephone.length < 10) {
            "Telephone must be ${telephone.length}/10 digits."
        } else if(telephone.length > 10){
            "Telephone must be ${telephone.length}/10 digits."
        }
        else{
            ""
        }
    }
    fun onCellphoneChange(cellphone: String){
        this.cellphone = cellphone
        cellphoneError = if(cellphone.isEmpty()){
            "Cellphone required."
        } else if (cellphone.length < 10){
            "Cellphone must be ${cellphone.length}/10 digits."
        } else if(cellphone.length > 10){
            "Cellphone must be ${cellphone.length}/10 digits."
        } else{
            ""
        }
    }
    fun onEmailChange(email: String){
        this.email = email
        emailError =
            if(email.isEmpty()){
            "Email required."
            } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                "Invalid email."
            } else{
                ""
            }
    }
    fun onAddressChange(address: String){
        this.address = address
        addressError = if(address.isEmpty()){
            "Address required."
        }else{
            ""
        }
    }
    fun onDateChange(date: String){
        this.date = date
        dateError = if(date.isEmpty()){
            "Date required."
        }
         else if(!date.matches(Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}$"))){
            "Invalid format should be: yyyy-MM-dd"
        }
        else{
            ""
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun onModify(){
        if (sharedAppointment != null){
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(sharedAppointment!!.date)
            val formattedDate = sdf.format(date!!)

            dog = sharedDog!!
            this.date = formattedDate
            userName = sharedAppointment!!.userName
            userSurname = sharedAppointment!!.userSurname
            identificationNumber = sharedAppointment!!.identificationNumber
            telephone = sharedAppointment!!.telephone
            cellphone = sharedAppointment!!.cellphone
            email = sharedAppointment!!.email
            address = sharedAppointment!!.address
        }
        else{
            dog = sharedDog!!
            userName = userLoged!!.name
            userSurname = userLoged!!.surname
            identificationNumber = userLoged!!.identificationNumber
            telephone = userLoged!!.telephone
            cellphone = userLoged!!.cellphone
            email = userLoged!!.email
            address = userLoged!!.address
        }
    }
    init{
        editable = userLoged!!.address.isNotEmpty()
        onModify()
    }
}