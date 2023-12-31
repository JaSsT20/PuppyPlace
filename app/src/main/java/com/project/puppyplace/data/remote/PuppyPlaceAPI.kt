package com.project.puppyplace.data.remote

import com.project.puppyplace.data.remote.dto.AppointmentDto
import com.project.puppyplace.data.remote.dto.DogDto
import com.project.puppyplace.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PuppyPlaceAPI {
    @GET("api/Dogs")
    suspend fun getDogs(): List<DogDto>
    @GET("api/Dogs/{id}")
    suspend fun getDogById(@Path("id")id: Int): DogDto
    @GET("api/Dogs/breed/{breed}")
    suspend fun getDogsByBreed(@Path("breed")breed: String): List<DogDto>
    @GET("api/Dogs/size/{size}")
    suspend fun getDogsBySize(@Path("size")size: String): List<DogDto>
    @GET("api/Dogs/sex/{sex}")
    suspend fun getDogsBySex(@Path("sex")sex: String): List<DogDto>
    @PUT("api/Dogs/{id}")
    suspend fun updateDog(@Path("id")id: Int, @Body dog: DogDto): DogDto

    //Appointments
    @GET("api/Appointment")
    suspend fun getAppointments(): List<AppointmentDto>
    @POST("api/Appointment")
    suspend fun createAppointment(@Body appointment: AppointmentDto): AppointmentDto
    @DELETE("api/Appointment/{id}")
    suspend fun deleteAppointment(@Path("id")id: Int): AppointmentDto
    @GET("api/Appointment/by/{id}")
    suspend fun getAppointmentById(@Path("id")id: Int): AppointmentDto
    @GET("api/Appointment/with/{email}")
    suspend fun getUserAppointments(@Path("email")email: String): List<AppointmentDto>
    @PUT("api/Appointment/{id}")
    suspend fun updateAppointment(@Path("id")id: Int, @Body appointment: AppointmentDto): AppointmentDto

    //User
    @GET("api/Users")
    suspend fun getUsers(): List<UserDto>
    @GET("api/Users/{id}")
    suspend fun getUsersById(@Path("id")id: Int): UserDto
    @GET("api/Users/Exist/{id}")
    suspend fun existUserById(@Path("id")id: Int): Boolean
    @GET("api/Users/auth/{email}")
    suspend fun getUserByEmail(@Path("email")email: String): UserDto
    @POST("api/Users")
    suspend fun createUser(@Body user: UserDto): UserDto
    @PUT("api/Users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserDto, @Query("dogId") dogId: Int): UserDto
    @PUT("api/Users/Simple/{id}")
    suspend fun updateSimpleUser(@Path("id") id: Int, @Body user: UserDto): UserDto
    @GET("api/Users/FavoriteDogs/{userId}")
    suspend fun getFavoritesByUserId(@Path("userId") id: Int): List<DogDto>
}