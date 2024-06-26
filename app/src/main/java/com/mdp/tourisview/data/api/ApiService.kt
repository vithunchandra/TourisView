package com.mdp.tourisview.data.api

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.mdp.tourisview.data.api.model.InsertReviewResult
import com.mdp.tourisview.data.api.model.SignInResult
import com.mdp.tourisview.data.api.model.SignUpResult
import com.mdp.tourisview.data.api.model.ToggleBookmarkResult
import com.mdp.tourisview.data.api.model.UploadDestinationResult
import com.mdp.tourisview.data.mock.server.model.MockServerDestination
import com.mdp.tourisview.data.mock.server.model.MockServerReview
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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
    

    @GET("getAllDestinations")
    suspend fun getAllDestinations(
        @Query("name") name: String?=null,
        @Query("email") email: String
    ): List<MockServerDestination>

    @GET("getAllHistory")
    suspend fun getAllHistory(
        @Query("email") email: String
    ): List<MockServerDestination>

    @Multipart
    @POST("uploadDestination")
    suspend fun uploadDestination(
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("locationName") locationName: RequestBody,
        @Part("poster") poster: RequestBody,
    ): UploadDestinationResult

    @GET("getAllReview")
    suspend fun getAllReview(
        @Query("destinationId") destinationId: Int
    ): List<MockServerReview>

    @FormUrlEncoded
    @POST("insertReview")
    suspend fun insertReview(
        @Field("reviewer") reviewer: String,
        @Field("destination_id") destination_id: Int,
        @Field("review") review: String,
        @Field("star") star: Int
    ): InsertReviewResult

    @FormUrlEncoded
    @POST("toggleBookmark")
    suspend fun toggleBookmark(
        @Field("reviewer") reviewer: String,
        @Field("destination_id") destinationId: Int
    ): ToggleBookmarkResult
}
