package com.ken.customerserviceagent.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ken.customerserviceagent.api.AgentApiRepository
import com.ken.customerserviceagent.model.AgentApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentViewModel @Inject constructor(
    private val agentApiRepository: AgentApiRepository
): ViewModel() {
    val loginResult: MutableLiveData<AgentApiResult<*>> = MutableLiveData()
    val messagesResult: MutableLiveData<AgentApiResult<*>> = MutableLiveData()
    val sendMessageResult: MutableLiveData<AgentApiResult<*>> = MutableLiveData()
    val resetResult: MutableLiveData<AgentApiResult<*>> = MutableLiveData()

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            loginResult.postValue(AgentApiResult.Loading())
            loginResult.postValue(agentApiRepository.loginUser(username, password))
        }
    }

    fun getAllMessages() {
        viewModelScope.launch {
            messagesResult.postValue(AgentApiResult.Loading())
            messagesResult.postValue(agentApiRepository.getAllMessages())
        }
    }

    fun sendMessage(threadId: Int, message: String) {
        viewModelScope.launch {
            sendMessageResult.postValue(AgentApiResult.Loading())
            sendMessageResult.postValue(agentApiRepository.sendMessage(threadId, message))
        }
    }

    fun resetMessages() {
        viewModelScope.launch {
            resetResult.postValue(AgentApiResult.Loading())
            resetResult.postValue(agentApiRepository.resetMessages())
        }
    }
}