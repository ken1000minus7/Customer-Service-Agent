package com.ken.customerserviceagent.api

import com.ken.customerserviceagent.model.AgentApiResult
import com.ken.customerserviceagent.model.Message

interface AgentApiRepository {
    suspend fun loginUser(username: String, password: String): AgentApiResult<*>
    suspend fun getAllMessages(): AgentApiResult<*>
    suspend fun sendMessage(threadId: Int, message: String): AgentApiResult<*>
    suspend fun resetMessages(): AgentApiResult<*>

}