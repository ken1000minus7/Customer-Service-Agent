package com.ken.customerserviceagent.model

import java.time.ZonedDateTime

data class Message(
    val id: Int,
    val threadId: Int,
    val userId: Int,
    val agentId: Int?,
    val body: String,
    val timestamp: ZonedDateTime
)
