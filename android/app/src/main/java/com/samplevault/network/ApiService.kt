package com.samplevault.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import com.samplevault.models.*

interface ApiService {

    @GET("samples/")
    suspend fun getSamples(): Response<List<Sample>>

    @POST("samples/")
    suspend fun createSample(@Body sample: SampleCreate): Response<Sample>

    @Multipart
    @POST("samples/{id}/upload")
    suspend fun uploadFile(
        @Path("id") sampleId: Int,
        @Part file: MultipartBody.Part,
        @Part("doc_type") docType: RequestBody
    ): Response<ResponseBody>

    @POST("samples/{id}/approve")
    suspend fun approveSample(@Path("id") sampleId: Int): Response<Void>

    @GET("samples/dashboard/analytics")
    suspend fun getAnalytics(): Response<Analytics>

    // THIS IS THE LINE THAT WAS MISSING!
    @POST("samples/{id}/send-results")
    suspend fun sendEmailToClient(@Path("id") sampleId: Int): Response<Void>
}