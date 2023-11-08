package com.ken.customerserviceagent.api

import android.content.SharedPreferences
import com.ken.customerserviceagent.data.Constants
import com.ken.customerserviceagent.model.AgentApiResult
import com.ken.customerserviceagent.model.AuthenticationRequest
import com.ken.customerserviceagent.model.MessageRequest
import javax.inject.Inject

class AgentApiRepositoryImpl @Inject constructor(
    private val agentApi: AgentApi,
    private val sharedPreferences: SharedPreferences
): AgentApiRepository {
    override suspend fun loginUser(username: String, password: String): AgentApiResult<*> {
        val authenticationRequest = AuthenticationRequest(
            username = username,
            password = password
        )
        val response = agentApi.loginUser(authenticationRequest)
        return if(response.isSuccessful) {
            val authenticationResponse = response.body()!!
            sharedPreferences.edit()
                .putString(Constants.AUTH_TOKEN, authenticationResponse.authToken)
                .apply()
            AgentApiResult.Success(authenticationResponse)
        } else {
            AgentApiResult.Failure()
        }
    }

    override suspend fun getAllMessages(): AgentApiResult<*> {
        val response = agentApi.getAllMessages()
        return if(response.isSuccessful) {
            val messages = response.body()!!
            AgentApiResult.Success(messages)
        } else {
            AgentApiResult.Failure()
        }
    }

    override suspend fun sendMessage(threadId: Int, message: String): AgentApiResult<*> {
        val messageRequest = MessageRequest(
            threadId = threadId,
            body = message
        )
        val response = agentApi.sendMessage(messageRequest)
        return if(response.isSuccessful) {
            val newMessage = response.body()!!
            AgentApiResult.Success(newMessage)
        } else {
            AgentApiResult.Failure()
        }
    }

    override suspend fun resetMessages(): AgentApiResult<*> {
        val response = agentApi.resetMessages()
        return if(response.isSuccessful) {
            AgentApiResult.Success(Unit)
        } else {
            AgentApiResult.Failure()
        }
    }
}