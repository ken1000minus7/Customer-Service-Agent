package com.ken.customerserviceagent.api

import com.ken.customerserviceagent.model.AuthenticationRequest
import com.ken.customerserviceagent.model.AuthenticationResponse
import com.ken.customerserviceagent.model.Message
import com.ken.customerserviceagent.model.MessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AgentApi {

    @POST("api/login")
    suspend fun loginUser(
        @Body authenticationRequest: AuthenticationRequest
    ): Response<AuthenticationResponse>

    @GET("api/messages")
    suspend fun getAllMessages(): Response<List<Message>>

    @POST("api/messages")
    suspend fun sendMessage(
        @Body messageRequest: MessageRequest
    ): Response<Message>

    @POST("api/reset")
    suspend fun resetMessages(): Response<Any>
}