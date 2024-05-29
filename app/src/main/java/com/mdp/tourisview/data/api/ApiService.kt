package com.mdp.tourisview.data.api

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.mdp.tourisview.data.api.model.SignInResult
import com.mdp.tourisview.data.api.model.SignUpResult
import com.mdp.tourisview.data.api.model.UploadDestinationResult
import com.mdp.tourisview.data.mock.server.model.MockServerDestination
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") displayName: String
    ): SignUpResult

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): SignInResult
    

//    @FormUrlEncoded
    @GET("getAllDestinations")
    suspend fun getAllDestinations(
        @Query("name") name: String?=null,
    ): List<MockServerDestination>

    @FormUrlEncoded
    @POST("uploadDestination")
    suspend fun uploadDestination(
        @Field("name") name: String,
        @Field("image") image: String,
        @Field("description") description: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
        @Field("locationName") locationName: String,
        @Field("poster") poster: String,
    ): UploadDestinationResult
}
