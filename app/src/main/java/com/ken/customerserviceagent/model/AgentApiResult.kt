package com.ken.customerserviceagent.model

sealed class AgentApiResult<T>(val data: T?) {
    class Success<T>(data: T): AgentApiResult<T>(data)
    class Failure: AgentApiResult<Any>(null)
    class Loading: AgentApiResult<Any>(null)
}