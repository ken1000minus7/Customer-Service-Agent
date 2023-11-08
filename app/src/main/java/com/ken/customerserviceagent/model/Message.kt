package com.ken.customerserviceagent.model

data class Message(
    val id: Int,
    val threadId: Int,
    val userId: Int,
    val agentId: Int,
    val body: String
)
